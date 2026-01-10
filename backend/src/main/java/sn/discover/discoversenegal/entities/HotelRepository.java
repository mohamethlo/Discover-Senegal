package sn.discover.discoversenegal.entities;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    
    // Recherche par ville
    List<Hotel> findByCity(String city);
    
    // Recherche par région
    List<Hotel> findByRegion(String region);
    
    // Recherche par catégorie
    List<Hotel> findByCategory(HotelCategory category);
    
    // Recherche par statut de partenariat
    List<Hotel> findByPartnershipStatus(PartnershipStatus status);
    
    // Recherche par statut de vérification
    List<Hotel> findByVerificationStatus(VerificationStatus status);
    
    // Hôtels mis en avant
    List<Hotel> findByFeaturedTrueAndActiveTrue();
    
    // Hôtels actifs
    List<Hotel> findByActiveTrue();
    
    // Recherche par propriétaire
    List<Hotel> findByOwnerId(Long ownerId);
    
    // Recherche par fourchette de prix
    @Query("SELECT h FROM Hotel h WHERE h.priceRangeMin >= :minPrice AND h.priceRangeMax <= :maxPrice AND h.active = true")
    List<Hotel> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    // Recherche par note minimale
    @Query("SELECT h FROM Hotel h WHERE h.averageRating >= :minRating AND h.active = true ORDER BY h.averageRating DESC")
    List<Hotel> findByMinimumRating(@Param("minRating") Double minRating);
    
    // Hôtels les mieux notés
    @Query("SELECT h FROM Hotel h WHERE h.active = true ORDER BY h.averageRating DESC, h.totalReviews DESC")
    List<Hotel> findTopRatedHotels();
    
    // Hôtels les plus populaires
    @Query("SELECT h FROM Hotel h WHERE h.active = true ORDER BY h.viewCount DESC, h.bookingCount DESC")
    List<Hotel> findMostPopularHotels();
    
    // Recherche par proximité géographique
    @Query("SELECT h FROM Hotel h WHERE h.active = true AND " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) * " +
           "cos(radians(h.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(h.latitude)))) <= :radius ORDER BY " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(h.latitude)) * " +
           "cos(radians(h.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(h.latitude))))")
    List<Hotel> findHotelsNearby(@Param("latitude") Double latitude, 
                                  @Param("longitude") Double longitude, 
                                  @Param("radius") Double radius);
    
    // Recherche par équipements
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.amenities a WHERE a IN :amenities AND h.active = true")
    List<Hotel> findByAmenities(@Param("amenities") List<String> amenities);
    
    // Vérifier l'existence par email
    boolean existsByEmail(String email);
    
    // Recherche par nom (insensible à la casse)
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')) AND h.active = true")
    List<Hotel> searchByName(@Param("name") String name);
    
    // Statistiques par ville
    @Query("SELECT h.city, COUNT(h) FROM Hotel h WHERE h.active = true GROUP BY h.city ORDER BY COUNT(h) DESC")
    List<Object[]> getHotelCountByCity();
    
    // Partenaires actifs
    @Query("SELECT h FROM Hotel h WHERE h.partnershipStatus = 'ACTIVE' AND h.verificationStatus = 'VERIFIED' AND h.active = true")
    List<Hotel> findActivePartners();
}