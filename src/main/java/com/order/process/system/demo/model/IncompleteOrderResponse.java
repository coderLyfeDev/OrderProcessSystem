package com.order.process.system.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncompleteOrderResponse implements OrderResponse {
    String message;
}
