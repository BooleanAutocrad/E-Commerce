package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.*;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.dto.cart.userCartDTO;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.CartItemsRepository;
import ideas.Ecommerce.repositories.OrderItemRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    CartItemsRepository cartItemsRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    OrderItemsService orderItemsService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    public void deleteCartItem(Integer cartItemId) {
        Optional<CartItem> cartItemOptional = cartItemsRepository.findById(cartItemId);
        if (!cartItemOptional.isPresent()) {
            throw new ResourceNotFound("CartItem with id: " + cartItemId);
        }
        CartItem cartItem = cartItemOptional.get();
        Integer cartId = cartItem.getCart().getCartId();
        cartItemsRepository.deleteById(cartItemId);
        cartService.updateCartTotalAmount(cartId);
    }

    public void buyNow(OrderItem orderItem, Integer userId, Integer cartItemId) {
        Product product = productRepository.findById(orderItem.getProduct().getProductId()).orElseThrow(() -> new ResourceNotFound("Product"));
        Double totalAmount = product.getProductPrice() * orderItem.getQuantity();
        Integer productStock = product.getProductStock();
        if (productStock < orderItem.getQuantity()) {
            throw new IllegalArgumentException("Product stock is less than the quantity you want to order");
        }
        Order order = orderService.createOrder(new Order(0, totalAmount, null, new ApplicationUser(userId, null, null, null, null, null, null, null, null), null));
        orderItem.setOrder(new Order(order.getOrderId(), null, null, null, null));
        productService.updateProductStock(orderItem.getProduct().getProductId(), productStock - orderItem.getQuantity());
        orderItemRepository.save(orderItem);
        deleteCartItems(cartItemId);
    }

    public userCartDTO getCartItemCount(Integer userId) {
        CartDTO cart = cartService.getCartForUser(userId);
        if (cart == null) {
            throw new ResourceNotFound("User Cart");
        }

        return new userCartDTO(cartItemsRepository.getCartItemCountByCartId(cart.getCartId()), cart.getTotalAmount());
    }

    public void deleteCartItems(Integer cartItemId) {
        cartItemsRepository.deleteById(cartItemId);
    }

    public void updateCartItem(CartItem cartItem, Integer userId) {
        CartDTO cart = cartService.getCartForUser(userId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(new ApplicationUser(userId, null, null, null, null, null, null, null, null));
            Cart updatedCart = cartService.updateCart(newCart);
            cartItem.setCart(updatedCart);
        } else {
            cartItem.setCart(new Cart(cart.getCartId(), null, null, null));
        }
        CartItem existingCartItemForProduct = cartItemsRepository.findByCart_CartIdAndProduct_ProductId(cartItem.getCart().getCartId(), cartItem.getProduct().getProductId());
        if (existingCartItemForProduct != null) {
            existingCartItemForProduct.setQuantity(cartItem.getQuantity());
            cartItemsRepository.save(existingCartItemForProduct);
        } else {
            cartItemsRepository.save(cartItem);
        }
        cartService.updateCartTotalAmount(cartItem.getCart().getCartId());
    }

    public void updateCartItemsQuantity(CartItem cartItem, Integer userId) {
        CartDTO cart = cartService.getCartForUser(userId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(new ApplicationUser(userId, null, null, null, null, null, null, null, null));
            Cart updatedCart = cartService.updateCart(newCart);
            cartItem.setCart(updatedCart);
        } else {
            cartItem.setCart(new Cart(cart.getCartId(), null, null, null));
        }
        CartItem existingCartItemForProduct = cartItemsRepository.findByCart_CartIdAndProduct_ProductId(cartItem.getCart().getCartId(), cartItem.getProduct().getProductId());
        if (existingCartItemForProduct != null) {
            existingCartItemForProduct.setQuantity(cartItem.getQuantity() + existingCartItemForProduct.getQuantity());
            cartItemsRepository.save(existingCartItemForProduct);
        } else {
            cartItemsRepository.save(cartItem);
        }
        cartService.updateCartTotalAmount(cartItem.getCart().getCartId());
    }

    public void emptyCart(Integer userId) {
        cartItemsRepository.deleteByCart_User_UserId(userId);
        cartService.emptyCart(userId);
    }

    public void checkOutCart(Integer userId) {
        CartDTO cart = cartService.getCartForUser(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is Empty");
        }
        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> new OrderItem(0, cartItem.getQuantity(), new Product(cartItem.getProduct().getProductId(), null, null, null, null, null, null, null, null), null)).toList();
        orderItemsService.saveAllOrderItems(orderItems, userId);

        cartItemsRepository.deleteByCart_User_UserId(userId);
        cartService.emptyCart(userId);
    }

}
