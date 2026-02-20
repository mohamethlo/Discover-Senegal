package sn.discover.discoversenegal.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sn.discover.discoversenegal.dto.EventDTO;
import sn.discover.discoversenegal.dto.EventResponseDTO;
import sn.discover.discoversenegal.entities.EventCategory;
import sn.discover.discoversenegal.entities.EventStatus;
import sn.discover.discoversenegal.services.EventService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // =========================================================
    //  LECTURE PUBLIQUE
    // =========================================================

    /**
     * GET /api/v1/events
     * Liste tous les événements publiés (paginé)
     * Params: page, size, sort
     */
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getAllPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(eventService.getAllPublished(pageable));
    }

    /**
     * GET /api/v1/events/{id}
     * Détail d'un événement
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    /**
     * GET /api/v1/events/search?keyword=xxx
     * Recherche par mot-clé
     */
    @GetMapping("/search")
    public ResponseEntity<Page<EventResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.searchByKeyword(keyword, pageable));
    }

    /**
     * GET /api/v1/events/upcoming
     * Événements à venir
     */
    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventResponseDTO>> getUpcoming(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getUpcoming(pageable));
    }

    /**
     * GET /api/v1/events/featured
     * Événements mis en avant
     */
    @GetMapping("/featured")
    public ResponseEntity<Page<EventResponseDTO>> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getFeaturedEvents(pageable));
    }

    /**
     * GET /api/v1/events/free
     * Événements gratuits
     */
    @GetMapping("/free")
    public ResponseEntity<Page<EventResponseDTO>> getFreeEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getFreeEvents(pageable));
    }

    /**
     * GET /api/v1/events/category/{category}
     * Filtrer par catégorie
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<EventResponseDTO>> getByCategory(
            @PathVariable EventCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getByCategory(category, pageable));
    }

    /**
     * GET /api/v1/events/city/{city}
     * Filtrer par ville
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<Page<EventResponseDTO>> getByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getByCity(city, pageable));
    }

    /**
     * GET /api/v1/events/filter
     * Recherche avancée multi-critères
     * Params optionnels: category, city, freeOnly, from, to, keyword, status
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<EventResponseDTO>> advancedFilter(
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) EventCategory category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean freeOnly,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(
                eventService.advancedSearch(status, category, city, freeOnly, from, to, keyword, pageable)
        );
    }

    /**
     * GET /api/v1/events/organizer/{organizerId}
     * Événements d'un organisateur spécifique
     */
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<Page<EventResponseDTO>> getByOrganizer(
            @PathVariable Long organizerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(eventService.getEventsByOrganizer(organizerId, pageable));
    }

    // =========================================================
    //  CRUD AUTHENTIFIÉ
    // =========================================================

    /**
     * POST /api/v1/events
     * Créer un événement (ORGANIZER, LOCAL, GUIDE, ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER','LOCAL','GUIDE','ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(
            @Valid @RequestBody EventDTO dto,
            Authentication authentication) {

        EventResponseDTO response = eventService.createEvent(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/v1/events/{id}
     * Mettre à jour un événement
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventDTO dto,
            Authentication authentication) {

        return ResponseEntity.ok(eventService.updateEvent(id, dto, authentication.getName()));
    }

    /**
     * DELETE /api/v1/events/{id}
     * Supprimer un événement
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            Authentication authentication) {

        eventService.deleteEvent(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/events/my-events
     * Mes événements (organisateur connecté)
     */
    @GetMapping("/my-events")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EventResponseDTO>> getMyEvents(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(eventService.getMyEvents(authentication.getName(), pageable));
    }

    // =========================================================
    //  WORKFLOW / MODÉRATION
    // =========================================================

    /**
     * PATCH /api/v1/events/{id}/submit
     * Soumettre pour validation
     */
    @PatchMapping("/{id}/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDTO> submit(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(eventService.submitForValidation(id, authentication.getName()));
    }

    /**
     * PATCH /api/v1/events/{id}/publish
     * Publier (ADMIN seulement)
     */
    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> publish(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.publishEvent(id));
    }

    /**
     * PATCH /api/v1/events/{id}/cancel
     * Annuler un événement
     */
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDTO> cancel(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(eventService.cancelEvent(id, authentication.getName()));
    }

    /**
     * PATCH /api/v1/events/{id}/featured
     * Mettre en avant / retirer (ADMIN seulement)
     */
    @PatchMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> toggleFeatured(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.toggleFeatured(id));
    }

    /**
     * PATCH /api/v1/events/{id}/verify
     * Certifier / vérifier (ADMIN seulement)
     */
    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> verify(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.verifyEvent(id));
    }

    /**
     * PATCH /api/v1/events/{id}/register
     * S'inscrire à un événement (utilisateur connecté)
     */
    @PatchMapping("/{id}/register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventResponseDTO> register(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.registerParticipant(id));
    }

    // =========================================================
    //  STATISTIQUES
    // =========================================================

    /**
     * GET /api/v1/events/stats
     * Statistiques globales (ADMIN seulement)
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(eventService.getStats());
    }
}
