package ideas.Ecommerce.ControllerTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.Entity.Review;
import ideas.Ecommerce.dto.review.ReviewDTO;
import ideas.Ecommerce.dto.review.ReviewForProductDTO;
import ideas.Ecommerce.service.ReviewService;
import ideas.Ecommerce.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class ReviewControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    JwtUtil jwtUtil;

    String header;

    private Review review;
    private ReviewDTO reviewDTO;
    private Product product;

    @BeforeEach
    public void setUp() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        user.setUserName("User Name");

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");

        review = new Review();
        review.setReviewId(1);
        review.setProduct(product);
        review.setUser(user);
        review.setReview("Great product!");
        review.setRating(5);

        reviewDTO = new ReviewDTO(1, "Great product!", 5, 1, "User Name", 1, "Test Product");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }


    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldAddReview() throws Exception {
        when(reviewService.writeReviewForProduct(any(Review.class))).thenReturn(reviewDTO);

        mockMvc.perform(post("/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content("{\"productId\":1,\"userId\":1,\"review\":\"Great product!\",\"rating\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1))
                .andExpect(jsonPath("$.review").value("Great product!"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldGetReviewsForProduct() throws Exception {
        List<ReviewForProductDTO> reviews = new ArrayList<>();
        reviews.add(createReviewDtoForProduct());

        when(reviewService.getReviewForProduct(1)).thenReturn(reviews);

        mockMvc.perform(get("/review/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content("{\"productId\":1}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldEditReview() throws Exception {
        when(reviewService.updateReview(any(Review.class))).thenReturn(reviewDTO);

        mockMvc.perform(put("/review/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reviewId\":1,\"productId\":1,\"userId\":1,\"review\":\"Updated review!\",\"rating\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1))
                .andExpect(jsonPath("$.review").value("Great product!"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldDeleteReview() throws Exception {
        mockMvc.perform(delete("/review/{reviewId}", 1))
                .andExpect(status().isOk());

        verify(reviewService, times(1)).deleteReview(1);
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldCheckIfUserHasOrderedProduct() throws Exception {
        when(reviewService.checkIfUserHasOrderedProduct(1, 1)).thenReturn(true);

        mockMvc.perform(get("/review/check/product/{productId}/user/{userId}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private static ReviewForProductDTO createReviewDtoForProduct() {
        return new ReviewForProductDTO() {
            @Override
            public Integer getReviewId() {
                return 0;
            }

            @Override
            public String getReview() {
                return "";
            }

            @Override
            public Integer getRating() {
                return 0;
            }

            @Override
            public User getUser() {
                return new User() {
                    @Override
                    public Integer getUserId() {
                        return 1;
                    }

                    @Override
                    public String getUserName() {
                        return "User Name";
                    }
                };
            }

            @Override
            public Product getProduct() {
                return new Product() {
                    @Override
                    public Integer getProductId() {
                        return 1;
                    }

                    @Override
                    public String getProductName() {
                        return "Test Product";
                    }

                    @Override
                    public String getProductImageURL() {
                        return "product.com/image.jpg";
                    }

                    @Override
                    public Double getProductPrice() {
                        return 10.0;
                    }
                };
            }
        };
    }
}
