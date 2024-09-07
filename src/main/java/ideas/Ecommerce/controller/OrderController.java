package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.Order;
import ideas.Ecommerce.dto.order.OrderDTO;
import ideas.Ecommerce.dto.order.OrderOnlyDTO;
import ideas.Ecommerce.dto.order.OrderWithUserAndProductsDTO;
import ideas.Ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

//    TODO: Create A New Order
    @PostMapping("/order/new")
    public Order createOrder(@RequestBody Order order){
        return orderService.createOrder(order);
    }

//    TODO: Get All Orders For User
    @GetMapping("/order/user/{userId}")
    public List<OrderOnlyDTO> getOrderForUser(@PathVariable Integer userId){
        return orderService.getOrderHistory(userId);
    }

//    TODO: Get Order Details Of A Specific Order
    @GetMapping("/order/{orderId}/user/{userId}")
    public OrderWithUserAndProductsDTO getOrderDetails(@PathVariable Integer orderId ,@PathVariable Integer userId){
        return orderService.getOrderDetails(orderId,userId);
    }

//    TODO: Get Order Details Of A Specific Order
    @GetMapping("/order/{orderId}")
    public OrderDTO getOrder(@PathVariable Integer orderId) throws Exception {
        return orderService.getOrderDetails(orderId);
    }

//    TODO: Get Orders Between Dates For User
    @GetMapping("/order/betweendate/{startDate}/{endDate}/user/{userId}")
    public List<OrderOnlyDTO> getOrdersBetweenDatesForUser(@PathVariable String startDate , @PathVariable String endDate , @PathVariable Integer userId){
        return orderService.getOrdersBetweenDatesForUser(startDate,endDate,userId);
    }

//    TODO: Get Orders Between Dates
    @GetMapping("/order/betweendate/{startDate}/{endDate}")
    public List<OrderOnlyDTO> getOrdersBetweenDates(@PathVariable String startDate , @PathVariable String endDate){
        return orderService.getOrdersBetweenDates(startDate,endDate);
    }

//    TODO: Get Orders Before Date For User
    @GetMapping("/order/enddate/{endDate}/user/{userId}")
    public List<OrderOnlyDTO> getOrdersBeforeDateForUser(@PathVariable String endDate , @PathVariable Integer userId){
        return orderService.getOrdersBeforeDateForUser(endDate,userId);
    }

//    TODO: Get Orders After Date For User
    @GetMapping("/order/startdate/{startDate}/user/{userId}")
    public List<OrderOnlyDTO> getOrdersAfterDateForUser(@PathVariable String startDate , @PathVariable Integer userId){
        return orderService.getOrdersAfterDateForUser(startDate,userId);
    }

//    TODO: Check if a user has ordered a specific product
    @GetMapping("/order/user/{userId}/product/{productId}")
    public boolean existsByUser_UserIdAndOrderItems_Product_ProductId(@PathVariable Integer userId ,@PathVariable Integer productId){
        return orderService.existsByUser_UserIdAndOrderItems_Product_ProductId(userId,productId);
    }

}
