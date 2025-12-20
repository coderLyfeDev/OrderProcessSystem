package com.order.process.system.demo.model;

import com.order.process.system.demo.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderErrorResponse  implements OrderResponse{
    HashMap<Item, Integer> items;
    String errorMessage;
}
