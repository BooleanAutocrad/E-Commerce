package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.ProductAndAverageRatingDTO;
import ideas.Ecommerce.dto.product.ProductDTO;
import ideas.Ecommerce.dto.product.ProductReviewUserAverageRatingDTO;
import ideas.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

//    TODO: Add Product
    @PostMapping("/product")
    public ProductDTO addProduct(@RequestBody Product product){
        Product savedProduct = productService.saveProduct(product);
        return new ProductDTO(savedProduct.getProductId(),savedProduct.getProductName(),savedProduct.getProductImageURL(),savedProduct.getProductPrice(),savedProduct.getProductStock());
    }

//    TODO: Get all products with rating and rating count
    @GetMapping("/dashboard/product")
    public List<ProductAndAverageRatingDTO> getProducts(){
        return productService.getAllProducts();
    }

//    TODO: Get product from productId with rating ,reviews and rating count
    @GetMapping("/dashboard/product/{id}")
    public ProductReviewUserAverageRatingDTO getCompleteProduct(@PathVariable Integer id){
        return productService.getProductById(id);
    }

//        TODO: filter products for price
//         VALID CONDITIONS ARE: gt,lt,eq,lte,gte
    @GetMapping("/dashboard/product/filter/{condition}/{price}")
    public List<ProductAndAverageRatingDTO> getProductsGreaterThan(@PathVariable String condition ,@PathVariable Double price){
        return productService.getAllFilteredProducts(condition,price);
    }

//    TODO: get product for category
    @GetMapping("/dashboard/product/category/{id}")
    public List<ProductAndAverageRatingDTO> getProductForCategory(@PathVariable Integer id){
        return productService.getProductsForCategory(id);
    }

//    TODO: delete product for productId
    @GetMapping("/delete/product/{id}")
    public void deleteProduct(@PathVariable Integer id){
        productService.deleteProductById(id);
    }
}
