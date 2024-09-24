package OrderItems;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.exception.ResourceNotFound;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(orderService.createOrder(any(Order.class))).thenReturn(new Order(1, 500.0, "2023-10-10", new ApplicationUser(1, "User1", "email", "password", "address", "role", null, null, null), null));
        doNothing().when(productService).updateProductStock(orderItem.getProduct().getProductId(), 8);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem result = orderItemsService.BuyNow(orderItem, 1);

        assertNotNull(result);
        assertNotNull(result.getOrder().getOrderId());
        assertEquals(1, result.getProduct().getProductId());
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void buyNow_ProductNotFound() {
        Integer productId = 1;
        OrderItem orderItem = new OrderItem(1, 2, new Product(productId, null, null, null, null, null, null, null, null), null);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> {
            orderItemsService.BuyNow(orderItem, 1);
        });
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
        Product product1 = new Product(1, "Product1", "url", 100.0, 10, null, null, null, null);
        Product product2 = new Product(2, "Product2", "url", 100.0, 10, null, null, null, null);
        OrderItem orderItem1 = new OrderItem(1, 2, product1, null);
        OrderItem orderItem2 = new OrderItem(2, 3, product2, null);
        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        ApplicationUser user = new ApplicationUser(1, "User1", "email", "password", "address", "role", null, null, null);
        Order order = new Order(1, 500.0, "2023-10-10", user, null);

        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
        when(productService.findAllProductsByIDs(anyList())).thenReturn(Arrays.asList(product1, product2));
        when(productRepository.saveAll(anyList())).thenReturn(Arrays.asList(product1, product2));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.saveAll(anyList())).thenReturn(orderItems);

        List<OrderItem> result = orderItemsService.saveAllOrderItems(orderItems, 1);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void saveAllOrderItems_InsufficientStock() {
        Product product1 = new Product(1, "Product1", "url", 100.0, 1, null, null, null, null);
        Product product2 = new Product(2, "Product2", "url", 100.0, 1, null, null, null, null);
        OrderItem orderItem1 = new OrderItem(1, 2, product1, null);
        OrderItem orderItem2 = new OrderItem(2, 3, product2, null);

        List<OrderItem> orderItems = Arrays.asList(orderItem1, orderItem2);

        when(orderService.createOrder(any(Order.class))).thenReturn(new Order(1, 100.0, null, null, null));
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2)).thenReturn(Optional.of(product2));
        when(productService.findAllProductsByIDs(anyList())).thenReturn(Arrays.asList(product1, product2));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderItemsService.saveAllOrderItems(orderItems, 1);
        });

        assertEquals("Product stock is less than the quantity you want to order", exception.getMessage());
    }
}
