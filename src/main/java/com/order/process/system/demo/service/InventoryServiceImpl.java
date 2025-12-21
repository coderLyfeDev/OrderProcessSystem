package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Inventory;
import com.order.process.system.demo.events.CreateOrderEvent;
import com.order.process.system.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryServive{

    @Autowired
    InventoryRepository inventoryRepository;


    public Inventory findByItemId(Long id){
        return inventoryRepository.findByItemId(id);
    }

    @EventListener
    public void decreaseInventory(CreateOrderEvent event){
        Inventory inventory = inventoryRepository.findByItemId(event.getItemRequest().getId());
        inventory.setQty(inventory.getQty() - event.getItemRequest().getQty());
        Inventory inventoryUpdated = inventoryRepository.save(inventory);
        String updatedMessage = "Inventory for item "+event.getItemRequest().getId()
                + " has been update to "+ inventoryUpdated.getQty() + " because of order " + event.getOrder().getId();
        System.out.println(updatedMessage);

    }

    public void increaseInventory(Long itemId, int amount){
        Inventory inventory = inventoryRepository.findByItemId(itemId);
        inventory.setQty(inventory.getQty() + amount);
        inventoryRepository.save(inventory);
    }
}
