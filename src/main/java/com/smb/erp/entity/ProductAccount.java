package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the prodaccount database table.
 * 
 */
@Entity
@Table(name="prodaccount")
@NamedQuery(name="ProductAccount.findAll", query="SELECT p FROM ProductAccount p")
public class ProductAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int prodaccountid;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="salesaccountid")
	private Account account1;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="purchaseaccountid")
	private Account account2;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="consumptionaccountid")
	private Account account3;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="productid")
	private Product product;

	public ProductAccount() {
	}

	public int getProdaccountid() {
		return this.prodaccountid;
	}

	public void setProdaccountid(int prodaccountid) {
		this.prodaccountid = prodaccountid;
	}

	public Account getAccount1() {
		return this.account1;
	}

	public void setAccount1(Account account1) {
		this.account1 = account1;
	}

	public Account getAccount2() {
		return this.account2;
	}

	public void setAccount2(Account account2) {
		this.account2 = account2;
	}

	public Account getAccount3() {
		return this.account3;
	}

	public void setAccount3(Account account3) {
		this.account3 = account3;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}