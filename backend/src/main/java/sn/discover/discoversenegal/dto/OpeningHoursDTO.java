package sn.discover.discoversenegal.dto;

import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningHoursDTO {
    private LocalTime mondayOpen;
    private LocalTime mondayClose;
    private LocalTime tuesdayOpen;
    private LocalTime tuesdayClose;
    private LocalTime wednesdayOpen;
    private LocalTime wednesdayClose;
    private LocalTime thursdayOpen;
    private LocalTime thursdayClose;
    private LocalTime fridayOpen;
    private LocalTime fridayClose;
    private LocalTime saturdayOpen;
    private LocalTime saturdayClose;
    private LocalTime sundayOpen;
    private LocalTime sundayClose;
    
    private Boolean closedMonday;
    private Boolean closedTuesday;
    private Boolean closedWednesday;
    private Boolean closedThursday;
    private Boolean closedFriday;
    private Boolean closedSaturday;
    private Boolean closedSunday;
}
