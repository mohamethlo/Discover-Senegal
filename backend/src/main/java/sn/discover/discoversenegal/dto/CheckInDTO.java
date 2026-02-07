package sn.discover.discoversenegal.dto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInDTO {
    private LocalDateTime checkInTime;
    private String notes;
}