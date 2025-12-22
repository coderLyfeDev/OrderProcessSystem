package com.order.process.system.demo.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for an item that is part of an order.
 * Requires an ID and quantity > 0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    @NotEmpty(message = "Must enter a quantity")
    private int qty;
}
