package ProductTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.ProductAndAverageRatingDTO;
import ideas.Ecommerce.dto.product.ProductDTO;
import ideas.Ecommerce.dto.product.ProductFilterDTO;
import ideas.Ecommerce.dto.product.ProductReviewUserAverageRatingDTO;
import ideas.Ecommerce.service.ProductService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    JwtUtil jwtUtil;

    String header;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProductId(1);
        product.setProductName("Test Product");
        product.setProductImageURL("http://example.com/image.jpg");
        product.setProductPrice(100.0);
        product.setProductStock(50);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldAddProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO(1, "Test Product", "http://example.com/image.jpg", 100.0, 50);
        when(productService.saveProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content("{\"productName\":\"Test Product\",\"productImageURL\":\"http://example.com/image.jpg\",\"productPrice\":100.0,\"productStock\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @WithMockUser(username = "customer@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldReturnForbiddenWhenCustomerTriesToAddProduct() throws Exception {
        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productName\":\"Test Product\",\"productImageURL\":\"http://example.com/image.jpg\",\"productPrice\":100.0,\"productStock\":50}"))
                .andExpect(status().isForbidden());
    }


    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldGetAllProducts() throws Exception {
        List<ProductAndAverageRatingDTO> productList = new ArrayList<>();
        ProductAndAverageRatingDTO productDTO = new ProductAndAverageRatingDTO(1, "Test Product", "http://example.com/image.jpg", 100.0, 50, null, null, 4.5, 10);
        productList.add(productDTO);

        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/dashboard/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldGetCompleteProduct() throws Exception {
        ProductReviewUserAverageRatingDTO completeProduct = new ProductReviewUserAverageRatingDTO();
        completeProduct.setProductId(1);
        completeProduct.setProductName("Test Product");
        completeProduct.setProductImageURL("http://example.com/image.jpg");
        completeProduct.setProductPrice(100.0);
        completeProduct.setProductStock(50);
        completeProduct.setAverageRating(4.5);
        completeProduct.setUserReviewCount(10L);
        completeProduct.setOrderCount(5L);

        when(productService.getProductById(1)).thenReturn(completeProduct);

        mockMvc.perform(get("/dashboard/product/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldGetProductsForCondition() throws Exception {
        List<ProductAndAverageRatingDTO> filteredProducts = new ArrayList<>();
        ProductAndAverageRatingDTO productDTO = new ProductAndAverageRatingDTO(1, "Test Product", "http://example.com/image.jpg", 100.0, 50, null, null, 4.5, 10);
        filteredProducts.add(productDTO);

        ProductFilterDTO filterDTO = new ProductFilterDTO();
        when(productService.getAllFilteredProducts(any(String.class), any(ProductFilterDTO.class))).thenReturn(filteredProducts);

        mockMvc.perform(post("/dashboard/product/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("searchText", "Test")
                        .content("{\"someFilterProperty\":\"someValue\"}")) // Adjust the filter JSON as needed
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldGetProductForCategory() throws Exception {
        List<ProductAndAverageRatingDTO> productList = new ArrayList<>();
        ProductAndAverageRatingDTO productDTO = new ProductAndAverageRatingDTO(1, "Test Product", "http://example.com/image.jpg", 100.0, 50, null, null, 4.5, 10);
        productList.add(productDTO);

        when(productService.getProductsForCategory(1)).thenReturn(productList);

        mockMvc.perform(get("/dashboard/product/category/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].productName").value("Test Product"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(get("/delete/product/{id}", 1))
                .andExpect(status().isOk());
    }
}
