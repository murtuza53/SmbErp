/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity;

import com.smb.erp.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Burhani152
 */
@Entity
@Table(name = "scbusdoc")
public class StockCountingBusDoc implements Serializable {

    @Id
    private String docno;

    @Temporal(TemporalType.TIMESTAMP)
    private Date docdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date processdate;

    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    private String description;

    private String refno;

    private String manualdocno;

    private boolean markdeleted;

    private String docstatus = "Counting";  //Draft, Counting, Completed

    private Double totalamount = 0.0;

    private Integer countyear = DateUtil.getYear(new Date());

    @ManyToOne
    @JoinColumn(name = "empid")
    private Emp emp;

    @ManyToOne
    @JoinColumn(name = "busdocinfoid")
    private BusDocInfo busdocinfo;

    @OneToMany(mappedBy = "busdoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<StockCountingBatch> stockCountingBatch;

    public StockCountingBusDoc() {
    }

    /**
     * @return the docno
     */
    public String getDocno() {
        return docno;
    }

    /**
     * @param docno the docno to set
     */
    public void setDocno(String docno) {
        this.docno = docno;
    }

    /**
     * @return the docdate
     */
    public Date getDocdate() {
        return docdate;
    }

    /**
     * @param docdate the docdate to set
     */
    public void setDocdate(Date docdate) {
        this.docdate = docdate;
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
     * @return the updatedon
     */
    public Date getUpdatedon() {
        return updatedon;
    }

    /**
     * @param updatedon the updatedon to set
     */
    public void setUpdatedon(Date updatedon) {
        this.updatedon = updatedon;
    }

    /**
     * @return the branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the refno
     */
    public String getRefno() {
        return refno;
    }

    /**
     * @param refno the refno to set
     */
    public void setRefno(String refno) {
        this.refno = refno;
    }

    /**
     * @return the manualdocno
     */
    public String getManualdocno() {
        return manualdocno;
    }

    /**
     * @param manualdocno the manualdocno to set
     */
    public void setManualdocno(String manualdocno) {
        this.manualdocno = manualdocno;
    }

    /**
     * @return the markdeleted
     */
    public boolean isMarkdeleted() {
        return markdeleted;
    }

    /**
     * @param markdeleted the markdeleted to set
     */
    public void setMarkdeleted(boolean markdeleted) {
        this.markdeleted = markdeleted;
    }

    /**
     * @return the docstatus
     */
    public String getDocstatus() {
        return docstatus;
    }

    /**
     * @param docstatus the docstatus to set
     */
    public void setDocstatus(String docstatus) {
        this.docstatus = docstatus;
    }

    /**
     * @return the totalamount
     */
    public Double getTotalamount() {
        return totalamount;
    }

    /**
     * @param totalamount the totalamount to set
     */
    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

    /**
     * @return the emp
     */
    public Emp getEmp() {
        return emp;
    }

    /**
     * @param emp the emp to set
     */
    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    /**
     * @return the busdocinfo
     */
    public BusDocInfo getBusdocinfo() {
        return busdocinfo;
    }

    /**
     * @param busdocinfo the busdocinfo to set
     */
    public void setBusdocinfo(BusDocInfo busdocinfo) {
        this.busdocinfo = busdocinfo;
    }

    @Override
    public String toString() {
        return docno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.docno);
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
        final StockCountingBusDoc other = (StockCountingBusDoc) obj;
        if (!Objects.equals(this.docno, other.docno)) {
            return false;
        }
        return true;
    }

    /**
     * @return the stockCountingBatch
     */
    public List<StockCountingBatch> getStockCountingBatch() {
        return stockCountingBatch;
    }

    /**
     * @param stockCountingBatch the stockCountingBatch to set
     */
    public void setStockCountingBatch(List<StockCountingBatch> stockCountingBatch) {
        this.stockCountingBatch = stockCountingBatch;
    }

    /**
     * @return the countyear
     */
    public Integer getCountyear() {
        return countyear;
    }

    /**
     * @param countyear the countyear to set
     */
    public void setCountyear(Integer countyear) {
        this.countyear = countyear;
    }

    public void refreshTotal() {
        totalamount = 0.0;
        if (getStockCountingBatch() != null) {
            for (StockCountingBatch scb : getStockCountingBatch()) {
                totalamount = totalamount + scb.getTotalAmount();
            }
        }
    }

    public String getStatusCss(String value) {
        //.status-new .status-draft{}
        //.status-negotiation .status-pending{}
        //.status-proposal .status-partial{}
        //.status-qualified .status-completed{}
        //.status-qualified .status-paid{}
        //.status-unqualified .status-cancelled{}
        //.status-renewal .status-returned{}
        if (value.equalsIgnoreCase("Draft")) {
            return "new";
        } else if (value.equalsIgnoreCase("Pending")) {
            return "negotiation";
        } else if (value.equalsIgnoreCase("Partial") || value.equalsIgnoreCase("Counting")) {
            return "proposal";
        } else if (value.equalsIgnoreCase("Completed") || value.equalsIgnoreCase("Paid") || value.equalsIgnoreCase("Processed")) {
            return "qualified";
        } else if (value.equalsIgnoreCase("Cancelled")) {
            return "unqualified";
        } else if (value.equalsIgnoreCase("Returned")) {
            return "renewal";
        }
        return "qualified";
    }

    /**
     * @return the processdate
     */
    public Date getProcessdate() {
        return processdate;
    }

    /**
     * @param processdate the processdate to set
     */
    public void setProcessdate(Date processdate) {
        this.processdate = processdate;
    }
}
