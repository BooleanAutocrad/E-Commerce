package ideas.Ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;
    private Double totalAmount;
    private String orderDate;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    ApplicationUser user;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems;

}
