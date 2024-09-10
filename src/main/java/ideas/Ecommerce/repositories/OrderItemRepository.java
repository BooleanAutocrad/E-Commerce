package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.dto.orderItem.OrderItemDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem , Integer> {

    List<OrderItemDTO> findByOrder_OrderIdAndOrder_User_UserId(Integer orderId, Integer userId);
}
