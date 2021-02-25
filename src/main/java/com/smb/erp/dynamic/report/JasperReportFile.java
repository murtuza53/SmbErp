/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author admin
 */
public class JasperReportFile implements Serializable{
    
    private boolean letterHeadRequired;
    private String reportName;
    private ReportField title;
    private List<ReportField> parameters;
    private ReportTable reportTable;
 
    public JasperReportFile(){
        parameters = new LinkedList<ReportField>();
        reportTable = new ReportTable();
        reportTable.setFieldList(new LinkedList<TableField>());
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
     * @return the parameters
     */
    public List<ReportField> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<ReportField> parameters) {
        this.parameters = parameters;
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

    @Override
    public String toString() {
        return "JasperReportFile{" + "reportName=" + getReportName() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.getReportName());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JasperReportFile other = (JasperReportFile) obj;
        if (!Objects.equals(this.reportName, other.reportName)) {
            return false;
        }
        return true;
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
    
}
