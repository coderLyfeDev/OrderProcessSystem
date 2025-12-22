package com.order.process.system.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "item")
@EqualsAndHashCode(exclude = "inventory")
@ToString(exclude = "inventory")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @JsonIgnore
    private String sku;
    @JsonIgnore
    private Double price;

    @JsonIgnore
    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
    private Inventory inventory;
}

