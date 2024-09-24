package ideas.Ecommerce.dto.cart;

import ideas.Ecommerce.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyNowDTO {
    private Integer quantity;
    private Product product;
    private Integer cartItemId;
}
