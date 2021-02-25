/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.LedgerLine;
import com.smb.erp.entity.ProductTransaction;
import com.smb.erp.util.BeanField;
import com.smb.erp.util.ReflectionUtil;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
@Named(value = "salesReportController")
@ViewScoped
public class SalesReportController implements Serializable{
    
    @Autowired
    ProductTransactionController tranCon;
    
    private Class selectedClass = ProductTransaction.class;
    
    private List<Class> classList;
    
    private List<BeanField> fields;
    
    private String query = "SELECT OBJECT(mov) FROM ProductTransaction";
    
    private List dataList;
    
    public SalesReportController(){
        
    }
    
    @PostConstruct
    public void init(){
        setClassList((List<Class>) new LinkedList());
        getClassList().add(ProductTransaction.class);
        getClassList().add(LedgerLine.class);
        refresh();
    }
    
    public void refresh(){
        setFields(ReflectionUtil.getFields(getSelectedClass(), 1));
    }
    
    public List<String> getPropertyList(){
        if(getFields()==null){
            return new LinkedList();
        }
        return getFields().stream().map(BeanField::getProperty)
                .collect(Collectors.toList());
    }

    /**
     * @return the selectedClass
     */
    public Class getSelectedClass() {
        return selectedClass;
    }

    /**
     * @param selectedClass the selectedClass to set
     */
    public void setSelectedClass(Class selectedClass) {
        this.selectedClass = selectedClass;
    }

    /**
     * @return the classList
     */
    public List<Class> getClassList() {
        return classList;
    }

    /**
     * @param classList the classList to set
     */
    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }

    /**
     * @return the fields
     */
    public List<BeanField> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<BeanField> fields) {
        this.fields = fields;
    }

    /**
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return the dataList
     */
    public List getDataList() {
        return dataList;
    }

    /**
     * @param dataList the dataList to set
     */
    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
}
