package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the account database table.
 *
 */
@Entity
@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a ORDER BY a.accountname")
public class Account implements Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

    @Id
    private String accountid;

    private String accountname;

    private String nodetype;

    private double openingbalance;

    //bi-directional many-to-one association to AccountGroup
    @ManyToOne
    @JoinColumn(name = "accountgroupid")
    private AccountGroup accountgroup;

    @ManyToOne
    @JoinColumn(name = "parentid")
    private Account parentid;

    //bi-directional many-to-one association to AccountType
    @ManyToOne
    @JoinColumn(name = "acctypeid")
    private AccountType accounttype;

    //bi-directional many-to-one association to BusinessPartner
    @ManyToOne
    @JoinColumn(name = "businesspartnerid")
    private BusinessPartner businesspartner;

    //bi-directional many-to-one association to LederLine
    //@OneToMany(mappedBy = "account")
    //private List<LederLine> ledlines;

    //bi-directional many-to-one association to ProductAccount
    //@OneToMany(mappedBy = "account1")
    //private List<ProductAccount> prodaccounts1;

    //bi-directional many-to-one association to ProductAccount
    //@OneToMany(mappedBy = "account2")
    //private List<ProductAccount> prodaccounts2;

    //bi-directional many-to-one association to ProductAccount
    //@OneToMany(mappedBy = "account3")
    //private List<ProductAccount> prodaccounts3;

    public Account() {
    }

    public String getAccountid() {
        return this.accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getAccountname() {
        return this.accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public double getOpeningbalance() {
        return this.openingbalance;
    }

    public void setOpeningbalance(double openingbalance) {
        this.openingbalance = openingbalance;
    }

    public AccountGroup getAccountgroup() {
        return this.accountgroup;
    }

    public void setAccountgroup(AccountGroup accountgroup) {
        this.accountgroup = accountgroup;
    }

    public BusinessPartner getBusinesspartner() {
        return this.businesspartner;
    }

    public void setBusinesspartner(BusinessPartner businesspartner) {
        this.businesspartner = businesspartner;
    }

    /*public List<LederLine> getLedlines() {
        return this.ledlines;
    }

    public void setLedlines(List<LederLine> ledlines) {
        this.ledlines = ledlines;
    }

    public LederLine addLedline(LederLine ledline) {
        getLedlines().add(ledline);
        ledline.setAccount(this);

        return ledline;
    }

    public LederLine removeLedline(LederLine ledline) {
        getLedlines().remove(ledline);
        ledline.setAccount(null);

        return ledline;
    }

    public List<ProductAccount> getProdaccounts1() {
        return this.prodaccounts1;
    }

    public void setProdaccounts1(List<ProductAccount> prodaccounts1) {
        this.prodaccounts1 = prodaccounts1;
    }

    public ProductAccount addProdaccounts1(ProductAccount prodaccounts1) {
        getProdaccounts1().add(prodaccounts1);
        prodaccounts1.setAccount1(this);

        return prodaccounts1;
    }

    public ProductAccount removeProdaccounts1(ProductAccount prodaccounts1) {
        getProdaccounts1().remove(prodaccounts1);
        prodaccounts1.setAccount1(null);

        return prodaccounts1;
    }

    public List<ProductAccount> getProdaccounts2() {
        return this.prodaccounts2;
    }

    public void setProdaccounts2(List<ProductAccount> prodaccounts2) {
        this.prodaccounts2 = prodaccounts2;
    }

    public ProductAccount addProdaccounts2(ProductAccount prodaccounts2) {
        getProdaccounts2().add(prodaccounts2);
        prodaccounts2.setAccount2(this);

        return prodaccounts2;
    }

    public ProductAccount removeProdaccounts2(ProductAccount prodaccounts2) {
        getProdaccounts2().remove(prodaccounts2);
        prodaccounts2.setAccount2(null);

        return prodaccounts2;
    }

    public List<ProductAccount> getProdaccounts3() {
        return this.prodaccounts3;
    }

    public void setProdaccounts3(List<ProductAccount> prodaccounts3) {
        this.prodaccounts3 = prodaccounts3;
    }

    public ProductAccount addProdaccounts3(ProductAccount prodaccounts3) {
        getProdaccounts3().add(prodaccounts3);
        prodaccounts3.setAccount3(this);

        return prodaccounts3;
    }

    public ProductAccount removeProdaccounts3(ProductAccount prodaccounts3) {
        getProdaccounts3().remove(prodaccounts3);
        prodaccounts3.setAccount3(null);

        return prodaccounts3;
    }*/

    @Override
    public String toString() {
        return accountname + " [" + accountid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.accountid);
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
        final Account other = (Account) obj;
        if (!Objects.equals(this.accountid, other.accountid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the parentid
     */
    public Account getParentid() {
        return parentid;
    }

    /**
     * @param parentid the parentid to set
     */
    public void setParentid(Account parentid) {
        this.parentid = parentid;
    }

    /**
     * @return the nodetype
     */
    public String getNodetype() {
        return nodetype;
    }

    /**
     * @param nodetype the nodetype to set
     */
    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    /**
     * @return the accounttype
     */
    public AccountType getAccounttype() {
        return accounttype;
    }

    /**
     * @param accounttype the accounttype to set
     */
    public void setAccounttype(AccountType accounttype) {
        this.accounttype = accounttype;
    }

    @Override
    public int compareTo(Account o) {
        return accountname.compareTo(o.getAccountname());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
