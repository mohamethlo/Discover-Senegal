package sn.discover.discoversenegal.entities;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantVerificationUpdateDTO {
    private VerificationStatus verificationStatus;
    private String verificationNotes;
}
