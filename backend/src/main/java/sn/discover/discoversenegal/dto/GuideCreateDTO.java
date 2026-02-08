package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideCreateDTO {
    private Long userId;
    
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private String email;
    private String phone;
    private String whatsapp;
    private String photoUrl;
    
    private String city;
    private String region;
    private List<String> coverageAreas;
    
    private List<String> languages;
    private String primaryLanguage;
    
    private List<String> specialties;
    private GuideType guideType;
    
    private Integer yearsOfExperience;
    private LocalDate startedGuidingDate;
    private String biography;
    private String expertise;
    
    private List<String> certifications;
    private Boolean isOfficiallyLicensed;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private List<String> qualifications;
    
    private ServiceType serviceType;
    private Integer maxGroupSize;
    private Boolean offersPrivateTours;
    private Boolean offersGroupTours;
    private Boolean offersMultiDayTours;
    private Boolean providesTransportation;
    private String transportationType;
    private Boolean providesAccommodationBooking;
    private Boolean offersCustomItineraries;
    
    private BigDecimal hourlyRate;
    private BigDecimal halfDayRate;
    private BigDecimal fullDayRate;
    private BigDecimal multiDayRate;
    private String currency;
    private String pricingNotes;
    
    private AvailabilityStatus availabilityStatus;
    private List<String> availableDays;
    private Boolean availableWeekends;
    private Boolean availableHolidays;
    private Integer advanceBookingDays;
    
    private List<String> equipmentProvided;
    
    private Boolean availableForOlympics;
    private String olympicExperience;
    private List<String> olympicSpecialties;
    
    private Boolean firstAidCertified;
    
    private String websiteUrl;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
}

