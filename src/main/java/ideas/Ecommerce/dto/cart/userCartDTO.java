package ideas.Ecommerce.dto.cart;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class userCartDTO {

    private Integer cartItemCount;
    private Double cartTotalAmount;
}
