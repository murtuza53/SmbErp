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
@Table(name = "accounttransactiontype")
public class AccountTransactionType implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transtypeid=0;

    private String transtype = "Credit";       //Debit, Credit

    private String referredaccount; //SelectedAccount, ProductAccount, CustomerAccount, SupplierAccount, Formula
    
    private String formula;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bdinfoid")
    private BusDocInfo bdinfoid;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne
    @JoinColumn(name = "accountid")
    private Account accountid;

    public AccountTransactionType() { 
    }

    /**
     * @return the transtypeid
     */
    public Integer getTranstypeid() {
        return transtypeid;
    }

    /**
     * @param transtypeid the transtypeid to set
     */
    public void setTranstypeid(Integer transtypeid) {
        this.transtypeid = transtypeid;
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
     * @return the referredaccount
     */
    public String getReferredaccount() {
        return referredaccount;
    }

    /**
     * @param referredaccount the referredaccount to set
     */
    public void setReferredaccount(String referredaccount) {
        this.referredaccount = referredaccount;
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
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @param formula the formula to set
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public String toString() {
        return "AccountTransactionType{" + "transtypeid=" + transtypeid + ", transtype=" + transtype + ", referredaccount=" + referredaccount + ", formula=" + formula + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.transtypeid);
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
        final AccountTransactionType other = (AccountTransactionType) obj;
        if (!Objects.equals(this.transtypeid, other.transtypeid)) {
            return false;
        }
        return true;
    }

}
