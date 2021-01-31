/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity;

import com.smb.erp.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author Burhani152
 */
@Entity
@Table(name = "scbatch")
public class StockCountingBatch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long countingno = new Date().getTime();

    private Boolean processed;

    private Integer countyear = DateUtil.getYear(new Date());

    private Double count = 1.00;

    private Double currentstock = 0.00;

    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredon;

    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    private Date countedon;

    private Double totalcount = 0.0;

    private Double cost = 0.0;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "docno")
    private StockCountingBusDoc busdoc;

    //bi-directional many-to-one association to Product
    @ManyToOne
    @JoinColumn(name = "productid")
    private Product productid;

    @ManyToOne
    @JoinColumn(name = "countedunitno")
    private Unit countedunitno;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "countedbyempno")
    private Emp countedbyempno;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "enteredbyempno")
    private Emp enteredbyempno;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "countedinbranchno")
    private Branch countedinbranchno;

    @Transient
    private Double difference = 0.0;

    public StockCountingBatch() {
    }

    /**
     * @return the countingno
     */
    public Long getCountingno() {
        return countingno;
    }

    /**
     * @param countingno the countingno to set
     */
    public void setCountingno(Long countingno) {
        this.countingno = countingno;
    }

    /**
     * @return the processed
     */
    public Boolean isProcessed() {
        return processed;
    }

    /**
     * @param processed the processed to set
     */
    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    /**
     * @return the count
     */
    public Double getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Double count) {
        this.count = count;
    }

    /**
     * @return the enteredon
     */
    public Date getEnteredon() {
        return enteredon;
    }

    /**
     * @param enteredon the enteredon to set
     */
    public void setEnteredon(Date enteredon) {
        this.enteredon = enteredon;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the countedon
     */
    public Date getCountedon() {
        return countedon;
    }

    /**
     * @param countedon the countedon to set
     */
    public void setCountedon(Date countedon) {
        this.countedon = countedon;
    }

    /**
     * @return the totalcount
     */
    public Double getTotalcount() {
        return totalcount;
    }

    /**
     * @param totalcount the totalcount to set
     */
    public void setTotalcount(Double totalcount) {
        this.totalcount = totalcount;
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
     * @return the busdoc
     */
    public StockCountingBusDoc getBusdoc() {
        return busdoc;
    }

    /**
     * @param busdoc the busdoc to set
     */
    public void setBusdoc(StockCountingBusDoc busdoc) {
        this.busdoc = busdoc;
    }

    /**
     * @return the productid
     */
    public Product getProductid() {
        return productid;
    }

    /**
     * @param productid the productid to set
     */
    public void setProductid(Product productid) {
        this.productid = productid;
    }

    /**
     * @return the countedunitno
     */
    public Unit getCountedunitno() {
        return countedunitno;
    }

    /**
     * @param countedunitno the countedunitno to set
     */
    public void setCountedunitno(Unit countedunitno) {
        this.countedunitno = countedunitno;
    }

    /**
     * @return the countedbyempno
     */
    public Emp getCountedbyempno() {
        return countedbyempno;
    }

    /**
     * @param countedbyempno the countedbyempno to set
     */
    public void setCountedbyempno(Emp countedbyempno) {
        this.countedbyempno = countedbyempno;
    }

    /**
     * @return the enteredbyempno
     */
    public Emp getEnteredbyempno() {
        return enteredbyempno;
    }

    /**
     * @param enteredbyempno the enteredbyempno to set
     */
    public void setEnteredbyempno(Emp enteredbyempno) {
        this.enteredbyempno = enteredbyempno;
    }

    /**
     * @return the countedinbranchno
     */
    public Branch getCountedinbranchno() {
        return countedinbranchno;
    }

    /**
     * @param countedinbranchno the countedinbranchno to set
     */
    public void setCountedinbranchno(Branch countedinbranchno) {
        this.countedinbranchno = countedinbranchno;
    }

    @Override
    public String toString() {
        return "StockCountingBatch{" + "countingno=" + countingno + ", countyear=" + countyear + ", count=" + count + ", productid=" + productid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.countingno);
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
        final StockCountingBatch other = (StockCountingBatch) obj;
        if (!Objects.equals(this.countingno, other.countingno)) {
            return false;
        }
        return true;
    }

    /**
     * @return the countyear
     */
    public Integer getCountyear() {
        return countyear;
    }

    /**
     * @param countyear the countyear to set
     */
    public void setCountyear(Integer countyear) {
        this.countyear = countyear;
    }

    public Double getTotalAmount() {
        return getCount() * getCost();
    }

    /**
     * @return the currentstock
     */
    public Double getCurrentstock() {
        return currentstock;
    }

    /**
     * @param currentstock the currentstock to set
     */
    public void setCurrentstock(Double currentstock) {
        this.currentstock = currentstock;
    }

    /**
     * @return the difference
     */
    public Double getDifference() {
        return difference;
    }

    /**
     * @param difference the difference to set
     */
    public void setDifference(Double difference) {
        this.difference = difference;
    }

}
