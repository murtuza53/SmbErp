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
public class ReportField implements Serializable {

    private long id = new Date().getTime();
    private String text;
    private String label;
    private boolean printLabel = false;
    private String propertyName;
    private String prefix;
    private String suffix;
    private boolean spellIt = false;
    private Class type;
    private FieldPrintLayout printLayout = FieldPrintLayout.LEFT_RIGHT;
    private String labelStyle;   //propertyName of the Theme
    private String textStyle;   //propertyName of the Theme

    @JsonIgnore
    private BeanField field;
    
    public static enum FieldPrintLayout {
        LEFT_RIGHT, TOP_BOTTOM
    };

    public ReportField() {

    }

    public ReportField(String text) {
        this.text = text;
    }

    public ReportField(String label, String propertyName, boolean printLabel) {
        this.label = label;
        this.propertyName = propertyName;
        this.printLabel = printLabel;
    }

    public ReportField(String label, String propertyName, boolean printLabel, FieldPrintLayout layout) {
        this.label = label;
        this.propertyName = propertyName;
        this.printLabel = printLabel;
        this.printLayout = layout;
    }

    public ReportField(String label, String propertyName, boolean printLabel, FieldPrintLayout layout, String labelStyle, String textStyle) {
        this(label, propertyName, printLabel, layout);
        this.labelStyle = labelStyle;
        this.textStyle = textStyle;
    }

    public ReportField(String label, String propertyName, boolean printLabel, String prefix) {
        this.label = label;
        this.propertyName = propertyName;
        this.printLabel = printLabel;
        this.prefix = prefix;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    @XmlElement
    public void setText(String text) {
        this.text = text;
        //return this;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    @XmlElement
    public void setLabel(String label) {
        this.label = label;
        //return this;
    }

    /**
     * @return the printLabel
     */
    public boolean isPrintLabel() {
        return printLabel;
    }

    /**
     * @param printLabel the printLabel to set
     */
    @XmlElement
    public void setPrintLabel(boolean printLabel) {
        this.printLabel = printLabel;
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
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    @XmlElement
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        //return this;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    @XmlElement
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        //return this;
    }

    /**
     * @return the printLayout
     */
    public FieldPrintLayout getPrintLayout() {
        return printLayout;
    }

    /**
     * @param printLayout the printLayout to set
     */
    public void setPrintLayout(FieldPrintLayout printLayout) {
        this.printLayout = printLayout;
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
        if (type != null) {
            return type.getSimpleName();
        }
        return null;
    }

    public String toString() {
        return getLabel() + ": " + getText();
    }

    public Object getFieldValue(Object target) {
        return ReportUtils.getFieldValue(getPropertyName(), target);
    }

    public ReportField clone() {
        ReportField r = new ReportField();
        r.setLabel(label);
        r.setPrefix(prefix);
        r.setPrintLabel(isPrintLabel());
        r.setPrintLayout(printLayout);
        r.setPropertyName(propertyName);
        r.setSuffix(suffix);
        r.setText(text);
        r.setType(type);

        return r;
    }

    public String getValue(Object data) {
        Object val = ReportUtils.getFormattedFieldValue(this, data);
        if (val == null) {
            return null;
        }
        return val.toString();
    }

    /**
     * @return the labelStyle
     */
    public String getLabelStyle() {
        return labelStyle;
    }

    /**
     * @param labelStyle the labelStyle to set
     */
    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
        //return this;
    }

    /**
     * @return the textStyle
     */
    public String getTextStyle() {
        return textStyle;
    }

    /**
     * @param textStyle the textStyle to set
     */
    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
        //return this;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
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
