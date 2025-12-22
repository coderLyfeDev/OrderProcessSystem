package com.order.process.system.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for the CreateOrder endpoint when
 * an order is missing something to be created.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncompleteOrderResponse implements OrderResponse {
    String message;
}
