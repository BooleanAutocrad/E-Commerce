package ideas.Ecommerce.dto.orderItem;

public interface OrderItemDTO {

    Integer getOrderItemId();
    Integer getQuantity();
    Product getProduct();
    Order getOrder();

    interface Product {
        Integer getProductId();
        String getProductName();
        String getProductImageURL();
        Double getProductPrice();
        Integer getProductStock();
    }

    interface Order{
        Integer getOrderId();
        Double getTotalAmount();
        String getOrderDate();
    }
}
