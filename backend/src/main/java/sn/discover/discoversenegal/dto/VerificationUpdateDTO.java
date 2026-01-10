package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationUpdateDTO {
    private VerificationStatus verificationStatus;
    private String verificationNotes;
}