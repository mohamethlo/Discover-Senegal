package sn.discover.discoversenegal.dto;


import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentUpdateDTO {
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private BigDecimal amountPaid;
}
