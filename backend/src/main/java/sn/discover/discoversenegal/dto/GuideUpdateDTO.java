package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideUpdateDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String whatsapp;
    private String photoUrl;
    
    private String city;
    private String region;
    private List<String> coverageAreas;
    
    private List<String> languages;
    
    private List<String> specialties;
    private GuideType guideType;
    
    private String biography;
    private String expertise;
    
    private List<String> certifications;
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
    
    private String websiteUrl;
    private String facebookUrl;
    private String instagramUrl;
    private String linkedinUrl;
    
    private Boolean featured;
    private Boolean active;
    private Boolean acceptingBookings;
}
