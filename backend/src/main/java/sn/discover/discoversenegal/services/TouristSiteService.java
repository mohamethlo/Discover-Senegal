package sn.discover.discoversenegal.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.discover.discoversenegal.dto.TouristSiteDTO;
import sn.discover.discoversenegal.dto.TouristSiteResponseDTO;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.repositories.TouristSiteRepository;
import sn.discover.discoversenegal.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TouristSiteService {

    private final TouristSiteRepository siteRepository;
    private final UserRepository userRepository;

    // =========================================================
    //  CRUD de base
    // =========================================================

    public TouristSiteResponseDTO createSite(TouristSiteDTO dto, String creatorEmail) {
        User creator = getUserByEmail(creatorEmail);
        TouristSite site = buildSiteFromDTO(dto, creator);
        TouristSite saved = siteRepository.save(site);
        log.info("Site touristique créé : id={}, nom={}", saved.getId(), saved.getName());
        return toResponseDTO(saved);
    }

    public TouristSiteResponseDTO updateSite(Long id, TouristSiteDTO dto, String requesterEmail) {
        TouristSite site = getSiteOrThrow(id);
        User requester = getUserByEmail(requesterEmail);
        assertCanEdit(site, requester);

        updateSiteFromDTO(site, dto);
        TouristSite saved = siteRepository.save(site);
        log.info("Site touristique mis à jour : id={}", saved.getId());
        return toResponseDTO(saved);
    }

    public void deleteSite(Long id, String requesterEmail) {
        TouristSite site = getSiteOrThrow(id);
        User requester = getUserByEmail(requesterEmail);
        assertCanEdit(site, requester);
        siteRepository.delete(site);
        log.info("Site touristique supprimé : id={}", id);
    }

    @Transactional(readOnly = true)
    public TouristSiteResponseDTO getSiteById(Long id) {
        TouristSite site = getSiteOrThrow(id);
        // Incrémenter le compteur de vues de manière asynchrone
        siteRepository.incrementViews(id);
        return toResponseDTO(site);
    }

    // =========================================================
    //  Listings & Recherches
    // =========================================================

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getAllPublished(Pageable pageable) {
        return siteRepository.findByStatus(SiteStatus.PUBLIE, pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getByCategory(SiteCategory category, Pageable pageable) {
        return siteRepository.findByCategoryAndStatus(category, SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getByCity(String city, Pageable pageable) {
        return siteRepository.findByCityIgnoreCaseAndStatus(city, SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getByRegion(String region, Pageable pageable) {
        return siteRepository.findByRegionIgnoreCaseAndStatus(region, SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getUnescoSites(Pageable pageable) {
        return siteRepository.findByUnescoListedTrueAndStatus(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getNationalHeritageSites(Pageable pageable) {
        return siteRepository.findByNationalHeritageTrueAndStatus(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getFeaturedSites(Pageable pageable) {
        return siteRepository.findByFeaturedTrueAndStatus(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getFreeSites(Pageable pageable) {
        return siteRepository.findByFreeEntryTrueAndStatus(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getWheelchairAccessible(Pageable pageable) {
        return siteRepository.findByWheelchairAccessibleTrueAndStatus(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getWithGuidedTour(Pageable pageable) {
        return siteRepository.findByGuidedTourAvailableTrueAndStatus(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getTopRated(int minReviews, Pageable pageable) {
        return siteRepository.findTopRated(SiteStatus.PUBLIE, minReviews, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getMostViewed(Pageable pageable) {
        return siteRepository.findMostViewed(SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> searchByKeyword(String keyword, Pageable pageable) {
        return siteRepository.searchByKeyword(keyword, SiteStatus.PUBLIE, pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<TouristSiteResponseDTO> findNearby(Double latitude, Double longitude, Double radiusKm) {
        if (radiusKm == null || radiusKm <= 0 || radiusKm > 500) {
            throw new IllegalArgumentException("Le rayon doit être compris entre 1 et 500 km.");
        }
        return siteRepository.findNearby(latitude, longitude, radiusKm)
                .stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> advancedSearch(
            SiteStatus status,
            SiteCategory category,
            String city,
            String region,
            Boolean freeOnly,
            Boolean unescoOnly,
            AccessibilityLevel accessibility,
            Boolean guidedTour,
            Boolean wheelchairOk,
            String keyword,
            Pageable pageable) {

        SiteStatus effectiveStatus = (status != null) ? status : SiteStatus.PUBLIE;
        return siteRepository.advancedSearch(
                effectiveStatus, category, city, region,
                freeOnly, unescoOnly, accessibility,
                guidedTour, wheelchairOk, keyword, pageable
        ).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<TouristSiteResponseDTO> getMySites(String email, Pageable pageable) {
        User user = getUserByEmail(email);
        return siteRepository.findByCreatedById(user.getId(), pageable).map(this::toResponseDTO);
    }

    // =========================================================
    //  Workflow / Modération
    // =========================================================

    public TouristSiteResponseDTO submitForValidation(Long id, String requesterEmail) {
        TouristSite site = getSiteOrThrow(id);
        assertCanEdit(site, getUserByEmail(requesterEmail));
        site.setStatus(SiteStatus.EN_ATTENTE_VALIDATION);
        return toResponseDTO(siteRepository.save(site));
    }

    public TouristSiteResponseDTO publishSite(Long id) {
        TouristSite site = getSiteOrThrow(id);
        site.setStatus(SiteStatus.PUBLIE);
        log.info("Site publié : id={}", id);
        return toResponseDTO(siteRepository.save(site));
    }

    public TouristSiteResponseDTO suspendSite(Long id) {
        TouristSite site = getSiteOrThrow(id);
        site.setStatus(SiteStatus.SUSPENDU);
        return toResponseDTO(siteRepository.save(site));
    }

    public TouristSiteResponseDTO archiveSite(Long id) {
        TouristSite site = getSiteOrThrow(id);
        site.setStatus(SiteStatus.ARCHIVE);
        return toResponseDTO(siteRepository.save(site));
    }

    public TouristSiteResponseDTO toggleFeatured(Long id) {
        TouristSite site = getSiteOrThrow(id);
        site.setFeatured(!site.isFeatured());
        return toResponseDTO(siteRepository.save(site));
    }

    public TouristSiteResponseDTO verifySite(Long id) {
        TouristSite site = getSiteOrThrow(id);
        site.setVerified(true);
        return toResponseDTO(siteRepository.save(site));
    }

    // =========================================================
    //  Favoris (compteur)
    // =========================================================

    public void addToFavorites(Long id) {
        getSiteOrThrow(id);
        siteRepository.incrementFavorites(id);
    }

    public void removeFromFavorites(Long id) {
        getSiteOrThrow(id);
        siteRepository.decrementFavorites(id);
    }

    // =========================================================
    //  Références (villes, régions)
    // =========================================================

    @Transactional(readOnly = true)
    public List<String> getAllCities() {
        return siteRepository.findDistinctCities();
    }

    @Transactional(readOnly = true)
    public List<String> getAllRegions() {
        return siteRepository.findDistinctRegions();
    }

    // =========================================================
    //  Statistiques
    // =========================================================

    @Transactional(readOnly = true)
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPublie", siteRepository.countByStatus(SiteStatus.PUBLIE));
        stats.put("totalEnAttente", siteRepository.countByStatus(SiteStatus.EN_ATTENTE_VALIDATION));
        stats.put("totalBrouillon", siteRepository.countByStatus(SiteStatus.BROUILLON));
        stats.put("totalSuspendu", siteRepository.countByStatus(SiteStatus.SUSPENDU));
        stats.put("totalArchive", siteRepository.countByStatus(SiteStatus.ARCHIVE));
        stats.put("totalUnesco", siteRepository.countByUnescoListedTrue());
        stats.put("totalPatrimoineNational", siteRepository.countByNationalHeritageTrue());

        Map<String, Long> byCategory = new HashMap<>();
        for (SiteCategory cat : SiteCategory.values()) {
            byCategory.put(cat.name(), siteRepository.countByCategory(cat));
        }
        stats.put("parCategorie", byCategory);
        return stats;
    }

    // =========================================================
    //  Méthodes privées
    // =========================================================

    private TouristSite getSiteOrThrow(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site touristique non trouvé : " + id));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));
    }

    private void assertCanEdit(TouristSite site, User requester) {
        boolean isOwner = site.getCreatedBy() != null &&
                          site.getCreatedBy().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole() == UserRole.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new SecurityException("Vous n'êtes pas autorisé à modifier ce site.");
        }
    }

    private TouristSite buildSiteFromDTO(TouristSiteDTO dto, User creator) {
        return TouristSite.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .shortDescription(dto.getShortDescription())
                .history(dto.getHistory())
                .practicalInfo(dto.getPracticalInfo())
                .category(dto.getCategory())
                .status(SiteStatus.BROUILLON)
                .address(dto.getAddress())
                .city(dto.getCity())
                .region(dto.getRegion())
                .country(dto.getCountry() != null ? dto.getCountry() : "Sénégal")
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .googleMapsUrl(dto.getGoogleMapsUrl())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .openMonday(dto.isOpenMonday())
                .openTuesday(dto.isOpenTuesday())
                .openWednesday(dto.isOpenWednesday())
                .openThursday(dto.isOpenThursday())
                .openFriday(dto.isOpenFriday())
                .openSaturday(dto.isOpenSaturday())
                .openSunday(dto.isOpenSunday())
                .specialHoursNote(dto.getSpecialHoursNote())
                .freeEntry(dto.isFreeEntry())
                .adultPrice(dto.getAdultPrice())
                .childPrice(dto.getChildPrice())
                .studentPrice(dto.getStudentPrice())
                .foreignerPrice(dto.getForeignerPrice())
                .currency(dto.getCurrency())
                .ticketInfo(dto.getTicketInfo())
                .accessibility(dto.getAccessibility())
                .bestPeriods(dto.getBestPeriods())
                .visitDurationMinutes(dto.getVisitDurationMinutes())
                .guidedTourAvailable(dto.isGuidedTourAvailable())
                .audioGuideAvailable(dto.isAudioGuideAvailable())
                .wheelchairAccessible(dto.isWheelchairAccessible())
                .parkingAvailable(dto.isParkingAvailable())
                .restroomAvailable(dto.isRestroomAvailable())
                .restaurantNearby(dto.isRestaurantNearby())
                .photoAllowed(dto.isPhotoAllowed())
                .droneAllowed(dto.isDroneAllowed())
                .unescoListed(dto.isUnescoListed())
                .unescoSince(dto.getUnescoSince())
                .nationalHeritage(dto.isNationalHeritage())
                .heritageClassification(dto.getHeritageClassification())
                .coverImageUrl(dto.getCoverImageUrl())
                .imageUrls(dto.getImageUrls())
                .videoUrl(dto.getVideoUrl())
                .virtualTourUrl(dto.getVirtualTourUrl())
                .contactPhone(dto.getContactPhone())
                .contactEmail(dto.getContactEmail())
                .website(dto.getWebsite())
                .facebookUrl(dto.getFacebookUrl())
                .instagramUrl(dto.getInstagramUrl())
                .tags(dto.getTags())
                .availableLanguages(dto.getAvailableLanguages())
                .createdBy(creator)
                .featured(false)
                .verified(false)
                .averageRating(0.0)
                .totalReviews(0)
                .totalViews(0)
                .totalFavorites(0)
                .build();
    }

    private void updateSiteFromDTO(TouristSite site, TouristSiteDTO dto) {
        site.setName(dto.getName());
        site.setDescription(dto.getDescription());
        site.setShortDescription(dto.getShortDescription());
        site.setHistory(dto.getHistory());
        site.setPracticalInfo(dto.getPracticalInfo());
        site.setCategory(dto.getCategory());
        site.setAddress(dto.getAddress());
        site.setCity(dto.getCity());
        site.setRegion(dto.getRegion());
        site.setCountry(dto.getCountry());
        site.setLatitude(dto.getLatitude());
        site.setLongitude(dto.getLongitude());
        site.setGoogleMapsUrl(dto.getGoogleMapsUrl());
        site.setOpeningTime(dto.getOpeningTime());
        site.setClosingTime(dto.getClosingTime());
        site.setOpenMonday(dto.isOpenMonday());
        site.setOpenTuesday(dto.isOpenTuesday());
        site.setOpenWednesday(dto.isOpenWednesday());
        site.setOpenThursday(dto.isOpenThursday());
        site.setOpenFriday(dto.isOpenFriday());
        site.setOpenSaturday(dto.isOpenSaturday());
        site.setOpenSunday(dto.isOpenSunday());
        site.setSpecialHoursNote(dto.getSpecialHoursNote());
        site.setFreeEntry(dto.isFreeEntry());
        site.setAdultPrice(dto.getAdultPrice());
        site.setChildPrice(dto.getChildPrice());
        site.setStudentPrice(dto.getStudentPrice());
        site.setForeignerPrice(dto.getForeignerPrice());
        site.setCurrency(dto.getCurrency());
        site.setTicketInfo(dto.getTicketInfo());
        site.setAccessibility(dto.getAccessibility());
        site.setBestPeriods(dto.getBestPeriods());
        site.setVisitDurationMinutes(dto.getVisitDurationMinutes());
        site.setGuidedTourAvailable(dto.isGuidedTourAvailable());
        site.setAudioGuideAvailable(dto.isAudioGuideAvailable());
        site.setWheelchairAccessible(dto.isWheelchairAccessible());
        site.setParkingAvailable(dto.isParkingAvailable());
        site.setRestroomAvailable(dto.isRestroomAvailable());
        site.setRestaurantNearby(dto.isRestaurantNearby());
        site.setPhotoAllowed(dto.isPhotoAllowed());
        site.setDroneAllowed(dto.isDroneAllowed());
        site.setUnescoListed(dto.isUnescoListed());
        site.setUnescoSince(dto.getUnescoSince());
        site.setNationalHeritage(dto.isNationalHeritage());
        site.setHeritageClassification(dto.getHeritageClassification());
        site.setCoverImageUrl(dto.getCoverImageUrl());
        site.setImageUrls(dto.getImageUrls());
        site.setVideoUrl(dto.getVideoUrl());
        site.setVirtualTourUrl(dto.getVirtualTourUrl());
        site.setContactPhone(dto.getContactPhone());
        site.setContactEmail(dto.getContactEmail());
        site.setWebsite(dto.getWebsite());
        site.setFacebookUrl(dto.getFacebookUrl());
        site.setInstagramUrl(dto.getInstagramUrl());
        site.setTags(dto.getTags());
        site.setAvailableLanguages(dto.getAvailableLanguages());
    }

    public TouristSiteResponseDTO toResponseDTO(TouristSite site) {
        // Formatage durée de visite
        String durationFormatted = null;
        if (site.getVisitDurationMinutes() != null) {
            int h = site.getVisitDurationMinutes() / 60;
            int m = site.getVisitDurationMinutes() % 60;
            durationFormatted = h > 0 ? (m > 0 ? h + "h" + m : h + "h") : m + "min";
        }

        TouristSiteResponseDTO.CreatorInfo creatorInfo = null;
        if (site.getCreatedBy() != null) {
            Profile profile = site.getCreatedBy().getProfile();
            creatorInfo = TouristSiteResponseDTO.CreatorInfo.builder()
                    .id(site.getCreatedBy().getId())
                    .displayName(profile != null ? profile.getDisplayName() : null)
                    .photoUrl(profile != null ? profile.getPhotoUrl() : null)
                    .build();
        }

        return TouristSiteResponseDTO.builder()
                .id(site.getId())
                .name(site.getName())
                .description(site.getDescription())
                .shortDescription(site.getShortDescription())
                .history(site.getHistory())
                .practicalInfo(site.getPracticalInfo())
                .category(site.getCategory())
                .status(site.getStatus())
                .address(site.getAddress())
                .city(site.getCity())
                .region(site.getRegion())
                .country(site.getCountry())
                .latitude(site.getLatitude())
                .longitude(site.getLongitude())
                .googleMapsUrl(site.getGoogleMapsUrl())
                .openingTime(site.getOpeningTime())
                .closingTime(site.getClosingTime())
                .openingSchedule(TouristSiteResponseDTO.OpeningSchedule.builder()
                        .monday(site.isOpenMonday())
                        .tuesday(site.isOpenTuesday())
                        .wednesday(site.isOpenWednesday())
                        .thursday(site.isOpenThursday())
                        .friday(site.isOpenFriday())
                        .saturday(site.isOpenSaturday())
                        .sunday(site.isOpenSunday())
                        .build())
                .specialHoursNote(site.getSpecialHoursNote())
                .freeEntry(site.isFreeEntry())
                .adultPrice(site.getAdultPrice())
                .childPrice(site.getChildPrice())
                .studentPrice(site.getStudentPrice())
                .foreignerPrice(site.getForeignerPrice())
                .currency(site.getCurrency())
                .ticketInfo(site.getTicketInfo())
                .accessibility(site.getAccessibility())
                .bestPeriods(site.getBestPeriods())
                .visitDurationMinutes(site.getVisitDurationMinutes())
                .visitDurationFormatted(durationFormatted)
                .guidedTourAvailable(site.isGuidedTourAvailable())
                .audioGuideAvailable(site.isAudioGuideAvailable())
                .wheelchairAccessible(site.isWheelchairAccessible())
                .parkingAvailable(site.isParkingAvailable())
                .restroomAvailable(site.isRestroomAvailable())
                .restaurantNearby(site.isRestaurantNearby())
                .photoAllowed(site.isPhotoAllowed())
                .droneAllowed(site.isDroneAllowed())
                .unescoListed(site.isUnescoListed())
                .unescoSince(site.getUnescoSince())
                .nationalHeritage(site.isNationalHeritage())
                .heritageClassification(site.getHeritageClassification())
                .coverImageUrl(site.getCoverImageUrl())
                .imageUrls(site.getImageUrls())
                .videoUrl(site.getVideoUrl())
                .virtualTourUrl(site.getVirtualTourUrl())
                .contactPhone(site.getContactPhone())
                .contactEmail(site.getContactEmail())
                .website(site.getWebsite())
                .facebookUrl(site.getFacebookUrl())
                .instagramUrl(site.getInstagramUrl())
                .tags(site.getTags())
                .availableLanguages(site.getAvailableLanguages())
                .averageRating(site.getAverageRating())
                .totalReviews(site.getTotalReviews())
                .totalViews(site.getTotalViews())
                .totalFavorites(site.getTotalFavorites())
                .createdBy(creatorInfo)
                .featured(site.isFeatured())
                .verified(site.isVerified())
                .createdAt(site.getCreatedAt())
                .updatedAt(site.getUpdatedAt())
                .build();
    }
}
