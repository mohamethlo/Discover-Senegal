package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSearchDTO {
    private Long hotelId;
    private Long userId;
    private String bookingReference;
    private LocalDate checkInDateFrom;
    private LocalDate checkInDateTo;
    private LocalDate checkOutDateFrom;
    private LocalDate checkOutDateTo;
    private BookingStatus status;
    private PaymentStatus paymentStatus;
    private Boolean isOlympicBooking;
    private String olympicDelegation;
    private String guestEmail;
    private String guestPhone;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
}