package ideas.Ecommerce.dto.product;

import java.util.List;

public interface ProductAndReviewDTO {
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
        User getUser();
    }
    interface User{
        Integer getUserId();
        String getUserName();
    }
}
