package utils;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.ProductAndAverageRatingDTO;
import ideas.Ecommerce.dto.product.ProductReviewUserAverageRatingDTO;
import ideas.Ecommerce.dto.product.ProductAndRatingDTO;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssertions {

    public static void assertProduct(Product product, int expectedId) {
        assertNotNull(product);
        assertEquals(expectedId, product.getProductId());
    }

    public static void assertProductDTO(ProductAndAverageRatingDTO productDTO, int expectedId, String expectedName, String expectedImageURL, double expectedPrice, int expectedStock) {
        assertNotNull(productDTO);
        assertEquals(expectedId, productDTO.getProductId());
        assertEquals(expectedName, productDTO.getProductName());
        assertEquals(expectedImageURL, productDTO.getProductImageURL());
        assertEquals(expectedPrice, productDTO.getProductPrice());
        assertEquals(expectedStock, productDTO.getProductStock());
    }

    public static void assertProductReviewUserAverageRatingDTO(ProductReviewUserAverageRatingDTO result, int expectedId, String expectedName, String expectedImageURL, double expectedPrice, int expectedStock) {
        assertNotNull(result);
        assertEquals(expectedId, result.getProductId());
        assertEquals(expectedName, result.getProductName());
        assertEquals(expectedImageURL, result.getProductImageURL());
        assertEquals(expectedPrice, result.getProductPrice());
        assertEquals(expectedStock, result.getProductStock());
    }

    public static void assertProductAndRatingDTO(ProductAndRatingDTO productDTO, int expectedId, String expectedName, String expectedImageURL, double expectedPrice, int expectedStock) {
        assertNotNull(productDTO);
        assertEquals(expectedId, productDTO.getProductId());
        assertEquals(expectedName, productDTO.getProductName());
        assertEquals(expectedImageURL, productDTO.getProductImageURL());
        assertEquals(expectedPrice, productDTO.getProductPrice());
        assertEquals(expectedStock, productDTO.getProductStock());
    }
}