package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem , Integer> {

    List<OrderItem> findByOrder_OrderIdAndOrder_User_UserId(Integer orderId, Integer userId);
}
