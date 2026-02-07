package sn.discover.discoversenegal.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelConfirmationDTO {
    private Boolean confirmed;
    private String notes;
}