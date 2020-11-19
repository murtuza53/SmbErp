/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Burhani152
 */
@Entity
@Table(name = "busdocexpense")
public class BusDocExpense implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer expenseid = new Random().nextInt(Integer.MAX_VALUE);

    private Double rate = 1.0;

    @ManyToOne
    @JoinColumn(name = "countryno")
    private Country country;

    //uni-directional many-to-one association to Account for Crediting Expense
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "accountno")
    private Account account;
    
    //bi-directional many-to-one association to BusDoc
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docno")
    private BusDoc busdoc;

    private Double amountlc = 0.0;
    
    private Double amountfc = 0.0;
    
    private String description;
    
    private String expensetype;
    
    @Transient
    private Double amount = 0.0;
    
    public BusDocExpense() {
    }

    /**
     * @return the expenseid
     */
    public Integer getExpenseid() {
        return expenseid;
    }

    /**
     * @param expenseid the expenseid to set
     */
    public void setExpenseid(Integer expenseid) {
        this.expenseid = expenseid;
    }

    /**
     * @return the rate
     */
    public Double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(Country country) {
        this.country = country;
        setRate(country.getRate());
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the amountLc
     */
    public Double getAmountlc() {
        return amountlc;
    }

    /**
     * @param amountLc the amountLc to set
     */
    public void setAmountlc(Double amountlc) {
        this.amountlc = amountlc;
    }

    /**
     * @return the amountFc
     */
    public Double getAmountfc() {
        return amountfc;
    }

    /**
     * @param amountFc the amountFc to set
     */
    public void setAmountfc(Double amountfc) {
        this.amountfc = amountfc;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BusDocExpense{" + "expenseid=" + expenseid + ", rate=" + rate + ", country=" + country + ", account=" + account + ", amountlc=" + amountlc + ", amountfc=" + amountfc + ", description=" + description + ", amount=" + amount + '}';
    }
  
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.expenseid);
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
        final BusDocExpense other = (BusDocExpense) obj;
        if (!Objects.equals(this.expenseid, other.expenseid)) {
            return false;
        }
        return true;
    }
    
    public void setAmount(Double amount){
        if(rate==1.0){
            setAmountlc(amount);
            setAmountfc(0.0);
        } else {
            setAmountfc(amount);
            setAmountlc(amount*rate);
        }
    }
    
    public Double getAmount(){
        if(rate==1.0){
            return getAmountlc();
        }
        return getAmountfc();
    }

    /**
     * @return the busdoc
     */
    public BusDoc getBusdoc() {
        return busdoc;
    }

    /**
     * @param busdoc the busdoc to set
     */
    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }

    /**
     * @return the expensetype
     */
    public String getExpensetype() {
        return expensetype;
    }

    /**
     * @param expensetype the expensetype to set
     */
    public void setExpensetype(String expensetype) {
        this.expensetype = expensetype;
    }
    
    public Double getTotalAmountInLC(){
        return getAmountlc() + getAmountfc()*rate;
    }
}
