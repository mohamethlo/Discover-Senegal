package sn.discover.discoversenegal.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    
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
    
    // Type de restaurant
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantType type;
    
    // Cuisine(s) servie(s)
    @ElementCollection
    @CollectionTable(name = "restaurant_cuisines", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "cuisine")
    private List<String> cuisineTypes; // Sénégalaise, Française, Italienne, etc.
    
    // Spécialités
    @ElementCollection
    @CollectionTable(name = "restaurant_specialties", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "specialty")
    private List<String> specialties; // Thiéboudienne, Yassa, Mafé, etc.
    
    // Fourchette de prix
    @Enumerated(EnumType.STRING)
    private PriceRange priceRange;
    
    private BigDecimal averagePricePerPerson;
    
    @Builder.Default
    private String currency = "XOF";
    
    // Capacité
    private Integer seatingCapacity;
    
    private Boolean acceptsReservations;
    
    // Horaires d'ouverture
    @Embedded
    private OpeningHours openingHours;
    
    // Services et équipements
    @ElementCollection
    @CollectionTable(name = "restaurant_services", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "service")
    private List<String> services; // WiFi, Climatisation, Terrasse, etc.
    
    @ElementCollection
    @CollectionTable(name = "restaurant_payment_methods", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "payment_method")
    private List<String> paymentMethods; // Espèces, Carte, Orange Money, Wave
    
    @ElementCollection
    @CollectionTable(name = "restaurant_languages", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "language")
    private List<String> spokenLanguages;
    
    @ElementCollection
    @CollectionTable(name = "restaurant_photos", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;
    
    // Menu
    private String menuUrl; // URL du menu PDF/image
    
    @Column(columnDefinition = "TEXT")
    private String menuDescription;
    
    // Options alimentaires
    @Builder.Default
    private Boolean hasVegetarianOptions = false;
    
    @Builder.Default
    private Boolean hasVeganOptions = false;
    
    @Builder.Default
    private Boolean hasHalalOptions = false;
    
    @Builder.Default
    private Boolean hasGlutenFreeOptions = false;
    
    // Aspects culturels
    @Column(columnDefinition = "TEXT")
    private String culturalSignificance;
    
    @Column(columnDefinition = "TEXT")
    private String chefStory; // Histoire du chef
    
    @Column(columnDefinition = "TEXT")
    private String localIngredients; // Ingrédients locaux utilisés
    
    // Atmosphère
    @Enumerated(EnumType.STRING)
    private Ambiance ambiance; // CASUAL, FINE_DINING, ROMANTIC, FAMILY_FRIENDLY
    
    @Builder.Default
    private Boolean hasLiveMusic = false;
    
    @Builder.Default
    private Boolean hasOutdoorSeating = false;
    
    @Builder.Default
    private Boolean hasPrivateDining = false;
    
    // Partenariat
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartnershipStatus partnershipStatus;
    
    private LocalDateTime partnershipStartDate;
    
    private LocalDateTime partnershipEndDate;
    
    private BigDecimal commissionRate;
    
    @Column(columnDefinition = "TEXT")
    private String specialOffersJOJ; // Offres spéciales JOJ 2026
    
    @Builder.Default
    private Boolean cateringAvailable = false; // Traiteur pour événements JOJ
    
    private Integer maxCateringCapacity;
    
    // Vérification
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
    
    private LocalDateTime lastVerifiedAt;
    
    // Certifications
    @Builder.Default
    private Boolean hasHygieneRating = false;
    
    private String hygieneCertificate;
    
    @Builder.Default
    private Boolean isHalalCertified = false;
    
    // Évaluations
    private Double averageRating;
    
    @Builder.Default
    private Integer totalReviews = 0;
    
    private Double foodQualityRating;
    
    private Double serviceRating;
    
    private Double ambianceRating;
    
    private Double valueForMoneyRating;
    
    // Statistiques
    @Builder.Default
    private Integer viewCount = 0;
    
    @Builder.Default
    private Integer reservationCount = 0;
    
    @Builder.Default
    private Integer favoriteCount = 0;
    
    // Administration
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @Builder.Default
    private Boolean featured = false;
    
    @Builder.Default
    private Boolean active = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
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

