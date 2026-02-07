package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String region;
    private Double latitude;
    private Double longitude;
    private String email;
    private String phone;
    private String website;
    
    private RestaurantType type;
    private List<String> cuisineTypes;
    private List<String> specialties;
    private PriceRange priceRange;
    private BigDecimal averagePricePerPerson;
    private String currency;
    
    private Integer seatingCapacity;
    private Boolean acceptsReservations;
    
    private OpeningHoursDTO openingHours;
    
    private List<String> services;
    private List<String> paymentMethods;
    private List<String> spokenLanguages;
    private List<String> photoUrls;
    
    private String menuUrl;
    private String menuDescription;
    
    private Boolean hasVegetarianOptions;
    private Boolean hasVeganOptions;
    private Boolean hasHalalOptions;
    private Boolean hasGlutenFreeOptions;
    
    private String culturalSignificance;
    private String chefStory;
    private String localIngredients;
    
    private Ambiance ambiance;
    private Boolean hasLiveMusic;
    private Boolean hasOutdoorSeating;
    private Boolean hasPrivateDining;
    
    private PartnershipStatus partnershipStatus;
    private String specialOffersJOJ;
    private Boolean cateringAvailable;
    private Integer maxCateringCapacity;
    
    private VerificationStatus verificationStatus;
    private Boolean hasHygieneRating;
    private Boolean isHalalCertified;
    
    private Double averageRating;
    private Integer totalReviews;
    private Double foodQualityRating;
    private Double serviceRating;
    private Double ambianceRating;
    private Double valueForMoneyRating;
    
    private Integer viewCount;
    private Integer reservationCount;
    private Integer favoriteCount;
    
    private Boolean featured;
    private Boolean active;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
