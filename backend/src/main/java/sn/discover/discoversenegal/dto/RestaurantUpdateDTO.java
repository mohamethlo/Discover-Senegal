package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantUpdateDTO {
    private String name;
    private String description;
    private String address;
    private String city;
    private String region;
    private String postalCode;
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
    
    private Boolean cateringAvailable;
    private Integer maxCateringCapacity;
    
    private Boolean featured;
    private Boolean active;
}
