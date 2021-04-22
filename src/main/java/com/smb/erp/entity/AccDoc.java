package com.smb.erp.entity;

import com.smb.erp.util.Speller;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the accdoc database table.
 *
 */
@Entity
@Table(name = "accdoc")
@NamedQuery(name = "AccDoc.findAll", query = "SELECT a FROM AccDoc a")
public class AccDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String docno;

    private String dcol;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date docdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String extra1;

    private byte extra11;

    private byte extra12;

    private String extra2;

    private String extra3;

    private String extra4;

    private String extra5;

    private Double extra6 = 0.0;

    private Double extra7 = 0.0;

    private Double extra8 = 0.0;

    private Double extra9 = 0.0;

    private Double extra10 = 0.0;

    private String manualno;

    private String refno;

    private Double rate = 1.0;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentno")
    private PaymentMethod paymentMethod;

    //bi-directional many-to-one association to Branch
    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    //bi-directional many-to-one association to BusinessPartner
    @ManyToOne
    @JoinColumn(name = "companyno")
    private BusinessPartner businesspartner;

    //bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "currencyno")
    private Country currency;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "empno")
    private Emp emp;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne
    @JoinColumn(name = "busdocinfoid")
    private BusDocInfo busdocinfo;

    //bi-directional many-to-one association to LedgerLine
    @OneToMany(mappedBy = "accdoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<LedgerLine> ledlines;

    @Transient
    private PrintReport currentPrintReport;

    @Transient
    private String spelledAmountInWords;
    
    @Transient
    private List<PartialPaymentDetail> paymentDetails;

    public AccDoc() {
    }

    public String getDocno() {
        return this.docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getDcol() {
        return this.dcol;
    }

    public void setDcol(String dcol) {
        this.dcol = dcol;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDocdate() {
        return this.docdate;
    }

    public void setDocdate(Date docdate) {
        this.docdate = docdate;
    }

    public String getExtra1() {
        return this.extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public Double getExtra10() {
        return this.extra10;
    }

    public void setExtra10(Double extra10) {
        this.extra10 = extra10;
    }

    public byte getExtra11() {
        return this.extra11;
    }

    public void setExtra11(byte extra11) {
        this.extra11 = extra11;
    }

    public byte getExtra12() {
        return this.extra12;
    }

    public void setExtra12(byte extra12) {
        this.extra12 = extra12;
    }

    public String getExtra2() {
        return this.extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return this.extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return this.extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getExtra5() {
        return this.extra5;
    }

    public void setExtra5(String extra5) {
        this.extra5 = extra5;
    }

    public Double getExtra6() {
        return this.extra6;
    }

    public void setExtra6(Double extra6) {
        this.extra6 = extra6;
    }

    public Double getExtra7() {
        return this.extra7;
    }

    public void setExtra7(Double extra7) {
        this.extra7 = extra7;
    }

    public Double getExtra8() {
        return this.extra8;
    }

    public void setExtra8(Double extra8) {
        this.extra8 = extra8;
    }

    public Double getExtra9() {
        return this.extra9;
    }

    public void setExtra9(Double extra9) {
        this.extra9 = extra9;
    }

    public String getManualno() {
        return this.manualno;
    }

    public void setManualno(String manualno) {
        this.manualno = manualno;
    }

    public String getRefno() {
        return this.refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BusinessPartner getBusinesspartner() {
        return this.businesspartner;
    }

    public void setBusinesspartner(BusinessPartner businesspartner) {
        this.businesspartner = businesspartner;
    }

    public Emp getEmp() {
        return this.emp;
    }

    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    public List<LedgerLine> getLedlines() {
        return this.ledlines;
    }

    public void setLedlines(List<LedgerLine> ledlines) {
        this.ledlines = ledlines;
    }

    public LedgerLine addLedline(LedgerLine ledline) {
        if (getLedlines() == null) {
            setLedlines(new LinkedList<>());
        }
        getLedlines().add(ledline);
        ledline.setAccdoc(this);

        return ledline;
    }

    public LedgerLine removeLedline(LedgerLine ledline) {
        getLedlines().remove(ledline);
        ledline.setAccdoc(null);

        return ledline;
    }

    @Override
    public String toString() {
        return docno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.docno);
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
        final AccDoc other = (AccDoc) obj;
        if (!Objects.equals(this.docno, other.docno)) {
            return false;
        }
        return true;
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

    public Double getTotalDebit() {
        Double t = 0.0;
        if (getLedlines() != null) {
            t = getLedlines().stream().mapToDouble(x -> x.getDebit()).sum();
        }
        return t;
    }

    public Double getTotalCredit() {
        Double t = 0.0;
        if (getLedlines() != null) {
            t = getLedlines().stream().mapToDouble(x -> x.getCredit()).sum();
        }
        return t;
    }

    public String getCompanyName() {
        HashSet<String> result = new HashSet<String>();
        if (getLedlines() != null) {
            for (LedgerLine ll : getLedlines()) {
                if (ll.getAccount().getBusinesspartner() != null) {
                    result.add(ll.getAccount().toString());
                }
            }
        }
        if (result.isEmpty()) {
            return "";
        }
        return result.toString();
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

    /**
     * @return the rate
     */
    public Double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * @return the paymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the currentPrintReport
     */
    public PrintReport getCurrentPrintReport() {
        return currentPrintReport;
    }

    /**
     * @param currentPrintReport the currentPrintReport to set
     */
    public void setCurrentPrintReport(PrintReport currentPrintReport) {
        this.currentPrintReport = currentPrintReport;
    }

    public String getReportTitle() {
        if (getCurrentPrintReport() != null) {
            return getCurrentPrintReport().getReportname();
        }
        return getBusdocinfo().getDocname();
    }

    public String getCompany() {
        StringBuilder com = null;
        if (getLedlines() != null) {
            for (LedgerLine line : getLedlines()) {
                if (line.getAccount().getBusinesspartner() != null) {
                    if (com == null) {
                        com = new StringBuilder(line.getAccount().getBusinesspartner().getCompanyname());
                    } else {
                        if (!com.toString().contains(line.getAccount().getBusinesspartner().getCompanyname())) {
                            com.append(", ").append(line.getAccount().getBusinesspartner().getCompanyname());
                        }
                    }
                }
            }
        }
        if (com == null) {
            return null;
        }
        return com.toString();
    }

    public String getSpelledAmountInWords() {
        return Speller.spellAmount(getCurrency().getCurrencysym(), getTotalDebit());
    }

    public void setSpelledAmountInWords(String value) {
        this.spelledAmountInWords = value;
    }

    /**
     * @return the paymentDetails
     */
    public List<PartialPaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * @param paymentDetails the paymentDetails to set
     */
    public void setPaymentDetails(List<PartialPaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public void preparePaymentDetailsForPrinting(){
        setPaymentDetails(new LinkedList());
        if(getLedlines()!=null){
            for(LedgerLine line: getLedlines()){
                if(line.getPpdetails()==null || line.getPpdetails().isEmpty()){
                    PartialPaymentDetail ppd = new PartialPaymentDetail();
                    ppd.setLedline(line);
                    ppd.setPplineno(0l);
                    getPaymentDetails().add(ppd);
                } else {
                    for(PartialPaymentDetail ppd: line.getPpdetails()){
                        getPaymentDetails().add(ppd);
                    }
                }
            }
        }
    }
    
    public static AccDoc clone(AccDoc doc){
        AccDoc newdoc = new AccDoc();
        newdoc.setBranch(doc.getBranch());
        newdoc.setBusdocinfo(doc.getBusdocinfo());
        newdoc.setCreatedon(doc.getCreatedon());
        newdoc.setCurrency(doc.getCurrency());
        newdoc.setDescription(doc.getDescription());
        newdoc.setDocdate(doc.getDocdate());
        newdoc.setEmp(doc.getEmp());
        newdoc.setManualno(doc.getManualno());
        newdoc.setExtra1(doc.getExtra1());
        newdoc.setExtra2(doc.getExtra2());
        newdoc.setExtra3(doc.getExtra3());
        newdoc.setExtra4(doc.getExtra4());
        newdoc.setExtra5(doc.getExtra5());
        newdoc.setExtra6(doc.getExtra6());
        newdoc.setExtra7(doc.getExtra7());
        newdoc.setExtra8(doc.getExtra8());
        newdoc.setExtra9(doc.getExtra9());
        newdoc.setExtra10(doc.getExtra10());
        newdoc.setExtra11(doc.getExtra11());
        newdoc.setExtra12(doc.getExtra12());
        
        for(LedgerLine line: doc.getLedlines()){
            newdoc.addLedline(LedgerLine.clone(line));
        }
        return newdoc;
    }
    
}
