package ideas.Ecommerce.dto.order;

import java.util.List;

public interface OrderWithUserAndProductsDTO {
    Integer getOrderId();
    Double getTotalAmount();
    String getOrderDate();
    OrderWithUserAndProductsDTO.User getUser();
    List<OrderWithUserAndProductsDTO.OrderItem> getOrderItems();

    interface User{
        Integer getUserId();
        String getUserName();
        String getAddress();
    }

    interface OrderItem{
        Integer getOrderItemId();
        Integer getQuantity();
        OrderWithUserAndProductsDTO.Product getProduct();
    }

    interface Product{
        Integer getProductId();
        String getProductName();
        String getProductImageURL();
        Double getProductPrice();
        Integer getProductStock();
    }
}
