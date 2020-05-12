/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author FatemaLaptop
 */
public enum BusDocTransactionType {

    NON_INVENTORY("Non_Inventory"),
    INVENTORY_ONLY("Inventory_Only"),
    INVENTORY_ACCOUNT("Inventory_Account");

    public static List<String> TYPES = new LinkedList<>();

    static {
        for (BusDocTransactionType t : BusDocTransactionType.values()) {
            TYPES.add(t.value);
        }
    }

    private String value;

    BusDocTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
