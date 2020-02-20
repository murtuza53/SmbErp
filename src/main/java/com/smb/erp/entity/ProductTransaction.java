package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String prodtransid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ceatedon;

    private String code;

    private double commission;

    private double commissionpercentage;

    private double cost;

    private String customizedname;

    private double discount;

    private double discountpercentage;

    private double executedqty;

    private double fcunitprice;

    private double linecost;

    private double linefcunitprice;

    private double linereceived;

    private double linesold;

    private double lineunitprice;

    private double location;

    private double received;

    private double sold;

    private String transactiontype;

    private double unitprice;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;

    @ManyToOne
    @JoinColumn(name = "busdocno")
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

    //bi-directional many-to-one association to ProductTransactionExecutedFrom
    @OneToMany(mappedBy = "prodtransaction")
    private List<ProductTransactionExecutedFrom> prodtransexecutedfroms;

    //bi-directional many-to-one association to ProductTransactionExecutedTo
    @OneToMany(mappedBy = "prodtransaction")
    private List<ProductTransactionExecutedTo> prodtransexecutedtos;

    public ProductTransaction() {
    }

    public String getProdtransid() {
        return this.prodtransid;
    }

    public void setProdtransid(String prodtransid) {
        this.prodtransid = prodtransid;
    }

    public Date getCeatedon() {
        return this.ceatedon;
    }

    public void setCeatedon(Date ceatedon) {
        this.ceatedon = ceatedon;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getCommission() {
        return this.commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getCommissionpercentage() {
        return this.commissionpercentage;
    }

    public void setCommissionpercentage(double commissionpercentage) {
        this.commissionpercentage = commissionpercentage;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCustomizedname() {
        return this.customizedname;
    }

    public void setCustomizedname(String customizedname) {
        this.customizedname = customizedname;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscountpercentage() {
        return this.discountpercentage;
    }

    public void setDiscountpercentage(double discountpercentage) {
        this.discountpercentage = discountpercentage;
    }

    public double getExecutedqty() {
        return this.executedqty;
    }

    public void setExecutedqty(double executedqty) {
        this.executedqty = executedqty;
    }

    public double getFcunitprice() {
        return this.fcunitprice;
    }

    public void setFcunitprice(double fcunitprice) {
        this.fcunitprice = fcunitprice;
    }

    public double getLinecost() {
        return this.linecost;
    }

    public void setLinecost(double linecost) {
        this.linecost = linecost;
    }

    public double getLinefcunitprice() {
        return this.linefcunitprice;
    }

    public void setLinefcunitprice(double linefcunitprice) {
        this.linefcunitprice = linefcunitprice;
    }

    public double getLinereceived() {
        return this.linereceived;
    }

    public void setLinereceived(double linereceived) {
        this.linereceived = linereceived;
    }

    public double getLinesold() {
        return this.linesold;
    }

    public void setLinesold(double linesold) {
        this.linesold = linesold;
    }

    public double getLineunitprice() {
        return this.lineunitprice;
    }

    public void setLineunitprice(double lineunitprice) {
        this.lineunitprice = lineunitprice;
    }

    public double getLocation() {
        return this.location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public double getReceived() {
        return this.received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public double getSold() {
        return this.sold;
    }

    public void setSold(double sold) {
        this.sold = sold;
    }

    public String getTransactiontype() {
        return this.transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public double getUnitprice() {
        return this.unitprice;
    }

    public void setUnitprice(double unitprice) {
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

    public List<ProductTransactionExecutedFrom> getProdtransexecutedfroms() {
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
        return "ProductTransaction{" + "prodtransid=" + prodtransid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.prodtransid);
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

}
