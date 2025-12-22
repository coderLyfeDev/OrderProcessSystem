package com.order.process.system.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request to create an order that contains
 * a customer ID and a list of items.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private int customerId;
    @Getter
    private List<ItemRequest> items;
}
