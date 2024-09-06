package ideas.Ecommerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndAverageRatingDTO {

    private Integer productId;
    private String productName;
    private String productImageURL;
    private Double productPrice;
    private Integer productStock;
    private CategoryDTO category;
    private RatingCountsDTO ratingCounts;
    private Double averageRating;

}
