package com.order.process.system.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for a successfully created orders.
 * Contains the order ID and Status(initially PENDING)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedResponse implements OrderResponse{
    private Long orderId;
    private Status status;
}
