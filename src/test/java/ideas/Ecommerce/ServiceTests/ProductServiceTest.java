package ideas.Ecommerce.ServiceTests;

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
import ideas.Ecommerce.utils.MockUtility;
import ideas.Ecommerce.utils.TestAssertions;

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
    public void testFindAllProductsByIDs() {
        List<Integer> productIds = List.of(1, 2, 3);

        Product product1 = new Product();
        product1.setProductId(1);
        product1.setProductName("Product 1");

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("Product 2");

        Product product3 = new Product();
        product3.setProductId(3);
        product3.setProductName("Product 3");

        List<Product> mockProductList = List.of(product1, product2, product3);

        when(productRepository.findAllById(productIds)).thenReturn(mockProductList);

        List<Product> actualProductList = productService.findAllProductsByIDs(productIds);

        assertEquals(mockProductList, actualProductList);

        verify(productRepository, times(1)).findAllById(productIds);
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

        ProductFilterDTO filterDTO = new ProductFilterDTO();
        filterDTO.setCondition("lt");
        filterDTO.setPrice(100.0);
        filterDTO.setCategoryId(0);

        List<ProductAndAverageRatingDTO> result = productService.getAllFilteredProducts("", filterDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        TestAssertions.assertProductDTO(result.get(0), 1, "Product 1", "image-url-1", 50.0, 20);
    }

    @Test
    void testGetAllFilteredProductsInvalidCondition() {
        ProductFilterDTO filterDTO = new ProductFilterDTO();
        filterDTO.setCondition("lp");
        filterDTO.setPrice(100.0);
        filterDTO.setCategoryId(0);

        assertThrows(IllegalArgumentException.class, () -> productService.getAllFilteredProducts("invalid", filterDTO));
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

    @Test
    void countProductOrdersInLast30Days_successfulCount() {
        Integer productId = 1;
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        String formattedDate = thirtyDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Long expectedOrderCount = 10L;
        when(orderItemRepository.countByOrder_OrderDateAfterAndProduct_ProductId(formattedDate, productId))
                .thenReturn(expectedOrderCount);

        Long actualOrderCount = productService.countProductOrdersInLast30Days(productId);

        assertEquals(expectedOrderCount, actualOrderCount);
        verify(orderItemRepository, times(1))
                .countByOrder_OrderDateAfterAndProduct_ProductId(formattedDate, productId);
    }

    @Test
    void countProductOrdersInLast30Days_zeroOrders() {
        Integer productId = 2;
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        String formattedDate = thirtyDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Long expectedOrderCount = 0L;
        when(orderItemRepository.countByOrder_OrderDateAfterAndProduct_ProductId(formattedDate, productId))
                .thenReturn(expectedOrderCount);

        Long actualOrderCount = productService.countProductOrdersInLast30Days(productId);

        assertEquals(expectedOrderCount, actualOrderCount);
        verify(orderItemRepository, times(1))
                .countByOrder_OrderDateAfterAndProduct_ProductId(formattedDate, productId);
    }

    @Test
    void countUsersWhoReviewedProduct_successfulCount() {
        Integer productId = 1;

        ProductAndReviewDTO.ReviewsDTO review1 = createMockReview(1, 1);
        ProductAndReviewDTO.ReviewsDTO review2 = createMockReview(2, 2);
        ProductAndReviewDTO.ReviewsDTO review3 = createMockReview(3, 3);
        ProductAndReviewDTO.ReviewsDTO review4 = createMockReview(4, 1);

        ProductAndReviewDTO product = mock(ProductAndReviewDTO.class);
        when(product.getReviews()).thenReturn(List.of(review1, review2, review3, review4));
        when(productRepository.findByProductId(productId)).thenReturn(product);

        long distinctUserCount = productService.countUsersWhoReviewedProduct(productId);

        assertEquals(3, distinctUserCount);
        verify(productRepository, times(1)).findByProductId(productId);
    }

    @Test
    void countUsersWhoReviewedProduct_noReviews() {
        Integer productId = 2;

        ProductAndReviewDTO product = mock(ProductAndReviewDTO.class);
        when(product.getReviews()).thenReturn(List.of());
        when(productRepository.findByProductId(productId)).thenReturn(product);

        long distinctUserCount = productService.countUsersWhoReviewedProduct(productId);

        assertEquals(0, distinctUserCount);
        verify(productRepository, times(1)).findByProductId(productId);
    }

    @Test
    void countUsersWhoReviewedProduct_productNotFound() {
        Integer productId = 3;

        when(productRepository.findByProductId(productId)).thenReturn(null);

        assertThrows(ResourceNotFound.class, () -> productService.countUsersWhoReviewedProduct(productId));
        verify(productRepository, times(1)).findByProductId(productId);
    }

    private ProductAndReviewDTO.ReviewsDTO createMockReview(int reviewId, int userId) {
        ProductAndReviewDTO.ReviewsDTO review = mock(ProductAndReviewDTO.ReviewsDTO.class);
        ProductAndReviewDTO.User user = mock(ProductAndReviewDTO.User.class);

        when(review.getReviewId()).thenReturn(reviewId);
        when(user.getUserId()).thenReturn(userId);
        when(review.getUser()).thenReturn(user);

        return review;
    }

}
