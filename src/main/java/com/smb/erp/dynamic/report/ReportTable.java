/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.smb.erp.dynamic.report.TableField.ColumnFunction;
import java.util.Date;
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

    private Long id = new Date().getTime();
    private String title;
    private Class dataClass;
    private String dataPropertyName;
    private List<TableField> fieldList = new LinkedList<>();
    private List<ReportField> summaryList = new LinkedList<>();
    private String titleStyle = "columnHeaderStyle";
    private String detailsOddStyle;
    private String detailsEvenStyle = "detailsEvenRowStyle";
    private String subtotalStyle = "subtotalStyle";
    private String columnTitleStyle = "columnTitleStyle";
    private String columnStyle = "columnStyle";
    private String valueStyle = "tableRowStyle";
    private Boolean printSummary = false;

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

    public void addTableField(TableField field) {
        if (getFieldList() == null) {
            setFieldList(new LinkedList<>());
        }
        getFieldList().add(field);
    }

    public void removeTableField(TableField field) {
        getFieldList().remove(field);
    }

    public void addSummaryField(ReportField field) {
        if (getSummaryList() == null) {
            setSummaryList(new LinkedList<>());
        }
        getSummaryList().add(field);
    }

    public void removeSummaryField(ReportField field) {
        getSummaryList().remove(field);
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
    public void setFieldList(List<TableField> fieldList) {
        this.fieldList = fieldList;
        //return this;
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
    public void setDataPropertyName(String dataPropertyName) {
        this.dataPropertyName = dataPropertyName;
        //return this;
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
    public void setSummaryList(List<ReportField> summaryList) {
        this.summaryList = summaryList;
        //return this;
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
    public void setTitle(String title) {
        this.title = title;
        //return this;
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
    public void setTitleStyle(String titleStyle) {
        this.titleStyle = titleStyle;
        //return this;
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
    public void setDetailsOddStyle(String detailsOddStyle) {
        this.detailsOddStyle = detailsOddStyle;
        //return this;
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
    public void setDetailsEvenStyle(String detailsEvenStyle) {
        this.detailsEvenStyle = detailsEvenStyle;
        //return this;
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
    public void setSubtotalStyle(String subtotalStyle) {
        this.subtotalStyle = subtotalStyle;
        //return this;
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
    public void setColumnTitleStyle(String columnTitleStyle) {
        this.columnTitleStyle = columnTitleStyle;
        //return this;
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
    public void setColumnStyle(String columnStyle) {
        this.columnStyle = columnStyle;
        //return this;
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
    public void setValueStyle(String valueStyle) {
        this.valueStyle = valueStyle;
        //return this;
    }

    /**
     * @return the dataClass
     */
    public Class getDataClass() {
        return dataClass;
    }

    /**
     * @param dataClass the dataClass to set
     */
    public void setDataClass(Class dataClass) {
        this.dataClass = dataClass;
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
     * @return the printSummary
     */
    public Boolean getPrintSummary() {
        return printSummary;
    }

    /**
     * @param printSummary the printSummary to set
     */
    public void setPrintSummary(Boolean printSummary) {
        this.printSummary = printSummary;
    }

}
