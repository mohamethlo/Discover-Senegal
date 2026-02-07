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
public class BookingResponseDTO {
    private Long id;
    private String bookingReference;
    
    // Hôtel
    private Long hotelId;
    private String hotelName;
    private String hotelCity;
    private String hotelAddress;
    
    // Client
    private String guestFirstName;
    private String guestLastName;
    private String guestEmail;
    private String guestPhone;
    private String guestCountry;
    
    // Dates
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfNights;
    
    // Occupants
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private Integer numberOfRooms;
    private String roomType;
    
    // Prix
    private BigDecimal pricePerNight;
    private BigDecimal totalPrice;
    private BigDecimal taxes;
    private BigDecimal serviceFees;
    private BigDecimal finalAmount;
    private String currency;
    private BigDecimal discountAmount;
    private String discountCode;
    
    // Statuts
    private BookingStatus status;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    
    // Demandes spéciales
    private String specialRequests;
    private Boolean needsAirportTransfer;
    private String flightNumber;
    
    // JOJ
    private Boolean isOlympicBooking;
    private String olympicDelegation;
    private String olympicRole;
    
    // Confirmation
    private Boolean isConfirmedByHotel;
    private LocalDateTime confirmedByHotelAt;
    
    // Check-in/out effectif
    private LocalDateTime actualCheckInTime;
    private LocalDateTime actualCheckOutTime;
    
    // Évaluation
    private Integer rating;
    private String review;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}