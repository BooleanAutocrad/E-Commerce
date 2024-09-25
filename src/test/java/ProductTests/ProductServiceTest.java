package ProductTests;

import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.*;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.OrderItemRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import ideas.Ecommerce.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.MockUtility;
import utils.TestAssertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

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

        TestAssertions.assertProduct(savedProduct, 1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        ProductAndRatingDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForRating();
        ProductAndRatingDTO mockProductDTO = MockUtility.createMockProductDTO(1, "Test Product", "test-image-url", 100.0, 10, mockCategoryDTO);

        ProductAndReviewDTO.CategoryDTO mockCategoryForReviewDTO = MockUtility.createMockCategoryDTOForReview();
        ProductAndReviewDTO.User mockUser = MockUtility.createMockUser(1, "Test User");

        ProductAndReviewDTO.ReviewsDTO mockReviewDTO = MockUtility.createMockReviewDTO(1, "Test Review", 5,mockUser);
        List<ProductAndReviewDTO.ReviewsDTO> mockReviews = List.of(mockReviewDTO);
        ProductAndReviewDTO mockProductWithReviewDTO = MockUtility.createMockProductReviewDTO(1, "Test Product", "test-image-url", 100.0, 10, mockCategoryForReviewDTO, mockReviews);

        List<ProductAndRatingDTO> products = new ArrayList<>();
        products.add(mockProductDTO);

        when(productRepository.findBy()).thenReturn(products);
        when(productRepository.findByProductId(1)).thenReturn(mockProductWithReviewDTO);

        List<ProductAndAverageRatingDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        TestAssertions.assertProductDTO(result.get(0), 1, "Test Product", "test-image-url", 100.0, 10);
    }


    @Test
    void testGetProductById() {
        ProductAndReviewDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForReview();

        ProductAndReviewDTO.User mockUser = MockUtility.createMockUser(1, "Test User");

        ProductAndReviewDTO.ReviewsDTO mockReviewDTO = MockUtility.createMockReviewDTO(1, "Test Review", 5, mockUser);
        List<ProductAndReviewDTO.ReviewsDTO> mockReviews = List.of(mockReviewDTO);
        ProductAndReviewDTO mockProductDTO = MockUtility.createMockProductReviewDTO(1, "Test Product", "test-image-url", 100.0, 10, mockCategoryDTO, mockReviews);

        when(productRepository.findByProductId(1)).thenReturn(mockProductDTO);

        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        String formattedDate = thirtyDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        when(orderItemRepository.countByOrder_OrderDateAfterAndProduct_ProductId(formattedDate, 1)).thenReturn(10L);

        ProductReviewUserAverageRatingDTO result = productService.getProductById(1);

        TestAssertions.assertProductReviewUserAverageRatingDTO(result, 1, "Test Product", "test-image-url", 100.0, 10);
    }

    @Test
    void testGetAllFilteredProducts() {
        ProductAndRatingDTO.CategoryDTO mockCategoryDTO = MockUtility.createMockCategoryDTOForRating();
        ProductAndRatingDTO mockProductDTO1 = MockUtility.createMockProductDTO(1, "Product 1", "image-url-1", 50.0, 20, mockCategoryDTO);
        ProductAndRatingDTO mockProductDTO2 = MockUtility.createMockProductDTO(2, "Product 2", "image-url-2", 150.0, 15, mockCategoryDTO);

        List<ProductAndRatingDTO> mockedProducts = List.of(mockProductDTO1);

        when(productRepository.findByProductPriceLessThan(100.0)).thenReturn(mockedProducts);

        Map<String, Object> filterConditions = new HashMap<>();
        filterConditions.put("condition", "lt");
        filterConditions.put("price", 100);
        filterConditions.put("categoryId", 0);

        List<ProductAndAverageRatingDTO> result = productService.getAllFilteredProducts("", filterConditions);

        assertNotNull(result);
        assertEquals(1, result.size());

        TestAssertions.assertProductDTO(result.get(0), 1, "Product 1", "image-url-1", 50.0, 20);
    }


    @Test
    void testGetAllFilteredProductsInvalidCondition() {

        Map<String, Object> filterConditions = new HashMap<>();
        filterConditions.put("condition", "lp");
        filterConditions.put("price", 100);
        filterConditions.put("categoryId", 0);

        assertThrows(IllegalArgumentException.class, () -> productService.getAllFilteredProducts("invalid", filterConditions));
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

        TestAssertions.assertProductDTO(result.get(0), 1, "Product 1", "image-url-1", 80.0, 30);
        TestAssertions.assertProductDTO(result.get(1), 2, "Product 2", "image-url-2", 200.0, 25);
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
    void testUpdateProductStockProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFound.class, () -> {
            productService.updateProductStock(1, 20);
        });

        assertEquals("Product not found", exception.getMessage());
    }

}
