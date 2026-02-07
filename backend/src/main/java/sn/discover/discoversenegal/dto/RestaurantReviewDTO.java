package sn.discover.discoversenegal.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantReviewDTO {
    private Double overallRating;
    private Double foodQualityRating;
    private Double serviceRating;
    private Double ambianceRating;
    private Double valueForMoneyRating;
    private String review;
}
