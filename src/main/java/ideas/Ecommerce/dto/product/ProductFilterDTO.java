package ideas.Ecommerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDTO {
    private String condition;
    private Double price;
    private Integer categoryId;
}
