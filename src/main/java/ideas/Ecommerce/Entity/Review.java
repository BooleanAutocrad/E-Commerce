package ideas.Ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reviewId;
    private String review;
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    ApplicationUser user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

}
