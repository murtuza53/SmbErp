/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.helper;

import com.smb.erp.entity.Product;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author FatemaLaptop
 */
public class StockValueHelper implements Serializable{
    
    private Product product;
    
    private LinkedHashMap<String, BalanceObject> stockTable = new LinkedHashMap<String, BalanceObject>();
    
    private Long productid = 0l;

    public StockValueHelper() {
    }

    public StockValueHelper(Product product) {
        this.product = product;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public void putStock(String branch, Double qty, Double price){
        putStock(branch, new BalanceObject(qty, price));
    }

    public void putStock(String branch, BalanceObject bal){
        stockTable.remove(branch);
        stockTable.put(branch, bal);
    }

    public BalanceObject findBalanceObject(String branch){
        return stockTable.get(branch);
    }
    
    public Double findQty(String branch){
        return findBalanceObject(branch).getQty();
    }
    
    public Double findPrice(String branch){
        return findBalanceObject(branch).getPrice();
    }

    public Double findValue(String branch){
        return findBalanceObject(branch).getBalance();
    }

    public String findStockStyle(String branch){
        if(stockTable.get(branch).getQty()>0){
            return "background: lightgreen; font-weight: bold";
        } else if(stockTable.get(branch).getQty()<0){
            return "background: lightpink; font-weight: bold";
        }
        return "";
    }
    
    public void recalculateTotal(){
        double qty = 0;
        double price = 0;
        stockTable.remove("TOTAL");
        for(String key: stockTable.keySet()){
            qty = qty + stockTable.get(key).getQty();
            price = price + stockTable.get(key).getPrice();
        }
        price = price / stockTable.keySet().size();
        stockTable.put("TOTAL", new BalanceObject(qty, qty*price));
    }
    
    public List<String> getBranches(){
        //System.out.println("getBranches: " + stockTable.keySet());
        if(stockTable==null){
            return new LinkedList<>();
        }
        return new LinkedList<String>(stockTable.keySet());
    }
    
    /**
     * @return the productid
     */
    public Long getProductid() {
        if(product!=null){
            return product.getProductid();
        }
        return productid;
    }

    /**
     * @param productid the productid to set
     */
    public void setProductid(Long productid) {
        this.productid = productid;
    }

    /**
     * @return the totalLabel
     */
    public String getTotalLabel() {
        return "TOTAL";
    }
        
    public BalanceObject getTotalObject(){
        if(stockTable.get("TOTAL")!=null){
            return stockTable.get("TOTAL");
        }
        return null;
    }
    
    public Double getTotal(){
        if(stockTable.get("TOTAL")!=null){
            return stockTable.get("TOTAL").getBalance();
        }
        return 0.0;
    }

    public String getStockBalances(){
        StringBuilder sb = new StringBuilder();
        for(String key: stockTable.keySet()){
            sb.append(key +": " + stockTable.get(key) + "\t");
        }
        return sb.toString();
    }
}
