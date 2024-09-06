package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.ApplicationUser;
import ideas.Ecommerce.dto.user.UserOnlyDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<ApplicationUser , Integer> {
    ApplicationUser findByEmailId(String emailId);
    List<UserOnlyDTO> findBy();
}
