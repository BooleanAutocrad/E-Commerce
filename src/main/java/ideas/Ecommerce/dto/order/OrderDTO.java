package ideas.Ecommerce.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer orderId;
    private Double totalAmount;
    private String orderDate;
    private Integer userId;
    private String userName;
    private String address;

}
