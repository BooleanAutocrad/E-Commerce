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
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartId;
    private Double totalAmount;

    @OneToOne
    @JoinColumn(name = "user_id")
    ApplicationUser user;

    @OneToMany(mappedBy = "cart")
    List<CartItem> cartItems;
}