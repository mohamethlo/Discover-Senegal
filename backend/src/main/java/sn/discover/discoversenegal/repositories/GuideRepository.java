package sn.discover.discoversenegal.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.discover.discoversenegal.entities.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long>, JpaSpecificationExecutor<Guide> {
    
    // Recherche par utilisateur
    Optional<Guide> findByUserId(Long userId);
    
    // Recherche par email
    Optional<Guide> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // Recherche par ville
    List<Guide> findByCity(String city);
    
    List<Guide> findByCityAndActiveTrue(String city);
    
    // Recherche par région
    List<Guide> findByRegion(String region);
    
    // Recherche par type
    List<Guide> findByGuideType(GuideType guideType);
    
    List<Guide> findByGuideTypeAndActiveTrue(GuideType guideType);
    
    // Guides actifs
    List<Guide> findByActiveTrue();
    
    // Guides acceptant les réservations
    @Query("SELECT g FROM Guide g WHERE g.acceptingBookings = true AND g.active = true")
    List<Guide> findAcceptingBookings();
    
    // Guides mis en avant
    List<Guide> findByFeaturedTrueAndActiveTrue();
    
    // Recherche par langue
    @Query("SELECT DISTINCT g FROM Guide g JOIN g.languages l WHERE l IN :languages AND g.active = true")
    List<Guide> findByLanguages(@Param("languages") List<String> languages);
    
    // Recherche par spécialité
    @Query("SELECT DISTINCT g FROM Guide g JOIN g.specialties s WHERE s IN :specialties AND g.active = true")
    List<Guide> findBySpecialties(@Param("specialties") List<String> specialties);
    
    // Recherche par zone de couverture
    @Query("SELECT DISTINCT g FROM Guide g JOIN g.coverageAreas a WHERE a IN :areas AND g.active = true")
    List<Guide> findByCoverageAreas(@Param("areas") List<String> areas);
    
    // Guides avec licence officielle
    @Query("SELECT g FROM Guide g WHERE g.isOfficiallyLicensed = true AND g.active = true")
    List<Guide> findOfficiallyLicensed();
    
    // Guides avec premiers secours
    @Query("SELECT g FROM Guide g WHERE g.firstAidCertified = true AND g.active = true")
    List<Guide> findFirstAidCertified();
    
    // Guides offrant tours privés
    @Query("SELECT g FROM Guide g WHERE g.offersPrivateTours = true AND g.active = true AND g.acceptingBookings = true")
    List<Guide> findOfferingPrivateTours();
    
    // Guides offrant tours de groupe
    @Query("SELECT g FROM Guide g WHERE g.offersGroupTours = true AND g.active = true AND g.acceptingBookings = true")
    List<Guide> findOfferingGroupTours();
    
    // Guides offrant tours multi-jours
    @Query("SELECT g FROM Guide g WHERE g.offersMultiDayTours = true AND g.active = true")
    List<Guide> findOfferingMultiDayTours();
    
    // Guides avec transport
    @Query("SELECT g FROM Guide g WHERE g.providesTransportation = true AND g.active = true")
    List<Guide> findProvidingTransportation();
    
    // Guides disponibles pour JOJ
    @Query("SELECT g FROM Guide g WHERE g.availableForOlympics = true AND g.active = true")
    List<Guide> findAvailableForOlympics();
    
    // Guides par statut de disponibilité
    List<Guide> findByAvailabilityStatus(AvailabilityStatus status);
    
    @Query("SELECT g FROM Guide g WHERE g.availabilityStatus = 'AVAILABLE' AND g.active = true AND g.acceptingBookings = true")
    List<Guide> findCurrentlyAvailable();
    
    // Top guides par note
    @Query("SELECT g FROM Guide g WHERE g.active = true ORDER BY g.averageRating DESC, g.totalReviews DESC")
    List<Guide> findTopRated();
    
    // Guides les plus expérimentés
    @Query("SELECT g FROM Guide g WHERE g.active = true ORDER BY g.yearsOfExperience DESC, g.totalTours DESC")
    List<Guide> findMostExperienced();
    
    // Guides les plus populaires
    @Query("SELECT g FROM Guide g WHERE g.active = true ORDER BY g.viewCount DESC, g.bookingCount DESC")
    List<Guide> findMostPopular();
    
    // Recherche par note minimale
    @Query("SELECT g FROM Guide g WHERE g.averageRating >= :minRating AND g.active = true ORDER BY g.averageRating DESC")
    List<Guide> findByMinimumRating(@Param("minRating") Double minRating);
    
    // Partenaires actifs
    @Query("SELECT g FROM Guide g WHERE g.partnershipStatus = 'ACTIVE' AND g.verificationStatus = 'VERIFIED' AND g.active = true")
    List<Guide> findActivePartners();
    
    // Guides par statut de partenariat
    List<Guide> findByPartnershipStatus(PartnershipStatus status);
    
    // Guides par statut de vérification
    List<Guide> findByVerificationStatus(VerificationStatus status);
    
    // Recherche par nom
    @Query("SELECT g FROM Guide g WHERE (LOWER(g.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(g.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) AND g.active = true")
    List<Guide> searchByName(@Param("name") String name);
    
    // Statistiques par ville
    @Query("SELECT g.city, COUNT(g) FROM Guide g WHERE g.active = true GROUP BY g.city ORDER BY COUNT(g) DESC")
    List<Object[]> getGuideCountByCity();
    
    // Statistiques par type
    @Query("SELECT g.guideType, COUNT(g) FROM Guide g WHERE g.active = true GROUP BY g.guideType ORDER BY COUNT(g) DESC")
    List<Object[]> getGuideCountByType();
    
    // Statistiques par langue
    @Query("SELECT l, COUNT(g) FROM Guide g JOIN g.languages l WHERE g.active = true GROUP BY l ORDER BY COUNT(g) DESC")
    List<Object[]> getGuideCountByLanguage();
    
    // Guides avec vérification d'antécédents
    @Query("SELECT g FROM Guide g WHERE g.backgroundCheckCompleted = true AND g.active = true")
    List<Guide> findWithBackgroundCheck();
}
