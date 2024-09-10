package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Cart;
import ideas.Ecommerce.dto.cart.CartDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CartRepository extends CrudRepository<Cart , Integer> {

    CartDTO findByUser_UserId(Integer userId);

    @Transactional
    void deleteByUser_UserId(Integer userId);

}
