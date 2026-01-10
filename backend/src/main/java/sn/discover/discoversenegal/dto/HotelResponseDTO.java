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
public class HotelResponseDTO {
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
    private HotelCategory category;
    private Integer starRating;
    private Integer totalRooms;
    private BigDecimal priceRangeMin;
    private BigDecimal priceRangeMax;
    private String currency;
    private List<String> amenities;
    private List<String> spokenLanguages;
    private List<String> photoUrls;
    private String culturalHighlights;
    private String sustainabilityPractices;
    private String checkInTime;
    private String checkOutTime;
    private PartnershipStatus partnershipStatus;
    private VerificationStatus verificationStatus;
    private String specialOffers;
    private Double averageRating;
    private Integer totalReviews;
    private Integer viewCount;
    private Integer bookingCount;
    private boolean featured;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

