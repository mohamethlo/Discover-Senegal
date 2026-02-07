package sn.discover.discoversenegal.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingReviewDTO {
    private Integer rating;
    private String review;
}
