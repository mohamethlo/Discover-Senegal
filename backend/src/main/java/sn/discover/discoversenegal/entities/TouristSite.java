package sn.discover.discoversenegal.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tourist_sites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TouristSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------------
    // Informations générales
    // -------------------------------------------------------
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String history;         // Histoire et contexte culturel

    @Column(columnDefinition = "TEXT")
    private String practicalInfo;   // Infos pratiques (comment s'y rendre, quoi apporter...)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SiteCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SiteStatus status = SiteStatus.BROUILLON;

    // -------------------------------------------------------
    // Localisation
    // -------------------------------------------------------
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String region;

    private String country;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private String googleMapsUrl;

    // -------------------------------------------------------
    // Horaires d'ouverture
    // -------------------------------------------------------
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean openMonday;
    private boolean openTuesday;
    private boolean openWednesday;
    private boolean openThursday;
    private boolean openFriday;
    private boolean openSaturday;
    private boolean openSunday;
    private String specialHoursNote; // Ex: "Fermé les jours fériés religieux"

    // -------------------------------------------------------
    // Tarification
    // -------------------------------------------------------
    private boolean freeEntry;

    @Column(precision = 10, scale = 2)
    private BigDecimal adultPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal childPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal studentPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal foreignerPrice;

    private String currency;

    private String ticketInfo; // Infos complémentaires billetterie

    // -------------------------------------------------------
    // Caractéristiques
    // -------------------------------------------------------
    @Enumerated(EnumType.STRING)
    private AccessibilityLevel accessibility;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "site_best_periods", joinColumns = @JoinColumn(name = "site_id"))
    @Column(name = "period")
    @Builder.Default
    private List<BestPeriod> bestPeriods = new ArrayList<>();

    private Integer visitDurationMinutes; // Durée moyenne de visite en minutes

    private boolean guidedTourAvailable;
    private boolean audioGuideAvailable;
    private boolean wheelchairAccessible;
    private boolean parkingAvailable;
    private boolean restroomAvailable;
    private boolean restaurantNearby;
    private boolean photoAllowed;
    private boolean droneAllowed;

    // -------------------------------------------------------
    // Patrimoine & Classement
    // -------------------------------------------------------
    private boolean unescoListed;
    private String unescoSince;          // Année de classement UNESCO

    private boolean nationalHeritage;    // Patrimoine national sénégalais
    private String heritageClassification; // Texte libre sur le classement

    // -------------------------------------------------------
    // Médias
    // -------------------------------------------------------
    private String coverImageUrl;

    @ElementCollection
    @CollectionTable(name = "site_images", joinColumns = @JoinColumn(name = "site_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private String videoUrl;

    private String virtualTourUrl;   // Lien visite virtuelle 360°

    // -------------------------------------------------------
    // Contact
    // -------------------------------------------------------
    private String contactPhone;
    private String contactEmail;
    private String website;
    private String facebookUrl;
    private String instagramUrl;

    // -------------------------------------------------------
    // Tags et langues de visite
    // -------------------------------------------------------
    @ElementCollection
    @CollectionTable(name = "site_tags", joinColumns = @JoinColumn(name = "site_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "site_languages", joinColumns = @JoinColumn(name = "site_id"))
    @Column(name = "language")
    @Builder.Default
    private List<String> availableLanguages = new ArrayList<>();

    // -------------------------------------------------------
    // Notes et statistiques (dénormalisées pour performance)
    // -------------------------------------------------------
    
    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    private Integer totalViews = 0;

    @Builder.Default
    private Integer totalFavorites = 0;

    // -------------------------------------------------------
    // Méta
    // -------------------------------------------------------
    private boolean featured;
    private boolean verified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.currency == null) this.currency = "XOF";
        if (this.country == null) this.country = "Sénégal";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
    