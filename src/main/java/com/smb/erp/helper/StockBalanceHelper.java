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
public class StockBalanceHelper implements Serializable{
    
    private Product product;
    
    private LinkedHashMap<String, Double> stockTable = new LinkedHashMap<String, Double>();
    
    private Long productid = 0l;

    public StockBalanceHelper() {
    }

    public StockBalanceHelper(Product product) {
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

    public void putStock(String branch, Double value){
        stockTable.remove(branch);
        stockTable.put(branch, value);
    }

    public Object findStock(String branch){
        return stockTable.get(branch);
    }
    
    public String findStockStyle(String branch){
        if(stockTable.get(branch)>0){
            return "background: lightgreen; font-weight: bold";
        } else if(stockTable.get(branch)<0){
            return "background: lightpink; font-weight: bold";
        }
        return "";
    }
    
    public void recalculateTotal(){
        double total = 0;
        stockTable.remove("TOTAL");
        for(String key: stockTable.keySet()){
            total = total + stockTable.get(key);
        }
        stockTable.put("TOTAL", total);
    }
    
    public List<String> getBranches(){
        System.out.println("getBranches: " + stockTable.keySet());
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
        
    public Double getTotal(){
        if(stockTable.get("TOTAL")!=null){
            return stockTable.get("TOTAL");
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
