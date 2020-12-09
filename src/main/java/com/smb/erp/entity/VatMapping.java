package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@Table(name = "vatmapping")
@NamedQuery(name = "VatMapping.findAll", query = "SELECT v FROM VatMapping v ORDER BY v.vatmappingid")
public class VatMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vatmappingid;

    private String producttype;

    private String transactiontype;

    @Temporal(TemporalType.TIMESTAMP)
    private Date wef;

    @ManyToOne
    @JoinColumn(name = "vatcategoryid")
    private VatCategory vatcategoryid;

    @ManyToOne
    @JoinColumn(name = "vataccounttypeid")
    private VatAccountType vataccounttypeid;

    @ManyToOne
    @JoinColumn(name = "vatsptypeid")
    private VatSalesPurchaseType vatsptypeid;

    @ManyToOne
    @JoinColumn(name = "accountid")
    private Account accountid;

    public VatMapping() {
    }

    /**
     * @return the vatmappingid
     */
    public Long getVatmappingid() {
        return vatmappingid;
    }

    /**
     * @param vatmappingid the vatmappingid to set
     */
    public void setVatmappingid(Long vatmappingid) {
        this.vatmappingid = vatmappingid;
    }

    /**
     * @return the producttype
     */
    public String getProducttype() {
        return producttype;
    }

    /**
     * @param producttype the producttype to set
     */
    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    /**
     * @return the transactiontype
     */
    public String getTransactiontype() {
        return transactiontype;
    }

    /**
     * @param transactiontype the transactiontype to set
     */
    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    /**
     * @return the wef
     */
    public Date getWef() {
        return wef;
    }

    /**
     * @param wef the wef to set
     */
    public void setWef(Date wef) {
        this.wef = wef;
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
     * @return the vataccounttypeid
     */
    public VatAccountType getVataccounttypeid() {
        return vataccounttypeid;
    }

    /**
     * @param vataccounttypeid the vataccounttypeid to set
     */
    public void setVataccounttypeid(VatAccountType vataccounttypeid) {
        this.vataccounttypeid = vataccounttypeid;
    }

    /**
     * @return the accountid
     */
    public Account getAccountid() {
        return accountid;
    }

    /**
     * @param accountid the accountid to set
     */
    public void setAccountid(Account accountid) {
        this.accountid = accountid;
    }

    @Override
    public String toString() {
        return "VatMapping{" + "vatmappingid=" + vatmappingid + ", wef=" + wef + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.vatmappingid);
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
        final VatMapping other = (VatMapping) obj;
        if (!Objects.equals(this.vatmappingid, other.vatmappingid)) {
            return false;
        }
        return true;
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

}
