package ideas.Ecommerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewUserAverageRatingDTO {

    private Integer productId;
    private String productName;
    private String productImageURL;
    private Double productPrice;
    private Integer productStock;
    private CategoryDTO category;
    private List<ReviewDTO> reviews;
    private RatingCountsDTO ratingCounts;
    private Double averageRating;
    private Long userReviewCount;
    private Long orderCount;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDTO{
        private Integer reviewId;
        private String review;
        private Integer rating;
        private UserDTO user;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Integer userId;
        private String userName;
    }
}
