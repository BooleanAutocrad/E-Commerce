package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.Cart;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/cart/user/{userId}")
    public CartDTO getCartForUser(@PathVariable Integer userId){
        return cartService.getCartForUser(userId);
    }
}
