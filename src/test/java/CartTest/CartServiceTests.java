package CartTest;

import ideas.Ecommerce.Entity.Cart;
import ideas.Ecommerce.Entity.CartItem;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.cart.CartDTO;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.repositories.CartItemsRepository;
import ideas.Ecommerce.repositories.CartRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import ideas.Ecommerce.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CartServiceTests {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartItemsRepository cartItemsRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartDTO mockCartDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCartForUser_Success() {
        Integer userId = 1;

        when(cartRepository.findByUser_UserId(userId)).thenReturn(mockCartDTO);

        CartDTO result = cartService.getCartForUser(userId);

        assertEquals(mockCartDTO, result);
    }

    @Test
    public void testGetCartForUser_NotFound() {
        Integer userId = 1;

        when(cartRepository.findByUser_UserId(userId)).thenReturn(null);

        assertThrows(ResourceNotFound.class, () -> {
            cartService.getCartForUser(userId);
        });
    }

    @Test
    public void testUpdateCart_Success() {
        Cart cart = new Cart();
        cart.setCartId(1);

        when(cartRepository.save(cart)).thenReturn(cart);

        Cart result = cartService.updateCart(cart);

        assertEquals(cart, result);
    }

    @Test
    public void testUpdateCart_NotFound() {
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        cart1.setCartId(1);
        cart2.setCartId(2);

        when(cartRepository.save(cart1)).thenReturn(cart2);

        assertThrows(ResourceNotFound.class, () -> {
            cartService.updateCart(cart1);
        });
    }

    @Test
    public void testUpdateCartTotalAmount_CartItemsExist_Success() {
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalAmount(100.0);

        Product product = new Product();
        product.setProductPrice(20.0);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCartItemId(1);

        List<CartItem> cartItemList = List.of(cartItem);

        when(cartRepository.findById(cartId)).thenReturn(java.util.Optional.of(cart));
        when(cartItemsRepository.findByCart_CartId(cartId)).thenReturn(cartItemList);
        when(productRepository.findById(product.getProductId())).thenReturn(java.util.Optional.of(product));

        cartService.updateCartTotalAmount(cartId);

        assertEquals(100.0, cart.getTotalAmount());
    }

    @Test
    public void testUpdateCartTotalAmount_CartItemsEmpty_Success() {
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalAmount(100.0);

        List<CartItem> cartItemList = List.of();

        when(cartRepository.findById(cartId)).thenReturn(java.util.Optional.of(cart));
        when(cartItemsRepository.findByCart_CartId(cartId)).thenReturn(cartItemList);

        cartService.updateCartTotalAmount(cartId);

        assertEquals(0.0, cart.getTotalAmount());
    }

    @Test
    public void testUpdateCartTotalAmount_ProductNotFound() {
        Integer cartId = 1;
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalAmount(100.0);

        Product product = new Product();
        product.setProductPrice(20.0);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCartItemId(1);

        List<CartItem> cartItemList = List.of(cartItem);

        when(cartRepository.findById(cartId)).thenReturn(java.util.Optional.of(cart));
        when(cartItemsRepository.findByCart_CartId(cartId)).thenReturn(cartItemList);
        when(productRepository.findById(product.getProductId())).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFound.class, () -> {
            cartService.updateCartTotalAmount(cartId);
        });
    }

    @Test
    public void testEmptyCart_Success() {
        Integer userId = 1;

        doNothing().when(cartRepository).deleteByUser_UserId(userId);

        cartService.emptyCart(userId);

        verify(cartRepository, times(1)).deleteByUser_UserId(userId);

    }


}
