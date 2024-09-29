package CartItemTest;

import ideas.Ecommerce.Entity.Cart;
import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.dto.cart.userCartDTO;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.CartItemsRepository;
import ideas.Ecommerce.repositories.OrderItemRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import ideas.Ecommerce.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CartItemServiceTests {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemsRepository cartItemsRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartDTO mockCartDTO;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderItemsService orderItemsService;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteCartItem_Success() {
        Integer cartItemId = 1;
        Cart cart = new Cart();
        cart.setCartId(1);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);

        when(cartItemsRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        cartItemService.deleteCartItem(cartItemId);

        verify(cartItemsRepository).deleteById(cartItemId);
        verify(cartService).updateCartTotalAmount(cart.getCartId());
    }

    @Test
    public void testDeleteCartItem_CartItemNotFound(){
        Integer cartItemId = 1;

        when(cartItemsRepository.findById(cartItemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFound.class, () -> {
            cartItemService.deleteCartItem(cartItemId);
        });

        assertEquals("CartItem with id: " + cartItemId + " not found", exception.getMessage());
    }

    @Test
    public void testBuyNow_ProductNotFound() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(new Product());
        orderItem.getProduct().setProductId(1);

        when(productRepository.findById(orderItem.getProduct().getProductId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFound.class, () -> {
            cartItemService.buyNow(orderItem, 1, 1);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testBuyNow_ProductStockLessThanQuantity() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(new Product());
        orderItem.getProduct().setProductId(1);
        orderItem.setQuantity(10);

        Product product = new Product();
        product.setProductStock(5);
        product.setProductPrice(100.0);

        when(productRepository.findById(orderItem.getProduct().getProductId())).thenReturn(Optional.of(product));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartItemService.buyNow(orderItem, 1, 1);
        });

        assertEquals("Product stock is less than the quantity you want to order", exception.getMessage());
    }

    @Test
    public void testBuyNow_Success() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(new Product());
        orderItem.getProduct().setProductId(1);
        orderItem.setQuantity(5);

        Product product = new Product();
        product.setProductStock(10);
        product.setProductPrice(100.0);

        when(productRepository.findById(orderItem.getProduct().getProductId())).thenReturn(Optional.of(product));
        when(orderService.createOrder(any())).thenReturn(new ideas.Ecommerce.Entity.Order());
        when(orderItemRepository.save(any())).thenReturn(orderItem);
        doNothing().when(productService).updateProductStock(orderItem.getProduct().getProductId(), 5);

        cartItemService.buyNow(orderItem, 1, 1);

        verify(productService).updateProductStock(orderItem.getProduct().getProductId(), 5);
        verify(orderItemRepository).save(orderItem);
        verify(cartItemsRepository).deleteById(1);
    }

    @Test
    public void testGetCartItemCount_UserCartNotFound() {
        Integer userId = 1;

        when(cartService.getCartForUser(userId)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFound.class, () -> {
            cartItemService.getCartItemCount(userId);
        });

        assertEquals("User Cart not found", exception.getMessage());
    }

    @Test
    public void testGetCartItemCount_Success(){
        Integer userId = 1;
        Integer cartId = 1;
        Integer cartItemCount = 5;
        Double totalAmount = 100.0;

        when(cartService.getCartForUser(userId)).thenReturn(mockCartDTO);
        when(mockCartDTO.getCartId()).thenReturn(cartId);
        when(cartItemsRepository.getCartItemCountByCartId(cartId)).thenReturn(cartItemCount);
        when(mockCartDTO.getTotalAmount()).thenReturn(totalAmount);

        assertEquals(cartItemCount, cartItemService.getCartItemCount(userId).getCartItemCount());
        assertEquals(totalAmount, cartItemService.getCartItemCount(userId).getCartTotalAmount());
    }


    @Test
    public void testDeleteCartItems_Success() {
        Integer cartItemId = 1;

        cartItemService.deleteCartItems(cartItemId);
        doNothing().when(cartItemsRepository).deleteById(cartItemId);

        verify(cartItemsRepository).deleteById(cartItemId);
    }

    @Test
    public void testUpdateCartItem_Success(){
        Integer userId = 1;
        CartItem cartItem = new CartItem();
        cartItem.setProduct(new Product());
        cartItem.getProduct().setProductId(1);
        cartItem.setQuantity(5);

        Cart cart = new Cart();
        cart.setCartId(1);

        when(cartService.getCartForUser(userId)).thenReturn(mockCartDTO);
        when(mockCartDTO.getCartId()).thenReturn(cart.getCartId());
        when(cartItemsRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), cartItem.getProduct().getProductId())).thenReturn(cartItem);
        when(cartItemsRepository.save(cartItem)).thenReturn(cartItem);

        cartItemService.updateCartItem(cartItem, userId);

        verify(cartItemsRepository).save(cartItem);
        verify(cartService).updateCartTotalAmount(cart.getCartId());
    }

    @Test
    public void testUpdateCartItemsQuantity_Success(){
        Integer userId = 1;
        CartItem cartItem = new CartItem();
        cartItem.setProduct(new Product());
        cartItem.getProduct().setProductId(1);
        cartItem.setQuantity(5);

        Cart cart = new Cart();
        cart.setCartId(1);

        when(cartService.getCartForUser(userId)).thenReturn(mockCartDTO);
        when(mockCartDTO.getCartId()).thenReturn(cart.getCartId());
        when(cartItemsRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), cartItem.getProduct().getProductId())).thenReturn(null);
        when(cartItemsRepository.save(cartItem)).thenReturn(cartItem);

        cartItemService.updateCartItemsQuantity(cartItem, userId);

        verify(cartItemsRepository).save(cartItem);
        verify(cartService).updateCartTotalAmount(cart.getCartId());
    }

    @Test
    public void testEmptyCart() {
        Integer userId = 1;

        cartItemService.emptyCart(userId);

        verify(cartItemsRepository).deleteByCart_User_UserId(userId);
        verify(cartService).emptyCart(userId);
    }

    @Test
    public void testCheckOutCart_CartIsEmpty() {
        Integer userId = 1;
        when(cartService.getCartForUser(userId)).thenReturn(mockCartDTO);
        when(mockCartDTO.getCartItems()).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartItemService.checkOutCart(userId);
        });

        assertEquals("Cart is Empty", exception.getMessage());
    }
}
