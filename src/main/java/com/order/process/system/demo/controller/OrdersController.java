package com.order.process.system.demo.controller;

import com.order.process.system.demo.model.OrderCreatedResponse;
import com.order.process.system.demo.model.OrderRequest;
import com.order.process.system.demo.model.OrderResponse;
import com.order.process.system.demo.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    OrderServiceImpl orderService;


    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request){
        OrderResponse orderResponse = orderService.createOrder(request);

        if(orderResponse instanceof OrderCreatedResponse){
            return ResponseEntity.ok(orderResponse);
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(orderResponse);
        }

    }
}
