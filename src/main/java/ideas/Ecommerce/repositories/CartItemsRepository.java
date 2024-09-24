package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemsRepository extends CrudRepository<CartItem , Integer> {

    @Transactional
    void deleteByCart_User_UserId(Integer userId);

    List<CartItem> findByCart_CartId(Integer cartId);

    CartItem findByCart_CartIdAndProduct_ProductId(Integer cartId , Integer productId);

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart.cartId = :cartId")
    Integer getCartItemCountByCartId(Integer cartId);
}
