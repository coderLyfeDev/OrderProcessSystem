package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Inventory;
import com.order.process.system.demo.events.CreateOrderEvent;
import com.order.process.system.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * Service layer responsible for creating updating an item's inventory.
 * Subscribes to the CreateOrderEvent so it knows when to update the inventory.
 */
@Service
public class InventoryServiceImpl implements InventoryServive{

    @Autowired
    InventoryRepository inventoryRepository;


    public Inventory findByItemId(Long id){
        return inventoryRepository.findByItemId(id);
    }

    @Async
    @EventListener
    public void decreaseInventory(CreateOrderEvent event){
        event.getItemRequest().forEach( i -> {
            Inventory inventory = inventoryRepository.findByItemId(i.getId());
            inventory.setQty(inventory.getQty() - i.getQty());
            Inventory inventoryUpdated = inventoryRepository.save(inventory);
            String updatedMessage = "Inventory for item "+i.getId()
                    + " has been update to "+ inventoryUpdated.getQty() + " because of order " + event.getOrder().getId();
            System.out.println(updatedMessage);
        });

    }

    public void increaseInventory(Long itemId, int amount){
        Inventory inventory = inventoryRepository.findByItemId(itemId);
        inventory.setQty(inventory.getQty() + amount);
        inventoryRepository.save(inventory);
    }
}
