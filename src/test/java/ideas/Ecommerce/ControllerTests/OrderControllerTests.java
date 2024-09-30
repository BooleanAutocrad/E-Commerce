package ideas.Ecommerce.ControllerTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.dto.order.OrderOnlyDTO;
import ideas.Ecommerce.dto.order.OrderWithUserAndProductsDTO;
import ideas.Ecommerce.service.OrderService;
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
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    JwtUtil jwtUtil;

    private String header;
    private Order order;
    private OrderOnlyDTO orderOnlyDTO;
    private OrderWithUserAndProductsDTO orderWithUserAndProductsDTO;

    @BeforeEach
    public void setUp() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1);
        user.setUserName("User Name");

        order = new Order();
        order.setOrderId(1);
        order.setTotalAmount(100.0);
        order.setOrderDate("2024-09-01");
        order.setUser(user);

        orderOnlyDTO = createOrderOnlyDto();
        orderWithUserAndProductsDTO = createOrderWithUserAndProductsDto();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/order/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .content("{\"totalAmount\":100.0,\"orderDate\":\"2024-09-01\",\"user\":{\"userId\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.0))
                .andExpect(jsonPath("$.orderDate").value("2024-09-01"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrderForUser() throws Exception {
        List<OrderOnlyDTO> orders = new ArrayList<>();
        orders.add(orderOnlyDTO);

        when(orderService.getOrderHistory(1)).thenReturn(orders);

        mockMvc.perform(get("/order/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrderDetails() throws Exception {
        when(orderService.getOrderDetails(1, 1)).thenReturn(orderWithUserAndProductsDTO);

        mockMvc.perform(get("/order/{orderId}/user/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrdersBetweenDatesForUser() throws Exception {
        List<OrderOnlyDTO> orders = new ArrayList<>();
        orders.add(orderOnlyDTO);

        when(orderService.getOrdersBetweenDatesForUser("2024-09-01", "2024-09-30", 1)).thenReturn(orders);

        mockMvc.perform(get("/order/betweendate/{startDate}/{endDate}/user/{userId}", "2024-09-01", "2024-09-30", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrdersBeforeDateForUser() throws Exception {
        List<OrderOnlyDTO> orders = new ArrayList<>();
        orders.add(orderOnlyDTO);

        when(orderService.getOrdersBeforeDateForUser("2024-09-30", 1)).thenReturn(orders);

        mockMvc.perform(get("/order/enddate/{endDate}/user/{userId}", "2024-09-30", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrdersAfterDateForUser() throws Exception {
        List<OrderOnlyDTO> orders = new ArrayList<>();
        orders.add(orderOnlyDTO);

        when(orderService.getOrdersAfterDateForUser("2024-09-01", 1)).thenReturn(orders);

        mockMvc.perform(get("/order/startdate/{startDate}/user/{userId}", "2024-09-01", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrdersBetweenDates() throws Exception {
        List<OrderOnlyDTO> orders = new ArrayList<>();
        orders.add(orderOnlyDTO);

        when(orderService.getOrdersBetweenDates("2024-09-01", "2024-09-30")).thenReturn(orders);

        mockMvc.perform(get("/order/betweendate/{startDate}/{endDate}", "2024-09-01", "2024-09-30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldGetOrdersForDate() throws Exception {
        List<OrderOnlyDTO> orders = new ArrayList<>();
        orders.add(orderOnlyDTO);

        when(orderService.getOrdersForDate("2024-09-01", 1)).thenReturn(orders);

        mockMvc.perform(get("/order/date/{date}/user/{userId}", "2024-09-01", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"CUSTOMER"})
    @Test
    void shouldCheckIfUserHasOrderedProduct() throws Exception {
        when(orderService.existsByUser_UserIdAndOrderItems_Product_ProductId(1, 1)).thenReturn(true);

        mockMvc.perform(get("/order/user/{userId}/product/{productId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private static OrderOnlyDTO createOrderOnlyDto() {
        return new OrderOnlyDTO() {
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

            @Override
            public OrderOnlyDTO.User getUser() {
                return new OrderOnlyDTO.User() {
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
            public List<OrderOnlyDTO.OrderItem> getOrderItems() {
                return new ArrayList<>();
            }
        };
    }

    private static OrderWithUserAndProductsDTO createOrderWithUserAndProductsDto() {
        return new OrderWithUserAndProductsDTO() {
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

            @Override
            public OrderWithUserAndProductsDTO.User getUser() {
                return new OrderWithUserAndProductsDTO.User() {
                    @Override
                    public Integer getUserId() {
                        return 1;
                    }

                    @Override
                    public String getUserName() {
                        return "User Name";
                    }

                    @Override
                    public String getAddress() {
                        return "User Address";
                    }
                };
            }

            @Override
            public List<OrderWithUserAndProductsDTO.OrderItem> getOrderItems() {
                return new ArrayList<>();
            }
        };
    }
}
