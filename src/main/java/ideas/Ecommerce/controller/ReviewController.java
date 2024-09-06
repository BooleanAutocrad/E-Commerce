package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.Product;
import ideas.Ecommerce.Entity.Review;
import ideas.Ecommerce.dto.review.ReviewDTO;
import ideas.Ecommerce.dto.review.ReviewForProductDTO;
import ideas.Ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/review/add")
    public ReviewDTO addReview(@RequestBody Review review) throws Exception {
//    TODO: Add Review For A Product
        return reviewService.writeReviewForProduct(review);
    }

//    TODO: Get Review For A Specific Product
    @GetMapping("/review/product")
    public List<ReviewForProductDTO> getReviewForProduct(@RequestBody Product product){
        return reviewService.getReviewForProduct(product.getProductId());
    }

//    TODO: Edit Review
    @PutMapping("/review/edit")
    public ReviewDTO editReview(@RequestBody ReviewDTO review){
        return reviewService.updateReview(review);
    }

//    TODO: Delete Review
    @DeleteMapping("/review/{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId){
        reviewService.deleteReview(reviewId);
    }

}
