/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.helper;

import com.smb.erp.entity.BusinessPartner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class AgeAnalysisHelper implements Serializable{

    private BusinessPartner businessPartner;

    private String refNo;

    private Double lastAmount = 0.0;

    private Date lastDate;

    private Double onAccount;

    private ArrayList<Double> rangeAmount;

    public AgeAnalysisHelper(){
        this(new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, null, "", null, 0.0);
    }
    
    public AgeAnalysisHelper(Double[] range, BusinessPartner businessPartner, String refNo, Date lastDate, Double onAccount) {
        this.rangeAmount = new ArrayList(Arrays.asList(range));
        this.businessPartner = businessPartner;
        this.refNo = refNo;
        this.lastDate = lastDate;
        this.onAccount = onAccount;
    }

    public Double getRangeAmount(int index) {
        if (getRangeAmount() == null) {
            return 0.0;
        }
        //else if (getRangeAmount().length < index) {
        //    return 0.0;
        //}
        return getRangeAmount().get(index - 1);
    }

    public Double getRange1() {
        //System.out.println(businessPartner.getCompanyname() + " => Range1: " + getRangeAmount());
        return getRangeAmount(1);
    }

    public Double getRange2() {
        return getRangeAmount(2);
    }

    public Double getRange3() {
        return getRangeAmount(3);
    }

    public Double getRange4() {
        return getRangeAmount(4);
    }

    public Double getRange5() {
        return getRangeAmount(5);
    }

    public Double getRange6() {
        return getRangeAmount(6);
    }

    public Double getRange7() {
        return getRangeAmount(7);
    }

    /**
     * @return the businessPartner
     */
    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    /**
     * @param businessPartner the businessPartner to set
     */
    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
    }

    /**
     * @return the refNo
     */
    public String getRefNo() {
        return refNo;
    }

    /**
     * @param refNo the refNo to set
     */
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    /**
     * @return the lastAmount
     */
    public Double getLastAmount() {
        return lastAmount;
    }

    /**
     * @param lastAmount the lastAmount to set
     */
    public void setLastAmount(Double lastAmount) {
        this.lastAmount = lastAmount;
    }

    /**
     * @return the lastDate
     */
    public Date getLastDate() {
        return lastDate;
    }

    /**
     * @param lastDate the lastDate to set
     */
    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    /**
     * @return the onAccount
     */
    public Double getOnAccount() {
        return onAccount;
    }

    /**
     * @param onAccount the onAccount to set
     */
    public void setOnAccount(Double onAccount) {
        this.onAccount = onAccount;
    }

    /**
     * @return the rangeAmount
     */
    public List<Double> getRangeAmount() {
        return rangeAmount;
    }

    public Double getTotalAmount() {
        if (rangeAmount == null) {
            return 0.0;
        }
        return rangeAmount.stream().mapToDouble(Double::doubleValue).sum();
        /*double total = 0.0;
        for(Double d: getRangeAmount()){
            total = total + d;
        }
        return total;*/
    }

    public Double getNetBalance() {
        return getTotalAmount() - onAccount;
    }

}
