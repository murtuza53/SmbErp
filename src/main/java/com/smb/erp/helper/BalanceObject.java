/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.helper;

import java.io.Serializable;

/**
 *
 * @author admin
 */
public class BalanceObject implements Serializable{
    
    private Double qty = 0.0;
    
    private Double price = 0.0;
    
    private String label;

    public BalanceObject() {
    }
    
    public BalanceObject(Double qty, Double price) {
        this.qty = qty;
        this.price = price;
    }
    
    public BalanceObject(String label, Double qty, Double price) {
        this(qty, price);
        this.label = label;
    }

    /**
     * @return the qty
     */
    public Double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public Double getBalance(){
        return qty*price;
    }
    
    @Override
    public String toString() {
        if(label==null){
            return "{" + "qty=" + qty + ", price=" + price + '}';
        }
        return  "{" + "qty=" + qty + ", price=" + price + ", label=" + label + '}';
    }
    
}
