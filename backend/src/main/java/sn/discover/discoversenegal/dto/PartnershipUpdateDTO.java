package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnershipUpdateDTO {
    private PartnershipStatus partnershipStatus;
    private LocalDateTime partnershipStartDate;
    private LocalDateTime partnershipEndDate;
    private BigDecimal commissionRate;
    private String specialOffers;
}