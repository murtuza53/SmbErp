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
public enum BusDocType {
    
    SALES("Sales"),
    PURCHASE("Purchase");

    public static List<String> TYPES = new LinkedList<>();
    
    static{
        for(BusDocType t: BusDocType.values()){
            TYPES.add(t.value);
        }
    }
    
    private String value;
    
    BusDocType(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
    
}
