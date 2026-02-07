package sn.discover.discoversenegal.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.discover.discoversenegal.dto.*;
import sn.discover.discoversenegal.entities.RestaurantType;
import sn.discover.discoversenegal.entities.RestaurantVerificationUpdateDTO;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    /**
     * Créer un nouveau restaurant
     */
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantCreateDTO restaurantDTO) {
        log.info("POST /api/restaurants - Creating new restaurant: {}", restaurantDTO.getName());
        RestaurantResponseDTO response = restaurantService.createRestaurant(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Récupérer tous les restaurants actifs
     */
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        log.info("GET /api/restaurants - Fetching all active restaurants");
        List<RestaurantResponseDTO> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Récupérer un restaurant par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Long id) {
        log.info("GET /api/restaurants/{} - Fetching restaurant details", id);
        RestaurantResponseDTO restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }
    
    /**
     * Mettre à jour un restaurant
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateDTO restaurantDTO) {
        log.info("PUT /api/restaurants/{} - Updating restaurant", id);
        RestaurantResponseDTO response = restaurantService.updateRestaurant(id, restaurantDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le partenariat d'un restaurant
     */
    @PatchMapping("/{id}/partnership")
    public ResponseEntity<RestaurantResponseDTO> updatePartnership(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantPartnershipUpdateDTO partnershipDTO) {
        log.info("PATCH /api/restaurants/{}/partnership - Updating partnership", id);
        RestaurantResponseDTO response = restaurantService.updatePartnership(id, partnershipDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mettre à jour le statut de vérification
     */
    @PatchMapping("/{id}/verification")
    public ResponseEntity<RestaurantResponseDTO> updateVerification(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantVerificationUpdateDTO verificationDTO) {
        log.info("PATCH /api/restaurants/{}/verification - Updating verification status", id);
        RestaurantResponseDTO response = restaurantService.updateVerification(id, verificationDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ajouter un avis
     */
    @PatchMapping("/{id}/review")
    public ResponseEntity<RestaurantResponseDTO> addReview(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantReviewDTO reviewDTO) {
        log.info("PATCH /api/restaurants/{}/review - Adding review", id);
        RestaurantResponseDTO response = restaurantService.addReview(id, reviewDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de vues
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<RestaurantResponseDTO> incrementViewCount(@PathVariable Long id) {
        log.info("POST /api/restaurants/{}/view - Incrementing view count", id);
        RestaurantResponseDTO response = restaurantService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de réservations
     */
    @PostMapping("/{id}/reservation")
    public ResponseEntity<RestaurantResponseDTO> incrementReservationCount(@PathVariable Long id) {
        log.info("POST /api/restaurants/{}/reservation - Incrementing reservation count", id);
        RestaurantResponseDTO response = restaurantService.incrementReservationCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Incrémenter le compteur de favoris
     */
    @PostMapping("/{id}/favorite")
    public ResponseEntity<RestaurantResponseDTO> incrementFavoriteCount(@PathVariable Long id) {
        log.info("POST /api/restaurants/{}/favorite - Incrementing favorite count", id);
        RestaurantResponseDTO response = restaurantService.incrementFavoriteCount(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Rechercher des restaurants par ville
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByCity(@PathVariable String city) {
        log.info("GET /api/restaurants/city/{} - Fetching restaurants by city", city);
        List<RestaurantResponseDTO> restaurants = restaurantService.getRestaurantsByCity(city);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Rechercher des restaurants par type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByType(@PathVariable RestaurantType type) {
        log.info("GET /api/restaurants/type/{} - Fetching restaurants by type", type);
        List<RestaurantResponseDTO> restaurants = restaurantService.getRestaurantsByType(type);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Rechercher des restaurants par cuisine
     */
    @GetMapping("/cuisine")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByCuisine(
            @RequestParam List<String> cuisineTypes) {
        log.info("GET /api/restaurants/cuisine - Fetching restaurants by cuisine types");
        List<RestaurantResponseDTO> restaurants = restaurantService.getRestaurantsByCuisine(cuisineTypes);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Récupérer les restaurants mis en avant
     */
    @GetMapping("/featured")
    public ResponseEntity<List<RestaurantResponseDTO>> getFeaturedRestaurants() {
        log.info("GET /api/restaurants/featured - Fetching featured restaurants");
        List<RestaurantResponseDTO> restaurants = restaurantService.getFeaturedRestaurants();
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Récupérer les restaurants les mieux notés
     */
    @GetMapping("/top-rated")
    public ResponseEntity<List<RestaurantResponseDTO>> getTopRatedRestaurants() {
        log.info("GET /api/restaurants/top-rated - Fetching top rated restaurants");
        List<RestaurantResponseDTO> restaurants = restaurantService.getTopRatedRestaurants();
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Récupérer les restaurants les plus populaires
     */
    @GetMapping("/popular")
    public ResponseEntity<List<RestaurantResponseDTO>> getMostPopularRestaurants() {
        log.info("GET /api/restaurants/popular - Fetching most popular restaurants");
        List<RestaurantResponseDTO> restaurants = restaurantService.getMostPopularRestaurants();
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Récupérer les partenaires actifs
     */
    @GetMapping("/partners/active")
    public ResponseEntity<List<RestaurantResponseDTO>> getActivePartners() {
        log.info("GET /api/restaurants/partners/active - Fetching active partners");
        List<RestaurantResponseDTO> restaurants = restaurantService.getActivePartners();
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Recherche avancée
     */
    @PostMapping("/search")
    public ResponseEntity<List<RestaurantResponseDTO>> searchRestaurants(
            @RequestBody RestaurantSearchDTO searchDTO) {
        log.info("POST /api/restaurants/search - Performing advanced search");
        List<RestaurantResponseDTO> restaurants = restaurantService.searchRestaurants(searchDTO);
        return ResponseEntity.ok(restaurants);
    }
    
    /**
     * Désactiver un restaurant (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        log.info("DELETE /api/restaurants/{} - Deactivating restaurant", id);
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Restaurant Service is running");
    }
}
