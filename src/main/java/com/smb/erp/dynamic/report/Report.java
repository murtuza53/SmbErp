/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import net.sf.dynamicreports.report.base.DRMargin;
import net.sf.dynamicreports.report.base.DRPage;
import net.sf.dynamicreports.report.constant.PageOrientation;

/**
 *
 * @author Burhani152
 */
@XmlRootElement
public class Report implements Serializable {

    private boolean letterHeadRequired = true;
    private String reportName;
    private ReportField title;
    private List<ReportSectionContainer> headerSection;
    private List<ReportSectionContainer> footerSection;
    private List<ReportField> signatureFields;
    private ReportTable reportTable;
    private boolean printSignatures = true;
    private String titleStyle;
    private DRPage page = new DRPage();

    @JsonIgnore
    private PageType pageType = PageType.A4;

    @JsonIgnore
    private PageOrientation pageOrientation = PageOrientation.PORTRAIT;
    
    @JsonIgnore
    private int margin = 20;

    public Report() {
        //page.setPageFormat(pageType, pageOrientation);
        page.setWidth(pageType.getWidth());
        page.setHeight(pageType.getHeight());
        page.setOrientation(pageOrientation);
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

    public void addHeaderSection(ReportSectionContainer header) {
        if (headerSection == null) {
            headerSection = new LinkedList<>();
        }
        headerSection.add(header);
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

    public void addFooterSection(ReportSectionContainer footer) {
        if (footerSection == null) {
            footerSection = new LinkedList<>();
        }
        footerSection.add(footer);
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
    public boolean getLetterHeadRequired() {
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

    /**
     * @return the reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportName the reportName to set
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * @return the pageType
     */
    public PageType getPageType() {
        return pageType;
    }

    /**
     * @param pageType the pageType to set
     */
    public void setPageType(PageType pageType) {
        this.pageType = pageType;
        //page.setPageFormat(pageType, pageOrientation);
        page.setWidth(pageType.getWidth());
        page.setHeight(pageType.getHeight());
        page.setOrientation(pageOrientation);
    }

    /**
     * @return the pageOrientation
     */
    public PageOrientation getPageOrientation() {
        return pageOrientation;
    }

    /**
     * @param pageOrientation the pageOrientation to set
     */
    public void setPageOrientation(PageOrientation pageOrientation) {
        this.pageOrientation = pageOrientation;
        //page.setPageFormat(pageType, pageOrientation);
        page.setWidth(pageType.getWidth());
        page.setHeight(pageType.getHeight());
        page.setOrientation(pageOrientation);
    }

    /**
     * @return the page
     */
    public DRPage getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(DRPage page) {
        this.page = page;
    }

    /**
     * @return the margin
     */
    public int getMargin() {
        return margin;
    }

    /**
     * @param margin the margin to set
     */
    public void setMargin(int margin) {
        this.margin = margin;
        page.setMargin(new DRMargin(margin));
    }

    /**
     * @return the signatureFields
     */
    public List<ReportField> getSignatureFields() {
        return signatureFields;
    }

    /**
     * @param signatureFields the signatureFields to set
     */
    public void setSignatureFields(List<ReportField> signatureFields) {
        this.signatureFields = signatureFields;
    }

    public void addSignatureField(ReportField field){
        if(getSignatureFields()==null){
            signatureFields = new LinkedList<>();
        }
        signatureFields.add(field);
    }
    
    public void removeSignatureField(ReportField field){
        signatureFields.remove(field);
    }

    /**
     * @return the printSignatures
     */
    public boolean isPrintSignatures() {
        return printSignatures;
    }

    /**
     * @param printSignatures the printSignatures to set
     */
    public void setPrintSignatures(boolean printSignatures) {
        this.printSignatures = printSignatures;
    }
    
}
