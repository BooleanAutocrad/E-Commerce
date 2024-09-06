package ideas.Ecommerce.dto.review;

import ideas.Ecommerce.dto.product.BaseReviewDTO;

public interface ReviewForProductDTO extends BaseReviewDTO {
    Integer getReviewId();
    String getReview();
    Integer getRating();
    User getUser();
    Product getProduct();

    interface User{
        Integer getUserId();
        String getUserName();
    }

    interface Product{
        Integer getProductId();
        String getProductName();
        String getProductImageURL();
        Double getProductPrice();
    }
}
