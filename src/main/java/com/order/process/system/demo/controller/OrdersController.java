package com.order.process.system.demo.controller;

import com.order.process.system.demo.entity.Order;
import com.order.process.system.demo.model.*;
import com.order.process.system.demo.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
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

    @GetMapping("get/OrderStatus/{id}")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@Valid @PathVariable Long id){
        OrderStatusResponse response = orderService.findOrderById(id);
        if(response.getOrder() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);
        }else{
            return ResponseEntity.ok(response);
        }
    }
}
