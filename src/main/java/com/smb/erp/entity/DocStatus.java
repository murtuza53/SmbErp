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
public enum DocStatus {
    
    DRAFT("Draft"), //blue
    PENDING("Pending"), //yellow
    PARTIAL("Partial"), //orange
    COMPLETED("Completed"), //green
    PAID("Paid"),   //green
    CANCELLED("Cancelled"), //red
    RETURNED("Returned");   //purple

    public static List<String> TYPES = new LinkedList<>();
    
    static{
        for(DocStatus t: DocStatus.values()){
            TYPES.add(t.value);
        }
    }
    
    private String value;
    
    DocStatus(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
    
}
