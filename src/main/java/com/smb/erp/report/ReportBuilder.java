/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.TupleElement;

/**
 *
 * @author Burhani152
 */
public class ReportBuilder implements Serializable {

    private List<ColumnPropertyDescriptor> columns;

    public static ReportBuilder newInstance() {
        return new ReportBuilder();
    }

    public ReportBuilder() {

    }

    public ReportBuilder setColumns(List<ColumnPropertyDescriptor> columns) {
        this.columns = columns;
        return this;
    }

    public ReportBuilder addColumn(String header, String property) {
        if (getColumns() == null) {
            columns = new LinkedList<ColumnPropertyDescriptor>();
        }
        try {
            getColumns().add(new ColumnPropertyDescriptor(property, NativeQueryReportObject.class, columns.size()));
        } catch (IntrospectionException ex) {
            Logger.getLogger(ReportBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public ReportBuilder addColumn(String header, String property, int index) {
        if (getColumns() == null) {
            columns = new LinkedList<ColumnPropertyDescriptor>();
        }
        try {
            getColumns().add(new ColumnPropertyDescriptor(property, NativeQueryReportObject.class, columns.size()));
        } catch (IntrospectionException ex) {
            Logger.getLogger(ReportBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public ReportBuilder addColumns(String[] headers, String[] properties) {
        if (headers == null) {
            return this;
        }
        if (properties == null) {
            return this;
        }
        if (headers.length != properties.length) {
            return this;
        }

        for (int i = 0; i < headers.length; i++) {
            addColumn(headers[i], properties[i], i);
        }
        return this;
    }
    
    public ReportBuilder addColumns(List<TupleElement> tlist){
        if(tlist!=null){
            int i=0;
            for(TupleElement te: tlist){
                addColumn(capitalize(te.getAlias()), te.getAlias(), i);
                i++;
            }
        }
        return this;
    }

    public ReportBuilder addColumns(String[] headers, String[] properties, int[] indexes) {
        if (headers == null) {
            return this;
        }
        if (properties == null) {
            return this;
        }
        if (headers.length != properties.length) {
            return this;
        }

        for (int i = 0; i < headers.length; i++) {
            addColumn(headers[i], properties[i], indexes[i]);
        }
        return this;
    }

    public ReportBuilder addColumns(String[] properties) {
        if (properties == null) {
            return this;
        }
        for (int i = 0; i < properties.length; i++) {
            addColumn(capitalize(properties[i]), properties[i], i);
        }
        return this;
    }

    public Object getValue(NativeQueryReportObject ro, ColumnPropertyDescriptor cm) {
        return ro.getField(cm.getIndex());
    }

    public int getColumnCount(){
        if(getColumns()==null){
            return 0;
        }
        return getColumns().size();
    }
    
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * @return the columns
     */
    public List<ColumnPropertyDescriptor> getColumns() {
        return columns;
    }

}
