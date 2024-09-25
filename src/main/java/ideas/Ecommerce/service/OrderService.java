package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.dto.order.OrderOnlyDTO;
import ideas.Ecommerce.dto.order.OrderWithUserAndProductsDTO;
import ideas.Ecommerce.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<OrderOnlyDTO> getOrderHistory(Integer userId) {
        return orderRepository.findByUser_UserId(userId, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public OrderWithUserAndProductsDTO getOrderDetails(Integer orderId, Integer userId) {
        return orderRepository.findByOrderIdAndUser_UserId(orderId, userId);
    }

    public List<OrderOnlyDTO> getOrdersBetweenDatesForUser(String startDate, String endDate, Integer userId) {
        return orderRepository.findByOrderDateBetweenAndUser_UserId(startDate, endDate, userId, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public List<OrderOnlyDTO> getOrdersBetweenDates(String startDate, String endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public List<OrderOnlyDTO> getOrdersBeforeDateForUser(String endDate, Integer userId) {
        return orderRepository.findByOrderDateBeforeAndUser_UserId(endDate, userId, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public List<OrderOnlyDTO> getOrdersAfterDateForUser(String startDate, Integer userId) {
        return orderRepository.findByOrderDateAfterAndUser_UserId(startDate, userId, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public boolean existsByUser_UserIdAndOrderItems_Product_ProductId(Integer userId, Integer productId) {
        return orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(userId, productId);
    }

    public Order createOrder(Order order) {
        order.setOrderDate(java.time.LocalDate.now().toString());
        return orderRepository.save(order);
    }

    public Order setOrderTotalAmount(Order order , Double totalAmount) {
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    public List<OrderOnlyDTO>  getOrdersForDate(String date , Integer userId){
        return orderRepository.findByOrderDateAndUser_UserId(date,userId, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

}
