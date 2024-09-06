package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review , Integer> {
}
