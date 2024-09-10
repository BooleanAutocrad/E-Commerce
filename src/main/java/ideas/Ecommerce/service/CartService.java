package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Cart;
import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.CartItemsRepository;
import ideas.Ecommerce.repositories.CartRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemsRepository cartItemsRepository;

    @Autowired
    ProductRepository productRepository;

    public CartDTO getCartForUser(Integer userId){
        return cartRepository.findByUser_UserId(userId);
    }


    public Cart updateCart(Cart cart){
        return cartRepository.save(cart);
    }

    public void updateCartTotalAmount(Integer cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFound("Cart"));
        List<CartItem> cartItems = cartItemsRepository.findByCart_CartId(cartId);
        if(cartItems.isEmpty()){
            cart.setTotalAmount(0.0);
            cartRepository.save(cart);
            return;
        }
        double totalAmount = cartItems.stream().mapToDouble(item -> productRepository.findById(item.getProduct().getProductId())
                .map(product -> item.getQuantity() * product.getProductPrice())
                .orElseThrow(() -> new ResourceNotFound("Product"))).sum();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    public void emptyCart(Integer userId){
        cartRepository.deleteByUser_UserId(userId);
    }
}