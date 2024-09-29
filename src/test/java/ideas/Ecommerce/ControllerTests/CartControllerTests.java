package ideas.Ecommerce.ControllerTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.service.CartService;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class CartControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private CartDTO cartDTO;

    @MockBean
    JwtUtil jwtUtil;

    private String header;
    @BeforeEach
    public void setUp() {
        cartDTO = createCartDTO();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetCartForUser() throws Exception {
        when(cartService.getCartForUser(1)).thenReturn(cartDTO);

        mockMvc.perform(get("/cart/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(200.0))
                .andExpect(jsonPath("$.user.userId").value(1))
                .andExpect(jsonPath("$.user.userName").value("User Name"))
                .andExpect(jsonPath("$.cartItems[0].cartItemId").value(1))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2))
                .andExpect(jsonPath("$.cartItems[0].product.productId").value(1))
                .andExpect(jsonPath("$.cartItems[0].product.productName").value("Product 1"))
                .andExpect(jsonPath("$.cartItems[0].product.productPrice").value(50.0))
                .andExpect(jsonPath("$.cartItems[0].product.productImageURL").value("product1.jpg"));
    }

    private static CartDTO createCartDTO() {
        return new CartDTO() {
            @Override
            public Integer getCartId() {
                return 1;
            }

            @Override
            public Double getTotalAmount() {
                return 200.0;
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
            public List<CartItem> getCartItems() {
                List<CartItem> cartItems = new ArrayList<>();
                cartItems.add(new CartItem() {
                    @Override
                    public Integer getCartItemId() {
                        return 1;
                    }

                    @Override
                    public Integer getQuantity() {
                        return 2;
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
                                return "Product 1";
                            }

                            @Override
                            public Double getProductPrice() {
                                return 50.0;
                            }

                            @Override
                            public String getProductImageURL() {
                                return "product1.jpg";
                            }
                        };
                    }
                });
                return cartItems;
            }
        };
    }
}
