package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the ledline database table.
 *
 */
@Entity
@Table(name = "ledline")
@NamedQuery(name = "LedgerLine.findAll", query = "SELECT l FROM LedgerLine l")
public class LedgerLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long llno = ThreadLocalRandom.current().nextLong();

    private Double credit = 0.0;

    private Double debit = 0.0;

    private String description;

    private Double fccredit = 0.0;

    private Double fcdebit = 0.0;

    private String processedref;

    private String processedrefback;

    private Double rate = 1.0;

    private String reflink1;

    private String reflink2;

    private String refno;

    private String reftype;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transdate;

    private BigInteger version;

    //bi-directional many-to-one association to Asset
    @OneToMany(mappedBy = "ledline", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Asset> assets;

    //bi-directional many-to-one association to Account
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "accountno")
    private Account account;

    //bi-directional many-to-one association to Branch
    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    //bi-directional many-to-one association to AccDoc
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docno")
    private AccDoc accdoc;

    @ManyToOne
    @JoinColumn(name = "currencyno")
    private Country currency;

    //bi-directional many-to-one association to PartialPaymentDetail
    @OneToMany(mappedBy = "ledline", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<PartialPaymentDetail> ppdetails;

    @Transient
    private Double cumulative = 0.0;
    
    @Transient
    private boolean totalRow = false;

    public LedgerLine() {
    }

    public LedgerLine(Double debit, Double credit, Double balance) {
        this();
        this.debit = debit;
        this.credit = credit;
        this.cumulative = balance;
    }

    public Long getLlno() {
        return this.llno;
    }

    public void setLlno(Long llno) {
        this.llno = llno;
    }

    /*public Double getCredit() {
        return this.credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        return this.debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }
    
        public Double getFccredit() {
        return this.fccredit;
    }

    public void setFccredit(Double fccredit) {
        this.fccredit = fccredit;
    }

    public Double getFcdebit() {
        return this.fcdebit;
    }

    public void setFcdebit(Double fcdebit) {
        this.fcdebit = fcdebit;
    }*/
    
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getRate() {
        return this.rate;
    }

    public void setRate(Double rate) {
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
        if(getPpdetails()==null){
            setPpdetails(new LinkedList());
        }
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
        final LedgerLine other = (LedgerLine) obj;
        return Objects.equals(this.llno, other.llno);
    }

    /**
     * @return the cumulative
     */
    public Double getCumulative() {
        return cumulative;
    }

    /**
     * @param cumulative the cumulative to set
     */
    public void setCumulative(Double cumulative) {
        this.cumulative = cumulative;
    }

    public boolean isDummy() {
        if(totalRow){
            return true;
        }
        return getAccount() == null && getAccdoc() == null;
    }

    public Double getCredit() {
        if (isDummy()) {
            return this.credit;
        }

        if (getFccredit() > 0) {
            return getFccredit() * getRate();
        }
        return credit;
    }

    public void setCredit(Double credit) {
        if (isDummy()) {
            this.credit = credit;
            return;
        }

        if (credit < 0) {
            credit = 0.0;
        }
        this.credit = credit;
        if (credit > 0) {
            this.debit = 0.0;
        }
    }

    public Double getDebit() {
        if (isDummy()) {
            return this.debit;
        }

        if (getFcdebit() > 0) {
            return getFcdebit() * getRate();
        }
        return debit;
    }

    public void setDebit(Double debit) {
        if (isDummy()) {
            this.debit = debit;
            return;
        }

        if (debit < 0) {
            debit = 0.0;
        }
        this.debit = debit;
        if (debit > 0) {
            this.credit = 0.0;
        }
    }

    public Double getFccredit() {
        return fccredit;
    }

    public void setFccredit(Double fcCredit) {
        if (isDummy()) {
            this.fccredit = fcCredit;
            return;
        }

        if (fcCredit < 0) {
            fcCredit = 0.0;
        }
        this.fccredit = fcCredit;
        if (fcCredit > 0) {
            this.fcdebit = 0.0;
            if (getRate() > 0) {
                setCredit(this.fccredit * getRate());
            }
        }
    }

    public Double getFcdebit() {
        return fcdebit;
    }

    public void setFcdebit(Double fcDebit) {
        if (isDummy()) {
            this.fcdebit = fcDebit;
            return;
        }

        if (fcDebit < 0) {
            fcDebit = 0.0;
        }
        this.fcdebit = fcDebit;
        if (fcDebit > 0) {
            this.fccredit = 0.0;
            if (getRate() > 0) {
                setDebit(this.fcdebit * getRate());
            }
        }
    }

    /**
     * @return the totalRow
     */
    public boolean isTotalRow() {
        return totalRow;
    }

    /**
     * @param totalRow the totalRow to set
     */
    public void setTotalRow(boolean totalRow) {
        this.totalRow = totalRow;
    }

    /**
     * @return the currency
     */
    public Country getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Country currency) {
        this.currency = currency;
    }

    public static LedgerLine clone(LedgerLine line){
        LedgerLine newline = new LedgerLine();
        
        newline.setAccount(line.getAccount());
        newline.setBranch(line.getBranch());
        newline.setCredit(line.getCredit());
        newline.setCurrency(line.getCurrency());
        newline.setDebit(line.getDebit());
        newline.setDescription(line.getDescription());
        newline.setFccredit(line.getFccredit());
        newline.setFcdebit(line.getFcdebit());
        newline.setRate(line.getRate());
        newline.setRefno(line.getRefno());
        newline.setReftype(line.getReftype());
        newline.setTransdate(line.getTransdate());
        
        return newline;
    }
    
}
