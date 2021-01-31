/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smb.erp.util.BeanField;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Murtuza
 */
@XmlRootElement
public class TableField implements Serializable {

    private Long id = new Date().getTime();
    private String columnTitle;
    private String propertyName;
    private Class type;
    private int width;
    private boolean spellIt = false;
    private ColumnFunction columnFunction = ColumnFunction.NONE;
    private FieldFunction fieldFunction = FieldFunction.DEFAULT;

    @JsonIgnore
    private BeanField field;

    public TableField() {

    }

    public TableField(String columnTitle, String propertyName, Class clz) {
        this.columnTitle = columnTitle;
        this.propertyName = propertyName;
        this.type = clz;
    }

    public TableField(String columnTitle, String propertyName, Class type, int width) {
        this.columnTitle = columnTitle;
        this.propertyName = propertyName;
        this.type = type;
        this.width = width;
    }

    /**
     * @return the reportTitle
     */
    public String getColumnTitle() {
        return columnTitle;
    }

    /**
     * @param reportTitle the reportTitle to set
     */
    @XmlElement
    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
        //return this;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param propertyName the propertyName to set
     */
    @XmlElement
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
        //return this;
    }

    /**
     * @return the columnFunction
     */
    public ColumnFunction getColumnFunction() {
        return columnFunction;
    }

    /**
     * @param columnFunction the columnFunction to set
     */
    @XmlElement
    public void setColumnFunction(ColumnFunction columnFunction) {
        this.columnFunction = columnFunction;
        //return this;
    }

    /**
     * @return the fieldFunction
     */
    public FieldFunction getFieldFunction() {
        return fieldFunction;
    }

    /**
     * @param fieldFunction the fieldFunction to set
     */
    @XmlElement
    public void setFieldFunction(FieldFunction fieldFunction) {
        this.fieldFunction = fieldFunction;
        //return this;
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
    @XmlElement
    public void setType(Class type) {
        this.type = type;
        //return this;
    }

    public String getSimpleType() {
        return type.getSimpleName();
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    @XmlElement
    public void setWidth(int width) {
        this.width = width;
        //return this;
    }

    /**
     * @return the spellIt
     */
    public boolean isSpellIt() {
        return spellIt;
    }

    /**
     * @param spellIt the spellIt to set
     */
    public void setSpellIt(boolean spellIt) {
        this.spellIt = spellIt;
        //return this;
    }

    public enum ColumnFunction {
        NONE, SUM, AVG, MIN, MAX, CUSTOM
    };

    public enum FieldFunction {
        DEFAULT, FORMULA
    };

    public Object getFieldValue(Object targetObject) {
        if (getFieldFunction() == FieldFunction.DEFAULT) {
            return ReportUtils.getFieldValue(getPropertyName(), targetObject);
        }
        return null;
    }

    public TableField clone() {
        TableField t = new TableField();
        t.setColumnFunction(columnFunction);
        t.setColumnTitle(columnTitle);
        t.setFieldFunction(fieldFunction);
        t.setPropertyName(propertyName);
        t.setType(type);
        t.setWidth(width);

        return t;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

        /**
     * @return the field
     */
    public BeanField getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(BeanField field) {
        this.field = field;
        setPropertyName(field.getProperty());
        setType(field.getType());
    }
}
