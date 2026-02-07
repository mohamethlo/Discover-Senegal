package sn.discover.discoversenegal.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.discover.discoversenegal.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    
    // Recherche par référence unique
    Optional<Booking> findByBookingReference(String bookingReference);
    
    // Recherches par utilisateur
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Recherches par hôtel
    List<Booking> findByHotelId(Long hotelId);
    
    List<Booking> findByHotelIdOrderByCheckInDateDesc(Long hotelId);
    
    // Recherches par statut
    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findByPaymentStatus(PaymentStatus paymentStatus);
    
    // Réservations en attente de confirmation
    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' ORDER BY b.createdAt ASC")
    List<Booking> findPendingBookings();
    
    // Réservations confirmées
    @Query("SELECT b FROM Booking b WHERE b.status = 'CONFIRMED' AND b.isConfirmedByHotel = true")
    List<Booking> findConfirmedBookings();
    
    // Arrivées du jour pour un hôtel
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId AND b.checkInDate = :date AND b.status = 'CONFIRMED'")
    List<Booking> findTodayCheckIns(@Param("hotelId") Long hotelId, @Param("date") LocalDate date);
    
    // Départs du jour pour un hôtel
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId AND b.checkOutDate = :date AND b.status = 'CHECKED_IN'")
    List<Booking> findTodayCheckOuts(@Param("hotelId") Long hotelId, @Param("date") LocalDate date);
    
    // Clients actuellement à l'hôtel
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId AND b.status = 'CHECKED_IN'")
    List<Booking> findCurrentGuests(@Param("hotelId") Long hotelId);
    
    // Réservations par période
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId AND " +
           "((b.checkInDate BETWEEN :startDate AND :endDate) OR " +
           "(b.checkOutDate BETWEEN :startDate AND :endDate) OR " +
           "(b.checkInDate <= :startDate AND b.checkOutDate >= :endDate))")
    List<Booking> findBookingsByPeriod(@Param("hotelId") Long hotelId, 
                                        @Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    // Vérifier disponibilité
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.hotel.id = :hotelId AND " +
           "b.status IN ('CONFIRMED', 'CHECKED_IN') AND " +
           "((b.checkInDate BETWEEN :checkIn AND :checkOut) OR " +
           "(b.checkOutDate BETWEEN :checkIn AND :checkOut) OR " +
           "(b.checkInDate <= :checkIn AND b.checkOutDate >= :checkOut))")
    Long countOverlappingBookings(@Param("hotelId") Long hotelId, 
                                   @Param("checkIn") LocalDate checkIn, 
                                   @Param("checkOut") LocalDate checkOut);
    
    // Réservations JOJ
    @Query("SELECT b FROM Booking b WHERE b.isOlympicBooking = true")
    List<Booking> findOlympicBookings();
    
    @Query("SELECT b FROM Booking b WHERE b.isOlympicBooking = true AND b.olympicDelegation = :delegation")
    List<Booking> findByOlympicDelegation(@Param("delegation") String delegation);
    
    // Réservations par email client
    List<Booking> findByGuestEmail(String email);
    
    List<Booking> findByGuestPhone(String phone);
    
    // Statistiques
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.hotel.id = :hotelId AND b.status = 'COMPLETED'")
    Long countCompletedBookings(@Param("hotelId") Long hotelId);
    
    @Query("SELECT SUM(b.finalAmount) FROM Booking b WHERE b.hotel.id = :hotelId AND b.paymentStatus = 'PAID'")
    Double getTotalRevenue(@Param("hotelId") Long hotelId);
    
    @Query("SELECT AVG(b.rating) FROM Booking b WHERE b.hotel.id = :hotelId AND b.rating IS NOT NULL")
    Double getAverageRating(@Param("hotelId") Long hotelId);
    
    // Réservations récentes
    @Query("SELECT b FROM Booking b WHERE b.createdAt >= :since ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(@Param("since") LocalDateTime since);
    
    // Réservations annulées
    @Query("SELECT b FROM Booking b WHERE b.status = 'CANCELLED' AND b.cancelledAt BETWEEN :startDate AND :endDate")
    List<Booking> findCancelledBookingsBetween(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);
    
    // No-shows
    @Query("SELECT b FROM Booking b WHERE b.status = 'NO_SHOW'")
    List<Booking> findNoShowBookings();
    
    // Réservations nécessitant un transfert aéroport
    @Query("SELECT b FROM Booking b WHERE b.needsAirportTransfer = true AND " +
           "b.checkInDate = :date AND b.status = 'CONFIRMED'")
    List<Booking> findBookingsNeedingTransferForDate(@Param("date") LocalDate date);
    
    // Vérifier existence référence
    boolean existsByBookingReference(String bookingReference);
}