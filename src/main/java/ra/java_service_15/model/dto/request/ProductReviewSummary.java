package ra.java_service_15.model.dto.response;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewSummary {

    private Long productId;
    private String productName;
    private Long totalReviews;
    private Double averageRating;
    private List<ReviewResponse> reviews;

    // Rating distribution
    private Long fiveStars;
    private Long fourStars;
    private Long threeStars;
    private Long twoStars;
    private Long oneStar;
}