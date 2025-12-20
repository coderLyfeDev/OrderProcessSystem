package com.order.process.system.demo.repository;

import com.order.process.system.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    public Inventory findByItemId(Long itemId);
}
