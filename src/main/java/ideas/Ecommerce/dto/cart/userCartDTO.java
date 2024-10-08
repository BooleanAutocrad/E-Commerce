package ideas.Ecommerce.dto.cart;


import ideas.Ecommerce.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class userCartDTO {

    private Integer cartItemCount;
    private Double cartTotalAmount;
    private List<ProductDTO> products;
}
