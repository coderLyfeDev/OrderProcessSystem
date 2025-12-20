package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Inventory;
import com.order.process.system.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryServive{

    @Autowired
    InventoryRepository inventoryRepository;


    public Inventory findByItemId(Long id){
        return inventoryRepository.findByItemId(id);
    }
}
