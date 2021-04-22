package com.smb.erp.entity;

import com.smb.erp.util.DateUtil;
import com.smb.erp.util.Speller;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the busdoc database table.
 *
 */
@Entity
@Table(name = "busdoc")
@NamedQuery(name = "BusDoc.findAll", query = "SELECT b FROM BusDoc b")
public class BusDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String docno;

    @Temporal(TemporalType.TIMESTAMP)
    private Date docdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;

    private Double rate = 1.0;

    private String description;

    private String extra1;

    private String extra2;

    private String extra3;

    private String extra4;

    private String extra5;

    private String extra6;

    private String extra7;

    private String extra8;

    private String extra9;

    private String extra10;

    private Double extra11 = 0.0;

    private Double extra12 = 0.0;

    private Double extra13 = 0.0;

    private Double extra14 = 0.0;

    private Double extra15 = 0.0;

    private Double extra16 = 0.0;

    private Double extra17 = 0.0;

    private Double extra18 = 0.0;

    private Double extra19 = 0.0;

    private Double extra20 = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra21;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra22;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra23;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra24;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra25;

    private String manualdocno;

    private boolean markdeleted;

    private String refno;

    private String lpono;

    private Double subtotal = 0.0;

    private Double discount = 0.0;

    private Double roundoff = 0.0;

    private Double totalamount = 0.0;

    private Double totalvat = 0.0;

    private Double grandtotal = 0.0;

    private Double totalcost = 0.0;

    private String docstatus = "";

    //bi-directional many-to-one association to ContactPerson
    @ManyToOne
    @JoinColumn(name = "cpid")
    private ContactPerson contactperson;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "empid")
    private Emp emp1;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "inaccountid")
    private Emp emp2;

    //bi-directional many-to-one association to BusDoc
    @ManyToOne
    @JoinColumn(name = "fromdocno")
    private BusDoc busdoc;

    //bi-directional many-to-one association to BusDoc
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "paytermsid")
    private PayTerms paytermsid;

    //bi-directional many-to-one association to BusDoc
    //@OneToMany(mappedBy="busdoc")
    //private List<BusDoc> busdocs;
    @OneToMany(mappedBy = "busdoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductTransaction> productTransactions;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne
    @JoinColumn(name = "busdocinfoid")
    private BusDocInfo busdocinfo;

    //bi-directional many-to-one association to BusinessPartner
    @ManyToOne
    @JoinColumn(name = "partnerid")
    private BusinessPartner businesspartner;

    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "currencyno")
    private Country currency;

    //bi-directional many-to-one association to PartialPaymentDetail
    @OneToMany(mappedBy = "busdoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<BusDocExpense> expenses;

    //bi-directional many-to-one association to PartialPaymentDetail
    @OneToMany(mappedBy = "busdoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<PartialPaymentDetail> ppdetails;

    @Transient
    private List<Account> accounts;

    @Transient
    private Double executedQty;

    @Transient
    private Double totalQty;

    @Transient
    private String titleBadge;

    @Transient
    private String statusCss;

    @Transient
    private Double totalPaid;

    @Transient
    private Double totalPending;

    @Transient
    private Integer invoiceAge;

    @Transient
    private Double cumulative;

    @Transient
    private Double lineTotalDiscount;

    @Transient
    private Double totalDiscount;

    @Transient
    private String spelledAmountInWords;

    @Transient
    private PrintReport currentPrintReport;

    public BusDoc() {
    }

    public String getDocno() {
        return this.docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public Date getCreatedon() {
        return this.createdon;
    }

    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra1() {
        return this.extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
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

    public String getExtra6() {
        return this.extra6;
    }

    public void setExtra6(String extra6) {
        this.extra6 = extra6;
    }

    public String getExtra7() {
        return this.extra7;
    }

    public void setExtra7(String extra7) {
        this.extra7 = extra7;
    }

    public String getExtra8() {
        return this.extra8;
    }

    public void setExtra8(String extra8) {
        this.extra8 = extra8;
    }

    public String getExtra9() {
        return this.extra9;
    }

    public void setExtra9(String extra9) {
        this.extra9 = extra9;
    }

    public String getExtra10() {
        return this.extra10;
    }

    public void setExtra10(String extra10) {
        this.extra10 = extra10;
    }

    /**
     * @return the extra11
     */
    public Double getExtra11() {
        return extra11;
    }

    /**
     * @param extra11 the extra11 to set
     */
    public void setExtra11(Double extra11) {
        this.extra11 = extra11;
    }

    /**
     * @return the extra12
     */
    public Double getExtra12() {
        return extra12;
    }

    /**
     * @param extra12 the extra12 to set
     */
    public void setExtra12(Double extra12) {
        this.extra12 = extra12;
    }

    /**
     * @return the extra13
     */
    public Double getExtra13() {
        return extra13;
    }

    /**
     * @param extra13 the extra13 to set
     */
    public void setExtra13(Double extra13) {
        this.extra13 = extra13;
    }

    /**
     * @return the extra14
     */
    public Double getExtra14() {
        return extra14;
    }

    /**
     * @param extra14 the extra14 to set
     */
    public void setExtra14(Double extra14) {
        this.extra14 = extra14;
    }

    /**
     * @return the extra15
     */
    public Double getExtra15() {
        return extra15;
    }

    /**
     * @param extra15 the extra15 to set
     */
    public void setExtra15(Double extra15) {
        this.extra15 = extra15;
    }

    /**
     * @return the extra16
     */
    public Double getExtra16() {
        return extra16;
    }

    /**
     * @param extra16 the extra16 to set
     */
    public void setExtra16(Double extra16) {
        this.extra16 = extra16;
    }

    /**
     * @return the extra17
     */
    public Double getExtra17() {
        return extra17;
    }

    /**
     * @param extra17 the extra17 to set
     */
    public void setExtra17(Double extra17) {
        this.extra17 = extra17;
    }

    /**
     * @return the extra18
     */
    public Double getExtra18() {
        return extra18;
    }

    /**
     * @param extra18 the extra18 to set
     */
    public void setExtra18(Double extra18) {
        this.extra18 = extra18;
    }

    /**
     * @return the extra19
     */
    public Double getExtra19() {
        return extra19;
    }

    /**
     * @param extra19 the extra19 to set
     */
    public void setExtra19(Double extra19) {
        this.extra19 = extra19;
    }

    /**
     * @return the extra20
     */
    public Double getExtra20() {
        return extra20;
    }

    /**
     * @param extra20 the extra20 to set
     */
    public void setExtra20(Double extra20) {
        this.extra20 = extra20;
    }

    /**
     * @return the extra21
     */
    public Date getExtra21() {
        return extra21;
    }

    /**
     * @param extra21 the extra21 to set
     */
    public void setExtra21(Date extra21) {
        this.extra21 = extra21;
    }

    /**
     * @return the extra22
     */
    public Date getExtra22() {
        return extra22;
    }

    /**
     * @param extra22 the extra22 to set
     */
    public void setExtra22(Date extra22) {
        this.extra22 = extra22;
    }

    /**
     * @return the extra23
     */
    public Date getExtra23() {
        return extra23;
    }

    /**
     * @param extra23 the extra23 to set
     */
    public void setExtra23(Date extra23) {
        this.extra23 = extra23;
    }

    /**
     * @return the extra24
     */
    public Date getExtra24() {
        return extra24;
    }

    /**
     * @param extra24 the extra24 to set
     */
    public void setExtra24(Date extra24) {
        this.extra24 = extra24;
    }

    /**
     * @return the extra25
     */
    public Date getExtra25() {
        return extra25;
    }

    /**
     * @param extra25 the extra25 to set
     */
    public void setExtra25(Date extra25) {
        this.extra25 = extra25;
    }

    public String getManualdocno() {
        return this.manualdocno;
    }

    public void setManualdocno(String manualdocno) {
        this.manualdocno = manualdocno;
    }

    public boolean getMarkdeleted() {
        return this.markdeleted;
    }

    public void setMarkdeleted(boolean markdeleted) {
        this.markdeleted = markdeleted;
    }

    public String getRefno() {
        return this.refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public Date getUpdatedon() {
        return this.updatedon;
    }

    public void setUpdatedon(Date updatedon) {
        this.updatedon = updatedon;
    }

    public ContactPerson getContactperson() {
        return this.contactperson;
    }

    public void setContactperson(ContactPerson contactperson) {
        this.contactperson = contactperson;
    }

    public Emp getEmp1() {
        return this.emp1;
    }

    public void setEmp1(Emp emp1) {
        this.emp1 = emp1;
    }

    public Emp getEmp2() {
        return this.emp2;
    }

    public void setEmp2(Emp emp2) {
        this.emp2 = emp2;
    }

    public BusDoc getBusdoc() {
        return this.busdoc;
    }

    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }

    public BusDocInfo getBusdocinfo() {
        return this.busdocinfo;
    }

    public void setBusdocinfo(BusDocInfo busdocinfo) {
        this.busdocinfo = busdocinfo;
    }

    /**
     * @return the paytermsid
     */
    public PayTerms getPaytermsid() {
        return paytermsid;
    }

    /**
     * @param paytermsid the paytermsid to set
     */
    public void setPaytermsid(PayTerms paytermsid) {
        this.paytermsid = paytermsid;
    }

    public BusinessPartner getBusinesspartner() {
        return this.businesspartner;
    }

    public void setBusinesspartner(BusinessPartner businesspartner) {
        this.businesspartner = businesspartner;
    }

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return the totalvat
     */
    public Double getTotalvat() {
        return totalvat;
    }

    /**
     * @param totalvat the totalvat to set
     */
    public void setTotalvat(Double totalvat) {
        this.totalvat = totalvat;
    }

    /**
     * @return the productTransactions
     */
    public List<ProductTransaction> getProductTransactions() {
        return productTransactions;
    }

    /**
     * @param productTransactions the productTransactions to set
     */
    public void setProductTransactions(List<ProductTransaction> productTransactions) {
        this.productTransactions = productTransactions;
    }

    public ProductTransaction addProductTransactions(ProductTransaction pt) {
        if (getProductTransactions() == null) {
            setProductTransactions(new LinkedList());
        }
        getProductTransactions().add(pt);
        pt.setBusdoc(this);

        return pt;
    }

    public ProductTransaction removeProductTransactions(ProductTransaction pt) {
        getProductTransactions().remove(pt);
        pt.setBusdoc(null);

        return pt;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.docno);
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
        final BusDoc other = (BusDoc) obj;
        if (!Objects.equals(this.docno, other.docno)) {
            return false;
        }
        return true;
    }

    /*@Override
    public String toString() {
        return docno;
    }*/
    @Override
    public String toString() {
        return "BusDoc{" + "docno=" + docno + ", busdocinfo=" + busdocinfo + ", docdate=" + docdate + ", extra1=" + extra1 + ", extra2=" + extra2 + ", refno=" + refno + ", emp1=" + emp1 + ", busdocinfo=" + busdocinfo + ", businesspartner=" + businesspartner + ", branch=" + branch + ", currency=" + currency + '}';
    }

    public void refreshTotal() {
        if (getProductTransactions() != null) {
            setSubtotal((Double) getProductTransactions().stream().mapToDouble(x -> x.getFcSubtotal()).sum());
            setTotalcost((Double) getProductTransactions().stream().mapToDouble(x -> x.getFcTotalcost()).sum());
            if (getSubtotal() == 0) {
                setSubtotal((Double) getProductTransactions().stream().mapToDouble(x -> x.getFcLineSubtotal()).sum());
            }
            setTotalamount((Double) getSubtotal() - getTotalDiscount());
            totalvat = getProductTransactions().stream().mapToDouble(x -> x.getVatamount()).sum();
            //lineTotalDiscount = getProductTransactions().stream().mapToDouble(x -> x.getDiscount()).sum();
            setGrandtotal((Double) getTotalamount() + totalvat - roundoff);
        }
    }

    /**
     * @return the subtotal
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
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
     * @return the grandtotal
     */
    public Double getGrandtotal() {
        return grandtotal;
    }

    /**
     * @param grandtotal the grandtotal to set
     */
    public void setGrandtotal(Double grandtotal) {
        this.grandtotal = grandtotal;
    }

    /**
     * @return the totalcost
     */
    public Double getTotalcost() {
        return totalcost;
    }

    /**
     * @param totalcost the totalcost to set
     */
    public void setTotalcost(Double totalcost) {
        this.totalcost = totalcost;
    }

    public Double getExecutedQty() {
        Double total = 0.0;
        if (getProductTransactions() != null && getProductTransactions().size() > 0) {
            total = getProductTransactions().stream().mapToDouble(o -> o.getExecutedqty()).sum();
        }
        return total;
    }

    public Double getTotalQty() {
        Double total = 0.0;
        if (getProductTransactions() != null && getProductTransactions().size() > 0) {
            total = getProductTransactions().stream().mapToDouble(o -> o.getLineqty()).sum();
        }
        return total;
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
        System.out.println(docno + ": " + this.docdate);
    }

    public String getTitleBadge() {
        if (getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())) {
            return "status-instock";
        } else if (getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.PURCHASE.getValue())) {
            return "status-outofstock";
        }
        return "status-lowstock";
    }

    /**
     * @return the docstatus
     */
    public String getDocstatus() {
        if (docstatus.equalsIgnoreCase(DocStatus.CANCELLED.toString()) || docstatus.equalsIgnoreCase(DocStatus.RETURNED.toString())) {
            return docstatus;
        }
        if (getBusdocinfo().getAccounttype().equalsIgnoreCase(BusDocTransactionType.ACCOUNTS_RECEIVABLE.toString())
                || getBusdocinfo().getAccounttype().equalsIgnoreCase(BusDocTransactionType.ACCOUNTS_PAYABLE.toString())) {
            if (getTotalPending() == 0) {
                return DocStatus.PAID.toString();
            } else if (getTotalPaid() > 0 && getTotalPaid() < getGrandtotal()) {
                return DocStatus.PAID_PARTIAL.toString();
            }
        }
        double qty = getTotalQty();
        double exe = getExecutedQty();
        docstatus = DocStatus.PENDING.toString();
        if (exe > 0) {
            if (qty == exe) {
                return DocStatus.COMPLETED.toString();
            } else if (exe < qty) {
                return DocStatus.PARTIAL.toString();
            }
        }
        return docstatus;
    }

    public String getDocumentExecutionStatus() {
        if (docstatus.equalsIgnoreCase(DocStatus.CANCELLED.toString()) || docstatus.equalsIgnoreCase(DocStatus.RETURNED.toString())) {
            return docstatus;
        }

        double qty = getTotalQty();
        double exe = getExecutedQty();
        if (exe > 0) {
            if (qty == exe) {
                return DocStatus.COMPLETED.toString();
            } else if (exe < qty) {
                return DocStatus.PARTIAL.toString();
            }
        }
        return DocStatus.PENDING.toString();
    }

    public String getDocumentPaymentStatus() {
        if (docstatus.equalsIgnoreCase(DocStatus.CANCELLED.toString()) || docstatus.equalsIgnoreCase(DocStatus.RETURNED.toString())) {
            return docstatus;
        }
        if (getBusdocinfo().getAccounttype().equalsIgnoreCase(BusDocTransactionType.ACCOUNTS_RECEIVABLE.toString())
                || getBusdocinfo().getAccounttype().equalsIgnoreCase(BusDocTransactionType.ACCOUNTS_PAYABLE.toString())) {
            if (getTotalPending() == 0) {
                return DocStatus.PAID.toString();
            } else if (getTotalPaid() > 0 && getTotalPaid() < getGrandtotal()) {
                return DocStatus.PAID_PARTIAL.toString();
            }
        }
        return DocStatus.PENDING.toString();
    }

    /**
     * @param docstatus the docstatus to set
     */
    public void setDocstatus(String docstatus) {
        this.docstatus = docstatus;
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
        } else if (value.equalsIgnoreCase("Partial")) {
            return "proposal";
        } else if (value.equalsIgnoreCase("Completed")) {
            return "qualified";
        } else if (value.equalsIgnoreCase("Paid")) {
            return "qualified";
        } else if (value.equalsIgnoreCase("Cancelled")) {
            return "unqualified";
        } else if (value.equalsIgnoreCase("Returned")) {
            return "renewal";
        }
        return "qualified";
    }

    /**
     * @return the accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the ppdetails
     */
    public List<PartialPaymentDetail> getPpdetails() {
        return ppdetails;
    }

    /**
     * @param ppdetails the ppdetails to set
     */
    public void setPpdetails(List<PartialPaymentDetail> ppdetails) {
        this.ppdetails = ppdetails;
    }

    public PartialPaymentDetail addPpdetail(PartialPaymentDetail ppdetail) {
        if (getPpdetails() == null) {
            setPpdetails(new LinkedList());
        }
        getPpdetails().add(ppdetail);
        ppdetail.setBusdoc(this);

        return ppdetail;
    }

    public PartialPaymentDetail removePpdetail(PartialPaymentDetail ppdetail) {
        getPpdetails().remove(ppdetail);
        ppdetail.setBusdoc(null);

        return ppdetail;
    }

    public Double getTotalPaid() {
        if (busdocinfo.hasCashRegister()) {
            return getGrandtotal();
        }
        if (getPpdetails() == null || getPpdetails().isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (PartialPaymentDetail pp : getPpdetails()) {
            //don't count ppd that are not yet saved
            if (pp.getPplineno() > 0l) {
                total = total + pp.getAmount();
            }
        }
        return total;
    }

    public Double getTotalPending() {
        return getGrandtotal() - getTotalPaid();
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
     * @return the expenses
     */
    public List<BusDocExpense> getExpenses() {
        return expenses;
    }

    /**
     * @param expenses the expenses to set
     */
    public void setExpenses(List<BusDocExpense> expenses) {
        this.expenses = expenses;
    }

    public Double getExpenseLc() {
        if (getExpenses() != null) {
            return getExpenses().stream().mapToDouble(o -> o.getAmountlc()).sum();
        }
        return 0.0;
    }

    public Double getExpenseFc() {
        if (getExpenses() != null) {
            return getExpenses().stream().mapToDouble(o -> o.getAmountfc()).sum();
        }
        return 0.0;
    }

    public BusDocExpense addBusDocExpense(BusDocExpense exp) {
        if (getExpenses() == null) {
            setExpenses(new LinkedList());
        }
        getExpenses().add(exp);
        exp.setBusdoc(this);

        return exp;
    }

    public BusDocExpense removeBusDocExpense(BusDocExpense exp) {
        getExpenses().remove(exp);
        exp.setBusdoc(null);

        return exp;
    }

    public int findInvoiceAge(Date toDate) {
        if (getDocdate() != null) {
            return DateUtil.getDaysDiff(getDocdate().getTime(), toDate.getTime());
        }
        return 0;
    }

    /**
     * @return the invoiceAge
     */
    public Integer getInvoiceAge() {
        return findInvoiceAge(new Date());
    }

    /**
     * @param invoiceAge the invoiceAge to set
     */
    public void setInvoiceAge(Integer invoiceAge) {
        this.invoiceAge = invoiceAge;
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

    public String getSpelledAmountInWords() {
        return Speller.spellAmount(getCurrency().getCurrencysym(), getGrandtotal());
    }

    public void setSpelledAmountInWords(String value) {
        this.spelledAmountInWords = value;
    }

    /**
     * @return the roundoff
     */
    public Double getRoundoff() {
        return roundoff;
    }

    /**
     * @param roundoff the roundoff to set
     */
    public void setRoundoff(Double roundoff) {
        this.roundoff = roundoff;
    }

    /**
     * @return the lineTotalDiscount
     */
    public Double getLineTotalDiscount() {
        //return lineTotalDiscount;
        return getProductTransactions().stream().mapToDouble(x -> x.getDiscount()).sum();
    }

    /**
     * @param lineTotalDiscount the lineTotalDiscount to set
     */
    public void setLineTotalDiscount(Double lineTotalDiscount) {
        this.lineTotalDiscount = lineTotalDiscount;
    }

    /**
     * @return the totalDiscount
     */
    public Double getTotalDiscount() {
        return getDiscount() + getLineTotalDiscount();
    }

    /**
     * @param totalDiscount the totalDiscount to set
     */
    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
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
            return getCurrentPrintReport().getReporttitle();
        }
        return getBusdocinfo().getDocname();
    }

    public List<BusDocExpense> getForeignExpenses() {
        List<BusDocExpense> exps = new LinkedList();
        if (getExpenses() != null) {
            getExpenses().stream().filter(exp -> (!exp.getCountry().getDefcountry())).forEachOrdered(exp -> {
                exps.add(exp);
            });
        }
        return exps;
    }

    public List<BusDocExpense> getLocalExpenses() {
        List<BusDocExpense> exps = new LinkedList();
        if (getExpenses() != null) {
            getExpenses().stream().filter(exp -> (exp.getCountry().getDefcountry())).forEachOrdered(exp -> {
                exps.add(exp);
            });
        }
        return exps;
    }

    public String getToDocs() {
        List<String> list = new LinkedList();
        if (getProductTransactions() != null) {
            for (ProductTransaction pt : getProductTransactions()) {
                list.addAll(pt.getToDocs());
            }
        }
        return list.stream().distinct().collect(Collectors.toList()).toString().replaceAll("\\[|\\]", "");
    }

    public String getFromDocs() {
        List<String> list = new LinkedList();
        if (getProductTransactions() != null) {
            for (ProductTransaction pt : getProductTransactions()) {
                list.addAll(pt.getFromDocs());
            }
        }
        return list.stream().distinct().collect(Collectors.toList()).toString().replaceAll("\\[|\\]", "");
    }

    /**
     * @return the lpono
     */
    public String getLpono() {
        return lpono;
    }

    /**
     * @param lpono the lpono to set
     */
    public void setLpono(String lpono) {
        this.lpono = lpono;
    }

}
