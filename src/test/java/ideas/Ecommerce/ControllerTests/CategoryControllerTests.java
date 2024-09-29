package ideas.Ecommerce.ControllerTests;

import ideas.Ecommerce.CapstoneDataApplication;
import ideas.Ecommerce.Entity.Category;
import ideas.Ecommerce.dto.category.CategoryDTO;
import ideas.Ecommerce.dto.category.CategoryIdAndNameDTO;
import ideas.Ecommerce.service.CategoryService;
import ideas.Ecommerce.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CapstoneDataApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    JwtUtil jwtUtil;

    private String header;
    private Category category;
    private CategoryDTO categoryDTO;
    private List<CategoryIdAndNameDTO> categoryList;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Electronics");

        categoryDTO = new CategoryDTO(category.getCategoryId(), category.getCategoryName());

        categoryList = new ArrayList<>();
        categoryList.add(createCategoryIdAndNameDTO());

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldAddCategory() throws Exception {
        when(categoryService.saveCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"Electronics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void shouldGetAllCategories() throws Exception {
        when(categoryService.getCategories()).thenReturn(categoryList);

        mockMvc.perform(get("/dashboard/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"));
    }

    private CategoryIdAndNameDTO createCategoryIdAndNameDTO() {
        return new CategoryIdAndNameDTO() {
            @Override
            public Integer getCategoryId() {
                return 1;
            }

            @Override
            public String getCategoryName() {
                return "Electronics";
            }

            @Override
            public CategoryIdAndNameDTO getParentCategory() {
                return null; // No parent category for this test
            }
        };
    }
}
