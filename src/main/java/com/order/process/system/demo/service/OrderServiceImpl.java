package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Inventory;
import com.order.process.system.demo.entity.Item;
import com.order.process.system.demo.entity.Order;
import com.order.process.system.demo.entity.OrderItem;
import com.order.process.system.demo.model.*;
import com.order.process.system.demo.repository.InventoryRepository;
import com.order.process.system.demo.repository.ItemRepository;
import com.order.process.system.demo.repository.OrderItemRepository;
import com.order.process.system.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    InventoryServiceImpl inventoryServive;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ItemRepository itemRepository;


    public OrderResponse createOrder(OrderRequest request){


        HashMap<Item,Integer> issueItems = new HashMap<>();
        List<ItemRequest> available = request.getItems().stream()
                .filter(i -> {
                    Inventory inv = inventoryServive.findByItemId(i.getId());
                    boolean orderItem = i.getQty() <= inv.getQty();
                    if(orderItem){
                        return true;
                    }else{
                        Item item = itemRepository.findById(i.getId()).get();
                        issueItems.put(item, Math.abs(inv.getQty() - i.getQty()));
                        return false;
                    }

                })
                .toList();

        if(available.size() != request.getItems().size()){
            return new OrderErrorResponse(issueItems, "One or more items do not have enough stock");
        }else{
            Order order = new Order();
            order.setCustomerId(request.getCustomerId());
            order.setCreated(LocalDateTime.now());
            order.setStatus(Status.PENDING);
            order.setOrderItems(new ArrayList<>());
            Order orderCreated = orderRepository.save(order);
            if(orderCreated.getId() > -1){
                request.getItems().forEach( i -> {
                    createOrderItem(orderCreated, i);
                });
            }
            return (new OrderCreatedResponse(order.getId(), order.getStatus()));
        }
    }

    private void createOrderItem(Order order, ItemRequest item) {
        Item itemDTO = itemRepository.findById(item.getId()).get();
        OrderItem  orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(itemDTO);
        orderItem.setQty(item.getQty());
        orderItemRepository.save(orderItem);

    }
}
