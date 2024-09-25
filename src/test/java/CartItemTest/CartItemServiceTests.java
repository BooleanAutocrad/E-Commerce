package CartItemTest;

import ideas.Ecommerce.Entity.Cart;
import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.cart.CartDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void testDeleteCartItem() {
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
    public void testGetCartItemCount_UserCartNotFound() {
        Integer userId = 1;

        when(cartService.getCartForUser(userId)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFound.class, () -> {
            cartItemService.getCartItemCount(userId);
        });

        assertEquals("User Cart not found", exception.getMessage());
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
