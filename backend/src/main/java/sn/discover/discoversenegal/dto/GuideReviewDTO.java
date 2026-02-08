package sn.discover.discoversenegal.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideReviewDTO {
    private Double overallRating;
    private Double professionalismRating;
    private Double knowledgeRating;
    private Double communicationRating;
    private Double punctualityRating;
    private String review;
    private String tourDescription;
}
