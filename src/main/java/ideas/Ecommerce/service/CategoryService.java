package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Category;
import ideas.Ecommerce.dto.category.CategoryIdAndNameDTO;
import ideas.Ecommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public List<CategoryIdAndNameDTO> getCategories(){
        return categoryRepository.findBy();
    }

}
