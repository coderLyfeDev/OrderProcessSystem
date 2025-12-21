package com.order.process.system.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusResponse implements OrderResponse {
    private OrderDto order;
    private String message;
}
