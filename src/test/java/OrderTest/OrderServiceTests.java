package OrderTest;

import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.dto.order.OrderOnlyDTO;
import ideas.Ecommerce.dto.order.OrderWithUserAndProductsDTO;
import ideas.Ecommerce.repositories.OrderRepository;
import ideas.Ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderOnlyDTO mockOrderOnlyDTO;

    @Mock
    private OrderWithUserAndProductsDTO mockedOrderWithUserAndProductsDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderHistory() {
        Integer userId = 1;
        List<OrderOnlyDTO> mockOrderOnlyDTOList = List.of(mockOrderOnlyDTO);

        when(orderRepository.findByUser_UserId(userId, Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(mockOrderOnlyDTOList);

        List<OrderOnlyDTO> result = orderService.getOrderHistory(userId);

        assertEquals(mockOrderOnlyDTOList, result);
    }

    @Test
    public void testGetOrderDetails_forOrderIdAndUserId() {
        Integer orderId = 1;
        Integer userId = 1;

        when(orderRepository.findByOrderIdAndUser_UserId(orderId, userId)).thenReturn(mockedOrderWithUserAndProductsDTO);

        OrderWithUserAndProductsDTO result = orderService.getOrderDetails(orderId, userId);

        assertEquals(mockedOrderWithUserAndProductsDTO, result);
    }

    @Test
    public void testGetOrdersBetweenDatesForUser() {
        String startDate = "2021-01-01";
        String endDate = "2021-01-31";
        Integer userId = 1;
        List<OrderOnlyDTO> mockOrderOnlyDTOList = List.of(mockOrderOnlyDTO);

        when(orderRepository.findByOrderDateBetweenAndUser_UserId(startDate, endDate, userId, Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(mockOrderOnlyDTOList);

        List<OrderOnlyDTO> result = orderService.getOrdersBetweenDatesForUser(startDate, endDate, userId);

        assertEquals(mockOrderOnlyDTOList, result);
    }

    @Test
    public void testGetOrdersBetweenDates() {
        String startDate = "2021-01-01";
        String endDate = "2021-01-31";
        List<OrderOnlyDTO> mockOrderOnlyDTOList = List.of(mockOrderOnlyDTO);

        when(orderRepository.findByOrderDateBetween(startDate, endDate, Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(mockOrderOnlyDTOList);

        List<OrderOnlyDTO> result = orderService.getOrdersBetweenDates(startDate, endDate);

        assertEquals(mockOrderOnlyDTOList, result);
    }

    @Test
    public void testGetOrdersBeforeDateForUser() {
        String endDate = "2021-01-31";
        Integer userId = 1;
        List<OrderOnlyDTO> mockOrderOnlyDTOList = List.of(mockOrderOnlyDTO);

        when(orderRepository.findByOrderDateBeforeAndUser_UserId(endDate, userId, Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(mockOrderOnlyDTOList);

        List<OrderOnlyDTO> result = orderService.getOrdersBeforeDateForUser(endDate, userId);

        assertEquals(mockOrderOnlyDTOList, result);
    }

    @Test
    public void testGetOrdersAfterDateForUser() {
        String startDate = "2021-01-01";
        Integer userId = 1;
        List<OrderOnlyDTO> mockOrderOnlyDTOList = List.of(mockOrderOnlyDTO);

        when(orderRepository.findByOrderDateAfterAndUser_UserId(startDate, userId, Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(mockOrderOnlyDTOList);

        List<OrderOnlyDTO> result = orderService.getOrdersAfterDateForUser(startDate, userId);

        assertEquals(mockOrderOnlyDTOList, result);
    }

    @Test
    public void testExistsByUser_UserIdAndOrderItems_Product_ProductId() {
        Integer userId = 1;
        Integer productId = 1;

        when(orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(userId, productId)).thenReturn(true);

        boolean result = orderService.existsByUser_UserIdAndOrderItems_Product_ProductId(userId, productId);

        assertTrue(result);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setOrderDate(java.time.LocalDate.now().toString());
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals(order, result);
    }

    @Test
    public void testSetOrderTotalAmount() {
        Order order = new Order();
        Double totalAmount = 100.0;
        order.setTotalAmount(totalAmount);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.setOrderTotalAmount(order, totalAmount);

        assertEquals(order, result);
        assertEquals(totalAmount, result.getTotalAmount());
    }

    @Test
    public void testGetOrdersForDate() {
        String date = "2021-01-01";
        Integer userId = 1;
        List<OrderOnlyDTO> mockOrderOnlyDTOList = List.of(mockOrderOnlyDTO);

        when(orderRepository.findByOrderDateAndUser_UserId(date, userId, Sort.by(Sort.Direction.DESC, "orderDate"))).thenReturn(mockOrderOnlyDTOList);

        List<OrderOnlyDTO> result = orderService.getOrdersForDate(date, userId);

        assertEquals(mockOrderOnlyDTOList, result);
    }

}
