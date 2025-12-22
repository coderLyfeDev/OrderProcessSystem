package com.order.process.system.demo.controller;

import com.order.process.system.demo.model.*;
import com.order.process.system.demo.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller service containing 3 endpoints
 * 1. CreateOrder - Accepts a CreateOrderRequest that contains a customer ID and a list of items for the order.
 * 2. getOrderStatus - Accepts an order ID and returns a response with the order details and the current status.
 * 3. updateOrderStatus - Accepts an order ID and returns 1/3 responses.
 *      status updated
 *      Order ID doesn't belong to an order.
 *      order already has a status of completed
 */

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrdersController {

    @Autowired
    OrderServiceImpl orderService;


    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request){

        OrderResponse orderResponse = orderService.createOrder(request);

        if(orderResponse instanceof OrderCreatedResponse){
            return ResponseEntity.ok(orderResponse);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(orderResponse);
        }

    }

    @GetMapping("/get/OrderStatus/{id}")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@Valid @PathVariable Long id){
        OrderStatusResponse response = orderService.findOrderById(id);
        if(response.getOrder() == null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);
        }else{
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<UpdateStatusResponse> updateOrderStatus(@Valid @PathVariable Long orderId){
        UpdateStatusResponse response = orderService.updateOrderStatus(orderId);
        if(response.getCode() == 1){
            return ResponseEntity.ok(response);
        }else if(response.getCode() == 2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

    }
}
