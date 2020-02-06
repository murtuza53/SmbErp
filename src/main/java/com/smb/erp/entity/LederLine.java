package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the ledline database table.
 *
 */
@Entity
@Table(name = "ledline")
@NamedQuery(name = "LederLine.findAll", query = "SELECT l FROM LederLine l")
public class LederLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String llno;

    private double credit;

    private double debit;

    private String description;

    private double fccredit;

    private double fcdebit;

    private String processedref;

    private String processedrefback;

    private double rate;

    private String reflink1;

    private String reflink2;

    private String refno;

    private String reftype;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transdate;

    private BigInteger version;

    //bi-directional many-to-one association to Asset
    @OneToMany(mappedBy = "ledline")
    private List<Asset> assets;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name = "accountno")
    private Account account;

    //bi-directional many-to-one association to Branch
    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    //bi-directional many-to-one association to AccDoc
    @ManyToOne
    @JoinColumn(name = "docno")
    private AccDoc accdoc;

    //bi-directional many-to-one association to PartialPaymentDetail
    @OneToMany(mappedBy = "ledline")
    private List<PartialPaymentDetail> ppdetails;

    public LederLine() {
    }

    public String getLlno() {
        return this.llno;
    }

    public void setLlno(String llno) {
        this.llno = llno;
    }

    public double getCredit() {
        return this.credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return this.debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFccredit() {
        return this.fccredit;
    }

    public void setFccredit(double fccredit) {
        this.fccredit = fccredit;
    }

    public double getFcdebit() {
        return this.fcdebit;
    }

    public void setFcdebit(double fcdebit) {
        this.fcdebit = fcdebit;
    }

    public String getProcessedref() {
        return this.processedref;
    }

    public void setProcessedref(String processedref) {
        this.processedref = processedref;
    }

    public String getProcessedrefback() {
        return this.processedrefback;
    }

    public void setProcessedrefback(String processedrefback) {
        this.processedrefback = processedrefback;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getReflink1() {
        return this.reflink1;
    }

    public void setReflink1(String reflink1) {
        this.reflink1 = reflink1;
    }

    public String getReflink2() {
        return this.reflink2;
    }

    public void setReflink2(String reflink2) {
        this.reflink2 = reflink2;
    }

    public String getRefno() {
        return this.refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getReftype() {
        return this.reftype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }

    public Date getTransdate() {
        return this.transdate;
    }

    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public List<Asset> getAssets() {
        return this.assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public Asset addAsset(Asset asset) {
        getAssets().add(asset);
        asset.setLedline(this);

        return asset;
    }

    public Asset removeAsset(Asset asset) {
        getAssets().remove(asset);
        asset.setLedline(null);

        return asset;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public AccDoc getAccdoc() {
        return this.accdoc;
    }

    public void setAccdoc(AccDoc accdoc) {
        this.accdoc = accdoc;
    }

    public List<PartialPaymentDetail> getPpdetails() {
        return this.ppdetails;
    }

    public void setPpdetails(List<PartialPaymentDetail> ppdetails) {
        this.ppdetails = ppdetails;
    }

    public PartialPaymentDetail addPpdetail(PartialPaymentDetail ppdetail) {
        getPpdetails().add(ppdetail);
        ppdetail.setLedline(this);

        return ppdetail;
    }

    public PartialPaymentDetail removePpdetail(PartialPaymentDetail ppdetail) {
        getPpdetails().remove(ppdetail);
        ppdetail.setLedline(null);

        return ppdetail;
    }

    @Override
    public String toString() {
        return "LederLine{" + "llno=" + llno + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.llno);
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
        final LederLine other = (LederLine) obj;
        if (!Objects.equals(this.llno, other.llno)) {
            return false;
        }
        return true;
    }

}
