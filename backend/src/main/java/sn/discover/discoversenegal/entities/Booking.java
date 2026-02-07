package sn.discover.discoversenegal.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String bookingReference; // Ex: BKG-2026-001234
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Client qui réserve
    
    // Informations du client
    @Column(nullable = false)
    private String guestFirstName;
    
    @Column(nullable = false)
    private String guestLastName;
    
    @Column(nullable = false)
    private String guestEmail;
    
    @Column(nullable = false)
    private String guestPhone;
    
    private String guestCountry;
    
    private String guestNationality;
    
    // Dates de séjour
    @Column(nullable = false)
    private LocalDate checkInDate;
    
    @Column(nullable = false)
    private LocalDate checkOutDate;
    
    private Integer numberOfNights; // Calculé automatiquement
    
    // Occupants
    @Column(nullable = false)
    private Integer numberOfAdults;
    
    @Builder.Default
    private Integer numberOfChildren = 0;
    
    @Column(nullable = false)
    private Integer numberOfRooms;
    
    private String roomType; // Simple, Double, Suite, etc.
    
    // Informations financières
    @Column(nullable = false)
    private BigDecimal pricePerNight;
    
    @Column(nullable = false)
    private BigDecimal totalPrice; // Calculé: pricePerNight * numberOfNights * numberOfRooms
    
    private BigDecimal taxes;
    
    private BigDecimal serviceFees;
    
    @Column(nullable = false)
    private BigDecimal finalAmount; // Total + taxes + fees
    
    @Column(nullable = false)
    @Builder.Default
    private String currency = "XOF";
    
    private BigDecimal discountAmount;
    
    private String discountCode; // Code promo appliqué
    
    // Statut de la réservation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    
    // Paiement
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private String transactionId; // ID transaction paiement
    
    private LocalDateTime paymentDate;
    
    // Demandes spéciales
    @Column(columnDefinition = "TEXT")
    private String specialRequests;
    
    @Builder.Default
    private Boolean needsAirportTransfer = false;
    
    private String flightNumber;
    
    private LocalDateTime arrivalTime;
    
    // Contexte JOJ 2026
    @Builder.Default
    private Boolean isOlympicBooking = false; // Réservation liée aux JOJ
    
    private String olympicDelegation; // Nom de la délégation si applicable
    
    private String olympicRole; // Athlete, Coach, Official, Family, etc.
    
    // Annulation
    private LocalDateTime cancelledAt;
    
    private String cancellationReason;
    
    private BigDecimal refundAmount;
    
    @Enumerated(EnumType.STRING)
    private CancellationPolicy cancellationPolicy;
    
    // Confirmation
    @Builder.Default
    private Boolean isConfirmedByHotel = false;
    
    private LocalDateTime confirmedByHotelAt;
    
    private String hotelConfirmationNotes;
    
    // Check-in/Check-out effectif
    private LocalDateTime actualCheckInTime;
    
    private LocalDateTime actualCheckOutTime;
    
    // Évaluation
    private Integer rating; // 1-5 étoiles
    
    @Column(columnDefinition = "TEXT")
    private String review;
    
    private LocalDateTime reviewedAt;
    
    // Communication
    private String languagePreference; // Français, English, Wolof, etc.
    
    // Source de la réservation
    @Enumerated(EnumType.STRING)
    private BookingSource source; // WEB, MOBILE_APP, PHONE, EMAIL, PARTNER
    
    // Metadata
    private String userIpAddress;
    
    private String userAgent;
    
    // Timestamps
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        
        if (status == null) {
            status = BookingStatus.PENDING;
        }
        
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
        
        // Générer référence unique
        if (bookingReference == null) {
            bookingReference = generateBookingReference();
        }
        
        // Calculer nombre de nuits
        if (checkInDate != null && checkOutDate != null) {
            numberOfNights = (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        
        // Calculer prix total si pas défini
        if (totalPrice == null && pricePerNight != null && numberOfNights != null && numberOfRooms != null) {
            totalPrice = pricePerNight.multiply(BigDecimal.valueOf(numberOfNights))
                                      .multiply(BigDecimal.valueOf(numberOfRooms));
        }
        
        // Calculer montant final
        if (finalAmount == null) {
            finalAmount = calculateFinalAmount();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        
        // Recalculer montant final si nécessaire
        finalAmount = calculateFinalAmount();
    }
    
    private String generateBookingReference() {
        return "BKG-" + LocalDate.now().getYear() + "-" + 
               String.format("%06d", System.currentTimeMillis() % 1000000);
    }
    
    private BigDecimal calculateFinalAmount() {
        BigDecimal amount = totalPrice != null ? totalPrice : BigDecimal.ZERO;
        
        if (taxes != null) {
            amount = amount.add(taxes);
        }
        
        if (serviceFees != null) {
            amount = amount.add(serviceFees);
        }
        
        if (discountAmount != null) {
            amount = amount.subtract(discountAmount);
        }
        
        return amount;
    }
}