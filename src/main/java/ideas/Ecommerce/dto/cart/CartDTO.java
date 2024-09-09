package ideas.Ecommerce.dto.cart;

import java.util.List;

public interface CartDTO {
    Integer getCartId();
    Double getTotalAmount();
    User getUser();
    List<CartItem> getCartItems();

    interface User {
        Integer getUserId();
        String getUserName();
    }

    interface CartItem {
        Integer getCartItemId();
        Integer getQuantity();
        Product getProduct();
    }

    interface Product {
        Integer getProductId();
        String getProductName();
        Double getPrice();
        String getProductImageURL();
    }
}
