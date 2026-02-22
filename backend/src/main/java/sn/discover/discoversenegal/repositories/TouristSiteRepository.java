package sn.discover.discoversenegal.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.discover.discoversenegal.entities.*;

import java.util.List;

@Repository
public interface TouristSiteRepository extends JpaRepository<TouristSite, Long> {

    // --- Listings de base ---
    Page<TouristSite> findByStatus(SiteStatus status, Pageable pageable);
    Page<TouristSite> findByCategoryAndStatus(SiteCategory category, SiteStatus status, Pageable pageable);
    Page<TouristSite> findByCityIgnoreCaseAndStatus(String city, SiteStatus status, Pageable pageable);
    Page<TouristSite> findByRegionIgnoreCaseAndStatus(String region, SiteStatus status, Pageable pageable);

    // --- UNESCO / Patrimoine ---
    Page<TouristSite> findByUnescoListedTrueAndStatus(SiteStatus status, Pageable pageable);
    Page<TouristSite> findByNationalHeritageTrueAndStatus(SiteStatus status, Pageable pageable);

    // --- Featured & Gratuits ---
    Page<TouristSite> findByFeaturedTrueAndStatus(SiteStatus status, Pageable pageable);
    Page<TouristSite> findByFreeEntryTrueAndStatus(SiteStatus status, Pageable pageable);

    // --- Accessibilité ---
    Page<TouristSite> findByWheelchairAccessibleTrueAndStatus(SiteStatus status, Pageable pageable);
    Page<TouristSite> findByAccessibilityAndStatus(AccessibilityLevel level, SiteStatus status, Pageable pageable);

    // --- Visite guidée dispo ---
    Page<TouristSite> findByGuidedTourAvailableTrueAndStatus(SiteStatus status, Pageable pageable);

    // --- Par créateur ---
    Page<TouristSite> findByCreatedById(Long userId, Pageable pageable);

    // --- Recherche plein texte ---
    @Query("SELECT s FROM TouristSite s WHERE s.status = :status AND (" +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.region) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.history) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<TouristSite> searchByKeyword(@Param("keyword") String keyword,
                                       @Param("status") SiteStatus status,
                                       Pageable pageable);

    // --- Recherche géographique (rayon en km via formule Haversine) ---
    @Query(value = "SELECT * FROM tourist_sites s WHERE s.status = 'PUBLIE' AND " +
                   "(6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * " +
                   "cos(radians(s.longitude) - radians(:lng)) + " +
                   "sin(radians(:lat)) * sin(radians(s.latitude)))) <= :radiusKm " +
                   "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) * " +
                   "cos(radians(s.longitude) - radians(:lng)) + " +
                   "sin(radians(:lat)) * sin(radians(s.latitude)))) ASC",
           nativeQuery = true)
    List<TouristSite> findNearby(@Param("lat") Double latitude,
                                  @Param("lng") Double longitude,
                                  @Param("radiusKm") Double radiusKm);

    // --- Recherche avancée multi-critères ---
    @Query("SELECT s FROM TouristSite s WHERE " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:category IS NULL OR s.category = :category) AND " +
           "(:city IS NULL OR LOWER(s.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:region IS NULL OR LOWER(s.region) LIKE LOWER(CONCAT('%', :region, '%'))) AND " +
           "(:freeOnly IS NULL OR s.freeEntry = :freeOnly) AND " +
           "(:unescoOnly IS NULL OR s.unescoListed = :unescoOnly) AND " +
           "(:accessibility IS NULL OR s.accessibility = :accessibility) AND " +
           "(:guidedTour IS NULL OR s.guidedTourAvailable = :guidedTour) AND " +
           "(:wheelchairOk IS NULL OR s.wheelchairAccessible = :wheelchairOk) AND " +
           "(:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<TouristSite> advancedSearch(@Param("status") SiteStatus status,
                                      @Param("category") SiteCategory category,
                                      @Param("city") String city,
                                      @Param("region") String region,
                                      @Param("freeOnly") Boolean freeOnly,
                                      @Param("unescoOnly") Boolean unescoOnly,
                                      @Param("accessibility") AccessibilityLevel accessibility,
                                      @Param("guidedTour") Boolean guidedTour,
                                      @Param("wheelchairOk") Boolean wheelchairOk,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);

    // --- Top sites par note ---
    @Query("SELECT s FROM TouristSite s WHERE s.status = :status AND s.totalReviews >= :minReviews " +
           "ORDER BY s.averageRating DESC")
    Page<TouristSite> findTopRated(@Param("status") SiteStatus status,
                                    @Param("minReviews") int minReviews,
                                    Pageable pageable);

    // --- Top sites les plus vus ---
    @Query("SELECT s FROM TouristSite s WHERE s.status = :status ORDER BY s.totalViews DESC")
    Page<TouristSite> findMostViewed(@Param("status") SiteStatus status, Pageable pageable);

    // --- Incrémenter les compteurs (atomic) ---
    @Modifying
    @Query("UPDATE TouristSite s SET s.totalViews = s.totalViews + 1 WHERE s.id = :id")
    void incrementViews(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TouristSite s SET s.totalFavorites = s.totalFavorites + 1 WHERE s.id = :id")
    void incrementFavorites(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TouristSite s SET s.totalFavorites = s.totalFavorites - 1 WHERE s.id = :id AND s.totalFavorites > 0")
    void decrementFavorites(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TouristSite s SET s.averageRating = :rating, s.totalReviews = :count WHERE s.id = :id")
    void updateRatingStats(@Param("id") Long id,
                           @Param("rating") Double rating,
                           @Param("count") Integer count);

    // --- Statistiques ---
    long countByStatus(SiteStatus status);
    long countByCategory(SiteCategory category);
    long countByUnescoListedTrue();
    long countByNationalHeritageTrue();

    // --- Villes distinctes avec sites publiés ---
    @Query("SELECT DISTINCT s.city FROM TouristSite s WHERE s.status = 'PUBLIE' ORDER BY s.city ASC")
    List<String> findDistinctCities();

    // --- Régions distinctes avec sites publiés ---
    @Query("SELECT DISTINCT s.region FROM TouristSite s WHERE s.status = 'PUBLIE' ORDER BY s.region ASC")
    List<String> findDistinctRegions();
}
