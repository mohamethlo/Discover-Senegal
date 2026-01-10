package sn.discover.discoversenegal.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.services.HotelService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HotelController {
    
    private final HotelService hotelService;
    
    /**
     * Créer un nouvel hôtel
     */
    @PostMapping
    public ResponseEntity<HotelResponseDTO> createHotel(@Valid @RequestBody HotelCreateDTO hotelDTO) {
        log.info("POST /api/hotels - Creating new hotel: {}", hotelDTO.getName());
        HotelResponseDTO response = hotelService.createHotel(hotelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Récupérer tous les hôtels actifs
     */
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> getAllHotels() {
        log.info("GET /api/hotels - Fetching all active hotels");
        List<HotelResponseDTO> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Récupérer un hôtel par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(@PathVariable Long id) {
        log.info("GET /api/hotels/{} - Fetching hotel details", id);
        HotelResponseDTO hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }
    
    /**
     * Mettre à jour un hôtel
     */
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelUpdateDTO hotelDTO) {
        log.info("PUT /api/hotels/{} - Updating hotel", id);
        HotelResponseDTO response = hotelService.updateHotel(id, hotelDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le partenariat d'un hôtel
     */
    @PatchMapping("/{id}/partnership")
    public ResponseEntity<HotelResponseDTO> updatePartnership(
            @PathVariable Long id,
            @Valid @RequestBody PartnershipUpdateDTO partnershipDTO) {
        log.info("PATCH /api/hotels/{}/partnership - Updating partnership", id);
        HotelResponseDTO response = hotelService.updatePartnership(id, partnershipDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le statut de vérification
     */
    @PatchMapping("/{id}/verification")
    public ResponseEntity<HotelResponseDTO> updateVerification(
            @PathVariable Long id,
            @Valid @RequestBody VerificationUpdateDTO verificationDTO) {
        log.info("PATCH /api/hotels/{}/verification - Updating verification status", id);
        HotelResponseDTO response = hotelService.updateVerification(id, verificationDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de vues
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<HotelResponseDTO> incrementViewCount(@PathVariable Long id) {
        log.info("POST /api/hotels/{}/view - Incrementing view count", id);
        HotelResponseDTO response = hotelService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de réservations
     */
    @PostMapping("/{id}/booking")
    public ResponseEntity<HotelResponseDTO> incrementBookingCount(@PathVariable Long id) {
        log.info("POST /api/hotels/{}/booking - Incrementing booking count", id);
        HotelResponseDTO response = hotelService.incrementBookingCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Rechercher des hôtels par ville
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<HotelResponseDTO>> getHotelsByCity(@PathVariable String city) {
        log.info("GET /api/hotels/city/{} - Fetching hotels by city", city);
        List<HotelResponseDTO> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Rechercher des hôtels par région
     */
    @GetMapping("/region/{region}")
    public ResponseEntity<List<HotelResponseDTO>> getHotelsByRegion(@PathVariable String region) {
        log.info("GET /api/hotels/region/{} - Fetching hotels by region", region);
        List<HotelResponseDTO> hotels = hotelService.getHotelsByRegion(region);
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Récupérer les hôtels mis en avant
     */
    @GetMapping("/featured")
    public ResponseEntity<List<HotelResponseDTO>> getFeaturedHotels() {
        log.info("GET /api/hotels/featured - Fetching featured hotels");
        List<HotelResponseDTO> hotels = hotelService.getFeaturedHotels();
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Récupérer les hôtels les mieux notés
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<HotelResponseDTO>> getTopRatedHotels() {
        log.info("GET /api/hotels/top-rated - Fetching top rated hotels");
        List<HotelResponseDTO> hotels = hotelService.getTopRatedHotels();
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Récupérer les hôtels les plus populaires
     */
    @GetMapping("/popular")
    public ResponseEntity<List<HotelResponseDTO>> getMostPopularHotels() {
        log.info("GET /api/hotels/popular - Fetching most popular hotels");
        List<HotelResponseDTO> hotels = hotelService.getMostPopularHotels();
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Récupérer les partenaires actifs
     */
    @GetMapping("/partners/active")
    public ResponseEntity<List<HotelResponseDTO>> getActivePartners() {
        log.info("GET /api/hotels/partners/active - Fetching active partners");
        List<HotelResponseDTO> hotels = hotelService.getActivePartners();
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Recherche avancée d'hôtels
     */
    @PostMapping("/search")
    public ResponseEntity<List<HotelResponseDTO>> searchHotels(
            @RequestBody HotelSearchDTO searchDTO) {
        log.info("POST /api/hotels/search - Performing advanced search");
        List<HotelResponseDTO> hotels = hotelService.searchHotels(searchDTO);
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Désactiver un hôtel (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        log.info("DELETE /api/hotels/{} - Deactivating hotel", id);
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Hotel Service is running");
    }
}