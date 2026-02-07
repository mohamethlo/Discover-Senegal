package sn.discover.discoversenegal.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.discover.discoversenegal.entities.*;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {
    
    // Recherche par ville
    List<Restaurant> findByCity(String city);
    
    List<Restaurant> findByCityAndActiveTrue(String city);
    
    // Recherche par région
    List<Restaurant> findByRegion(String region);
    
    // Recherche par type
    List<Restaurant> findByType(RestaurantType type);
    
    List<Restaurant> findByTypeAndActiveTrue(RestaurantType type);
    
    // Recherche par fourchette de prix
    List<Restaurant> findByPriceRange(PriceRange priceRange);
    
    List<Restaurant> findByPriceRangeAndActiveTrue(PriceRange priceRange);
    
    // Restaurants actifs
    List<Restaurant> findByActiveTrue();
    
    // Restaurants mis en avant
    List<Restaurant> findByFeaturedTrueAndActiveTrue();
    
    // Recherche par cuisine
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.cuisineTypes c WHERE c IN :cuisineTypes AND r.active = true")
    List<Restaurant> findByCuisineTypes(@Param("cuisineTypes") List<String> cuisineTypes);
    
    // Recherche par spécialité
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.specialties s WHERE s IN :specialties AND r.active = true")
    List<Restaurant> findBySpecialties(@Param("specialties") List<String> specialties);
    
    // Restaurants avec options végétariennes
    @Query("SELECT r FROM Restaurant r WHERE r.hasVegetarianOptions = true AND r.active = true")
    List<Restaurant> findWithVegetarianOptions();
    
    // Restaurants avec options vegan
    @Query("SELECT r FROM Restaurant r WHERE r.hasVeganOptions = true AND r.active = true")
    List<Restaurant> findWithVeganOptions();
    
    // Restaurants halal
    @Query("SELECT r FROM Restaurant r WHERE r.hasHalalOptions = true AND r.active = true")
    List<Restaurant> findWithHalalOptions();
    
    @Query("SELECT r FROM Restaurant r WHERE r.isHalalCertified = true AND r.active = true")
    List<Restaurant> findHalalCertified();
    
    // Restaurants acceptant les réservations
    @Query("SELECT r FROM Restaurant r WHERE r.acceptsReservations = true AND r.active = true")
    List<Restaurant> findAcceptingReservations();
    
    // Restaurants avec traiteur
    @Query("SELECT r FROM Restaurant r WHERE r.cateringAvailable = true AND r.active = true")
    List<Restaurant> findWithCateringServices();
    
    // Restaurants avec musique live
    @Query("SELECT r FROM Restaurant r WHERE r.hasLiveMusic = true AND r.active = true")
    List<Restaurant> findWithLiveMusic();
    
    // Restaurants avec terrasse
    @Query("SELECT r FROM Restaurant r WHERE r.hasOutdoorSeating = true AND r.active = true")
    List<Restaurant> findWithOutdoorSeating();
    
    // Recherche par ambiance
    List<Restaurant> findByAmbianceAndActiveTrue(Ambiance ambiance);
    
    // Top restaurants par note
    @Query("SELECT r FROM Restaurant r WHERE r.active = true ORDER BY r.averageRating DESC, r.totalReviews DESC")
    List<Restaurant> findTopRated();
    
    // Restaurants les plus populaires
    @Query("SELECT r FROM Restaurant r WHERE r.active = true ORDER BY r.viewCount DESC, r.reservationCount DESC")
    List<Restaurant> findMostPopular();
    
    // Restaurants les plus favoris
    @Query("SELECT r FROM Restaurant r WHERE r.active = true ORDER BY r.favoriteCount DESC")
    List<Restaurant> findMostFavorited();
    
    // Recherche par note minimale
    @Query("SELECT r FROM Restaurant r WHERE r.averageRating >= :minRating AND r.active = true ORDER BY r.averageRating DESC")
    List<Restaurant> findByMinimumRating(@Param("minRating") Double minRating);
    
    // Recherche géographique (proximité)
    @Query("SELECT r FROM Restaurant r WHERE r.active = true AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(r.latitude)))) <= :radius ORDER BY " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * " +
           "cos(radians(r.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(r.latitude))))")
    List<Restaurant> findRestaurantsNearby(@Param("latitude") Double latitude,
                                           @Param("longitude") Double longitude,
                                           @Param("radius") Double radius);
    
    // Partenaires actifs
    @Query("SELECT r FROM Restaurant r WHERE r.partnershipStatus = 'ACTIVE' AND r.verificationStatus = 'VERIFIED' AND r.active = true")
    List<Restaurant> findActivePartners();
    
    // Restaurants par statut de partenariat
    List<Restaurant> findByPartnershipStatus(PartnershipStatus status);
    
    // Restaurants par statut de vérification
    List<Restaurant> findByVerificationStatus(VerificationStatus status);
    
    // Restaurants par propriétaire
    List<Restaurant> findByOwnerId(Long ownerId);
    
    // Vérifier existence par email
    boolean existsByEmail(String email);
    
    // Recherche par nom
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND r.active = true")
    List<Restaurant> searchByName(@Param("name") String name);
    
    // Statistiques par ville
    @Query("SELECT r.city, COUNT(r) FROM Restaurant r WHERE r.active = true GROUP BY r.city ORDER BY COUNT(r) DESC")
    List<Object[]> getRestaurantCountByCity();
    
    // Statistiques par type de cuisine
    @Query("SELECT c, COUNT(r) FROM Restaurant r JOIN r.cuisineTypes c WHERE r.active = true GROUP BY c ORDER BY COUNT(r) DESC")
    List<Object[]> getRestaurantCountByCuisine();
    
    // Restaurants ouverts maintenant (nécessite logique complexe côté service)
    @Query("SELECT r FROM Restaurant r WHERE r.active = true")
    List<Restaurant> findAllActive();
}
