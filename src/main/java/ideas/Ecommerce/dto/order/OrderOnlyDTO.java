package ideas.Ecommerce.dto.order;

import java.util.List;

public interface OrderOnlyDTO {
    Integer getOrderId();
    Double getTotalAmount();
    String getOrderDate();
    User getUser();
    List<OrderItem> getOrderItems();

    interface User{
        Integer getUserId();
        String getUserName();
    }

    interface OrderItem{
        Integer getOrderItemId();
        Integer getQuantity();
        Product getProduct();
    }

    interface Product{
        Integer getProductId();
        String getProductName();
    }
}
