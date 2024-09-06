package ideas.Ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer categoryId;
    @Column(nullable = false)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_category_id") // FK name
    Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    List<Category> subCategories;

    @OneToMany(mappedBy = "category")
    List<Product> products;

}
