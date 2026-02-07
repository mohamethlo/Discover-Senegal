package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantSearchDTO {
    private String city;
    private String region;
    private RestaurantType type;
    private List<String> cuisineTypes;
    private PriceRange priceRange;
    private BigDecimal maxPrice;
    private Ambiance ambiance;
    private Boolean hasVegetarianOptions;
    private Boolean hasVeganOptions;
    private Boolean hasHalalOptions;
    private Boolean hasGlutenFreeOptions;
    private Boolean acceptsReservations;
    private Boolean hasOutdoorSeating;
    private Boolean hasLiveMusic;
    private Boolean cateringAvailable;
    private Double minRating;
    private PartnershipStatus partnershipStatus;
    private VerificationStatus verificationStatus;
    private Boolean featured;
    private Boolean active;
}
