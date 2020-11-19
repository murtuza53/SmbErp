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
@Table(name = "cashregister")
public class CashRegister implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cashregisterid=0;
    
    private String transtype = "Debit";       //Debit, Credit

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bdinfoid")
    private BusDocInfo bdinfoid;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne
    @JoinColumn(name = "accountid")
    private Account accountid;

    public CashRegister() { 
    }

    /**
     * @return the cashregisterid
     */
    public Integer getCashregisterid() {
        return cashregisterid;
    }

    /**
     * @param cashregisterid the cashregisterid to set
     */
    public void setCashregisterid(Integer cashregisterid) {
        this.cashregisterid = cashregisterid;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.cashregisterid);
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
        final CashRegister other = (CashRegister) obj;
        if (!Objects.equals(this.cashregisterid, other.cashregisterid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CashRegister{" + "cashregisterid=" + cashregisterid + ", bdinfoid=" + bdinfoid + ", accountid=" + accountid + '}';
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

}
