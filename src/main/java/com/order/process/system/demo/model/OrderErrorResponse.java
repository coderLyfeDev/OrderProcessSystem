package com.order.process.system.demo.model;

import com.order.process.system.demo.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;

/**
 * Response for createOrder endpoint when there's
 * an issue with the items
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderErrorResponse  implements OrderResponse{
    HashMap<Item, String> items;
    String errorMessage;
}
