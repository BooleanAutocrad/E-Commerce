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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private String productImageURL;
    private Double productPrice;
    private Integer productStock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @OneToMany(mappedBy = "product")
    List<CartItem> cartItems;

    @OneToMany(mappedBy = "product")
    List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product")
    List<Review> reviews;

}
