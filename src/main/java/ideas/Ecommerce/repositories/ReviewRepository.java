package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Review;
import ideas.Ecommerce.dto.review.ReviewForProductDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review , Integer> {

    List<ReviewForProductDTO> findByProduct_ProductId(Integer id);
}
