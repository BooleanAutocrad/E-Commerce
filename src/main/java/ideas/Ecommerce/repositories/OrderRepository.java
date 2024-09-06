package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order , Integer> {
    boolean existsByUser_UserIdAndOrderItems_Product_ProductId(Integer userId ,Integer productId);
}
