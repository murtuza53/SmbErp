package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the busdocinfo database table.
 *
 */
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "defaultmodeaccounts")
public class DefaultModeAccount implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer defaultaccid=0;
    
    private String transtype = "Debit";       //Debit, Credit

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bdinfoid")
    private BusDocInfo bdinfoid;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne
    @JoinColumn(name = "accountid")
    private Account accountid;

    public DefaultModeAccount() { 
    }

    /**
     * @return the bdinfoid
     */
    public BusDocInfo getBdinfoid() {
        return bdinfoid;
    }

    /**
     * @param bdinfoid the bdinfoid to set
     */
    public void setBdinfoid(BusDocInfo bdinfoid) {
        this.bdinfoid = bdinfoid;
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

    /**
     * @return the transtype
     */
    public String getTranstype() {
        return transtype;
    }

    /**
     * @param transtype the transtype to set
     */
    public void setTranstype(String transtype) {
        this.transtype = transtype;
    }

    /**
     * @return the defaultaccid
     */
    public Integer getDefaultaccid() {
        return defaultaccid;
    }

    /**
     * @param defaultaccid the defaultaccid to set
     */
    public void setDefaultaccid(Integer defaultaccid) {
        this.defaultaccid = defaultaccid;
    }

    @Override
    public String toString() {
        return "DefaultModeAccount{" + "defaultaccid=" + defaultaccid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.defaultaccid);
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
        final DefaultModeAccount other = (DefaultModeAccount) obj;
        if (!Objects.equals(this.defaultaccid, other.defaultaccid)) {
            return false;
        }
        return true;
    }

}
