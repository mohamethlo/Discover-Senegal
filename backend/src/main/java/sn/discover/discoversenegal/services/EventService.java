package sn.discover.discoversenegal.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.discover.discoversenegal.dto.EventDTO;
import sn.discover.discoversenegal.dto.EventResponseDTO;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.repositories.EventRepository;
import sn.discover.discoversenegal.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // =========================================================
    //  CRUD de base
    // =========================================================

    /**
     * Créer un événement — statut initial : BROUILLON
     */
    public EventResponseDTO createEvent(EventDTO dto, String organizerEmail) {
        User organizer = getUserByEmail(organizerEmail);
        validateDates(dto.getStartDateTime(), dto.getEndDateTime());

        Event event = buildEventFromDTO(dto, organizer);
        Event saved = eventRepository.save(event);
        log.info("Événement créé : id={}, titre={}", saved.getId(), saved.getTitle());
        return toResponseDTO(saved);
    }

    /**
     * Mettre à jour un événement
     */
    public EventResponseDTO updateEvent(Long id, EventDTO dto, String requesterEmail) {
        Event event = getEventOrThrow(id);
        User requester = getUserByEmail(requesterEmail);

        assertCanEdit(event, requester);
        validateDates(dto.getStartDateTime(), dto.getEndDateTime());

        updateEventFromDTO(event, dto);
        Event saved = eventRepository.save(event);
        log.info("Événement mis à jour : id={}", saved.getId());
        return toResponseDTO(saved);
    }

    /**
     * Supprimer un événement
     */
    public void deleteEvent(Long id, String requesterEmail) {
        Event event = getEventOrThrow(id);
        User requester = getUserByEmail(requesterEmail);
        assertCanEdit(event, requester);

        eventRepository.delete(event);
        log.info("Événement supprimé : id={}", id);
    }

    /**
     * Récupérer un événement par son ID (lecture seule)
     */
    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(Long id) {
        return toResponseDTO(getEventOrThrow(id));
    }

    // =========================================================
    //  Listings et recherches
    // =========================================================

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getAllPublished(Pageable pageable) {
        return eventRepository.findByStatus(EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getByCategory(EventCategory category, Pageable pageable) {
        return eventRepository.findByCategoryAndStatus(category, EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getByCity(String city, Pageable pageable) {
        return eventRepository.findByCityIgnoreCaseAndStatus(city, EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> searchByKeyword(String keyword, Pageable pageable) {
        return eventRepository.searchByKeyword(keyword, EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getUpcoming(Pageable pageable) {
        return eventRepository.findUpcoming(LocalDateTime.now(), EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getFreeEvents(Pageable pageable) {
        return eventRepository.findByFreeEntryTrueAndStatus(EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getFeaturedEvents(Pageable pageable) {
        return eventRepository.findByFeaturedTrueAndStatus(EventStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByOrganizer(Long organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getMyEvents(String email, Pageable pageable) {
        User user = getUserByEmail(email);
        return eventRepository.findByOrganizerId(user.getId(), pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> advancedSearch(
            EventStatus status,
            EventCategory category,
            String city,
            Boolean freeOnly,
            LocalDateTime from,
            LocalDateTime to,
            String keyword,
            Pageable pageable) {

        // Les visiteurs non-admin ne voient que les PUBLIE
        EventStatus effectiveStatus = (status != null) ? status : EventStatus.PUBLIE;

        return eventRepository.advancedSearch(
                effectiveStatus, category, city, freeOnly, from, to, keyword, pageable
        ).map(this::toResponseDTO);
    }

    // =========================================================
    //  Actions de modération / workflow (ADMIN / ORGANIZER)
    // =========================================================

    /**
     * Soumettre un événement pour validation
     */
    public EventResponseDTO submitForValidation(Long id, String requesterEmail) {
        Event event = getEventOrThrow(id);
        User requester = getUserByEmail(requesterEmail);
        assertCanEdit(event, requester);

        event.setStatus(EventStatus.EN_ATTENTE_VALIDATION);
        return toResponseDTO(eventRepository.save(event));
    }

    /**
     * Publier un événement (ADMIN uniquement)
     */
    public EventResponseDTO publishEvent(Long id) {
        Event event = getEventOrThrow(id);
        event.setStatus(EventStatus.PUBLIE);
        log.info("Événement publié : id={}", id);
        return toResponseDTO(eventRepository.save(event));
    }

    /**
     * Annuler un événement
     */
    public EventResponseDTO cancelEvent(Long id, String requesterEmail) {
        Event event = getEventOrThrow(id);
        User requester = getUserByEmail(requesterEmail);
        assertCanEdit(event, requester);

        event.setStatus(EventStatus.ANNULE);
        return toResponseDTO(eventRepository.save(event));
    }

    /**
     * Marquer comme mis en avant / retiré (ADMIN uniquement)
     */
    public EventResponseDTO toggleFeatured(Long id) {
        Event event = getEventOrThrow(id);
        event.setFeatured(!event.isFeatured());
        return toResponseDTO(eventRepository.save(event));
    }

    /**
     * Certifier / vérifier un événement (ADMIN uniquement)
     */
    public EventResponseDTO verifyEvent(Long id) {
        Event event = getEventOrThrow(id);
        event.setVerified(true);
        return toResponseDTO(eventRepository.save(event));
    }

    /**
     * Inscription à un événement
     */
    public EventResponseDTO registerParticipant(Long id) {
        Event event = getEventOrThrow(id);

        if (event.getStatus() != EventStatus.PUBLIE) {
            throw new IllegalStateException("L'événement n'est pas disponible à l'inscription.");
        }
        if (event.getMaxParticipants() != null &&
            event.getCurrentParticipants() >= event.getMaxParticipants()) {
            throw new IllegalStateException("L'événement est complet.");
        }

        event.setCurrentParticipants(event.getCurrentParticipants() + 1);

        // Auto-passer à COMPLET si la capacité max est atteinte
        if (event.getMaxParticipants() != null &&
            event.getCurrentParticipants().equals(event.getMaxParticipants())) {
            event.setStatus(EventStatus.COMPLET);
        }

        return toResponseDTO(eventRepository.save(event));
    }

    // =========================================================
    //  Statistiques
    // =========================================================

    @Transactional(readOnly = true)
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPublie", eventRepository.countByStatus(EventStatus.PUBLIE));
        stats.put("totalEnAttente", eventRepository.countByStatus(EventStatus.EN_ATTENTE_VALIDATION));
        stats.put("totalBrouillon", eventRepository.countByStatus(EventStatus.BROUILLON));
        stats.put("totalAnnule", eventRepository.countByStatus(EventStatus.ANNULE));
        stats.put("totalTermine", eventRepository.countByStatus(EventStatus.TERMINE));

        // Comptage par catégorie
        Map<String, Long> byCategory = new HashMap<>();
        for (EventCategory cat : EventCategory.values()) {
            byCategory.put(cat.name(), eventRepository.countByCategory(cat));
        }
        stats.put("parCategorie", byCategory);
        return stats;
    }

    // =========================================================
    //  Méthodes privées utilitaires
    // =========================================================

    private Event getEventOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'id : " + id));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return;
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
        }
    }

    private void assertCanEdit(Event event, User requester) {
        boolean isOwner = event.getOrganizer().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new SecurityException("Vous n'avez pas l'autorisation de modifier cet événement.");
        }
    }

    private Event buildEventFromDTO(EventDTO dto, User organizer) {
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .shortDescription(dto.getShortDescription())
                .category(dto.getCategory())
                .status(EventStatus.BROUILLON)
                .startDateTime(dto.getStartDateTime())
                .endDateTime(dto.getEndDateTime())
                .recurring(dto.isRecurring())
                .recurrencePattern(dto.getRecurrencePattern())
                .location(dto.getLocation())
                .address(dto.getAddress())
                .city(dto.getCity())
                .region(dto.getRegion())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .maxParticipants(dto.getMaxParticipants())
                .currentParticipants(0)
                .freeEntry(dto.isFreeEntry())
                .price(dto.getPrice())
                .currency(dto.getCurrency())
                .ticketUrl(dto.getTicketUrl())
                .coverImageUrl(dto.getCoverImageUrl())
                .imageUrls(dto.getImageUrls())
                .videoUrl(dto.getVideoUrl())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .website(dto.getWebsite())
                .facebookUrl(dto.getFacebookUrl())
                .instagramUrl(dto.getInstagramUrl())
                .tags(dto.getTags())
                .languages(dto.getLanguages())
                .organizer(organizer)
                .featured(false)
                .verified(false)
                .build();
    }

    private void updateEventFromDTO(Event event, EventDTO dto) {
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setShortDescription(dto.getShortDescription());
        event.setCategory(dto.getCategory());
        event.setStartDateTime(dto.getStartDateTime());
        event.setEndDateTime(dto.getEndDateTime());
        event.setRecurring(dto.isRecurring());
        event.setRecurrencePattern(dto.getRecurrencePattern());
        event.setLocation(dto.getLocation());
        event.setAddress(dto.getAddress());
        event.setCity(dto.getCity());
        event.setRegion(dto.getRegion());
        event.setLatitude(dto.getLatitude());
        event.setLongitude(dto.getLongitude());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setFreeEntry(dto.isFreeEntry());
        event.setPrice(dto.getPrice());
        event.setCurrency(dto.getCurrency());
        event.setTicketUrl(dto.getTicketUrl());
        event.setCoverImageUrl(dto.getCoverImageUrl());
        event.setImageUrls(dto.getImageUrls());
        event.setVideoUrl(dto.getVideoUrl());
        event.setContactEmail(dto.getContactEmail());
        event.setContactPhone(dto.getContactPhone());
        event.setWebsite(dto.getWebsite());
        event.setFacebookUrl(dto.getFacebookUrl());
        event.setInstagramUrl(dto.getInstagramUrl());
        event.setTags(dto.getTags());
        event.setLanguages(dto.getLanguages());
    }

    private EventResponseDTO toResponseDTO(Event event) {
        Profile profile = event.getOrganizer().getProfile();

        EventResponseDTO.OrganizerInfo organizerInfo = EventResponseDTO.OrganizerInfo.builder()
                .id(event.getOrganizer().getId())
                .displayName(profile != null ? profile.getDisplayName() : null)
                .photoUrl(profile != null ? profile.getPhotoUrl() : null)
                .verified(profile != null && profile.isVerified())
                .build();

        Integer available = null;
        if (event.getMaxParticipants() != null) {
            available = event.getMaxParticipants() - event.getCurrentParticipants();
        }

        return EventResponseDTO.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .shortDescription(event.getShortDescription())
                .category(event.getCategory())
                .status(event.getStatus())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .recurring(event.isRecurring())
                .recurrencePattern(event.getRecurrencePattern())
                .location(event.getLocation())
                .address(event.getAddress())
                .city(event.getCity())
                .region(event.getRegion())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .maxParticipants(event.getMaxParticipants())
                .currentParticipants(event.getCurrentParticipants())
                .availableSpots(available)
                .freeEntry(event.isFreeEntry())
                .price(event.getPrice())
                .currency(event.getCurrency())
                .ticketUrl(event.getTicketUrl())
                .coverImageUrl(event.getCoverImageUrl())
                .imageUrls(event.getImageUrls())
                .videoUrl(event.getVideoUrl())
                .contactEmail(event.getContactEmail())
                .contactPhone(event.getContactPhone())
                .website(event.getWebsite())
                .facebookUrl(event.getFacebookUrl())
                .instagramUrl(event.getInstagramUrl())
                .tags(event.getTags())
                .languages(event.getLanguages())
                .organizer(organizerInfo)
                .featured(event.isFeatured())
                .verified(event.isVerified())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
