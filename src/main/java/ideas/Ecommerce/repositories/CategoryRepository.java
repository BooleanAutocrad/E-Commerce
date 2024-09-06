package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Category;
import ideas.Ecommerce.dto.category.CategoryIdAndNameDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Integer> {
    List<CategoryIdAndNameDTO> findBy();
}
