package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideSearchDTO {
    private String city;
    private String region;
    private List<String> coverageAreas;
    private GuideType guideType;
    private List<String> languages;
    private List<String> specialties;
    private ServiceType serviceType;
    private Boolean offersPrivateTours;
    private Boolean offersGroupTours;
    private Boolean offersMultiDayTours;
    private Boolean providesTransportation;
    private Boolean offersCustomItineraries;
    private BigDecimal maxHourlyRate;
    private BigDecimal maxDayRate;
    private Boolean availableForOlympics;
    private AvailabilityStatus availabilityStatus;
    private Boolean isOfficiallyLicensed;
    private Double minRating;
    private PartnershipStatus partnershipStatus;
    private VerificationStatus verificationStatus;
    private Boolean featured;
    private Boolean active;
    private Boolean acceptingBookings;
}

