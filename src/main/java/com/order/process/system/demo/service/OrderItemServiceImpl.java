package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.OrderItem;
import com.order.process.system.demo.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    OrderItemRepository orderItemRepository;

    public void createOrderItem(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }
}
