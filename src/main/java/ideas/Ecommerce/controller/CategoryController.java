package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.Category;
import ideas.Ecommerce.dto.category.CategoryDTO;
import ideas.Ecommerce.dto.category.CategoryIdAndNameDTO;
import ideas.Ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

//    TODO: add Category
    @PostMapping("/category")
    public CategoryDTO addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return new CategoryDTO(savedCategory.getCategoryId(), savedCategory.getCategoryName());
    }

//    TODO: get all categories
    @GetMapping("/dashboard/category")
    public List<CategoryIdAndNameDTO> getAllCategories() {
        return categoryService.getCategories();
    }
}
