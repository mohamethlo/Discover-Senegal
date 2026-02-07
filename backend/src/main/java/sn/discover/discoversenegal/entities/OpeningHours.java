package sn.discover.discoversenegal.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHours {
    
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
    
    private Boolean closedMonday = false;
    private Boolean closedTuesday = false;
    private Boolean closedWednesday = false;
    private Boolean closedThursday = false;
    private Boolean closedFriday = false;
    private Boolean closedSaturday = false;
    private Boolean closedSunday = false;
}
