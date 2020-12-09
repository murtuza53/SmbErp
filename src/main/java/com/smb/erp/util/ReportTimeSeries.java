/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.jfree.data.time.DateRange;

/**
 *
 * @author Burhani152
 */
public class ReportTimeSeries implements Serializable{
    
    public enum MODE {
        DAILY("Daily"), MONTHLY("Monthly"), QUARTERLY("Quarterly"), Yearly("Yearly");

        public static List<String> TYPES = new LinkedList<>();

        static {
            for (MODE t : MODE.values()) {
                TYPES.add(t.value);
            }
        }

        private String value;

        MODE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    
    private Date fromDate = DateUtil.startOfYear(new Date());
    private Date toDate = DateUtil.endOfYear(new Date());
    private List<DateRange> series;
    
    public ReportTimeSeries() {
    }
    
    public ReportTimeSeries(Date fromDate, Date toDate){
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    
    public ReportTimeSeries(int year){
        this.fromDate = DateUtil.createDate(1, Calendar.JANUARY, year);
        this.fromDate = DateUtil.startOfYear(fromDate);
        this.toDate = DateUtil.endOfDay(fromDate);
    }
    
    public void calculateTimeSeries(MODE mode){
        setSeries(new LinkedList<DateRange>());
        switch(mode){
            case DAILY:{
                break;
            }
            case MONTHLY:{
                Date date = DateUtil.cloneDate(fromDate);
                while(date.before(toDate)){
                    getSeries().add(new DateRange(DateUtil.startOfMonth(date), DateUtil.endOfMonth(date)));
                    date = DateUtil.addMonths(date, 1);
                }
                break;
            }
        }
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the series
     */
    public List<DateRange> getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(List<DateRange> series) {
        this.series = series;
    }
    
}
