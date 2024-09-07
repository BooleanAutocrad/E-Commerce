package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.dto.order.OrderDTO;
import ideas.Ecommerce.dto.order.OrderOnlyDTO;
import ideas.Ecommerce.dto.order.OrderWithUserAndProductsDTO;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<OrderOnlyDTO> getOrderHistory(Integer id){
        return orderRepository.findByUser_UserId(id);
    }

    public OrderWithUserAndProductsDTO getOrderDetails(Integer orderId ,Integer userId){
        return orderRepository.findByOrderIdAndUser_UserId(orderId,userId);
    }

    public OrderDTO getOrderDetails(Integer orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()){
            return convertToOrderDTO(order.get());
        } else {
            throw new ResourceNotFound("Order");
        }
    }

    public List<OrderOnlyDTO> getOrdersBetweenDatesForUser(String startDate , String endDate , Integer userId){
        return orderRepository.findByOrderDateBetweenAndUser_UserId(startDate,endDate,userId);
    }

    private OrderDTO convertToOrderDTO(Order order){
        return new OrderDTO(order.getOrderId(), order.getTotalAmount(),order.getOrderDate(), order.getUser().getUserId(),order.getUser().getUserName(),order.getUser().getAddress());
    }

    public List<OrderOnlyDTO> getOrdersBetweenDates(String startDate , String endDate){
        return orderRepository.findByOrderDateBetween(startDate,endDate);
    }

    public List<OrderOnlyDTO> getOrdersBeforeDateForUser(String endDate , Integer userId){
        return orderRepository.findByOrderDateBeforeAndUser_UserId(endDate,userId);
    }

    public List<OrderOnlyDTO> getOrdersAfterDateForUser(String startDate , Integer userId){
        return orderRepository.findByOrderDateAfterAndUser_UserId(startDate,userId);
    }

    public boolean existsByUser_UserIdAndOrderItems_Product_ProductId(Integer userId ,Integer productId){
        return orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(userId,productId);
    }

    public Order createOrder(Order order){
        return orderRepository.save(order);
    }
}
