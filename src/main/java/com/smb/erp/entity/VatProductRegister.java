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
@Table(name = "vatproductregister")
@NamedQuery(name = "VatProductRegister.findAll", query = "SELECT v FROM VatProductRegister v ORDER BY v.producttype")
public class VatProductRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long vatregisterid=0l;

    private String producttype;

    @Temporal(TemporalType.TIMESTAMP)
    private Date wef;

    @ManyToOne
    @JoinColumn(name = "vatcategoryid")
    private VatCategory vatcategoryid;

    //@ManyToOne
    //@JoinColumn(name = "productid")
    //private Product productid;

    public VatProductRegister() {
    }

    /**
     * @return the vatregisterid
     */
    public Long getVatregisterid() {
        return vatregisterid;
    }

    /**
     * @param vatregisterid the vatregisterid to set
     */
    public void setVatregisterid(Long vatregisterid) {
        this.vatregisterid = vatregisterid;
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
     * @return the productid
     */
    //public Product getProductid() {
    //    return productid;
    //}

    /**
     * @param productid the productid to set
     */
    //public void setProductid(Product productid) {
    //    this.productid = productid;
    //}

    @Override
    public String toString() {
        if(vatcategoryid!=null){
            return vatcategoryid.getCategoryname();
        }
        return "N/A";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.vatregisterid);
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
        final VatProductRegister other = (VatProductRegister) obj;
        if (!Objects.equals(this.vatregisterid, other.vatregisterid)) {
            return false;
        }
        return true;
    }

}
