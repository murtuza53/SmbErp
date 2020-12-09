/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Burhani152
 */
@XmlRootElement
public class Report implements Serializable{
    
    private boolean letterHeadRequired;
    private ReportField title;
    private List<ReportSectionContainer> headerSection;
    private List<ReportSectionContainer> footerSection;
    private ReportTable reportTable;
    private String titleStyle;
    
    public Report(){
        
    }

    /**
     * @return the title
     */
    public ReportField getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(ReportField title) {
        this.title = title;
    }

    /**
     * @return the headerSection
     */
    public List<ReportSectionContainer> getHeaderSection() {
        return headerSection;
    }

    /**
     * @param headerSection the headerSection to set
     */
    public void setHeaderSection(List<ReportSectionContainer> headerSection) {
        this.headerSection = headerSection;
    }

    /**
     * @return the footerSection
     */
    public List<ReportSectionContainer> getFooterSection() {
        return footerSection;
    }

    /**
     * @param footerSection the footerSection to set
     */
    public void setFooterSection(List<ReportSectionContainer> footerSection) {
        this.footerSection = footerSection;
    }

    /**
     * @return the reportTable
     */
    public ReportTable getReportTable() {
        return reportTable;
    }

    /**
     * @param reportTable the reportTable to set
     */
    public void setReportTable(ReportTable reportTable) {
        this.reportTable = reportTable;
    }

    /**
     * @return the letterHeadRequired
     */
    public boolean isLetterHeadRequired() {
        return letterHeadRequired;
    }

    /**
     * @param letterHeadRequired the letterHeadRequired to set
     */
    public void setLetterHeadRequired(boolean letterHeadRequired) {
        this.letterHeadRequired = letterHeadRequired;
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
