package sn.discover.discoversenegal.dto;

import lombok.*;
import sn.discover.discoversenegal.entities.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideVerificationUpdateDTO {
    private VerificationStatus verificationStatus;
    private Boolean backgroundCheckCompleted;
    private LocalDate backgroundCheckDate;
    private String verificationNotes;
}
