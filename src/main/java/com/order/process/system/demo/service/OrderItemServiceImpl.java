package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Item;
import com.order.process.system.demo.entity.OrderItem;
import com.order.process.system.demo.events.CreateOrderEvent;
import com.order.process.system.demo.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ItemServiceImpl itemService;

    public void createOrderItem(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    @Async
    @EventListener
    public void createOrderItem(CreateOrderEvent event) {

        event.getItemRequest().forEach(i -> {
            Item itemDTO = itemService.findById(i.getId());
            OrderItem  orderItem = new OrderItem();
            orderItem.setOrder(event.getOrder());
            orderItem.setItem(itemDTO);
            orderItem.setQty(i.getQty());
            createOrderItem(orderItem);
            System.out.println("Creating orderItem for item "+i.getId() + " as part of order "+ event.getOrder().getId());
        });


    }
}
