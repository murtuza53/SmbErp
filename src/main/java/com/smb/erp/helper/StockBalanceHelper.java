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
    
    private Double price = 0.0;
    
    private String productname;
    
    private String unit;

    private String brand;
    
    private String category;
    
    private String supcode;
    
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

    public Double findStock(String branch){
        return stockTable.get(branch);
    }
    
    public Double findStockValue(String branch){
        return stockTable.get(branch) * getPrice();
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
        
    public Double getTotal(){
        if(stockTable.get("TOTAL")!=null){
            return stockTable.get("TOTAL");
        }
        return 0.0;
    }
    
    public Double getTotalValue(){
        return getTotal() * getPrice();
    }
    
    public String getStockBalances(){
        StringBuilder sb = new StringBuilder();
        for(String key: stockTable.keySet()){
            sb.append(key +": " + stockTable.get(key) + "\t");
        }
        return sb.toString();
    }

        public String getStockBalancesWithValue(){
        StringBuilder sb = new StringBuilder();
        for(String key: stockTable.keySet()){
            sb.append("[" + key +": " + stockTable.get(key) + "," + findStockValue(key) + "]\t");
        }
        return sb.toString();
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
     * @return the productname
     */
    public String getProductname() {
        return productname;
    }

    /**
     * @param productname the productname to set
     */
    public void setProductname(String productname) {
        this.productname = productname;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the supcode
     */
    public String getSupcode() {
        return supcode;
    }

    /**
     * @param supcode the supcode to set
     */
    public void setSupcode(String supcode) {
        this.supcode = supcode;
    }
}
