package ideas.Ecommerce.ServiceTests;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.Entity.Review;
import ideas.Ecommerce.dto.review.ReviewDTO;
import ideas.Ecommerce.dto.review.ReviewForProductDTO;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.repositories.OrderRepository;
import ideas.Ecommerce.repositories.ReviewRepository;
import ideas.Ecommerce.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ReviewServiceTests {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewForProductDTO mockReviewForProductDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReviewForProduct() {
        List<ReviewForProductDTO> mockedReviewForProductDTOList = List.of(mockReviewForProductDTO);
        when(reviewRepository.findByProduct_ProductId(any())).thenReturn(mockedReviewForProductDTOList);

        List<ReviewForProductDTO> result = reviewService.getReviewForProduct(1);

        assertEquals(mockedReviewForProductDTOList, result);
    }

    @Test
    public void testWriteReviewForProduct() throws Exception {
        Review review = new Review();
        ApplicationUser user = new ApplicationUser();
        Product product = new Product();

        review.setReviewId(1);
        review.setReview("review");
        review.setRating(5);

        user.setUserId(1);
        user.setUserName("user");
        review.setUser(user);

        product.setProductId(1);
        product.setProductName("product");
        review.setProduct(product);

        ReviewDTO expectedReviewDTO = new ReviewDTO(
                review.getReviewId(),
                review.getReview(),
                review.getRating(),
                review.getUser().getUserId(),
                review.getUser().getUserName(),
                review.getProduct().getProductId(),
                review.getProduct().getProductName()
        );

        when(orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(any(), any())).thenReturn(true);
        when(reviewRepository.save(any())).thenReturn(review);

        ReviewDTO actualReviewDTO = reviewService.writeReviewForProduct(review);

        assertEquals(expectedReviewDTO.getReviewId(), actualReviewDTO.getReviewId());
        assertEquals(expectedReviewDTO.getReview(), actualReviewDTO.getReview());
        assertEquals(expectedReviewDTO.getRating(), actualReviewDTO.getRating());
        assertEquals(expectedReviewDTO.getUserId(), actualReviewDTO.getUserId());
        assertEquals(expectedReviewDTO.getUserName(), actualReviewDTO.getUserName());
        assertEquals(expectedReviewDTO.getProductId(), actualReviewDTO.getProductId());
        assertEquals(expectedReviewDTO.getProductName(), actualReviewDTO.getProductName());
    }

    @Test
    public void testWriteReviewForProduct_UserHasNeverOrderedTheProduct() {
        Review review = new Review();
        ApplicationUser user = new ApplicationUser();
        Product product = new Product();

        review.setReviewId(1);
        review.setReview("review");
        review.setRating(5);

        user.setUserId(1);
        user.setUserName("user");
        review.setUser(user);

        product.setProductId(1);
        product.setProductName("product");
        review.setProduct(product);

        when(orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(any(), any())).thenReturn(false);

        Exception exception = new Exception("User Has Never Ordered The Product");

        try {
            reviewService.writeReviewForProduct(review);
        } catch (Exception e) {
            assertEquals(exception.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testUpdateReview() {
        Review review = new Review();
        ApplicationUser user = new ApplicationUser();
        Product product = new Product();

        review.setReviewId(1);
        review.setReview("review");
        review.setRating(5);

        user.setUserId(1);
        user.setUserName("user");
        review.setUser(user);

        product.setProductId(1);
        product.setProductName("product");
        review.setProduct(product);

        ReviewDTO expectedReviewDTO = new ReviewDTO(
                review.getReviewId(),
                review.getReview(),
                review.getRating(),
                review.getUser().getUserId(),
                review.getUser().getUserName(),
                review.getProduct().getProductId(),
                review.getProduct().getProductName()
        );

        when(reviewRepository.save(any())).thenReturn(review);

        ReviewDTO actualReviewDTO = reviewService.updateReview(review);

        assertEquals(expectedReviewDTO.getReviewId(), actualReviewDTO.getReviewId());
        assertEquals(expectedReviewDTO.getReview(), actualReviewDTO.getReview());
        assertEquals(expectedReviewDTO.getRating(), actualReviewDTO.getRating());
        assertEquals(expectedReviewDTO.getUserId(), actualReviewDTO.getUserId());
        assertEquals(expectedReviewDTO.getUserName(), actualReviewDTO.getUserName());
        assertEquals(expectedReviewDTO.getProductId(), actualReviewDTO.getProductId());
        assertEquals(expectedReviewDTO.getProductName(), actualReviewDTO.getProductName());
    }

    @Test
    public void testDeleteReview_Success() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        reviewService.deleteReview(1);

        verify(reviewRepository, times(1)).deleteById(1);
        verify(reviewRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteReview_ResourceNotDeleted() {
        Review mockReview = new Review();
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(mockReview));

        assertThrows(ResourceNotDeleted.class, () -> reviewService.deleteReview(1));

        verify(reviewRepository, times(1)).deleteById(1);
        verify(reviewRepository, times(1)).findById(1);
    }

    @Test
    public void testCheckIfUserHasOrderedProduct() {
        Integer userId = 1;
        Integer productId = 1;

        when(orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(userId, productId)).thenReturn(true);

        boolean result = reviewService.checkIfUserHasOrderedProduct(userId, productId);

        assertTrue(result);
    }

    @Test
    public void testCheckIfUserHasOrderedProduct_False() {
        Integer userId = 1;
        Integer productId = 1;

        when(orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(userId, productId)).thenReturn(false);

        boolean result = reviewService.checkIfUserHasOrderedProduct(userId, productId);

        assertFalse(result);
    }

    @Test
    public void testConvertReviewToReviewDTO() {
        Review review = new Review();
        ApplicationUser user = new ApplicationUser();
        Product product = new Product();

        review.setReviewId(1);
        review.setReview("review");
        review.setRating(5);

        user.setUserId(1);
        user.setUserName("user");
        review.setUser(user);

        product.setProductId(1);
        product.setProductName("product");
        review.setProduct(product);

        ReviewDTO expectedReviewDTO = new ReviewDTO(
                review.getReviewId(),
                review.getReview(),
                review.getRating(),
                review.getUser().getUserId(),
                review.getUser().getUserName(),
                review.getProduct().getProductId(),
                review.getProduct().getProductName()
        );

        ReviewDTO actualReviewDTO = reviewService.convertReviewToReviewDTO(review);

        assertEquals(expectedReviewDTO.getReviewId(), actualReviewDTO.getReviewId());
        assertEquals(expectedReviewDTO.getReview(), actualReviewDTO.getReview());
        assertEquals(expectedReviewDTO.getRating(), actualReviewDTO.getRating());
        assertEquals(expectedReviewDTO.getUserId(), actualReviewDTO.getUserId());
        assertEquals(expectedReviewDTO.getUserName(), actualReviewDTO.getUserName());
        assertEquals(expectedReviewDTO.getProductId(), actualReviewDTO.getProductId());
        assertEquals(expectedReviewDTO.getProductName(), actualReviewDTO.getProductName());
    }


}
