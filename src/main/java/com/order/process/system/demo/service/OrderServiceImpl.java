package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Inventory;
import com.order.process.system.demo.entity.Item;
import com.order.process.system.demo.entity.Order;
import com.order.process.system.demo.entity.OrderItem;
import com.order.process.system.demo.model.*;
import com.order.process.system.demo.repository.ItemRepository;
import com.order.process.system.demo.repository.OrderItemRepository;
import com.order.process.system.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    ItemServiceImpl itemService;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){
        HashMap<Item,Integer> issueItems = new HashMap<>();
        List<ItemRequest> available = request.getItems().stream()
                .filter(i -> {
                    Inventory inv = inventoryServive.findByItemId(i.getId());
                    boolean orderItem = i.getQty() <= inv.getQty();
                    if(orderItem){
                        return true;
                    }else{
                        Item item = itemService.findById(i.getId());
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
                    inventoryServive.updateInventory(i.getId(),i.getQty(),"-");
                });
            }
            return (new OrderCreatedResponse(order.getId(), order.getStatus()));
        }
    }
    private void createOrderItem(Order order, ItemRequest item) {
        Item itemDTO = itemService.findById(item.getId());
        OrderItem  orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setItem(itemDTO);
        orderItem.setQty(item.getQty());
        orderItemRepository.save(orderItem);

    }

    public OrderStatusResponse findOrderById(@Valid Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        OrderStatusResponse orderStatusResponse = new OrderStatusResponse();
        if(order == null){
            orderStatusResponse.setMessage("No order with id: "+id+" was found.");
        }else{
            orderStatusResponse.setOrder(new OrderDto(order.getId(),
                    order.getCustomerId(), order.getStatus(),
                    order.getOrderItems()));
            orderStatusResponse.setMessage("Order retrieved and has a status of: "+order.getStatus());
        }
        return orderStatusResponse;
    }


    public UpdateStatusResponse updateOrderStatus(@Valid Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        Status current = order.getStatus();
        Status next = current.next();
        order.setStatus(next);


        if(order == null){
            return new UpdateStatusResponse(2, "No order found for order ID: "+id);
        }else if(order.getStatus() == null){
            return new UpdateStatusResponse(3,"Order has already been completed for order with ID: "+id);
        }else{
            orderRepository.save(order);
            return new UpdateStatusResponse(1,"Status has been updated to: "+next.getLabel());
        }
    }
}
