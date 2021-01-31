/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Burhani152
 */
@Entity
@Table(name = "printreport")
public class PrintReport implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportid = new Date().getTime();

    private String filename;

    private String reportname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    @ManyToOne
    @JoinColumn(name = "empid")
    private Emp empid;

    @ManyToOne
    @JoinColumn(name = "bdinfoid")
    private BusDocInfo bdinfoid;

    private Boolean defaultreport = false;

    public PrintReport() {
    }

    /**
     * @return the reportid
     */
    public Long getReportid() {
        return reportid;
    }

    /**
     * @param reportid the reportid to set
     */
    public void setReportid(Long reportid) {
        this.reportid = reportid;
    }

    /**
     * @return the bdinfoid
     */
    public BusDocInfo getBdinfoid() {
        return bdinfoid;
    }

    /**
     * @param bdinfoid the bdinfoid to set
     */
    public void setBdinfoid(BusDocInfo bdinfoid) {
        this.bdinfoid = bdinfoid;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the createdon
     */
    public Date getCreatedon() {
        return createdon;
    }

    /**
     * @param createdon the createdon to set
     */
    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    /**
     * @return the empid
     */
    public Emp getEmpid() {
        return empid;
    }

    /**
     * @param empid the empid to set
     */
    public void setEmpid(Emp empid) {
        this.empid = empid;
    }

    /**
     * @return the defaultreport
     */
    public Boolean getDefaultreport() {
        return defaultreport;
    }

    /**
     * @param defaultreport the defaultreport to set
     */
    public void setDefaultreport(Boolean defaultreport) {
        this.defaultreport = defaultreport;
    }

    @Override
    public String toString() {
        return getBdinfoid().getDocname() + " [" + reportid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.reportid);
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
        final PrintReport other = (PrintReport) obj;
        if (!Objects.equals(this.reportid, other.reportid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the reportname
     */
    public String getReportname() {
        return reportname;
    }

    /**
     * @param reportname the reportname to set
     */
    public void setReportname(String reportname) {
        this.reportname = reportname;
    }
}
