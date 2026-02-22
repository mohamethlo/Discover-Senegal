package sn.discover.discoversenegal.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import sn.discover.discoversenegal.entities.AccessibilityLevel;
import sn.discover.discoversenegal.entities.BestPeriod;
import sn.discover.discoversenegal.entities.SiteCategory;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TouristSiteDTO {

    // --- Informations générales ---
    @NotBlank(message = "Le nom du site est obligatoire")
    @Size(min = 3, max = 255)
    private String name;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Size(max = 500)
    private String shortDescription;

    private String history;

    private String practicalInfo;

    @NotNull(message = "La catégorie est obligatoire")
    private SiteCategory category;

    // --- Localisation ---
    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "La ville est obligatoire")
    private String city;

    @NotBlank(message = "La région est obligatoire")
    private String region;

    private String country;

    @NotNull(message = "La latitude est obligatoire")
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @NotNull(message = "La longitude est obligatoire")
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    private String googleMapsUrl;

    // --- Horaires ---
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean openMonday;
    private boolean openTuesday;
    private boolean openWednesday;
    private boolean openThursday;
    private boolean openFriday;
    private boolean openSaturday;
    private boolean openSunday;
    private String specialHoursNote;

    // --- Tarification ---
    private boolean freeEntry;

    @DecimalMin("0.0")
    private BigDecimal adultPrice;

    @DecimalMin("0.0")
    private BigDecimal childPrice;

    @DecimalMin("0.0")
    private BigDecimal studentPrice;

    @DecimalMin("0.0")
    private BigDecimal foreignerPrice;

    private String currency;
    private String ticketInfo;

    // --- Caractéristiques ---
    private AccessibilityLevel accessibility;

    @Builder.Default
    private List<BestPeriod> bestPeriods = new ArrayList<>();

    @Min(1)
    private Integer visitDurationMinutes;

    private boolean guidedTourAvailable;
    private boolean audioGuideAvailable;
    private boolean wheelchairAccessible;
    private boolean parkingAvailable;
    private boolean restroomAvailable;
    private boolean restaurantNearby;
    private boolean photoAllowed;
    private boolean droneAllowed;

    // --- Patrimoine ---
    private boolean unescoListed;
    private String unescoSince;
    private boolean nationalHeritage;
    private String heritageClassification;

    // --- Médias ---
    private String coverImageUrl;

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    private String videoUrl;
    private String virtualTourUrl;

    // --- Contact ---
    @Email
    private String contactEmail;
    private String contactPhone;
    private String website;
    private String facebookUrl;
    private String instagramUrl;

    // --- Tags & Langues ---
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private List<String> availableLanguages = new ArrayList<>();
}