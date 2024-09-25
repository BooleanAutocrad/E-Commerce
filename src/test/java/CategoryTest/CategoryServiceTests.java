package CategoryTest;

import ideas.Ecommerce.Entity.Category;
import ideas.Ecommerce.dto.category.CategoryIdAndNameDTO;
import ideas.Ecommerce.repositories.CategoryRepository;
import ideas.Ecommerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryServiceTests {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryIdAndNameDTO mockCategoryIdAndNameDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category();
        when(categoryRepository.save(category)).thenReturn(category);
        categoryService.saveCategory(category);
        verify(categoryRepository).save(category);
    }

    @Test
    public void testGetCategories() {
        when(categoryRepository.findBy()).thenReturn(List.of(mockCategoryIdAndNameDTO));
        categoryService.getCategories();
        verify(categoryRepository).findBy();
    }
}
