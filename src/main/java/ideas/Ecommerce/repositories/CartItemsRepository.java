package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemsRepository extends CrudRepository<CartItem , Integer> {

    void deleteByCart_User_UserId(Integer userId);

    List<CartItem> findByCart_CartId(Integer cartId);
}
