package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the product database table.
 *
 */
@Entity
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p ORDER BY p.productname")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long productid;

    private String barcode1;

    private String barcode2;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String description;

    private boolean inactive;

    private double packqty;

    private String partnumber;

    private String productname;

    private double reorderlevel;

    private String stockid;

    private String supplierscode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;

    //bi-directional many-to-one association to PriceList
    @OneToMany(mappedBy = "product")
    private List<PriceList> pricelists;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "prodaccountid")
    private ProductAccount prodaccount;

    //bi-directional many-to-one association to ProductTransaction
    @OneToMany(mappedBy = "product")
    private List<ProductTransaction> prodtransactions;

    //bi-directional many-to-one association to Brand
    @ManyToOne
    @JoinColumn(name = "brandid")
    private Brand brand;

    //bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "countryid")
    private Country country;

    //bi-directional many-to-one association to ProductCategry
    @ManyToOne
    @JoinColumn(name = "prodcatid")
    private ProductCategory prodcategory;

    //bi-directional many-to-one association to Unit
    @ManyToOne
    @JoinColumn(name = "punitid")
    private Unit punit;

    //bi-directional many-to-one association to Unit
    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    //bi-directional many-to-one association to ProductType
    @ManyToOne
    @JoinColumn(name = "producttypeid")
    private ProductType producttype;

    public Product() {
    }

    public Long getProductid() {
        return this.productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getBarcode1() {
        return this.barcode1;
    }

    public void setBarcode1(String barcode1) {
        this.barcode1 = barcode1;
    }

    public String getBarcode2() {
        return this.barcode2;
    }

    public void setBarcode2(String barcode2) {
        this.barcode2 = barcode2;
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

    public boolean getInactive() {
        return this.inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public double getPackqty() {
        return this.packqty;
    }

    public void setPackqty(double packqty) {
        this.packqty = packqty;
    }

    public String getPartnumber() {
        return this.partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getProductname() {
        return this.productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getReorderlevel() {
        return this.reorderlevel;
    }

    public void setReorderlevel(double reorderlevel) {
        this.reorderlevel = reorderlevel;
    }

    public String getStockid() {
        return this.stockid;
    }

    public void setStockid(String stockid) {
        this.stockid = stockid;
    }

    public String getSupplierscode() {
        return this.supplierscode;
    }

    public void setSupplierscode(String supplierscode) {
        this.supplierscode = supplierscode;
    }

    public Date getUpdatedon() {
        return this.updatedon;
    }

    public void setUpdatedon(Date updatedon) {
        this.updatedon = updatedon;
    }

    public List<PriceList> getPricelists() {
        return this.pricelists;
    }

    public void setPricelists(List<PriceList> pricelists) {
        this.pricelists = pricelists;
    }

    public PriceList addPricelist(PriceList pricelist) {
        getPricelists().add(pricelist);
        pricelist.setProduct(this);

        return pricelist;
    }

    public PriceList removePricelist(PriceList pricelist) {
        getPricelists().remove(pricelist);
        pricelist.setProduct(null);

        return pricelist;
    }

    public List<ProductTransaction> getProdtransactions() {
        return this.prodtransactions;
    }

    public void setProdtransactions(List<ProductTransaction> prodtransactions) {
        this.prodtransactions = prodtransactions;
    }

    public ProductTransaction addProdtransaction(ProductTransaction prodtransaction) {
        getProdtransactions().add(prodtransaction);
        prodtransaction.setProduct(this);

        return prodtransaction;
    }

    public ProductTransaction removeProdtransaction(ProductTransaction prodtransaction) {
        getProdtransactions().remove(prodtransaction);
        prodtransaction.setProduct(null);

        return prodtransaction;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ProductCategory getProdcategory() {
        return this.prodcategory;
    }

    public void setProdcategory(ProductCategory prodcategory) {
        this.prodcategory = prodcategory;
    }

    public ProductType getProducttype() {
        return this.producttype;
    }

    public void setProducttype(ProductType producttype) {
        this.producttype = producttype;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.productid);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productid, other.productid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return productname + " [" + productid  + "]";
    }

    /**
     * @return the punit
     */
    public Unit getPunit() {
        return punit;
    }

    /**
     * @param punit the punit to set
     */
    public void setPunit(Unit punit) {
        this.punit = punit;
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * @return the prodaccount
     */
    public ProductAccount getProdaccount() {
        return prodaccount;
    }

    /**
     * @param prodaccount the prodaccount to set
     */
    public void setProdaccount(ProductAccount prodaccount) {
        this.prodaccount = prodaccount;
    }

}
