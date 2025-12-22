package com.order.process.system.demo.model;

import com.order.process.system.demo.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO for Order entity
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private int customerId;
    private Status status;
    private List<OrderItem> items;
}
