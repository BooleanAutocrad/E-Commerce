package ideas.Ecommerce.repositories;

import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.dto.order.OrderOnlyDTO;
import ideas.Ecommerce.dto.order.OrderWithUserAndProductsDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    boolean existsByUser_UserIdAndOrderItems_Product_ProductId(Integer userId, Integer productId);

    List<OrderOnlyDTO> findByUser_UserId(Integer id, Sort sort);

    OrderWithUserAndProductsDTO findByOrderIdAndUser_UserId(Integer orderId, Integer userId);

    List<OrderOnlyDTO> findByOrderDateBetweenAndUser_UserId(String startDate, String endDate, Integer userId, Sort sort);

    List<OrderOnlyDTO> findByOrderDateBetween(String startDate, String endDate, Sort sort);

    List<OrderOnlyDTO> findByOrderDateBeforeAndUser_UserId(String endDate, Integer userId, Sort sort);

    List<OrderOnlyDTO> findByOrderDateAfterAndUser_UserId(String startDate, Integer userId, Sort sort);

    List<OrderOnlyDTO> findByOrderDateAndUser_UserId(String date, Integer userId, Sort sort);

    List<Order> findByOrderDateAfter(String startDate);

}
