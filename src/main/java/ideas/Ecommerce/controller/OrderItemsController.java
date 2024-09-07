package ideas.Ecommerce.controller;

import ideas.Ecommerce.Entity.OrderItem;
import ideas.Ecommerce.service.OrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderItemsController {

    @Autowired
    OrderItemsService orderItemsService;

//    TODO: Buy Now (Buy A Single Product Directly)
    @PostMapping("/orderItems/buynow/user/{userId}")
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem, @PathVariable Integer userId){
        return orderItemsService.createOrderItem(orderItem , userId);
    }

//    TODO: PlaceOrder (Buy Multiple Products)
    @PostMapping("/orderItems/saveall/user/{userId}")
    public List<OrderItem> saveAllOrderItems(@RequestBody List<OrderItem> orderItems, @PathVariable Integer userId){
        return orderItemsService.saveAllOrderItems(orderItems , userId);
    }

//    TODO: Get Order Items For Order And User
    @GetMapping("/orderItems/user/{userId}/order/{orderId}")
    public List<OrderItem> getOrderItemsForUser(@PathVariable Integer userId, @PathVariable Integer orderId){
        return orderItemsService.getOrderItemsForUser(userId,orderId);
    }

}
