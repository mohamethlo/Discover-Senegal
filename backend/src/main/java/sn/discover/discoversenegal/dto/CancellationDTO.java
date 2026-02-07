package sn.discover.discoversenegal.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationDTO {
    private String cancellationReason;
    private Boolean requestRefund;
}
