package ideas.Ecommerce;

import ideas.Ecommerce.Entity.Category;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.ProductDTO;
import ideas.Ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProductsDriver implements CommandLineRunner {


    @Autowired
    ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(CapstoneDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        TODO: get product by id
//        List<ProductDTO> products = productRepository.findByProductId(52);
//        displayProducts(products);

//        TODO: filter products for price
//        List<ProductDTO> products = productRepository.findByProductPriceLessThan(100.00);
//        List<ProductDTO> products = productRepository.findByProductPriceGreaterThan(100.00);
//        List<ProductDTO> products = productRepository.findByProductPriceEquals(100.00);
//        List<ProductDTO> products = productRepository.findByProductPriceLessThanEqual(100.00);
//        List<ProductDTO> products = productRepository.findByProductPriceGreaterThanEqual(100.00);
//        displayProducts(products);

//        TODO: filter products by category
//        List<ProductDTO> products = productRepository.findProductsByCategoryIdOrParentCategoryId(102);
//        displayProducts(products);

//        TODO: delete products
//        productRepository.deleteById(152);

//        TODO: update stock
//        productRepository.updateProductStock(52,5);


    }
}
