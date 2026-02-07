package sn.discover.discoversenegal.dto;


import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityCheckDTO {
    private Long hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfRooms;
}