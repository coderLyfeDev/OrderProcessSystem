package com.order.process.system.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for OrderStatus endpoint
 * Returns an order object with order details
 * and a message about the status
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusResponse implements OrderResponse {
    private OrderDto order;
    private String message;
}
