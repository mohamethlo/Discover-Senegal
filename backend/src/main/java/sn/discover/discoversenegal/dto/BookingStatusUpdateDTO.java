package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.BookingStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusUpdateDTO {
    private BookingStatus status;
    private String notes;
}
