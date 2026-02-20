package sn.discover.discoversenegal.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.discover.discoversenegal.entities.Event;
import sn.discover.discoversenegal.entities.EventCategory;
import sn.discover.discoversenegal.entities.EventStatus;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Tous les événements publiés paginés
    Page<Event> findByStatus(EventStatus status, Pageable pageable);

    // Par catégorie et statut
    Page<Event> findByCategoryAndStatus(EventCategory category, EventStatus status, Pageable pageable);

    // Par ville et statut
    Page<Event> findByCityIgnoreCaseAndStatus(String city, EventStatus status, Pageable pageable);

    // Recherche par mot-clé (titre ou description)
    @Query("SELECT e FROM Event e WHERE e.status = :status AND " +
           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Event> searchByKeyword(@Param("keyword") String keyword,
                                @Param("status") EventStatus status,
                                Pageable pageable);

    // Événements à venir
    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.startDateTime >= :now ORDER BY e.startDateTime ASC")
    Page<Event> findUpcoming(@Param("now") LocalDateTime now,
                             @Param("status") EventStatus status,
                             Pageable pageable);

    // Événements gratuits
    Page<Event> findByFreeEntryTrueAndStatus(EventStatus status, Pageable pageable);

    // Événements mis en avant
    Page<Event> findByFeaturedTrueAndStatus(EventStatus status, Pageable pageable);

    // Par organisateur
    Page<Event> findByOrganizerId(Long organizerId, Pageable pageable);

    // Événements entre deux dates
    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.startDateTime BETWEEN :from AND :to ORDER BY e.startDateTime ASC")
    Page<Event> findBetweenDates(@Param("from") LocalDateTime from,
                                 @Param("to") LocalDateTime to,
                                 @Param("status") EventStatus status,
                                 Pageable pageable);

    // Recherche avancée multi-critères
    @Query("SELECT e FROM Event e WHERE " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:city IS NULL OR LOWER(e.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:freeOnly IS NULL OR e.freeEntry = :freeOnly) AND " +
           "(:from IS NULL OR e.startDateTime >= :from) AND " +
           "(:to IS NULL OR e.startDateTime <= :to) AND " +
           "(:keyword IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Event> advancedSearch(@Param("status") EventStatus status,
                               @Param("category") EventCategory category,
                               @Param("city") String city,
                               @Param("freeOnly") Boolean freeOnly,
                               @Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to,
                               @Param("keyword") String keyword,
                               Pageable pageable);

    // Statistiques
    long countByStatus(EventStatus status);
    long countByCategory(EventCategory category);
    long countByOrganizerId(Long organizerId);
}
