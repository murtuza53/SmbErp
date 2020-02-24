package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@NamedQuery(name = "VatSalesPurchaseType.findAll", query = "SELECT v FROM VatSalesPurchaseType v ORDER BY v.typename")
public class VatSalesPurchaseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer vatsptypeid;

    private String typename;

    private String description;
    
    private String category;

    public VatSalesPurchaseType() {
    }

    /**
     * @return the typename
     */
    public String getTypename() {
        return typename;
    }

    /**
     * @param typename the typename to set
     */
    public void setTypename(String typename) {
        this.typename = typename;
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

    /**
     * @return the vatsptypeid
     */
    public Integer getVatsptypeid() {
        return vatsptypeid;
    }

    /**
     * @param vatsptypeid the vatsptypeid to set
     */
    public void setVatsptypeid(Integer vatsptypeid) {
        this.vatsptypeid = vatsptypeid;
    }

    @Override
    public String toString() {
        return "VatSalesPurchaseType{" + "vatsptypeid=" + vatsptypeid + ", typename=" + typename + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.vatsptypeid);
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
        final VatSalesPurchaseType other = (VatSalesPurchaseType) obj;
        if (!Objects.equals(this.vatsptypeid, other.vatsptypeid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

}
