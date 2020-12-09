package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 * The persistent class for the prodaccount database table.
 *
 */
@Entity
@Table(name = "prodaccount")
@NamedQuery(name = "ProductAccount.findAll", query = "SELECT p FROM ProductAccount p")
public class ProductAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long prodaccountid;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name = "salesaccountid")
    private Account salesAccount;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name = "purchaseaccountid")
    private Account purchaseAccount;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name = "consumptionaccountid")
    private Account consumptionAccount;

    public ProductAccount() {
    }

    public Long getProdaccountid() {
        return this.prodaccountid;
    }

    public void setProdaccountid(Long prodaccountid) {
        this.prodaccountid = prodaccountid;
    }

    /**
     * @return the salesAccount
     */
    public Account getSalesAccount() {
        return salesAccount;
    }

    /**
     * @param salesAccount the salesAccount to set
     */
    public void setSalesAccount(Account salesAccount) {
        this.salesAccount = salesAccount;
    }

    /**
     * @return the purchaseAccount
     */
    public Account getPurchaseAccount() {
        return purchaseAccount;
    }

    /**
     * @param purchaseAccount the purchaseAccount to set
     */
    public void setPurchaseAccount(Account purchaseAccount) {
        this.purchaseAccount = purchaseAccount;
    }

    /**
     * @return the consumptionAccount
     */
    public Account getConsumptionAccount() {
        return consumptionAccount;
    }

    /**
     * @param consumptionAccount the consumptionAccount to set
     */
    public void setConsumptionAccount(Account consumptionAccount) {
        this.consumptionAccount = consumptionAccount;
    }

    @Override
    public String toString() {
        return "ProductAccount{" + "prodaccountid=" + prodaccountid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.prodaccountid);
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
        final ProductAccount other = (ProductAccount) obj;
        if (!Objects.equals(this.prodaccountid, other.prodaccountid)) {
            return false;
        }
        return true;
    }

}
