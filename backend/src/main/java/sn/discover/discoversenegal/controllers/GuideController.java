package sn.discover.discoversenegal.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.entities.GuideType;
import sn.discover.discoversenegal.services.GuideService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class GuideController {
    
    private final GuideService guideService;
    
    /**
     * Créer un nouveau guide
     */
    @PostMapping
    public ResponseEntity<GuideResponseDTO> createGuide(@Valid @RequestBody GuideCreateDTO guideDTO) {
        log.info("POST /api/guides - Creating new guide: {} {}", guideDTO.getFirstName(), guideDTO.getLastName());
        GuideResponseDTO response = guideService.createGuide(guideDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Récupérer tous les guides actifs
     */
    @GetMapping
    public ResponseEntity<List<GuideResponseDTO>> getAllGuides() {
        log.info("GET /api/guides - Fetching all active guides");
        List<GuideResponseDTO> guides = guideService.getAllGuides();
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Récupérer un guide par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GuideResponseDTO> getGuideById(@PathVariable Long id) {
        log.info("GET /api/guides/{} - Fetching guide details", id);
        GuideResponseDTO guide = guideService.getGuideById(id);
        return ResponseEntity.ok(guide);
    }
    
    /**
     * Mettre à jour un guide
     */
    @PutMapping("/{id}")
    public ResponseEntity<GuideResponseDTO> updateGuide(
            @PathVariable Long id,
            @Valid @RequestBody GuideUpdateDTO guideDTO) {
        log.info("PUT /api/guides/{} - Updating guide", id);
        GuideResponseDTO response = guideService.updateGuide(id, guideDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le partenariat d'un guide
     */
    @PatchMapping("/{id}/partnership")
    public ResponseEntity<GuideResponseDTO> updatePartnership(
            @PathVariable Long id,
            @Valid @RequestBody GuidePartnershipUpdateDTO partnershipDTO) {
        log.info("PATCH /api/guides/{}/partnership - Updating partnership", id);
        GuideResponseDTO response = guideService.updatePartnership(id, partnershipDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le statut de vérification
     */
    @PatchMapping("/{id}/verification")
    public ResponseEntity<GuideResponseDTO> updateVerification(
            @PathVariable Long id,
            @Valid @RequestBody GuideVerificationUpdateDTO verificationDTO) {
        log.info("PATCH /api/guides/{}/verification - Updating verification status", id);
        GuideResponseDTO response = guideService.updateVerification(id, verificationDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ajouter un avis
     */
    @PatchMapping("/{id}/review")
    public ResponseEntity<GuideResponseDTO> addReview(
            @PathVariable Long id,
            @Valid @RequestBody GuideReviewDTO reviewDTO) {
        log.info("PATCH /api/guides/{}/review - Adding review", id);
        GuideResponseDTO response = guideService.addReview(id, reviewDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de vues
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<GuideResponseDTO> incrementViewCount(@PathVariable Long id) {
        log.info("POST /api/guides/{}/view - Incrementing view count", id);
        GuideResponseDTO response = guideService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de réservations
     */
    @PostMapping("/{id}/booking")
    public ResponseEntity<GuideResponseDTO> incrementBookingCount(@PathVariable Long id) {
        log.info("POST /api/guides/{}/booking - Incrementing booking count", id);
        GuideResponseDTO response = guideService.incrementBookingCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de favoris
     */
    @PostMapping("/{id}/favorite")
    public ResponseEntity<GuideResponseDTO> incrementFavoriteCount(@PathVariable Long id) {
        log.info("POST /api/guides/{}/favorite - Incrementing favorite count", id);
        GuideResponseDTO response = guideService.incrementFavoriteCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Rechercher des guides par ville
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<GuideResponseDTO>> getGuidesByCity(@PathVariable String city) {
        log.info("GET /api/guides/city/{} - Fetching guides by city", city);
        List<GuideResponseDTO> guides = guideService.getGuidesByCity(city);
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Rechercher des guides par type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<GuideResponseDTO>> getGuidesByType(@PathVariable GuideType type) {
        log.info("GET /api/guides/type/{} - Fetching guides by type", type);
        List<GuideResponseDTO> guides = guideService.getGuidesByType(type);
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Rechercher des guides par langues
     */
    @GetMapping("/languages")
    public ResponseEntity<List<GuideResponseDTO>> getGuidesByLanguages(
            @RequestParam List<String> languages) {
        log.info("GET /api/guides/languages - Fetching guides by languages");
        List<GuideResponseDTO> guides = guideService.getGuidesByLanguages(languages);
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Récupérer les guides mis en avant
     */
    @GetMapping("/featured")
    public ResponseEntity<List<GuideResponseDTO>> getFeaturedGuides() {
        log.info("GET /api/guides/featured - Fetching featured guides");
        List<GuideResponseDTO> guides = guideService.getFeaturedGuides();
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Récupérer les guides les mieux notés
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<GuideResponseDTO>> getTopRatedGuides() {
        log.info("GET /api/guides/top-rated - Fetching top rated guides");
        List<GuideResponseDTO> guides = guideService.getTopRatedGuides();
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Récupérer les guides les plus populaires
     */
    @GetMapping("/popular")
    public ResponseEntity<List<GuideResponseDTO>> getMostPopularGuides() {
        log.info("GET /api/guides/popular - Fetching most popular guides");
        List<GuideResponseDTO> guides = guideService.getMostPopularGuides();
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Récupérer les partenaires actifs
     */
    @GetMapping("/partners/active")
    public ResponseEntity<List<GuideResponseDTO>> getActivePartners() {
        log.info("GET /api/guides/partners/active - Fetching active partners");
        List<GuideResponseDTO> guides = guideService.getActivePartners();
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Récupérer les guides disponibles
     */
    @GetMapping("/available")
    public ResponseEntity<List<GuideResponseDTO>> getAvailableGuides() {
        log.info("GET /api/guides/available - Fetching available guides");
        List<GuideResponseDTO> guides = guideService.getAvailableGuides();
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Recherche avancée
     */
    @PostMapping("/search")
    public ResponseEntity<List<GuideResponseDTO>> searchGuides(
            @RequestBody GuideSearchDTO searchDTO) {
        log.info("POST /api/guides/search - Performing advanced search");
        List<GuideResponseDTO> guides = guideService.searchGuides(searchDTO);
        return ResponseEntity.ok(guides);
    }
    
    /**
     * Désactiver un guide (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long id) {
        log.info("DELETE /api/guides/{} - Deactivating guide", id);
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Guide Service is running");
    }
}
