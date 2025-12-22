package com.order.process.system.demo.service;

import com.order.process.system.demo.entity.Item;
import com.order.process.system.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for finding items given an item ID
 */

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    ItemRepository itemRepository;


    public Item findById(Long id){
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }
}
