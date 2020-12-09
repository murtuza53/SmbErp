/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.smb.erp.dynamic.report.TableField.ColumnFunction;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Murtuza
 */
@XmlRootElement
public class ReportTable {

    private String title;
    private String dataPropertyName;
    private List<TableField> fieldList;
    private List<ReportField> summaryList;
    private String titleStyle;
    private String detailsOddStyle;
    private String detailsEvenStyle;
    private String subtotalStyle;
    private String columnTitleStyle;
    private String columnStyle;
    private String valueStyle;

    public ReportTable() {
    }

    public ReportTable(String dataPropertyName) {
        this.dataPropertyName = dataPropertyName;
    }

    public ReportTable(String dataPropertyName, List<TableField> fieldList) {
        this.dataPropertyName = dataPropertyName;
        this.fieldList = fieldList;
    }

    public TableField addColumn(String colTitle, String property, Class clz) {
        if (getFieldList() == null) {
            setFieldList((List<TableField>) new LinkedList());
        }

        TableField field = new TableField(colTitle, property, clz);
        fieldList.add(field);
        return field;
    }

    public TableField addColumn(String colTitle, String property, Class clz, ColumnFunction function) {
        TableField field = addColumn(colTitle, property, clz);
        if (field != null) {
            field.setColumnFunction(function);
        }
        return field;
    }

    public TableField addColumn(String colTitle, String property, Class clz, int width) {
        TableField field = addColumn(colTitle, property, clz);
        if (field != null) {
            field.setWidth(width);
        }
        return field;
    }

    public TableField addColumn(String colTitle, String property, Class clz, int width, ColumnFunction function) {
        TableField field = addColumn(colTitle, property, clz);
        if (field != null) {
            field.setWidth(width);
            field.setColumnFunction(function);
        }
        return field;
    }

    /**
     * @return the fieldList
     */
    public List<TableField> getFieldList() {
        return fieldList;
    }

    /**
     * @param fieldList the fieldList to set
     */
    @XmlElement
    public ReportTable setFieldList(List<TableField> fieldList) {
        this.fieldList = fieldList;
        return this;
    }

    /**
     * @return the dataPropertyName
     */
    public String getDataPropertyName() {
        return dataPropertyName;
    }

    /**
     * @param dataPropertyName the dataPropertyName to set
     */
    @XmlElement
    public ReportTable setDataPropertyName(String dataPropertyName) {
        this.dataPropertyName = dataPropertyName;
        return this;
    }

    /**
     * @return the summaryList
     */
    public List<ReportField> getSummaryList() {
        return summaryList;
    }

    /**
     * @param summaryList the summaryList to set
     */
    @XmlElement
    public ReportTable setSummaryList(List<ReportField> summaryList) {
        this.summaryList = summaryList;
        return this;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    @XmlElement
    public ReportTable setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean hasSubtotal() {
        if (fieldList != null) {
            for (TableField tf : fieldList) {
                if (tf.getColumnFunction() != ColumnFunction.NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the titleStyle
     */
    public String getTitleStyle() {
        return titleStyle;
    }

    /**
     * @param titleStyle the titleStyle to set
     */
    public ReportTable setTitleStyle(String titleStyle) {
        this.titleStyle = titleStyle;
        return this;
    }

    /**
     * @return the detailsOddStyle
     */
    public String getDetailsOddStyle() {
        return detailsOddStyle;
    }

    /**
     * @param detailsOddStyle the detailsOddStyle to set
     */
    public ReportTable setDetailsOddStyle(String detailsOddStyle) {
        this.detailsOddStyle = detailsOddStyle;
        return this;
    }

    /**
     * @return the detailsEvenStyle
     */
    public String getDetailsEvenStyle() {
        return detailsEvenStyle;
    }

    /**
     * @param detailsEvenStyle the detailsEvenStyle to set
     */
    public ReportTable setDetailsEvenStyle(String detailsEvenStyle) {
        this.detailsEvenStyle = detailsEvenStyle;
        return this;
    }

    /**
     * @return the subtotalStyle
     */
    public String getSubtotalStyle() {
        return subtotalStyle;
    }

    /**
     * @param subtotalStyle the subtotalStyle to set
     */
    public ReportTable setSubtotalStyle(String subtotalStyle) {
        this.subtotalStyle = subtotalStyle;
        return this;
    }

    /**
     * @return the columnTitleStyle
     */
    public String getColumnTitleStyle() {
        return columnTitleStyle;
    }

    /**
     * @param columnTitleStyle the columnTitleStyle to set
     */
    public ReportTable setColumnTitleStyle(String columnTitleStyle) {
        this.columnTitleStyle = columnTitleStyle;
        return this;
    }

    /**
     * @return the columnStyle
     */
    public String getColumnStyle() {
        return columnStyle;
    }

    /**
     * @param columnStyle the columnStyle to set
     */
    public ReportTable setColumnStyle(String columnStyle) {
        this.columnStyle = columnStyle;
        return this;
    }

    /**
     * @return the valueStyle
     */
    public String getValueStyle() {
        return valueStyle;
    }

    /**
     * @param valueStyle the valueStyle to set
     */
    public ReportTable setValueStyle(String valueStyle) {
        this.valueStyle = valueStyle;
        return this;
    }

}
