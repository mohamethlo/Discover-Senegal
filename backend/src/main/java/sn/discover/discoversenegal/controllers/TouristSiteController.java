package sn.discover.discoversenegal.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sn.discover.discoversenegal.dto.TouristSiteDTO;
import sn.discover.discoversenegal.dto.TouristSiteResponseDTO;
import sn.discover.discoversenegal.entities.*;
import sn.discover.discoversenegal.services.TouristSiteService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class TouristSiteController {

    private final TouristSiteService siteService;

    // =========================================================
    //  LECTURE PUBLIQUE
    // =========================================================

    /**
     * GET /api/v1/sites
     * Tous les sites publiés (paginé, trié)
     */
    @GetMapping
    public ResponseEntity<Page<TouristSiteResponseDTO>> getAllPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(siteService.getAllPublished(pageable));
    }

    /**
     * GET /api/v1/sites/{id}
     * Détail d'un site (incrémente le compteur de vues)
     */
    @GetMapping("/{id}")
    public ResponseEntity<TouristSiteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.getSiteById(id));
    }

    /**
     * GET /api/v1/sites/search?keyword=gorée
     * Recherche full-text
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TouristSiteResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(siteService.searchByKeyword(keyword, pageable));
    }

    /**
     * GET /api/v1/sites/nearby?lat=14.6937&lng=-17.4441&radius=50
     * Sites dans un rayon (km) autour d'un point GPS
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<TouristSiteResponseDTO>> getNearby(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "30") Double radius) {

        return ResponseEntity.ok(siteService.findNearby(lat, lng, radius));
    }

    /**
     * GET /api/v1/sites/featured
     * Sites mis en avant
     */
    @GetMapping("/featured")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getFeatured(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        return ResponseEntity.ok(siteService.getFeaturedSites(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/top-rated?minReviews=5
     * Sites les mieux notés
     */
    @GetMapping("/top-rated")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getTopRated(
            @RequestParam(defaultValue = "3") int minReviews,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(siteService.getTopRated(minReviews, PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/most-viewed
     * Sites les plus consultés
     */
    @GetMapping("/most-viewed")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getMostViewed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(siteService.getMostViewed(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/free
     * Sites à entrée gratuite
     */
    @GetMapping("/free")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getFreeSites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getFreeSites(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/unesco
     * Sites classés UNESCO
     */
    @GetMapping("/unesco")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getUnescoSites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getUnescoSites(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/heritage
     * Patrimoine national sénégalais
     */
    @GetMapping("/heritage")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getNationalHeritage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getNationalHeritageSites(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/accessible
     * Sites accessibles aux PMR
     */
    @GetMapping("/accessible")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getWheelchairAccessible(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getWheelchairAccessible(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/guided-tour
     * Sites avec visite guidée disponible
     */
    @GetMapping("/guided-tour")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getWithGuidedTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getWithGuidedTour(PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/category/{category}
     * Par catégorie
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getByCategory(
            @PathVariable SiteCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(siteService.getByCategory(category, PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    /**
     * GET /api/v1/sites/city/{city}
     * Par ville
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getByCity(city, PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/region/{region}
     * Par région administrative
     */
    @GetMapping("/region/{region}")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getByRegion(
            @PathVariable String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(siteService.getByRegion(region, PageRequest.of(page, size)));
    }

    /**
     * GET /api/v1/sites/filter
     * Filtre avancé multi-critères
     * Tous les paramètres sont optionnels
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<TouristSiteResponseDTO>> advancedFilter(
            @RequestParam(required = false) SiteStatus status,
            @RequestParam(required = false) SiteCategory category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean freeOnly,
            @RequestParam(required = false) Boolean unescoOnly,
            @RequestParam(required = false) AccessibilityLevel accessibility,
            @RequestParam(required = false) Boolean guidedTour,
            @RequestParam(required = false) Boolean wheelchairOk,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return ResponseEntity.ok(siteService.advancedSearch(
                status, category, city, region,
                freeOnly, unescoOnly, accessibility,
                guidedTour, wheelchairOk, keyword, pageable));
    }

    /**
     * GET /api/v1/sites/references/cities
     * Liste toutes les villes ayant des sites publiés
     */
    @GetMapping("/references/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(siteService.getAllCities());
    }

    /**
     * GET /api/v1/sites/references/regions
     * Liste toutes les régions ayant des sites publiés
     */
    @GetMapping("/references/regions")
    public ResponseEntity<List<String>> getRegions() {
        return ResponseEntity.ok(siteService.getAllRegions());
    }

    // =========================================================
    //  CRUD AUTHENTIFIÉ
    // =========================================================

    /**
     * POST /api/v1/sites
     * Créer un site (LOCAL, GUIDE, ORGANIZER, ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('LOCAL','GUIDE','ORGANIZER','ADMIN')")
    public ResponseEntity<TouristSiteResponseDTO> createSite(
            @Valid @RequestBody TouristSiteDTO dto,
            Authentication authentication) {

        TouristSiteResponseDTO response = siteService.createSite(dto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/v1/sites/{id}
     * Modifier un site (propriétaire ou ADMIN)
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TouristSiteResponseDTO> updateSite(
            @PathVariable Long id,
            @Valid @RequestBody TouristSiteDTO dto,
            Authentication authentication) {

        return ResponseEntity.ok(siteService.updateSite(id, dto, authentication.getName()));
    }

    /**
     * DELETE /api/v1/sites/{id}
     * Supprimer un site (propriétaire ou ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSite(
            @PathVariable Long id,
            Authentication authentication) {

        siteService.deleteSite(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/sites/my-sites
     * Mes sites (utilisateur connecté)
     */
    @GetMapping("/my-sites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TouristSiteResponseDTO>> getMySites(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(siteService.getMySites(authentication.getName(), pageable));
    }

    // =========================================================
    //  INTERACTIONS UTILISATEUR
    // =========================================================

    /**
     * POST /api/v1/sites/{id}/favorite
     * Ajouter aux favoris
     */
    @PostMapping("/{id}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> addFavorite(@PathVariable Long id) {
        siteService.addToFavorites(id);
        return ResponseEntity.ok(Map.of("message", "Site ajouté aux favoris"));
    }

    /**
     * DELETE /api/v1/sites/{id}/favorite
     * Retirer des favoris
     */
    @DeleteMapping("/{id}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> removeFavorite(@PathVariable Long id) {
        siteService.removeFromFavorites(id);
        return ResponseEntity.ok(Map.of("message", "Site retiré des favoris"));
    }

    // =========================================================
    //  WORKFLOW / MODÉRATION
    // =========================================================

    /**
     * PATCH /api/v1/sites/{id}/submit
     * Soumettre pour validation
     */
    @PatchMapping("/{id}/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TouristSiteResponseDTO> submit(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(siteService.submitForValidation(id, authentication.getName()));
    }

    /**
     * PATCH /api/v1/sites/{id}/publish
     * Publier (ADMIN)
     */
    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TouristSiteResponseDTO> publish(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.publishSite(id));
    }

    /**
     * PATCH /api/v1/sites/{id}/suspend
     * Suspendre un site (ADMIN)
     */
    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TouristSiteResponseDTO> suspend(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.suspendSite(id));
    }

    /**
     * PATCH /api/v1/sites/{id}/archive
     * Archiver un site (ADMIN)
     */
    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TouristSiteResponseDTO> archive(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.archiveSite(id));
    }

    /**
     * PATCH /api/v1/sites/{id}/featured
     * Mettre en avant / retirer (ADMIN)
     */
    @PatchMapping("/{id}/featured")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TouristSiteResponseDTO> toggleFeatured(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.toggleFeatured(id));
    }

    /**
     * PATCH /api/v1/sites/{id}/verify
     * Certifier un site (ADMIN)
     */
    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TouristSiteResponseDTO> verify(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.verifySite(id));
    }

    // =========================================================
    //  STATISTIQUES
    // =========================================================

    /**
     * GET /api/v1/sites/stats
     * Statistiques globales (ADMIN)
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(siteService.getStats());
    }
}
