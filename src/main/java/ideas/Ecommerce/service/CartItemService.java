package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.*;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.repositories.CartItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    CartItemsRepository cartItemsRepository;

    @Autowired
    CartService cartService;

    @Autowired
    OrderItemsService orderItemsService;

    public void deleteCartItem(Integer cartItemId){
        Integer cartId = cartItemsRepository.findById(cartItemId).get().getCart().getCartId();
        cartItemsRepository.deleteById(cartItemId);
        cartService.updateCartTotalAmount(cartId);
    }

    public void deleteMultipleCartItems(List<Integer> cartItemIds) {
        for (Integer cartItemId : cartItemIds) {
            cartItemsRepository.deleteById(cartItemId);
        }
    }

    public void updateCartItem(CartItem cartItem , Integer userId){
        CartDTO cart = cartService.getCartForUser(userId);
        if(cart == null){
            Cart newCart = new Cart();
            newCart.setUser(new ApplicationUser(userId,null,null,null,null,null,null,null,null));
            Cart updatedCart = cartService.updateCart(newCart);
            cartItem.setCart(updatedCart);
        } else {
            cartItem.setCart(new Cart(cart.getCartId(),null,null,null));
        }
        cartItemsRepository.save(cartItem);
        cartService.updateCartTotalAmount(cartItem.getCart().getCartId());
    }

    public void emptyCart(Integer userId){
        cartItemsRepository.deleteByCart_User_UserId(userId);
        cartService.emptyCart(userId);
    }

    public void checkOutCart(Integer userId){
        CartDTO cart = cartService.getCartForUser(userId);
        if(cart.getCartItems().isEmpty()){
            throw new RuntimeException("Cart is Empty");
        }
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> new OrderItem(0,cartItem.getQuantity(),new Product(cartItem.getProduct().getProductId(),null,null,null,null,null,null,null,null),null)).toList();
        orderItemsService.saveAllOrderItems(orderItems,userId);

        cartItemsRepository.deleteByCart_User_UserId(userId);
        cartService.emptyCart(userId);
    }

}
