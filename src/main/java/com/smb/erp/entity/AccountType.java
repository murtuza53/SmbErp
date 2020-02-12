package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the accounttype database table.
 *
 */
@Entity
@NamedQuery(name = "AccountType.findAll", query = "SELECT a FROM AccountType a")
public class AccountType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int acctypeid;

    private String description;

    private String name;

    //bi-directional many-to-one association to Account
    //@OneToMany(mappedBy = "accounttype")
    //private List<Account> accounts;

    public AccountType() {
    }

    public int getAcctypeid() {
        return this.acctypeid;
    }

    public void setAcctypeid(int acctypeid) {
        this.acctypeid = acctypeid;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account addAccount(Account account) {
        getAccounts().add(account);
        account.setAccounttype(this);

        return account;
    }

    public Account removeAccount(Account account) {
        getAccounts().remove(account);
        account.setAccounttype(null);

        return account;
    }*/

    @Override
    public String toString() {
        return "AccountType{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.acctypeid;
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
        final AccountType other = (AccountType) obj;
        if (this.acctypeid != other.acctypeid) {
            return false;
        }
        return true;
    }

}
