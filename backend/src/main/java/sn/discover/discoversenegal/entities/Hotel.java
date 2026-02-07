package sn.discover.discoversenegal.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String city;
    
    private String region;
    
    private String postalCode;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(unique = true)
    private String email;
    
    private String phone;
    
    private String website;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HotelCategory category; // 1 à 5 étoiles, boutique, resort, etc.
    
    private Integer starRating; // 1-5
    
    private Integer totalRooms;
    
    private BigDecimal priceRangeMin;
    
    private BigDecimal priceRangeMax;
    
    @Column(nullable = false)
    @Builder.Default
    private String currency = "XOF"; // Franc CFA par défaut
    
    @ElementCollection
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity")
    private List<String> amenities; // WiFi, Piscine, Restaurant, Spa, etc.
    
    @ElementCollection
    @CollectionTable(name = "hotel_languages", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "language")
    private List<String> spokenLanguages;
    
    @ElementCollection
    @CollectionTable(name = "hotel_photos", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;
    
    @Column(columnDefinition = "TEXT")
    private String culturalHighlights; // Spécificités culturelles de l'hôtel
    
    @Column(columnDefinition = "TEXT")
    private String sustainabilityPractices; // Pratiques écologiques
    
    private String checkInTime;
    
    private String checkOutTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartnershipStatus partnershipStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
    
    private LocalDateTime partnershipStartDate;
    
    private LocalDateTime partnershipEndDate;
    
    private BigDecimal commissionRate; // Taux de commission en %
    
    @Column(columnDefinition = "TEXT")
    private String specialOffers; // Offres spéciales pour les JOJ
    
    private Double averageRating;
    
    @Builder.Default
    private Integer totalReviews = 0;
    
    @Builder.Default
    private Integer viewCount = 0;
    
    @Builder.Default
    private Integer bookingCount = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner; // Gestionnaire/propriétaire de l'hôtel
    
    @Builder.Default
    private boolean featured = false; // Mise en avant sur la plateforme
    
    @Builder.Default
    private boolean active = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastVerifiedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (verificationStatus == null) {
            verificationStatus = VerificationStatus.PENDING;
        }
        if (partnershipStatus == null) {
            partnershipStatus = PartnershipStatus.PROSPECT;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}