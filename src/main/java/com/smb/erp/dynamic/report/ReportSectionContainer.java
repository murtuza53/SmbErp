/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Burhani152
 */
public class ReportSectionContainer {
    
    private List<ReportSection> reportSection = new LinkedList<>();
    private int gapBefore = 0;
    private int gapAfter = 0;
    private ContainerPrintLayout printLayout = ContainerPrintLayout.HORIZONTAL;
    
    public static enum ContainerPrintLayout {HORIZONTAL, VERTICAL};

    public ReportSectionContainer() {
    }

    /**
     * @return the reportSection
     */
    public List<ReportSection> getReportSection() {
        return reportSection;
    }

    /**
     * @param reportSection the reportSection to set
     */
    public void setReportSection(List<ReportSection> reportSection) {
        this.reportSection = reportSection;
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

    /**
     * @return the printLayout
     */
    public ContainerPrintLayout getPrintLayout() {
        return printLayout;
    }

    /**
     * @param printLayout the printLayout to set
     */
    public void setPrintLayout(ContainerPrintLayout printLayout) {
        this.printLayout = printLayout;
    }
    
    
}
