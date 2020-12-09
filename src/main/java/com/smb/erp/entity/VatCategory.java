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
@Table(name = "vatcategory")
@NamedQuery(name = "VatCategory.findAll", query = "SELECT v FROM VatCategory v ORDER BY v.categoryname")
public class VatCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long vatcategoryid = new Date().getTime();

    private String categoryname;

    private Double vatpercentage = 0.0;
    
    private String description;

    public VatCategory() {
    }

    /**
     * @return the vatcategoryid
     */
    public Long getVatcategoryid() {
        return vatcategoryid;
    }

    /**
     * @param vatcategoryid the vatcategoryid to set
     */
    public void setVatcategoryid(Long vatcategoryid) {
        this.vatcategoryid = vatcategoryid;
    }

    /**
     * @return the categoryname
     */
    public String getCategoryname() {
        return categoryname;
    }

    /**
     * @param categoryname the categoryname to set
     */
    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    /**
     * @return the vatpercentage
     */
    public Double getVatpercentage() {
        return vatpercentage;
    }

    /**
     * @param vatpercentage the vatpercentage to set
     */
    public void setVatpercentage(Double vatpercentage) {
        this.vatpercentage = vatpercentage;
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

    @Override
    public String toString() {
        return categoryname + " [" + vatcategoryid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.vatcategoryid);
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
        final VatCategory other = (VatCategory) obj;
        if (!Objects.equals(this.vatcategoryid, other.vatcategoryid)) {
            return false;
        }
        return true;
    }

}
