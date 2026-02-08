package sn.discover.discoversenegal.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "guides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guide {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Informations personnelles
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private String nationality;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    private String whatsapp;
    
    private String photoUrl;
    
    // Localisation
    @Column(nullable = false)
    private String city; // Ville principale d'opération
    
    private String region;
    
    @ElementCollection
    @CollectionTable(name = "guide_coverage_areas", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "area")
    private List<String> coverageAreas; // Zones couvertes (Dakar, Saint-Louis, etc.)
    
    // Langues parlées
    @ElementCollection
    @CollectionTable(name = "guide_languages", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "language")
    private List<String> languages; // Français, Anglais, Wolof, Espagnol, etc.
    
    @Column(nullable = false)
    private String primaryLanguage; // Langue principale
    
    // Spécialisations
    @ElementCollection
    @CollectionTable(name = "guide_specialties", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "specialty")
    private List<String> specialties; // Histoire, Nature, Culture, Gastronomie, etc.
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GuideType guideType; // CULTURAL, NATURE, ADVENTURE, CITY, HISTORICAL, etc.
    
    // Expérience
    private Integer yearsOfExperience;
    
    private LocalDate startedGuidingDate;
    
    @Column(columnDefinition = "TEXT")
    private String biography; // Présentation du guide
    
    @Column(columnDefinition = "TEXT")
    private String expertise; // Domaines d'expertise détaillés
    
    // Certifications et formations
    @ElementCollection
    @CollectionTable(name = "guide_certifications", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "certification")
    private List<String> certifications; // Guide officiel, Premiers secours, etc.
    
    @Builder.Default
    private Boolean isOfficiallyLicensed = false; // Licence officielle de guide
    
    private String licenseNumber;
    
    private LocalDate licenseExpiryDate;
    
    @ElementCollection
    @CollectionTable(name = "guide_qualifications", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "qualification")
    private List<String> qualifications; // Diplômes, formations
    
    // Services proposés
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType; // INDIVIDUAL, GROUP, BOTH
    
    private Integer maxGroupSize; // Taille max du groupe
    
    @Builder.Default
    private Boolean offersPrivateTours = false;
    
    @Builder.Default
    private Boolean offersGroupTours = false;
    
    @Builder.Default
    private Boolean offersMultiDayTours = false;
    
    @Builder.Default
    private Boolean providesTransportation = false;
    
    private String transportationType; // Voiture, 4x4, Minibus, etc.
    
    @Builder.Default
    private Boolean providesAccommodationBooking = false;
    
    @Builder.Default
    private Boolean offersCustomItineraries = false;
    
    // Tarification
    private BigDecimal hourlyRate; // Tarif horaire
    
    private BigDecimal halfDayRate; // Tarif demi-journée
    
    private BigDecimal fullDayRate; // Tarif journée complète
    
    private BigDecimal multiDayRate; // Tarif par jour pour circuits multi-jours
    
    @Builder.Default
    private String currency = "XOF";
    
    @Column(columnDefinition = "TEXT")
    private String pricingNotes; // Notes sur la tarification
    
    // Disponibilité
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus; // AVAILABLE, BUSY, UNAVAILABLE
    
    @ElementCollection
    @CollectionTable(name = "guide_available_days", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "day")
    private List<String> availableDays; // Lundi, Mardi, etc.
    
    @Builder.Default
    private Boolean availableWeekends = true;
    
    @Builder.Default
    private Boolean availableHolidays = true;
    
    private Integer advanceBookingDays; // Nombre de jours de préavis minimum
    
    // Équipements fournis
    @ElementCollection
    @CollectionTable(name = "guide_equipment", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "equipment")
    private List<String> equipmentProvided; // Jumelles, Eau, Collations, etc.
    
    // Spécialités JOJ 2026
    @Builder.Default
    private Boolean availableForOlympics = false;
    
    @Column(columnDefinition = "TEXT")
    private String olympicExperience; // Expérience avec événements sportifs
    
    @ElementCollection
    @CollectionTable(name = "guide_olympic_specialties", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "specialty")
    private List<String> olympicSpecialties; // Accompagnement délégations, protocole, etc.
    
    // Partenariat
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartnershipStatus partnershipStatus;
    
    private LocalDateTime partnershipStartDate;
    
    private LocalDateTime partnershipEndDate;
    
    private BigDecimal commissionRate;
    
    @Column(columnDefinition = "TEXT")
    private String specialOffersJOJ;
    
    // Vérification
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus;
    
    private LocalDateTime lastVerifiedAt;
    
    @Builder.Default
    private Boolean backgroundCheckCompleted = false;
    
    private LocalDate backgroundCheckDate;
    
    @Builder.Default
    private Boolean firstAidCertified = false;
    
    // Évaluations
    private Double averageRating;
    
    @Builder.Default
    private Integer totalReviews = 0;
    
    private Double professionalismRating;
    
    private Double knowledgeRating;
    
    private Double communicationRating;
    
    private Double punctualityRating;
    
    // Statistiques
    @Builder.Default
    private Integer totalTours = 0; // Nombre total de tours effectués
    
    @Builder.Default
    private Integer totalClients = 0;
    
    @Builder.Default
    private Integer viewCount = 0;
    
    @Builder.Default
    private Integer bookingCount = 0;
    
    @Builder.Default
    private Integer favoriteCount = 0;
    
    // Réseaux sociaux et site web
    private String websiteUrl;
    
    private String facebookUrl;
    
    private String instagramUrl;
    
    private String linkedinUrl;
    
    // Administration
    @Builder.Default
    private Boolean featured = false;
    
    @Builder.Default
    private Boolean active = true;
    
    @Builder.Default
    private Boolean acceptingBookings = true;
    
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
        if (availabilityStatus == null) {
            availabilityStatus = AvailabilityStatus.AVAILABLE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

