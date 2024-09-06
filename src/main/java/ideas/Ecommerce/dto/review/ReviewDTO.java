package ideas.Ecommerce.dto.review;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReviewDTO {

    private Integer reviewId;
    private String review;
    private Integer rating;
    private Integer userId;
    private String userName;
    private Integer productId;
    private String productName;
}
