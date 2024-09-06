package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.ProductAndRatingDTO;
import ideas.Ecommerce.dto.product.ProductAndReviewDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product , Integer> {

    List<ProductAndRatingDTO> findBy();
    ProductAndReviewDTO findByProductId(Integer id);
    List<ProductAndRatingDTO> findByProductPriceLessThan(Double price);

    List<ProductAndRatingDTO> findByProductPriceGreaterThan(Double price);

    List<ProductAndRatingDTO> findByProductPriceEquals(Double price);

    List<ProductAndRatingDTO> findByProductPriceLessThanEqual(Double price);

    List<ProductAndRatingDTO> findByProductPriceGreaterThanEqual(Double price);

    @Query("SELECT p FROM Product p " +
            "JOIN p.category c1 " +
            "LEFT JOIN c1.parentCategory c2 " +
            "WHERE c1.categoryId = :categoryId OR c2.categoryId = :categoryId")
    List<ProductAndRatingDTO> findProductsByCategoryIdOrParentCategoryId(@Param("categoryId") Integer categoryId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET product_stock = :stock WHERE product_id = :id", nativeQuery = true)
    void updateProductStock(@Param("id") Integer id, @Param("stock") Integer stock);

}
