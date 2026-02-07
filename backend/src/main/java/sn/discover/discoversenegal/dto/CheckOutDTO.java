package sn.discover.discoversenegal.dto;


import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckOutDTO {
    private LocalDateTime checkOutTime;
    private String notes;
}