package sn.discover.discoversenegal.dto;


import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingUpdateDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private Integer numberOfRooms;
    private String roomType;
    private String specialRequests;
    private Boolean needsAirportTransfer;
    private String flightNumber;
    private LocalDateTime arrivalTime;
}