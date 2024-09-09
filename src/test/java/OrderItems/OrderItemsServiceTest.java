package OrderItems;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.repositories.OrderItemRepository;
import ideas.Ecommerce.repositories.OrderRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import ideas.Ecommerce.service.OrderItemsService;
import ideas.Ecommerce.service.OrderService;
import ideas.Ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderItemsServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemsService orderItemsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buyNow_Success() {
        Integer productId = 1;
        Product product = new Product(1, "Product1", "url", 100.0, 10, null, null, null, null);
        OrderItem orderItem = new OrderItem(1, 2, product, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        when(productService.getProductStockByProductId(productId)).thenReturn(10);
        when(orderService.createOrder(any(Order.class))).thenReturn(new Order(1, 500.0, "2023-10-10", new ApplicationUser(1, "User1", "email", "password", "address", "role", null, null, null), null));

        OrderItem result = orderItemsService.BuyNow(orderItem, 1);

        assertNotNull(result);
        assertEquals(1, result.getProduct().getProductId());
        verify(productService, times(1)).getProductStockByProductId(1);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void buyNow_InsufficientStock() {
        Product product = new Product(1, "Product1", "url", 100.0, 1, null, null, null, null);
        OrderItem orderItem = new OrderItem(1, 2, product, null);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderItemsService.BuyNow(orderItem, 1);
        });

        assertEquals("Product stock is less than the quantity you want to order", exception.getMessage());
    }

    @Test
    void saveAllOrderItems_Success() {
        Product product = new Product(1, "Product1", "url", 100.0, 10, null, null, null, null);
        OrderItem orderItem1 = new OrderItem(1, 2, product, null);
        OrderItem orderItem2 = new OrderItem(2, 3, product, null);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        ApplicationUser user = new ApplicationUser(1, "User1", "email", "password", "address", "role", null, null, null);
        Order order = new Order(1, 500.0, "2023-10-10", user, null);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.saveAll(anyList())).thenReturn(orderItems);
        when(productService.getProductStockByProductId(1)).thenReturn(10);

        List<OrderItem> result = orderItemsService.saveAllOrderItems(orderItems, 1);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void saveAllOrderItems_InsufficientStock() {
        Product product = new Product(1, "Product1", "url", 100.0, 2, null, null, null, null);
        OrderItem orderItem1 = new OrderItem(1, 2, product, null);
        OrderItem orderItem2 = new OrderItem(2, 3, product, null);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productService.getProductStockByProductId(1)).thenReturn(2);

        when(orderService.createOrder(any(Order.class))).thenReturn(new Order(1, 100.0, null, null, null));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderItemsService.saveAllOrderItems(orderItems, 1);
        });

        assertEquals("Product stock is less than the quantity you want to order", exception.getMessage());
    }
}
