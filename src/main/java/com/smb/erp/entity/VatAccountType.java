package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@NamedQuery(name = "VatAccountType.findAll", query = "SELECT v FROM VatAccountType v ORDER BY v.accounttype")
public class VatAccountType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer vataccounttypeid;

    private String accounttype;

    private String description;

    public VatAccountType() {
    }

    /**
     * @return the vataccounttypeid
     */
    public Integer getVataccounttypeid() {
        return vataccounttypeid;
    }

    /**
     * @param vataccounttypeid the vataccounttypeid to set
     */
    public void setVataccounttypeid(Integer vataccounttypeid) {
        this.vataccounttypeid = vataccounttypeid;
    }

    /**
     * @return the accounttype
     */
    public String getAccounttype() {
        return accounttype;
    }

    /**
     * @param accounttype the accounttype to set
     */
    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
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
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.vataccounttypeid);
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
        final VatAccountType other = (VatAccountType) obj;
        if (!Objects.equals(this.vataccounttypeid, other.vataccounttypeid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return accounttype + " [" + vataccounttypeid + "]";
    }


}
