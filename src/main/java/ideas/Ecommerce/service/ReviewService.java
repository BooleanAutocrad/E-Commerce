package ideas.Ecommerce.service;

import ideas.Ecommerce.Entity.Review;
import ideas.Ecommerce.dto.review.ReviewDTO;
import ideas.Ecommerce.dto.review.ReviewForProductDTO;
import ideas.Ecommerce.exception.ResourceNotDeleted;
import ideas.Ecommerce.repositories.OrderRepository;
import ideas.Ecommerce.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<ReviewForProductDTO> getReviewForProduct(Integer Id){
        return reviewRepository.findByProduct_ProductId(Id);
    }

    public ReviewDTO writeReviewForProduct(Review review) throws Exception {
        if(checkIfUserHasOrderedProduct(review.getUser().getUserId(),review.getProduct().getProductId())){
            return convertReviewToReviewDTO(reviewRepository.save(review));
        } else {
            throw new Exception("User Has Never Ordered The Product");
        }
    }

    public ReviewDTO updateReview(ReviewDTO review){
        Review updatedReview = new Review();
        updatedReview.setReviewId(review.getReviewId());
        updatedReview.setReview(review.getReview());
        updatedReview.setRating(review.getRating());
        return convertReviewToReviewDTO(reviewRepository.save(updatedReview));
    }

    public void deleteReview(Integer id){
        reviewRepository.deleteById(id);
        Optional<Review> review = reviewRepository.findById(id);
        if(review.isPresent()){
            throw new ResourceNotDeleted("Review");
        }
    }

    private boolean checkIfUserHasOrderedProduct(Integer userId , Integer productId){
        return orderRepository.existsByUser_UserIdAndOrderItems_Product_ProductId(userId,productId);
    }

    private ReviewDTO convertReviewToReviewDTO(Review review){
        return new ReviewDTO(review.getReviewId(),review.getReview(),review.getRating(),review.getUser().getUserId(),review.getUser().getUserName(),review.getProduct().getProductId(),review.getProduct().getProductName());
    }


}
