package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.dto.cart.userCartDTO;
import ideas.Ecommerce.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
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

//    TODO: Get number of products in user's cart
    @GetMapping("/cartitem/count/user/{userId}")
    public userCartDTO getCartItemCount(@PathVariable Integer userId){
        return cartItemService.getCartItemCount(userId);
    }

//    TODO: Add product to cart and increase the quantity if product is already in cart
    @PutMapping("/cartitem/add/user/{userId}")
    public void addProductToCart(@RequestBody CartItem cartItem, @PathVariable Integer userId){
        cartItemService.updateCartItemsQuantity(cartItem, userId);
    }
//    TODO: Buy Now (Buy A Single Product Directly)
    @PostMapping("/cartitem/buynow/user/{userId}/cartItemId/{cartItemId}")
    public void buyNow(@RequestBody OrderItem orderItem, @PathVariable Integer userId, @PathVariable Integer cartItemId){
        cartItemService.buyNow(orderItem, userId,cartItemId);
    }
}
