/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import com.smb.erp.util.Utils;
import java.io.Serializable;

/**
 *
 * @author Burhani152
 */
public class ColumnModel implements Serializable {

    private String header;
    private String property;
    private int index;
    private ColumnStyle columnStyle = new ColumnStyle();
    private Class type;
    
    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    public ColumnModel(String header, String property, int index) {
        this.header = header;
        this.property = property;
        this.index = index;
    }

    public ColumnModel(String header, String property, Class type, int index) {
        this.header = header;
        this.property = property;
        this.index = index;
        this.type = type;
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }
    
    @Override
    public String toString(){
        return header; 
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the columnStyle
     */
    public ColumnStyle getColumnStyle() {
        return columnStyle;
    }

    /**
     * @param columnStyle the columnStyle to set
     */
    public void setColumnStyle(ColumnStyle columnStyle) {
        this.columnStyle = columnStyle;
    }

    /**
     * @return the type
     */
    public Class getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Class type) {
        this.type = type;
        columnStyle.setFormat(Utils.getFormatter(type));
        columnStyle.setTextAlign(Utils.getCssStyle(type));
    }
    
    public Object getDefaultValue(){
        return Utils.getDefaultValue(type);
    }

}
