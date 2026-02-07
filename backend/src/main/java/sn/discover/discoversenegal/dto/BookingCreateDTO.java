package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreateDTO {
    private Long hotelId;
    private Long userId;
    
    // Informations du client
    private String guestFirstName;
    private String guestLastName;
    private String guestEmail;
    private String guestPhone;
    private String guestCountry;
    private String guestNationality;
    
    // Dates de séjour
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    
    // Occupants
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private Integer numberOfRooms;
    private String roomType;
    
    // Prix
    private BigDecimal pricePerNight;
    private String currency;
    private String discountCode;
    
    // Demandes spéciales
    private String specialRequests;
    private Boolean needsAirportTransfer;
    private String flightNumber;
    private LocalDateTime arrivalTime;
    
    // JOJ 2026
    private Boolean isOlympicBooking;
    private String olympicDelegation;
    private String olympicRole;
    
    // Communication
    private String languagePreference;
    
    // Source
    private BookingSource source;
    private String userIpAddress;
    private String userAgent;
}