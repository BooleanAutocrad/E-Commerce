package ideas.Ecommerce.dto.category;

import java.util.List;

public interface CategoryIdAndNameDTO {
    Integer getCategoryId();
    String getCategoryName();
    CategoryIdAndNameDTO getParentCategory();
}
