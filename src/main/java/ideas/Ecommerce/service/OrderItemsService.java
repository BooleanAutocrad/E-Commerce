package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.orderItem.OrderItemDTO;
import ideas.Ecommerce.dto.product.ProductReviewUserAverageRatingDTO;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.OrderItemRepository;
import ideas.Ecommerce.repositories.ProductRepository;
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

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    public OrderItem BuyNow(OrderItem orderItem, Integer userId) {
        Product product = productRepository.findById(orderItem.getProduct().getProductId()).orElseThrow(() -> new ResourceNotFound("Product"));
        Double totalAmount = product.getProductPrice() * orderItem.getQuantity();
        Integer productStock = product.getProductStock();
        if (productStock < orderItem.getQuantity()) {
            throw new IllegalArgumentException("Product stock is less than the quantity you want to order");
        }
        Order order = orderService.createOrder(new Order(0, totalAmount, null, new ApplicationUser(userId, null, null, null, null, null, null, null, null), null));
        orderItem.setOrder(new Order(order.getOrderId(), null, null, null, null));
        productService.updateProductStock(orderItem.getProduct().getProductId(), productStock - orderItem.getQuantity());
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItemDTO> getOrderItemsForUser(Integer userId, Integer orderId) {
        return orderItemRepository.findByOrder_OrderIdAndOrder_User_UserId(orderId, userId);
    }

    public List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems, Integer userId) {
        Order order = orderService.createOrder(new Order(0, null, null, new ApplicationUser(userId, null, null, null, null, null, null, null, null), null));
        final Double[] totalAmount = {0.0};
        orderItems.forEach(orderItem -> {
            orderItem.setOrder(new Order(order.getOrderId(), null, null, null, null));
            Product product = productRepository.findById(orderItem.getProduct().getProductId()).orElseThrow(() -> new ResourceNotFound("Product"));
            Integer productStock = product.getProductStock();
            if (productStock < orderItem.getQuantity()) {
                throw new IllegalArgumentException("Product stock is less than the quantity you want to order");
            }
            productService.updateProductStock(orderItem.getProduct().getProductId(), productStock - orderItem.getQuantity());
            totalAmount[0] += product.getProductPrice() * orderItem.getQuantity();
        });
        orderService.setOrderTotalAmount(order , totalAmount[0]);

        return StreamSupport.stream(orderItemRepository.saveAll(orderItems).spliterator(), false)
                .collect(Collectors.toList());
    }
}
