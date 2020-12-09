/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import net.sf.dynamicreports.report.builder.style.SimpleStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;

/**
 *
 * @author Burhani152
 */
public class Theme {
    
    private String themeName;
    
    private ReportElementStyle reportTitleStyle;
    
    private ReportElementStyle titleStyle1;
    private ReportElementStyle titleStyle2;
    private ReportElementStyle titleStyle3;
    
    private ReportElementStyle labelStyle1;
    private ReportElementStyle labelStyle2;
    private ReportElementStyle labelStyle3;
    
    private ReportElementStyle textStyle1;
    private ReportElementStyle textStyle2;
    private ReportElementStyle textStyle3;
    
    private ReportElementStyle columnTitleStyle;
    
    private ReportElementStyle columnHeaderStyle;
    
    private ReportElementStyle columnStyle;
    
    private ReportElementStyle subtotalStyle;
    
    private ReportElementStyle tableRowStyle;
    
    private ReportElementStyle detailsEvenRowStyle;
    
    private ReportElementStyle detailsOddRowStyle;
    
    private ReportElementStyle footerSectionStyle;
    
    private ReportElementStyle defaultStyle;
    
    private ColorScheme colorScheme;
    
    public Theme(ColorScheme colorScheme){
        this.colorScheme = colorScheme;
    }

    public Theme(String themeName, ColorScheme colorScheme) {
        this(colorScheme);
        this.themeName = themeName;
    }

    /**
     * @return the themeName
     */
    public String getThemeName() {
        return themeName;
    }

    /**
     * @param themeName the themeName to set
     */
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    /**
     * @return the reportTitleStyle
     */
    public ReportElementStyle getReportTitleStyle() {
        return reportTitleStyle;
    }

    /**
     * @param reportTitleStyle the reportTitleStyle to set
     */
    public void setReportTitleStyle(ReportElementStyle reportTitleStyle) {
        this.reportTitleStyle = reportTitleStyle;
    }

    /**
     * @return the columnTitleStyle
     */
    public ReportElementStyle getColumnTitleStyle() {
        return columnTitleStyle;
    }

    /**
     * @param columnTitleStyle the columnTitleStyle to set
     */
    public void setColumnTitleStyle(ReportElementStyle columnTitleStyle) {
        this.columnTitleStyle = columnTitleStyle;
    }

    /**
     * @return the columnHeaderStyle
     */
    public ReportElementStyle getColumnHeaderStyle() {
        return columnHeaderStyle;
    }

    /**
     * @param columnHeaderStyle the columnHeaderStyle to set
     */
    public void setColumnHeaderStyle(ReportElementStyle columnHeaderStyle) {
        this.columnHeaderStyle = columnHeaderStyle;
    }

    /**
     * @return the columnStyle
     */
    public ReportElementStyle getColumnStyle() {
        return columnStyle;
    }

    /**
     * @param columnStyle the columnStyle to set
     */
    public void setColumnStyle(ReportElementStyle columnStyle) {
        this.columnStyle = columnStyle;
    }

    /**
     * @return the detailsEvenRowStyle
     */
    public ReportElementStyle getDetailsEvenRowStyle() {
        return detailsEvenRowStyle;
    }

    /**
     * @param detailsEvenRowStyle the detailsEvenRowStyle to set
     */
    public void setDetailsEvenRowStyle(ReportElementStyle detailsEvenRowStyle) {
        this.detailsEvenRowStyle = detailsEvenRowStyle;
    }

    /**
     * @return the detailsOddRowStyle
     */
    public ReportElementStyle getDetailsOddRowStyle() {
        return detailsOddRowStyle;
    }

    /**
     * @param detailsOddRowStyle the detailsOddRowStyle to set
     */
    public void setDetailsOddRowStyle(ReportElementStyle detailsOddRowStyle) {
        this.detailsOddRowStyle = detailsOddRowStyle;
    }

    /**
     * @return the footerSectionStyle
     */
    public ReportElementStyle getFooterSectionStyle() {
        return footerSectionStyle;
    }

    /**
     * @param footerSectionStyle the footerSectionStyle to set
     */
    public void setFooterSectionStyle(ReportElementStyle footerSectionStyle) {
        this.footerSectionStyle = footerSectionStyle;
    }

    /**
     * @return the colorScheme
     */
    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * @param colorScheme the colorScheme to set
     */
    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    /**
     * @return the tableRowStyle
     */
    public ReportElementStyle getTableRowStyle() {
        return tableRowStyle;
    }

    /**
     * @param tableRowStyle the tableRowStyle to set
     */
    public void setTableRowStyle(ReportElementStyle tableRowStyle) {
        this.tableRowStyle = tableRowStyle;
    }

    /**
     * @return the subtotalStyle
     */
    public ReportElementStyle getSubtotalStyle() {
        return subtotalStyle;
    }

    /**
     * @param subtotalStyle the subtotalStyle to set
     */
    public void setSubtotalStyle(ReportElementStyle subtotalStyle) {
        this.subtotalStyle = subtotalStyle;
    }

    /**
     * @return the defaultStyle
     */
    public ReportElementStyle getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * @param defaultStyle the defaultStyle to set
     */
    public Theme setDefaultStyle(ReportElementStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
        return this;
    }

    public ReportElementStyle getStyle(String styleName){
        if(styleName!=null){
            return (ReportElementStyle)ReportUtils.getFieldValue(styleName, this);
        }
        return getDefaultStyle();
    }

    public StyleBuilder createStyle(String styleName){
        return ReportUtils.createStyle(getStyle(styleName), colorScheme);
    }
    
    public SimpleStyleBuilder createSimpleStyle(String styleName){
        return ReportUtils.createSimpleStyle(getStyle(styleName), colorScheme);
    }

    /**
     * @return the titleStyle1
     */
    public ReportElementStyle getTitleStyle1() {
        return titleStyle1;
    }

    /**
     * @param titleStyle1 the titleStyle1 to set
     */
    public void setTitleStyle1(ReportElementStyle titleStyle1) {
        this.titleStyle1 = titleStyle1;
    }

    /**
     * @return the titleStyle2
     */
    public ReportElementStyle getTitleStyle2() {
        return titleStyle2;
    }

    /**
     * @param titleStyle2 the titleStyle2 to set
     */
    public void setTitleStyle2(ReportElementStyle titleStyle2) {
        this.titleStyle2 = titleStyle2;
    }

    /**
     * @return the titleStyle3
     */
    public ReportElementStyle getTitleStyle3() {
        return titleStyle3;
    }

    /**
     * @param titleStyle3 the titleStyle3 to set
     */
    public void setTitleStyle3(ReportElementStyle titleStyle3) {
        this.titleStyle3 = titleStyle3;
    }

    /**
     * @return the labelStyle1
     */
    public ReportElementStyle getLabelStyle1() {
        return labelStyle1;
    }

    /**
     * @param labelStyle1 the labelStyle1 to set
     */
    public void setLabelStyle1(ReportElementStyle labelStyle1) {
        this.labelStyle1 = labelStyle1;
    }

    /**
     * @return the labelStyle2
     */
    public ReportElementStyle getLabelStyle2() {
        return labelStyle2;
    }

    /**
     * @param labelStyle2 the labelStyle2 to set
     */
    public void setLabelStyle2(ReportElementStyle labelStyle2) {
        this.labelStyle2 = labelStyle2;
    }

    /**
     * @return the labelStyle3
     */
    public ReportElementStyle getLabelStyle3() {
        return labelStyle3;
    }

    /**
     * @param labelStyle3 the labelStyle3 to set
     */
    public void setLabelStyle3(ReportElementStyle labelStyle3) {
        this.labelStyle3 = labelStyle3;
    }

    /**
     * @return the textStyle1
     */
    public ReportElementStyle getTextStyle1() {
        return textStyle1;
    }

    /**
     * @param textStyle1 the textStyle1 to set
     */
    public void setTextStyle1(ReportElementStyle textStyle1) {
        this.textStyle1 = textStyle1;
    }

    /**
     * @return the textStyle2
     */
    public ReportElementStyle getTextStyle2() {
        return textStyle2;
    }

    /**
     * @param textStyle2 the textStyle2 to set
     */
    public void setTextStyle2(ReportElementStyle textStyle2) {
        this.textStyle2 = textStyle2;
    }

    /**
     * @return the textStyle3
     */
    public ReportElementStyle getTextStyle3() {
        return textStyle3;
    }

    /**
     * @param textStyle3 the textStyle3 to set
     */
    public void setTextStyle3(ReportElementStyle textStyle3) {
        this.textStyle3 = textStyle3;
    }
}
