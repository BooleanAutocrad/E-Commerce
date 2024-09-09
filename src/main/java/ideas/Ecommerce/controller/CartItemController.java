package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartItemController {

    @Autowired
    CartItemService cartItemService;

//    TODO: Delete Product from cart
    @DeleteMapping("/cartitem/{cartItemId}")
    public void deleteCartItem(@PathVariable Integer cartItemId){
        cartItemService.deleteCartItem(cartItemId);
    }

//    TODO: Updating Cart or Add Product to Cart
    @PutMapping("/cartitem/update/user/{userId}")
    public void updateCartItem(@RequestBody CartItem cartItem, @PathVariable Integer userId){
        cartItemService.updateCartItem(cartItem, userId);
    }

//    TODO: Empty Cart
    @DeleteMapping("/cartitem/empty/user/{userId}")
    public void emptyCart(@PathVariable Integer userId){
        cartItemService.emptyCart(userId);
    }

//    TODO: Check Out Cart (Buy All Products in cart)
    @PostMapping("/cartitem/checkout/user/{userId}")
    public void checkOutCart(@PathVariable Integer userId){
        cartItemService.checkOutCart(userId);
    }
}
