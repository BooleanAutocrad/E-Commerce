package ideas.Ecommerce;

import ideas.Ecommerce.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReviewDriver implements CommandLineRunner {

    @Autowired
    ReviewRepository reviewRepository;

    public static void main(String[] args) {
        SpringApplication.run(CapstoneDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        TODO: add Review
//        Review review = reviewRepository.save(new Review(0,"will buy again" , 5 , new ApplicationUser(1,null,null,null,null,null,null,null),new Product(2,null,null,null,null,null,null,null,null)));

    }
}
