package ideas.Ecommerce.ControllerTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.dto.orderItem.OrderItemDTO;
import ideas.Ecommerce.service.OrderItemsService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class OrderItemsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemsService orderItemsService;

    @MockBean
    JwtUtil jwtUtil;


    private String header;
    private OrderItem orderItem;
    private List<OrderItem> orderItemList;
    private OrderItemDTO orderItemDTO;
    private List<OrderItemDTO> orderItemDTOList;

    @BeforeEach
    public void setUp() {
        orderItem = new OrderItem();
        orderItem.setOrderItemId(1);
        orderItem.setQuantity(2);

        orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        orderItemDTO = createOrderItemDTO();
        orderItemDTOList = new ArrayList<>();
        orderItemDTOList.add(orderItemDTO);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldCreateOrderItem() throws Exception {
        when(orderItemsService.BuyNow(any(OrderItem.class), any(Integer.class))).thenReturn(orderItem);

        mockMvc.perform(post("/orderItems/buynow/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":2,\"product\":{\"productId\":1},\"order\":{\"orderId\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderItemId").value(1))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldSaveAllOrderItems() throws Exception {
        when(orderItemsService.saveAllOrderItems(any(List.class), any(Integer.class))).thenReturn(orderItemList);

        mockMvc.perform(post("/orderItems/saveall/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"quantity\":2,\"product\":{\"productId\":1},\"order\":{\"orderId\":1}}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderItemId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrderItemsForUser() throws Exception {
        when(orderItemsService.getOrderItemsForUser(1, 1)).thenReturn(orderItemDTOList);

        mockMvc.perform(get("/orderItems/user/{userId}/order/{orderId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderItemId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    private OrderItemDTO createOrderItemDTO() {
        return new OrderItemDTO() {
            @Override
            public Integer getOrderItemId() {
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
                    public String getProductImageURL() {
                        return "image_url";
                    }

                    @Override
                    public Double getProductPrice() {
                        return 50.0;
                    }

                    @Override
                    public Integer getProductStock() {
                        return 100;
                    }
                };
            }

            @Override
            public Order getOrder() {
                return new Order() {
                    @Override
                    public Integer getOrderId() {
                        return 1;
                    }

                    @Override
                    public Double getTotalAmount() {
                        return 100.0;
                    }

                    @Override
                    public String getOrderDate() {
                        return "2024-09-01";
                    }
                };
            }
        };
    }
}
