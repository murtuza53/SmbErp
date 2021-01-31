/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.helper;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Burhani152
 */
public class StatementHelper implements Serializable{
    
    private Double invoiceAmount = 0.0;
    
    private Double paid = 0.0;
    
    private Double pending = 0.0;
    
    private Double cumulative = 0.0;
    
    private Integer days = 1;

    public StatementHelper() {
    }

    public StatementHelper(Double invAmt){
        this.invoiceAmount = invAmt;
    }
    
    public StatementHelper(Double invAmt, Double paid, Double pending, Double cumulative, Integer days){
        this.invoiceAmount = invAmt;
        this.paid = paid;
        this.pending = pending;
        this.cumulative = cumulative;
        this.days = days;
    }
    
    @Override
    public String toString() {
        return "StatementHelper{" + "invoiceAmount=" + invoiceAmount + ", paid=" + paid + ", pending=" + pending + ", days=" + days + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.invoiceAmount);
        hash = 59 * hash + Objects.hashCode(this.paid);
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
        final StatementHelper other = (StatementHelper) obj;
        if (!Objects.equals(this.invoiceAmount, other.invoiceAmount)) {
            return false;
        }
        if (!Objects.equals(this.paid, other.paid)) {
            return false;
        }
        return true;
    }


    
    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Double getPending() {
        return pending;
    }

    public void setPending(Double pending) {
        this.pending = pending;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * @return the cumulative
     */
    public Double getCumulative() {
        return cumulative;
    }

    /**
     * @param cumulative the cumulative to set
     */
    public void setCumulative(Double cumulative) {
        this.cumulative = cumulative;
    }
    
    
    
}
