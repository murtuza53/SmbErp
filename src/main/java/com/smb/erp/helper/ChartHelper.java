/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.helper;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author admin
 */
public class ChartHelper implements Serializable{
    
    private Integer id = ThreadLocalRandom.current().nextInt();
    
    private String monthName;
    
    private Integer year = 0;
    
    private String docType;
    
    private Double qty;
    
    private Double price;
    
    private Double cost;
    
    private Double profit;

    public ChartHelper(){
        
    }
    
    public ChartHelper(String monthName, int year, String docType, Double qty, Double price, Double cost, Double profit) {
        this.monthName = monthName;
        this.year = year;
        this.docType = docType;
        this.qty = qty;
        this.price = price;
        this.cost = cost;
        this.profit = profit;
    }

    public ChartHelper(Object[] data){
        //this.id = Integer.parseInt(data[0].toString());
        this.monthName = data[1].toString();
        this.year = Integer.parseInt(data[2].toString());
        this.docType = data[3].toString();
        this.price = Double.parseDouble(data[4].toString());
        this.cost = Double.parseDouble(data[5].toString());
        this.profit = Double.parseDouble(data[6].toString());

    }
    
    /**
     * @return the monthName
     */
    public String getMonthName() {
        return monthName;
    }

    /**
     * @param monthName the monthName to set
     */
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
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
     * @return the cost
     */
    public Double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * @return the profit
     */
    public Double getProfit() {
        return profit;
    }

    /**
     * @param profit the profit to set
     */
    public void setProfit(Double profit) {
        this.profit = profit;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChartHelper{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChartHelper other = (ChartHelper) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }
    
}
