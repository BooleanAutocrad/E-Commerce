package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem , Integer> {

}
