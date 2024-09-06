package utils;

import ideas.Ecommerce.dto.product.*;
import ideas.Ecommerce.Entity.Product;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockUtility {

    public static ProductAndRatingDTO.CategoryDTO createMockCategoryDTOForRating() {
        ProductAndRatingDTO.CategoryDTO mockCategoryDTO = mock(ProductAndRatingDTO.CategoryDTO.class);
        when(mockCategoryDTO.getCategoryId()).thenReturn(1);
        when(mockCategoryDTO.getCategoryName()).thenReturn("Category 1");
        return mockCategoryDTO;
    }
    public static ProductAndReviewDTO.CategoryDTO createMockCategoryDTOForReview() {
        ProductAndReviewDTO.CategoryDTO mockCategoryDTO = mock(ProductAndReviewDTO.CategoryDTO.class);
        when(mockCategoryDTO.getCategoryId()).thenReturn(1);
        when(mockCategoryDTO.getCategoryName()).thenReturn("Test Category");
        return mockCategoryDTO;
    }

    public static ProductAndRatingDTO createMockProductDTO(int id, String name, String imageUrl, double price, int stock, ProductAndRatingDTO.CategoryDTO categoryDTO) {
        ProductAndRatingDTO mockProductDTO = mock(ProductAndRatingDTO.class);
        when(mockProductDTO.getProductId()).thenReturn(id);
        when(mockProductDTO.getProductName()).thenReturn(name);
        when(mockProductDTO.getProductImageURL()).thenReturn(imageUrl);
        when(mockProductDTO.getProductPrice()).thenReturn(price);
        when(mockProductDTO.getProductStock()).thenReturn(stock);
        when(mockProductDTO.getCategory()).thenReturn(categoryDTO);
        return mockProductDTO;
    }

    public static ProductAndReviewDTO createMockProductReviewDTO(int id, String name, String imageUrl, double price, int stock, ProductAndReviewDTO.CategoryDTO categoryDTO, List<ProductAndReviewDTO.ReviewsDTO> reviews) {
        ProductAndReviewDTO mockProductDTO = mock(ProductAndReviewDTO.class);
        when(mockProductDTO.getProductId()).thenReturn(id);
        when(mockProductDTO.getProductName()).thenReturn(name);
        when(mockProductDTO.getProductImageURL()).thenReturn(imageUrl);
        when(mockProductDTO.getProductPrice()).thenReturn(price);
        when(mockProductDTO.getProductStock()).thenReturn(stock);
        when(mockProductDTO.getCategory()).thenReturn(categoryDTO);
        when(mockProductDTO.getReviews()).thenReturn(reviews);
        return mockProductDTO;
    }

    public static ProductAndReviewDTO.ReviewsDTO createMockReviewDTO(int reviewId, String review, int rating) {
        ProductAndReviewDTO.ReviewsDTO mockReviewDTO = mock(ProductAndReviewDTO.ReviewsDTO.class);
        when(mockReviewDTO.getReviewId()).thenReturn(reviewId);
        when(mockReviewDTO.getReview()).thenReturn(review);
        when(mockReviewDTO.getRating()).thenReturn(rating);
        return mockReviewDTO;
    }

    public static Product createMockProduct(int id, int stock) {
        Product mockProduct = mock(Product.class);
        when(mockProduct.getProductId()).thenReturn(id);
        when(mockProduct.getProductStock()).thenReturn(stock);
        return mockProduct;
    }

}
