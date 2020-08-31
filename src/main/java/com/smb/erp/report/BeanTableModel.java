/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import com.smb.erp.util.StringUtils;
import com.smb.erp.util.SystemConfig;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class BeanTableModel<T> extends AbstractTableModel {

    private Class<T> beanClass;
    private List<ColumnPropertyDescriptor> properties = new ArrayList<ColumnPropertyDescriptor>();
    private List<T> rows;

    public BeanTableModel(Class<T> beanClass, String[] property) {
        this(beanClass, property, property);
    }

    public BeanTableModel(Class<T> beanClass, String[] columnNames, String[] property) {
        this.beanClass = beanClass;

        if (columnNames.length != property.length) {
            return;
        }
        for (int i = 0; i < columnNames.length; i++) {
            addColumn(StringUtils.capitalize(columnNames[i]), property[i]);
        }
    }

    public BeanTableModel(Class<T> beanClass, Collection<T> rows) {
        this.beanClass = beanClass;
        this.rows = new ArrayList<T>(rows);
    }

    public String getColumnName(int column) {
        return properties.get(column).getDisplayName();
    }

    public int getColumnCount() {
        return properties.size();
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int column) {
        Object value = null;
        T entityInstance = rows.get(row);
        if (entityInstance != null) {
            PropertyDescriptor property = properties.get(column);
            Method readMethod = property.getReadMethod();
            try {
                value = readMethod.invoke(entityInstance);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return value;
    }

    public String getValueAtFormatted(int row, int column) {
        Object val = getValueAt(row, column);
        if (val != null) {
            if (properties.get(column).getColumnStyle().getFormat() != null) {
                return properties.get(column).getColumnStyle().getFormat().format(val);
            }
            return formatValue(val);
            /*Class type = properties.get(column).getClass();
            if (type == double.class || type == Double.class) {
                return SystemConfig.DECIMAL_FORMAT.format((Double) val);
            } else if (type == float.class || type == Float.class) {
                return SystemConfig.DECIMAL_FORMAT.format((Double) val);
            } else if (type == Date.class) {
                return SystemConfig.DATE_FORMAT.format((Date) val);
            } else {
                val.toString();
            }*/
        }
        return "";
    }

    public String formatValue(Object val) {
        if (val == null) {
            return "";
        }
        Class type = val.getClass();
        if (type == double.class || type == Double.class) {
            return SystemConfig.DECIMAL_FORMAT.format((Double) val);
        } else if (type == float.class || type == Float.class) {
            return SystemConfig.DECIMAL_FORMAT.format((Double) val);
        } else if (type == Date.class) {
            return SystemConfig.DATE_FORMAT.format((Date) val);
        }
        return val.toString();
    }

    public Object getValueByProperty(int row, String property) {
        T val = rows.get(row);
        try {
            return val.getClass().getDeclaredField(property).get(val);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(BeanTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getValueByPropertyFormatted(int row, String property){
        return formatValue(getValueByProperty(row, property));
    }

    public Object getValue(Object bean, String property){
        if(bean==null){
            return null;
        }
        try {
            return bean.getClass().getDeclaredField(property).get(bean);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(BeanTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String getValueFormatted(Object bean, String property){
        return formatValue(getValue(bean, property));
    }
    
    public void addColumn(String displayName, String propertyName) {
        try {
            ColumnPropertyDescriptor property
                    = new ColumnPropertyDescriptor(displayName, beanClass, propertyName, null);
            property.setDisplayName(displayName);
            properties.add(property);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addColumn(String displayName, String propertyName, int index) {
        try {
            ColumnPropertyDescriptor property
                    = new ColumnPropertyDescriptor(displayName, beanClass, propertyName, null);
            property.setIndex(index);
            property.setDisplayName(displayName);
            properties.add(property);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void resetColumns() {
        properties = new ArrayList<ColumnPropertyDescriptor>();
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(Collection<T> rows) {
        this.rows = new ArrayList<T>(rows);
    }

    public void addRow(T value) {
        rows.add(value);
    }

    public void removeRow(int row) {
        if (rows.size() > row && row != -1) {
            rows.remove(row);
        }
    }

    public void setRow(int row, T entityInstance) {
        rows.remove(row);
        rows.add(row, entityInstance);
    }

}
