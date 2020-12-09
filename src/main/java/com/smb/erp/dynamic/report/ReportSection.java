/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Murtuza
 */
@XmlRootElement
public class ReportSection implements Serializable {

    private String title;
    private String sectionWidth;
    private List<ReportField> reportFields = new LinkedList<ReportField>();
    private SectionPrintLayout printLayout = SectionPrintLayout.VERTICAL;
    private int gapBefore = 0;
    private int gapAfter = 0;
    private String titleStyle;

    public static enum SectionPrintLayout {
        HORIZONTAL, VERTICAL
    };

    public ReportSection() {

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
    }

    /**
     * @return the sectionWidth
     */
    public String getSectionWidth() {
        return sectionWidth;
    }

    /**
     * @param sectionWidth the sectionWidth to set
     */
    @XmlElement
    public void setSectionWidth(String sectionWidth) {
        this.sectionWidth = sectionWidth;
    }

    /**
     * @return the reportFields
     */
    public List<ReportField> getReportFields() {
        return reportFields;
    }

    /**
     * @param reportFields the reportFields to set
     */
    @XmlElement
    public void setReportFields(List<ReportField> reportFields) {
        this.reportFields = reportFields;
    }

    public void addReportField(ReportField field) {
        if (reportFields == null) {
            reportFields = new LinkedList<ReportField>();
        }

        reportFields.add(field);
    }

    public String toString() {
        return getTitle();
    }

    /**
     * @return the printLayout
     */
    public SectionPrintLayout getPrintLayout() {
        return printLayout;
    }

    /**
     * @param printLayout the printLayout to set
     */
    public void setPrintLayout(SectionPrintLayout printLayout) {
        this.printLayout = printLayout;
    }

    /**
     * @return the gapBefore
     */
    public int getGapBefore() {
        return gapBefore;
    }

    /**
     * @param gapBefore the gapBefore to set
     */
    public void setGapBefore(int gapBefore) {
        this.gapBefore = gapBefore;
    }

    /**
     * @return the gapAfter
     */
    public int getGapAfter() {
        return gapAfter;
    }

    /**
     * @param gapAfter the gapAfter to set
     */
    public void setGapAfter(int gapAfter) {
        this.gapAfter = gapAfter;
    }

    public int getSectionWith(int pageWidth){
        if(sectionWidth==null || sectionWidth.trim().length()==0){
            return 0;
        } else if(sectionWidth.contains("%")){
            return Integer.parseInt(sectionWidth.substring(0, sectionWidth.length()-1))*pageWidth/100;
        }
        return Integer.parseInt(sectionWidth);
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
    }
}
