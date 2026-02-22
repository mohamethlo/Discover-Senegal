package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TouristSiteResponseDTO {

    private Long id;

    // Informations générales
    private String name;
    private String description;
    private String shortDescription;
    private String history;
    private String practicalInfo;
    private SiteCategory category;
    private SiteStatus status;

    // Localisation
    private String address;
    private String city;
    private String region;
    private String country;
    private Double latitude;
    private Double longitude;
    private String googleMapsUrl;

    // Horaires
    private LocalTime openingTime;
    private LocalTime closingTime;
    private OpeningSchedule openingSchedule;
    private String specialHoursNote;

    // Tarification
    private boolean freeEntry;
    private BigDecimal adultPrice;
    private BigDecimal childPrice;
    private BigDecimal studentPrice;
    private BigDecimal foreignerPrice;
    private String currency;
    private String ticketInfo;

    // Caractéristiques
    private AccessibilityLevel accessibility;
    private List<BestPeriod> bestPeriods;
    private Integer visitDurationMinutes;
    private String visitDurationFormatted; // Ex: "1h30"

    // Équipements / Services
    private boolean guidedTourAvailable;
    private boolean audioGuideAvailable;
    private boolean wheelchairAccessible;
    private boolean parkingAvailable;
    private boolean restroomAvailable;
    private boolean restaurantNearby;
    private boolean photoAllowed;
    private boolean droneAllowed;

    // Patrimoine
    private boolean unescoListed;
    private String unescoSince;
    private boolean nationalHeritage;
    private String heritageClassification;

    // Médias
    private String coverImageUrl;
    private List<String> imageUrls;
    private String videoUrl;
    private String virtualTourUrl;

    // Contact
    private String contactPhone;
    private String contactEmail;
    private String website;
    private String facebookUrl;
    private String instagramUrl;

    // Tags & Langues
    private List<String> tags;
    private List<String> availableLanguages;

    // Statistiques
    private Double averageRating;
    private Integer totalReviews;
    private Integer totalViews;
    private Integer totalFavorites;

    // Créateur
    private CreatorInfo createdBy;

    // Méta
    private boolean featured;
    private boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // -------------------------------------------------------
    // Classes imbriquées
    // -------------------------------------------------------

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OpeningSchedule {
        private boolean monday;
        private boolean tuesday;
        private boolean wednesday;
        private boolean thursday;
        private boolean friday;
        private boolean saturday;
        private boolean sunday;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatorInfo {
        private Long id;
        private String displayName;
        private String photoUrl;
    }
}
