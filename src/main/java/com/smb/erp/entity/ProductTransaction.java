package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the prodtransaction database table.
 *
 */
@Entity
@Table(name = "prodtransaction")
@NamedQuery(name = "ProductTransaction.findAll", query = "SELECT p FROM ProductTransaction p")
public class ProductTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prodtransid")
    private Long prodtransid = ThreadLocalRandom.current().nextLong();

    @Temporal(TemporalType.TIMESTAMP)
    private Date transdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String code;

    private Double commission = 0.0;

    private Double commissionpercentage = 0.0;

    private Double cost = 0.0;

    private Double fccost = 0.0;

    private String customizedname;

    private Double discount = 0.0;

    private Double fcdiscount = 0.0;

    private Double discountpercentage = 0.0;

    private Double executedqty = 0.0;

    private Double fcunitprice = 0.0;

    private Double linecost = 0.0;

    private Double linefccost = 0.0;

    private Double linefcunitprice = 0.0;

    private Double linereceived = 0.0;

    private Double linesold = 0.0;

    private Double lineunitprice = 0.0;

    private String location;

    private Double received = 0.0;

    private Double sold = 0.0;

    private String transactiontype;

    //private String accounttype;
    private Double unitprice = 0.0;

    private Double vatamount = 0.0;

    private Double exchangerate = 1.0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "docno")
    private BusDoc busdoc;

    //bi-directional many-to-one association to Unit
    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    //bi-directional many-to-one association to Warehouse
    @ManyToOne
    @JoinColumn(name = "branchid")
    private Branch branch;

    //bi-directional many-to-one association to Product
    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "vatcategoryid")
    private VatCategory vatcategoryid;

    @ManyToOne
    @JoinColumn(name = "vatsptypeid")
    private VatSalesPurchaseType vatsptypeid;

    @OneToMany(mappedBy = "fromprodtransid", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductTransactionExecution> fromprodtransaction;

    @OneToMany(mappedBy = "toprodtransid", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductTransactionExecution> toprodtransaction;

    @Transient
    private Double lineqty = 0.0;

    @Transient
    private Double balance = 0.0;

    @Transient
    private String reference;

    @Transient
    private Double cumulative = 0.0;

    @Transient
    private Double fccumulative = 0.0;

    @Transient
    private Double lineSubtotal;

    @Transient
    private Double fcLineSubtotal;

    @Transient
    private Double subtotal;

    @Transient
    private Double fcSubtotal;

    @Transient
    private Double totalamount;

    @Transient
    private Double fcTotalamount;

    @Transient
    private Double grandtotal;

    @Transient
    private Double fcGrandtotal;

    @Transient
    private Double totalcost;

    @Transient
    private Double fcTotalcost;

    @Transient
    String vatPercentage;

    @Transient
    private String partnerId;

    @Transient
    private Double qty = 1.0;

    @Transient
    private String extra1;

    @Transient
    private String extra2;

    @Transient
    private String extra3;

    public ProductTransaction() {
    }

    public Long getProdtransid() {
        return this.prodtransid;
    }

    public void setProdtransid(Long prodtransid) {
        this.prodtransid = prodtransid;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCommission() {
        return this.commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommissionpercentage() {
        return this.commissionpercentage;
    }

    public void setCommissionpercentage(Double commissionpercentage) {
        this.commissionpercentage = commissionpercentage;
    }

    public Double getCost() {
        return this.cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
        //System.out.println(getProduct() + " => " + cost);
    }

    public String getCustomizedname() {
        return this.customizedname;
    }

    public void setCustomizedname(String customizedname) {
        this.customizedname = customizedname;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
        refreshTotals();
    }

    public Double getDiscountpercentage() {
        return this.discountpercentage;
    }

    public void setDiscountpercentage(Double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public Double getExecutedqty() {
        //System.out.println("getExecutedqty: " + getProdtransid() + " => " + getProduct() + " => " + getFromprodtransaction());
        executedqty = 0.0;
        if (getFromprodtransaction() != null) {
            for (ProductTransactionExecution pt : getFromprodtransaction()) {
                executedqty = executedqty + pt.getExecutionqty();
            }
        }
        return executedqty;
    }

    public void setExecutedqty(Double executedqty) {
        this.executedqty = executedqty;
    }

    public Double getFcunitprice() {
        return this.fcunitprice;
    }

    public void setFcunitprice(Double fcunitprice) {
        this.fcunitprice = fcunitprice;
    }

    public Double getLinecost() {
        return this.linecost;
    }

    public void setLinecost(Double linecost) {
        this.linecost = linecost;
    }

    public Double getLinefcunitprice() {
        return this.linefcunitprice;
    }

    public void setLinefcunitprice(Double linefcunitprice) {
        this.linefcunitprice = linefcunitprice;
        this.lineunitprice = linefcunitprice * exchangerate;
    }

    public Double getLinereceived() {
        return this.linereceived;
    }

    public void setLinereceived(Double linereceived) {
        this.linereceived = linereceived;
        //this.received = linereceived;
    }

    public Double getLinesold() {
        return this.linesold;
    }

    public void setLinesold(Double linesold) {
        this.linesold = linesold;
        //this.sold = linesold;
    }

    public Double getLineunitprice() {
        return this.lineunitprice;
    }

    public void setLineunitprice(Double lineunitprice) {
        this.lineunitprice = lineunitprice;
        refreshTotals();
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getReceived() {
        return this.received;
    }

    public void setReceived(Double received) {
        this.received = received;
    }

    public Double getSold() {
        return this.sold;
    }

    public void setSold(Double sold) {
        this.sold = sold;
    }

    public String getTransactiontype() {
        return this.transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public Double getUnitprice() {
        return this.unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public Date getUpdatedon() {
        return this.updatedon;
    }

    public void setUpdatedon(Date updatedon) {
        this.updatedon = updatedon;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /*public List<ProductTransactionExecutedFrom> getProdtransexecutedfroms() {
        return this.prodtransexecutedfroms;
    }

    public void setProdtransexecutedfroms(List<ProductTransactionExecutedFrom> prodtransexecutedfroms) {
        this.prodtransexecutedfroms = prodtransexecutedfroms;
    }

    public ProductTransactionExecutedFrom addProdtransexecutedfrom(ProductTransactionExecutedFrom prodtransexecutedfrom) {
        getProdtransexecutedfroms().add(prodtransexecutedfrom);
        prodtransexecutedfrom.setProdtransaction(this);

        return prodtransexecutedfrom;
    }

    public ProductTransactionExecutedFrom removeProdtransexecutedfrom(ProductTransactionExecutedFrom prodtransexecutedfrom) {
        getProdtransexecutedfroms().remove(prodtransexecutedfrom);
        prodtransexecutedfrom.setProdtransaction(null);

        return prodtransexecutedfrom;
    }

    public List<ProductTransactionExecutedTo> getProdtransexecutedtos() {
        return this.prodtransexecutedtos;
    }

    public void setProdtransexecutedtos(List<ProductTransactionExecutedTo> prodtransexecutedtos) {
        this.prodtransexecutedtos = prodtransexecutedtos;
    }

    public ProductTransactionExecutedTo addProdtransexecutedto(ProductTransactionExecutedTo prodtransexecutedto) {
        getProdtransexecutedtos().add(prodtransexecutedto);
        prodtransexecutedto.setProdtransaction(this);

        return prodtransexecutedto;
    }

    public ProductTransactionExecutedTo removeProdtransexecutedto(ProductTransactionExecutedTo prodtransexecutedto) {
        getProdtransexecutedtos().remove(prodtransexecutedto);
        prodtransexecutedto.setProdtransaction(null);

        return prodtransexecutedto;
    }
     */
    /**
     * @return the busdoc
     */
    public BusDoc getBusdoc() {
        return busdoc;
    }

    /**
     * @param busdoc the busdoc to set
     */
    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }

    @Override
    public String toString() {
        return prodtransid + "\t" + busdoc + "\t" + product + "\t" + getReceived() + "\t" + getSold();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.prodtransid);
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
        final ProductTransaction other = (ProductTransaction) obj;
        if (!Objects.equals(this.prodtransid, other.prodtransid)) {
            return false;
        }
        return true;
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

    public Double getLineSubtotal() {
        return getLineqty() * getLineunitprice();
    }

    public Double getFcLineSubtotal() {
        return getLineqty() * getLinefcunitprice();
    }

    public Double getSubtotal() {
        return (getSold() + getReceived()) * getLineunitprice();
    }

    public Double getFcSubtotal() {
        return (getSold() + getReceived()) * getLinefcunitprice();
    }

    public Double getTotalamount() {
        return getLineSubtotal() - getDiscount() * getExchangerate();
    }

    public Double getFcTotalamount() {
        return getFcLineSubtotal() - getFcdiscount();
    }

    public Double getGrandtotal() {
        return getTotalamount() + getVatamount() * getExchangerate();
    }

    public Double getFcGrandtotal() {
        return getFcTotalamount() + getVatamount();
    }

    public Double getTotalcost() {
        //if (getCost() == 0) {
        //    return getSubtotal();
        //}
        //return getLinefcunitprice() * getCost();
        return (getSold() + getReceived()) * getCost();
    }

    public Double getFcTotalcost() {
        //if (getCost() == 0) {
        //    return getSubtotal();
        //}
        //return getLinefcunitprice() * getCost();
        return (getSold() + getReceived()) * getLinecost() * getExchangerate();
    }

    /**
     * @return the lineqty
     */
    public Double getLineqty() {
        if (lineqty == 0.0) {
            lineqty = linesold + linereceived;
        }
        return lineqty;
    }

    /**
     * @param lineqty the lineqty to set
     */
    public void setLineqty(Double lineqty) {
        this.lineqty = lineqty;
        refreshTotals();
    }

    public void refreshTotals() {
        calculateActualQtyFromLineQty();

        if (getFcdiscount() > 0 && getLineqty() > 0) {
            setFcunitprice(getLinefcunitprice() - getFcdiscount() / getLineqty());
        } else {
            setFcunitprice(getLinefcunitprice());
        }
        setUnitprice(getFcunitprice() * getExchangerate());
        if (getBusdoc() != null) {
            if (getBusdoc().getBusdocinfo().getPrefix().equalsIgnoreCase("STX")) {
                vatamount = 0.0;
                return;
            }
        }
        if (getVatcategoryid() != null) {
            vatamount = getVatcategoryid().getVatpercentage() * 0.01 * getFcTotalamount();
        } else if (product.getVatregisterid() != null) {
            vatamount = product.getVatregisterid().getVatcategoryid().getVatpercentage() * 0.01 * getFcTotalamount();
            //System.out.println(product.getVatregisterid() + "VatAmount: " + getVatamount());
        } else {
            vatamount = 0.0;
            //System.out.println(product.getVatregisterid() + "VatAmount: " + getVatamount());
        }

        if (getFcdiscount() > 0) {
            discount = getFcdiscount() * getExchangerate();
        }
        //if (busdoc != null) {
        //    busdoc.refreshTotal();
        //}
    }

    public void calculateActualQtyFromLineQty() {
        if (getBusdoc() != null) {
            //if (getLinecost() > 0) {
            //    setCost(getLinecost());
            //}
            if (getBusdoc().getBusdocinfo().getPrefix().equalsIgnoreCase("STX") || getBusdoc().getBusdocinfo().getPrefix().equalsIgnoreCase("SCP")) {
                return; //do nothing
            }
            if (getBusdoc().getBusdocinfo().getDoctype().equalsIgnoreCase("SALES") || getBusdoc().getBusdocinfo().getPrefix().equalsIgnoreCase("SHS")) {
                setLinesold(lineqty);
                setSold(getLinesold());
                setLinereceived(0.0);
                setReceived(0.0);
            } else {        //Purchase or EXS
                setLinereceived(lineqty);
                setReceived(getLinereceived());
                setLinesold(0.0);
                setSold(0.0);
            }
        }
    }

    public void refreshExecutedQty() {
        System.out.println("refreshExecutedQty: " + getProdtransid() + " => " + getProduct() + " => " + getFromprodtransaction());
        if (getFromprodtransaction() == null || getFromprodtransaction().size() == 0) {
            return;
        }
        double total = 0.0;
        for (ProductTransactionExecution pt : getFromprodtransaction()) {
            total = total + pt.getExecutionqty();
        }
        setExecutedqty(total);
    }

    public String getVatPercentage() {
        return getVatcategoryid().getVatpercentage().intValue() + "%";
    }

    public void setVatPercentage(String perString) {
        this.vatPercentage = perString;
    }

    /**
     * @return the vatamount
     */
    public Double getVatamount() {
        //System.out.println("getVatamount: " + vatamount);
        return vatamount;
    }

    /**
     * @param vatamount the vatamount to set
     */
    public void setVatamount(Double vatamount) {
        this.vatamount = vatamount;
    }

    /**
     * @return the vatcategoryid
     */
    public VatCategory getVatcategoryid() {
        return vatcategoryid;
    }

    /**
     * @param vatcategoryid the vatcategoryid to set
     */
    public void setVatcategoryid(VatCategory vatcategoryid) {
        this.vatcategoryid = vatcategoryid;
    }

    /**
     * @return the transdate
     */
    public Date getTransdate() {
        return transdate;
    }

    /**
     * @param transdate the transdate to set
     */
    public void setTransdate(Date transdate) {
        this.transdate = transdate;
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
     * @return the exchangerate
     */
    public Double getExchangerate() {
        return exchangerate;
    }

    /**
     * @param exchangerate the exchangerate to set
     */
    public void setExchangerate(Double exchangerate) {
        this.exchangerate = exchangerate;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the balance
     */
    public Double getBalance() {
        return getLineqty() - getExecutedqty();
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * @return the vatsptypeid
     */
    public VatSalesPurchaseType getVatsptypeid() {
        return vatsptypeid;
    }

    /**
     * @param vatsptypeid the vatsptypeid to set
     */
    public void setVatsptypeid(VatSalesPurchaseType vatsptypeid) {
        this.vatsptypeid = vatsptypeid;
    }

    /**
     * @return the fromprodtransaction
     */
    public List<ProductTransactionExecution> getFromprodtransaction() {
        return fromprodtransaction;
    }

    /**
     * @param fromprodtransaction the fromprodtransaction to set
     */
    public void setFromprodtransaction(List<ProductTransactionExecution> fromprodtransaction) {
        this.fromprodtransaction = fromprodtransaction;
    }

    public String getFromProdTranstionDocNo() {
        if (fromprodtransaction == null || fromprodtransaction.isEmpty()) {
            return "";
        }
        return fromprodtransaction.get(0).getFromprodtransid().getBusdoc().getDocno();
    }

    public void addFromprodtransaction(ProductTransactionExecution pt) {
        if (fromprodtransaction == null) {
            fromprodtransaction = new LinkedList();
        }
        fromprodtransaction.add(pt);
    }

    public ProductTransactionExecution removeFromprodtransaction(ProductTransactionExecution pt) {
        fromprodtransaction.remove(pt);
        pt.setFromprodtransid(null);
        pt.setToprodtransid(null);

        return pt;
    }

    public void removeAllFromprodtransaction() {
        if (fromprodtransaction != null) {
            for (ProductTransactionExecution pte : fromprodtransaction) {
                pte.setFromprodtransid(null);
                pte.setToprodtransid(null);
            }
            fromprodtransaction.clear();
        }
    }

    public String getToProdTranstionDocNo() {
        if (toprodtransaction == null || toprodtransaction.isEmpty()) {
            return "";
        }
        return toprodtransaction.get(0).getFromprodtransid().getBusdoc().getDocno();
    }

    public void addToprodtransaction(ProductTransactionExecution pt) {
        if (toprodtransaction == null) {
            toprodtransaction = new LinkedList();
        }
        toprodtransaction.add(pt);
    }

    public ProductTransactionExecution removeToprodtransaction(ProductTransactionExecution pt) {
        toprodtransaction.remove(pt);
        pt.setFromprodtransid(null);
        pt.setToprodtransid(null);

        return pt;
    }

    public void removeAllToprodtransaction() {
        if (toprodtransaction != null) {
            for (ProductTransactionExecution pte : toprodtransaction) {
                pte.setFromprodtransid(null);
                pte.setToprodtransid(null);
            }
            toprodtransaction.clear();
        }
    }

    /**
     * @return the toprodtransaction
     */
    public List<ProductTransactionExecution> getToprodtransaction() {
        return toprodtransaction;
    }

    /**
     * @param toprodtransaction the toprodtransaction to set
     */
    public void setToprodtransaction(List<ProductTransactionExecution> toprodtransaction) {
        this.toprodtransaction = toprodtransaction;
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
     * @return the fccost
     */
    public Double getFccost() {
        return fccost;
    }

    /**
     * @param fccost the fccost to set
     */
    public void setFccost(Double fccost) {
        this.fccost = fccost;
        //this.cost = this.fccost * exchangerate;
    }

    /**
     * @return the fcdiscount
     */
    public Double getFcdiscount() {
        return fcdiscount;
    }

    /**
     * @param fcdiscount the fcdiscount to set
     */
    public void setFcdiscount(Double fcdiscount) {
        this.fcdiscount = fcdiscount;
        this.discount = this.fcdiscount * exchangerate;
    }

    /**
     * @return the linefccost
     */
    public Double getLinefccost() {
        return linefccost;
    }

    /**
     * @param linefccost the linefccost to set
     */
    public void setLinefccost(Double linefccost) {
        this.linefccost = linefccost;
        //this.linecost = this.linefccost * exchangerate;
    }

    public String getCostingGroupBy() {
        //"#{pt.busdoc.docno} - #{pt.toProdTranstionDocNo} - #{pt.busdoc.businesspartner.companyname}"
        return getBusdoc().getDocno() + " - " + getToProdTranstionDocNo() + " - " + getBusdoc().getBusinesspartner().getCompanyname();
    }

    /**
     * @return the partnerId
     */
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * @param partnerId the partnerId to set
     */
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * @return the qty
     */
    public Double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return the extra1
     */
    public String getExtra1() {
        return extra1;
    }

    /**
     * @param extra1 the extra1 to set
     */
    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    /**
     * @return the extra2
     */
    public String getExtra2() {
        return extra2;
    }

    /**
     * @param extra2 the extra2 to set
     */
    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    /**
     * @return the extra3
     */
    public String getExtra3() {
        return extra3;
    }

    /**
     * @param extra3 the extra3 to set
     */
    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public List<String> getFromDocs() {
        List<String> list = new LinkedList();
        if (fromprodtransaction != null) {
            for (ProductTransactionExecution pte : fromprodtransaction) {
                //build.append(pte.getFromprodtransid().getBusdoc().getDocno()).append(" ");
                //System.out.println("PTE_getFromDocs: " + pte);
                if (pte.getFromprodtransid() != null) {
                    list.add(pte.getFromprodtransid().getBusdoc().getDocno());
                }
            }
        }
        return list.stream().distinct().collect(Collectors.toList());
        /*if (fromprodtransaction == null || fromprodtransaction.isEmpty()) {
            return list;
        }
        for (int i = 0; i < fromprodtransaction.size(); i++) {
            list.add(fromprodtransaction.get(i).getFromprodtransid().getBusdoc().getDocno());
        }
        return list.stream().distinct().collect(Collectors.toList());*/
    }

    public List<String> getToDocs() {
        List<String> list = new LinkedList();
        if (fromprodtransaction != null) {
            for (ProductTransactionExecution pte : fromprodtransaction) {
                //build.append(pte.getToprodtransid().getBusdoc().getDocno()).append(" ");
                //System.out.println("PTE_getToDocs: " + pte);
                if (pte.getToprodtransid() != null) {
                    list.add(pte.getToprodtransid().getBusdoc().getDocno());
                }
            }
        }
        return list.stream().distinct().collect(Collectors.toList());
        /*if (fromprodtransaction == null || fromprodtransaction.isEmpty()) {
            return list;
        }
        for(int i=0; i<fromprodtransaction.size(); i++){
            list.add(fromprodtransaction.get(i).getToprodtransid().getBusdoc().getDocno());
        }
        return list.stream().distinct().collect(Collectors.toList());*/
    }

    public boolean hasConversion() {
        //return getFromDocs().isEmpty() || getToDocs().isEmpty();

        return getFromProdTranstionDocNo().length() > 0 || getToProdTranstionDocNo().length() > 0;
    }
}
