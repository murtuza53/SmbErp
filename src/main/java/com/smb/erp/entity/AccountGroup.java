package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the accountgroup database table.
 * 
 */
@Entity
@NamedQuery(name="AccountGroup.findAll", query="SELECT a FROM AccountGroup a")
public class AccountGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String accountgroupid;

	private String groupname;

	private String grouptype;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="accountgroup")
	private List<Account> accounts;

	public AccountGroup() {
	}

	public String getAccountgroupid() {
		return this.accountgroupid;
	}

	public void setAccountgroupid(String accountgroupid) {
		this.accountgroupid = accountgroupid;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGrouptype() {
		return this.grouptype;
	}

	public void setGrouptype(String grouptype) {
		this.grouptype = grouptype;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Account addAccount(Account account) {
		getAccounts().add(account);
		account.setAccountgroup(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setAccountgroup(null);

		return account;
	}

}