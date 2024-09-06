package ideas.Ecommerce.dto.product;

import java.util.List;

public interface ProductAndRatingDTO {
    Integer getProductId();
    String getProductName();
    String getProductImageURL();
    Double getProductPrice();
    Integer getProductStock();
    CategoryDTO getCategory();
    List<ReviewsDTO> getReviews();
    interface CategoryDTO extends BaseCategoryDTO{
    }

    interface ReviewsDTO extends BaseReviewDTO{
    }
}
