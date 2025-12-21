package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Inventory;
import com.order.process.system.demo.entity.Item;
import com.order.process.system.demo.entity.Order;
import com.order.process.system.demo.entity.OrderItem;
import com.order.process.system.demo.events.CreateOrderEvent;
import com.order.process.system.demo.model.*;
import com.order.process.system.demo.repository.ItemRepository;
import com.order.process.system.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
    ItemServiceImpl itemService;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){
        if(!checkRequestInput(request) ){
            return new IncompleteOrderResponse("Order cannot be created as is. Customer ID must be greater than 0 " +
                    "\n and all items must have have a quantity of 1 or more.");
        }
        HashMap<Item,String> issueItems = new HashMap<>();
        List<ItemRequest> available = request.getItems().stream()
                .filter(i -> {
                    Inventory inv = inventoryServive.findByItemId(i.getId());
                    boolean orderItem = i.getQty() <= inv.getQty();
                    if(orderItem){
                        return true;
                    }else{
                        Item item = itemService.findById(i.getId());
                        issueItems.put(item, "Only " + inv.getQty() +" are available");
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

            //publish event once the order is created
            eventPublisher.publishEvent(new CreateOrderEvent(this, orderCreated, request.getItems()));

            return (new OrderCreatedResponse(order.getId(), order.getStatus()));
        }
    }

    private boolean checkRequestInput(CreateOrderRequest request) {

        if (request == null) return false;

        if (request.getCustomerId() <= 0) return false;

        if (request.getItems() == null || request.getItems().isEmpty()) return false;

        return request.getItems().stream().allMatch(item ->
                item != null &&
                        item.getId() != null &&
                        item.getQty() > 0
        );
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

        Status next = null;
        if(order != null) {
            Status current = order.getStatus();
             next = current.next();
        }

        if(order == null){
            return new UpdateStatusResponse(2, "No order found for order ID: "+id);
        }else if(next == null){
            return new UpdateStatusResponse(3,"Order has already been completed for order with ID: "+id);
        }else{
            order.setStatus(next);
            orderRepository.save(order);
            return new UpdateStatusResponse(1,"Status has been updated to: "+next.getLabel());
        }
    }
}
