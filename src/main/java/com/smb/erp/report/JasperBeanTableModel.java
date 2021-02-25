/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.report;

import com.smb.erp.dynamic.report.TableField;
import com.smb.erp.util.EvaluateExpression;
import com.smb.erp.util.SystemConfig;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class JasperBeanTableModel<T> extends AbstractTableModel {

    private Class<T> beanClass;
    private List<T> rows;
    private List<TableField> fields;
    
    public JasperBeanTableModel(Class<T> beanClass, List<TableField> fields, List<T> rows) {
        this.beanClass = beanClass;
        this.rows = rows;
        this.fields = fields;
    }

    @Override
    public String getColumnName(int column) {
        return fields.get(column).getColumnTitle();
    }

    @Override
    public int getColumnCount() {
        return fields.size();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object value = null;
        T entityInstance = rows.get(row);
        if (entityInstance != null) {
            value = EvaluateExpression.evaluateExpression(entityInstance, fields.get(column).getPropertyName(), fields.get(column).getType());
        }
        return value;
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

}
