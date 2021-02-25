/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import com.smb.erp.entity.BusDoc;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author admin
 */
public class AgeingAnalysisRangeCalculator implements Serializable {

    private Double[] rangeAmount;

    private List<Integer> range = Arrays.asList(new Integer[]{30, 60, 90, 180, 365});

    private ArrayList<Double> rangeAmountTotal;

    public AgeingAnalysisRangeCalculator() {
    }

    public void init(){
        rangeAmount = new Double[range.size() + 1];
        //rangeAmountTotal = new double[range.size() + 1];
        rangeAmountTotal = new ArrayList();
    }
    
    public void resetAll() {
        //rangeAmountTotal = new double[range.size() + 1];
        resetRangeAmount();;
        resetRangeAmountTotal();
    }

    public void resetRangeAmount() {
        rangeAmount = new Double[range.size() + 1];
        for (int i = 0; i < getRangeAmount().length; i++) {
            rangeAmount[i] = 0.0;
        }
    }

    public void resetRangeAmountTotal() {
        //for (int i = 0; i < getRangeAmountTotal().length; i++) {
        //    rangeAmountTotal[i] = 0.0;
        //}
        rangeAmountTotal = new ArrayList();
    }

    public Double[] calculateRange(List<BusDoc> docs, Date date) {
        resetRangeAmount();
        if (docs == null) {
            return getRangeAmount();
        }
        for (BusDoc doc : docs) {
            int days = DateUtil.findAge(doc.getDocdate(), date);
            boolean calculated = false;
            for (int i = 0; i < range.size(); i++) {
                if (days < range.get(i) && !calculated) {
                    calculated = true;
                    rangeAmount[i] = getRangeAmount()[i] + doc.getGrandtotal();
                    //rangeAmountTotal[i] = getRangeAmountTotal()[i] + doc.getGrandtotal();
                    //Double tt = rangeAmountTotal.remove(i);
                    //rangeAmountTotal.add(i, tt + doc.getGrandtotal());
                    //System.out.println(doc.getDocno() + " " + days + " " + rangeAmount[i] + " " + calculated);
                }
            }
            if (!calculated) {
                rangeAmount[range.size()] = getRangeAmount()[range.size()] + doc.getGrandtotal();
                //rangeAmountTotal[range.size()] = getRangeAmountTotal()[range.size()] + doc.getGrandtotal();
                //Double tt = rangeAmountTotal.remove(range.size());
                //rangeAmountTotal.add(range.size(), tt + doc.getGrandtotal());
            }
        }
        //addToTotal(rangeAmount);
        return getRangeAmount();
    }
    
    public void addToTotal(Double[] totals){
        Double[] rtotal = ArrayUtils.toObject(new double[totals.length]);
        if(rangeAmountTotal.size()>0){
            rangeAmountTotal.toArray(rtotal);
        }
        for(int i=0; i<totals.length; i++){
            rtotal[i] = rtotal[i] + totals[i];
        }
        rangeAmountTotal = new ArrayList<>(Arrays.asList(rtotal));
        System.out.println(rangeAmountTotal);
    }

    /**
     * @return the rangeAmount
     */
    public Double[] getRangeAmount() {
        return rangeAmount;
    }

    /**
     * @return the rangeAmountTotal
     */
    public Double[] getRangeAmountTotal() {
        System.out.println("getRangeAmountTotal: " + rangeAmountTotal);
        Double[] rtotal = new Double[rangeAmountTotal.size()];
        rangeAmountTotal.toArray(rtotal);
        return rtotal;
    }

}
