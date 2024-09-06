package ProductTests;

import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.*;
import ideas.Ecommerce.exception.IllegalArgument;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.UpdateNotPerformed;
import ideas.Ecommerce.repositories.ProductRepository;
import ideas.Ecommerce.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.MockUtility;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;


    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setProductId(1);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertNotNull(savedProduct);
        assertEquals(1, savedProduct.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        ProductAndRatingDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForRating();
        ProductAndRatingDTO mockProductDTO = MockUtility.createMockProductDTO(1, "Test Product", "test-image-url", 100.0, 10, mockCategoryDTO);

        List<ProductAndRatingDTO> products = new ArrayList<>();
        products.add(mockProductDTO);

        when(productRepository.findBy()).thenReturn(products);

        List<ProductAndAverageRatingDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getProductId());
        assertEquals("Test Product", result.get(0).getProductName());
        assertEquals("test-image-url", result.get(0).getProductImageURL());
        assertEquals(100.0, result.get(0).getProductPrice());
        assertEquals(10, result.get(0).getProductStock());
    }


    @Test
    void testGetProductById() {
        ProductAndReviewDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForReview();
        ProductAndReviewDTO.ReviewsDTO mockReviewDTO = MockUtility.createMockReviewDTO(1, "Test Review", 5);
        List<ProductAndReviewDTO.ReviewsDTO> mockReviews = List.of(mockReviewDTO);
        ProductAndReviewDTO mockProductDTO = MockUtility.createMockProductReviewDTO(1, "Test Product", "test-image-url", 100.0, 10, mockCategoryDTO, mockReviews);

        when(productRepository.findByProductId(1)).thenReturn(mockProductDTO);

        ProductReviewUserAverageRatingDTO result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
        assertEquals("Test Product", result.getProductName());
        assertEquals("test-image-url", result.getProductImageURL());
        assertEquals(100.0, result.getProductPrice());
        assertEquals(10, result.getProductStock());

        assertNotNull(result.getCategory());
        assertEquals(1, result.getCategory().getCategoryId());
        assertEquals("Test Category", result.getCategory().getCategoryName());

        assertNotNull(result.getReviews());
        assertEquals(1, result.getReviews().size());
        assertEquals(1, result.getReviews().get(0).getReviewId());
        assertEquals("Test Review", result.getReviews().get(0).getReview());
        assertEquals(5, result.getReviews().get(0).getRating());
    }

    @Test
    void testGetAllFilteredProducts() {
        ProductAndRatingDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForRating();
        ProductAndRatingDTO mockProductDTO1 = MockUtility.createMockProductDTO(1, "Product 1", "image-url-1", 50.0, 20, mockCategoryDTO);
        ProductAndRatingDTO mockProductDTO2 = MockUtility.createMockProductDTO(2, "Product 2", "image-url-2", 150.0, 15, mockCategoryDTO);

        List<ProductAndRatingDTO> mockedProducts = List.of(mockProductDTO1, mockProductDTO2);

        when(productRepository.findByProductPriceGreaterThan(100.0)).thenReturn(mockedProducts);

        List<ProductAndAverageRatingDTO> result = productService.getAllFilteredProducts("gt", 100.0);

        assertNotNull(result);
        assertEquals(2, result.size());

        ProductAndAverageRatingDTO resultProduct1 = result.get(0);
        assertEquals(1, resultProduct1.getProductId());
        assertEquals("Product 1", resultProduct1.getProductName());
        assertEquals("image-url-1", resultProduct1.getProductImageURL());
        assertEquals(50.0, resultProduct1.getProductPrice());
        assertEquals(20, resultProduct1.getProductStock());

        ProductAndAverageRatingDTO resultProduct2 = result.get(1);
        assertEquals(2, resultProduct2.getProductId());
        assertEquals("Product 2", resultProduct2.getProductName());
        assertEquals("image-url-2", resultProduct2.getProductImageURL());
        assertEquals(150.0, resultProduct2.getProductPrice());
        assertEquals(15, resultProduct2.getProductStock());
    }


    @Test
    void testGetAllFilteredProductsInvalidCondition() {
        assertThrows(IllegalArgument.class, () -> productService.getAllFilteredProducts("invalid", 100.0));
    }

    @Test
    void testGetProductsForCategory() {
        ProductAndRatingDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForRating();
        ProductAndRatingDTO mockProductDTO1 = MockUtility.createMockProductDTO(1, "Product 1", "image-url-1", 80.0, 30, mockCategoryDTO);
        ProductAndRatingDTO mockProductDTO2 = MockUtility.createMockProductDTO(2, "Product 2", "image-url-2", 200.0, 25, mockCategoryDTO);

        List<ProductAndRatingDTO> mockedProducts = List.of(mockProductDTO1, mockProductDTO2);

        when(productRepository.findProductsByCategoryIdOrParentCategoryId(1)).thenReturn(mockedProducts);

        List<ProductAndAverageRatingDTO> result = productService.getProductsForCategory(1);

        assertNotNull(result);
        assertEquals(2, result.size());

        ProductAndAverageRatingDTO resultProduct1 = result.get(0);
        assertEquals(1, resultProduct1.getProductId());
        assertEquals("Product 1", resultProduct1.getProductName());
        assertEquals("image-url-1", resultProduct1.getProductImageURL());
        assertEquals(80.0, resultProduct1.getProductPrice());
        assertEquals(30, resultProduct1.getProductStock());

        ProductAndAverageRatingDTO resultProduct2 = result.get(1);
        assertEquals(2, resultProduct2.getProductId());
        assertEquals("Product 2", resultProduct2.getProductName());
        assertEquals("image-url-2", resultProduct2.getProductImageURL());
        assertEquals(200.0, resultProduct2.getProductPrice());
        assertEquals(25, resultProduct2.getProductStock());
    }

    @Test
    public void testDeleteProductById_ProductDeletedSuccessfully() {
        Integer productId = 1;

        doNothing().when(productRepository).deleteById(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        productService.deleteProductById(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }



    @Test
    public void testDeleteProductById_ProductNotDeleted() {
        Integer productId = 1;
        Product product = new Product();

        doNothing().when(productRepository).deleteById(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Assertions.assertThrows(ResourceNotDeleted.class, () -> productService.deleteProductById(productId));

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testUpdateProductStock() {
        Product mockProduct = MockUtility.createMockProduct(1, 20);

        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        doNothing().when(productRepository).updateProductStock(1, 20);

        boolean isUpdated = productService.updateProductStock(1, 20);

        assertTrue(isUpdated);
        verify(productRepository).updateProductStock(1, 20);
    }

    @Test
    void testUpdateProductStockFailure() {
        Product mockProduct = MockUtility.createMockProduct(1, 20);

        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        doNothing().when(productRepository).updateProductStock(1, 20);

        when(productRepository.findById(1)).thenReturn(Optional.of(mockProduct));
        when(mockProduct.getProductStock()).thenReturn(15);

        Exception exception = assertThrows(UpdateNotPerformed.class, () -> {
            productService.updateProductStock(1, 20);
        });

        assertEquals("Product Stock Not Updated. Something went wrong", exception.getMessage());
    }
}
