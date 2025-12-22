package com.order.process.system.demo.events;

import com.order.process.system.demo.entity.Order;
import com.order.process.system.demo.model.ItemRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import java.util.List;

/**
 * Spring Application even for when an Order is created.
 * Has two subscribers, InventoryServiceImpl and OrderItemsImpl.
 */

@Getter
@Setter
public class CreateOrderEvent extends ApplicationEvent {
    private Order order;
    private List<ItemRequest> itemRequest;

    public CreateOrderEvent(Object source, Order order, List<ItemRequest> itemRequest){
        super(source);
        System.out.println("Creating order "+order.getId());
        this.order = order;
        this.itemRequest = itemRequest;
    }



}
