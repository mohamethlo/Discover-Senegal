package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelSearchDTO {
    private String city;
    private String region;
    private HotelCategory category;
    private Integer minStarRating;
    private Integer maxStarRating;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> amenities;
    private PartnershipStatus partnershipStatus;
    private VerificationStatus verificationStatus;
    private Boolean featured;
    private Boolean active;
}
