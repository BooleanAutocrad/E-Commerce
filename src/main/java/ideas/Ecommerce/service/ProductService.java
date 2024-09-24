package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.dto.product.*;
import ideas.Ecommerce.exception.IllegalArgument;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.exception.ResourceNotFound;
import ideas.Ecommerce.exception.UpdateNotPerformed;
import ideas.Ecommerce.repositories.OrderItemRepository;
import ideas.Ecommerce.repositories.OrderRepository;
import ideas.Ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<ProductAndAverageRatingDTO> getAllProducts() {
        List<ProductAndRatingDTO> products = productRepository.findBy();
//        Collections.shuffle(products);
        return products.stream()
                .map(product -> {
                    ProductAndAverageRatingDTO dto = convertToProductAndAverageRatingDTO(product);
                    long userReviewCount = countUsersWhoReviewedProduct(product.getProductId());
                    dto.setUserReviewCount(userReviewCount);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Product> findAllProductsByIDs(List<Integer> productIds) {
        return StreamSupport.stream(productRepository.findAllById(productIds).spliterator(), false)
                .collect(Collectors.toList());
    }

    public ProductReviewUserAverageRatingDTO getProductById(Integer id) {
        ProductAndReviewDTO product = productRepository.findByProductId(id);
        return convertToProductReviewUserAverageRatingDTO(product);
    }

    public long countUsersWhoReviewedProduct(Integer productId) {
        ProductAndReviewDTO product = productRepository.findByProductId(productId);
        if (product == null) {
            throw new ResourceNotFound("Product not found with ID: " + productId);
        }
        return product.getReviews().stream()
                .map(review -> review.getUser().getUserId())
                .distinct()
                .count();
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

    public List<ProductAndAverageRatingDTO> getProductsForCategory(Integer id) {
        List<ProductAndRatingDTO> products = productRepository.findProductsByCategoryIdOrParentCategoryId(id);
        return products.stream()
                .map(this::convertToProductAndAverageRatingDTO)
                .collect(Collectors.toList());
    }

    public Integer getProductStock(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get().getProductStock();
        } else {
            throw new ResourceNotFound("Product Not Found");
        }
    }

    public void updateProductStock(Integer productId, Integer updatedStock) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFound("Product"));
        product.setProductStock(updatedStock);
        productRepository.save(product);
    }

    public Integer getProductStockByProductId(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get().getProductStock();
        } else {
            throw new ResourceNotFound("Product Not Found");
        }
    }

    public void deleteProductById(Integer Id) {
        productRepository.deleteById(Id);
        Optional<Product> product = productRepository.findById(Id);
        if (product.isPresent()) {
            throw new ResourceNotDeleted("User ");
        }
    }

    public Long countProductOrdersInLast30Days(Integer productId) {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        String formattedDate = thirtyDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return orderItemRepository.countByOrder_OrderDateAfterAndProduct_ProductId(formattedDate, productId);
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
        dto.setUserReviewCount(product.getReviews().stream()
                .map(review -> review.getUser().getUserId())
                .distinct()
                .count());
        dto.setOrderCount(countProductOrdersInLast30Days(product.getProductId()));

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
