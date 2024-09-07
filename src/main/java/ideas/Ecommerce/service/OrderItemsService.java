package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderItemsService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderService orderService;

    public OrderItem createOrderItem(OrderItem orderItem, Integer userId) {
        Double totalAmount = orderItem.getProduct().getProductPrice() * orderItem.getQuantity();
        Order order = orderService.createOrder(new Order(0, totalAmount, null, new ApplicationUser(userId, null, null, null, null, null, null, null, null), null));
        orderItem.setOrder(new Order(order.getOrderId(), null, null, null, null));
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getOrderItemsForUser(Integer userId,Integer orderId) {
        return orderItemRepository.findByOrder_OrderIdAndOrder_User_UserId(orderId, userId);
    }

    public List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems, Integer userId) {
        Double totalAmount = calculateTotalAmountOfOrder(orderItems);
        Order order = orderService.createOrder(new Order(0, totalAmount, null, new ApplicationUser(userId, null, null, null, null, null, null, null, null), null));
        orderItems.forEach(orderItem -> orderItem.setOrder(new Order(order.getOrderId(),null,null,null,null)));
        return StreamSupport.stream(orderItemRepository.saveAll(orderItems).spliterator(), false)
                    .collect(Collectors.toList());
    }

    private Double calculateTotalAmountOfOrder(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getProduct().getProductPrice() * orderItem.getQuantity())
                .sum();
    }
}
