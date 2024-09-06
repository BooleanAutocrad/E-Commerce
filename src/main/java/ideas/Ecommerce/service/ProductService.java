package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.*;
import ideas.Ecommerce.exception.IllegalArgument;
import ideas.Ecommerce.exception.UpdateNotPerformed;
import ideas.Ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<ProductAndAverageRatingDTO> getAllProducts() {
        List<ProductAndRatingDTO> products = productRepository.findBy();
        return products.stream()
                .map(this::convertToProductAndAverageRatingDTO)
                .collect(Collectors.toList());
    }

    public ProductReviewUserAverageRatingDTO getProductById(Integer id) {
        ProductAndReviewDTO product = productRepository.findByProductId(id);
        return convertToProductReviewUserAverageRatingDTO(product);
    }

    public List<ProductAndAverageRatingDTO> getAllFilteredProducts(String condition, Double price) {
        Map<String, Function<Double, List<ProductAndRatingDTO>>> conditionMap = Map.of(
                "gt", productRepository::findByProductPriceGreaterThan,
                "lt", productRepository::findByProductPriceLessThan,
                "eq", productRepository::findByProductPriceEquals,
                "lte", productRepository::findByProductPriceLessThanEqual,
                "gte", productRepository::findByProductPriceGreaterThanEqual
        );

        if (!conditionMap.containsKey(condition)) {
            throw new IllegalArgument(condition);
        }

        return conditionMap.get(condition).apply(price)
                .stream()
                .map(this::convertToProductAndAverageRatingDTO)
                .collect(Collectors.toList());
    }

    public List<ProductAndAverageRatingDTO> getProductsForCategory(Integer id){
        List<ProductAndRatingDTO> products = productRepository.findProductsByCategoryIdOrParentCategoryId(id);
        return products.stream()
                .map(this::convertToProductAndAverageRatingDTO)
                .collect(Collectors.toList());
    }

    public Integer getProductStock(Integer productId){
        return productRepository.findByProductId(productId).getProductStock();
    }

    public boolean updateProductStock(Integer productId , Integer updatedStock){
        productRepository.updateProductStock(productId , updatedStock);
        if(Objects.equals(updatedStock, getProductStock(productId))){
            return true;
        } else{
            throw new UpdateNotPerformed("Product Stock Not Updated.");
        }
    }

//    TODO: Add Exception handling and check
    public void deleteProductById(Integer Id){
        productRepository.deleteById(Id);
    }


    private ProductAndAverageRatingDTO convertToProductAndAverageRatingDTO(ProductAndRatingDTO product) {
        ProductAndAverageRatingDTO dto = new ProductAndAverageRatingDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductImageURL(product.getProductImageURL());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductStock(product.getProductStock());

        dto.setCategory(convertToCategoryDTO(product.getCategory()));
        dto.setRatingCounts(calculateRatingCounts(product.getReviews()));
        dto.setAverageRating(calculateAverageRating(product.getReviews()));

        return dto;
    }

    private ProductReviewUserAverageRatingDTO convertToProductReviewUserAverageRatingDTO(ProductAndReviewDTO product) {
        ProductReviewUserAverageRatingDTO dto = new ProductReviewUserAverageRatingDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductImageURL(product.getProductImageURL());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductStock(product.getProductStock());

        dto.setCategory(convertToCategoryDTO(product.getCategory()));
        dto.setRatingCounts(calculateRatingCounts(product.getReviews()));
        dto.setAverageRating(calculateAverageRating(product.getReviews()));
        dto.setReviews(convertToReviewDTOs(product.getReviews()));
        return dto;
    }


    private <T extends BaseCategoryDTO> CategoryDTO convertToCategoryDTO(T category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }

    private <T extends BaseReviewDTO> RatingCountsDTO calculateRatingCounts(List<T> reviews) {
        RatingCountsDTO ratingCounts = new RatingCountsDTO();
        if (reviews != null) {
            Map<Integer, Long> counts = reviews.stream()
                    .map(T::getRating)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            ratingCounts.setCount0(counts.getOrDefault(0, 0L).intValue());
            ratingCounts.setCount1(counts.getOrDefault(1, 0L).intValue());
            ratingCounts.setCount2(counts.getOrDefault(2, 0L).intValue());
            ratingCounts.setCount3(counts.getOrDefault(3, 0L).intValue());
            ratingCounts.setCount4(counts.getOrDefault(4, 0L).intValue());
            ratingCounts.setCount5(counts.getOrDefault(5, 0L).intValue());
        }
        return ratingCounts;
    }

    private <T extends BaseReviewDTO> double calculateAverageRating(List<T> reviews) {
        return reviews == null || reviews.isEmpty() ? 0.0 :
                reviews.stream()
                        .mapToInt(T::getRating)
                        .average()
                        .orElse(0.0);
    }

    private List<ProductReviewUserAverageRatingDTO.ReviewDTO> convertToReviewDTOs(List<ProductAndReviewDTO.ReviewsDTO> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return Collections.emptyList();
        }
        return reviews.stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
    }

    private ProductReviewUserAverageRatingDTO.ReviewDTO convertToReviewDTO(ProductAndReviewDTO.ReviewsDTO review) {
        ProductReviewUserAverageRatingDTO.ReviewDTO dto = new ProductReviewUserAverageRatingDTO.ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setReview(review.getReview());
        dto.setRating(review.getRating());

        if (review.getUser() != null) {
            ProductReviewUserAverageRatingDTO.UserDTO userDTO = new ProductReviewUserAverageRatingDTO.UserDTO();
            userDTO.setUserId(review.getUser().getUserId());
            userDTO.setUserName(review.getUser().getUserName());
            dto.setUser(userDTO);
        }

        return dto;
    }


}
