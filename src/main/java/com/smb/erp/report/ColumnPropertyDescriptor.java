/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 *
 * @author Burhani152
 */
public class ColumnPropertyDescriptor extends PropertyDescriptor {

    private int index;
    private ColumnStyle columnStyle;

    public ColumnPropertyDescriptor(String propertyName, Class<?> beanClass) throws IntrospectionException {
        super(propertyName, beanClass);
    }
    
    public ColumnPropertyDescriptor(String propertyName, Class<?> beanClass, int index) throws IntrospectionException {
        super(propertyName, beanClass);
        this.index = index;
    }
    
    public ColumnPropertyDescriptor(String propertyName, Class<?> beanClass, String readMethodName, String writeMethodName) throws IntrospectionException {
        super(propertyName, beanClass, readMethodName, writeMethodName);
    }

    public ColumnPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod) throws IntrospectionException {
        super(propertyName, readMethod, writeMethod);
    }
    
    
    @Override
    public String toString(){
        return getDisplayName(); 
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
}
