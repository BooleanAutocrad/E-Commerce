package ideas.Ecommerce.ControllerTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.dto.cart.userCartDTO;
import ideas.Ecommerce.service.CartItemService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class CartItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    JwtUtil jwtUtil;

    private String header;
    private CartItem cartItem;
    private OrderItem orderItem;
    private userCartDTO userCart;

    @BeforeEach
    public void setUp() {
        cartItem = new CartItem();
        cartItem.setCartItemId(1);
        cartItem.setQuantity(2);

        orderItem = new OrderItem();
        orderItem.setOrderItemId(1);
        orderItem.setQuantity(1);

        userCart = new userCartDTO();
        userCart.setCartItemCount(2);
        userCart.setCartTotalAmount(200.0);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/cartitem/{cartItemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldUpdateCartItem() throws Exception {
        mockMvc.perform(put("/cartitem/update/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cartItemId\":1,\"quantity\":2}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldEmptyCart() throws Exception {
        mockMvc.perform(delete("/cartitem/empty/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldCheckOutCart() throws Exception {
        mockMvc.perform(post("/cartitem/checkout/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetCartItemCount() throws Exception {
        when(cartItemService.getCartItemCount(1)).thenReturn(userCart);

        mockMvc.perform(get("/cartitem/count/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItemCount").value(2))
                .andExpect(jsonPath("$.cartTotalAmount").value(200.0));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldAddProductToCart() throws Exception {
        mockMvc.perform(put("/cartitem/add/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cartItemId\":1,\"quantity\":2}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldBuyNow() throws Exception {
        mockMvc.perform(post("/cartitem/buynow/user/{userId}/cartItemId/{cartItemId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderItemId\":1,\"quantity\":1}"))
                .andExpect(status().isOk());
    }
}
