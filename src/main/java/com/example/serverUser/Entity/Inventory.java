package com.example.serverUser.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Data
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inventoryID;
    @Column(name="address",nullable = false,length = 50,unique = true)
    private String address;
    @Column(name="nameinventory",nullable = false,length = 50)
    private String nameInventory;

    public Long getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Long inventoryID) {
        this.inventoryID = inventoryID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNameInventory() {
        return nameInventory;
    }

    public void setNameInventory(String nameInventory) {
        this.nameInventory = nameInventory;
    }
}
