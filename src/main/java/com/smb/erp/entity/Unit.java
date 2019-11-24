package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the unit database table.
 * 
 */
@Entity
@NamedQuery(name="Unit.findAll", query="SELECT u FROM Unit u")
public class Unit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long unitid;

	private String description;

	private String unitname;

	private String unitsym;

	//bi-directional many-to-one association to ProductTransaction
	@OneToMany(mappedBy="unit")
	private List<ProductTransaction> prodtransactions;

	//bi-directional many-to-one association to Product
	@OneToMany(mappedBy="unit1")
	private List<Product> products1;

	//bi-directional many-to-one association to Product
	@OneToMany(mappedBy="unit2")
	private List<Product> products2;

	public Unit() {
	}

	public Long getUnitid() {
		return this.unitid;
	}

	public void setUnitid(Long unitid) {
		this.unitid = unitid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitname() {
		return this.unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getUnitsym() {
		return this.unitsym;
	}

	public void setUnitsym(String unitsym) {
		this.unitsym = unitsym;
	}

	public List<ProductTransaction> getProdtransactions() {
		return this.prodtransactions;
	}

	public void setProdtransactions(List<ProductTransaction> prodtransactions) {
		this.prodtransactions = prodtransactions;
	}

	public ProductTransaction addProdtransaction(ProductTransaction prodtransaction) {
		getProdtransactions().add(prodtransaction);
		prodtransaction.setUnit(this);

		return prodtransaction;
	}

	public ProductTransaction removeProdtransaction(ProductTransaction prodtransaction) {
		getProdtransactions().remove(prodtransaction);
		prodtransaction.setUnit(null);

		return prodtransaction;
	}

	public List<Product> getProducts1() {
		return this.products1;
	}

	public void setProducts1(List<Product> products1) {
		this.products1 = products1;
	}

	public Product addProducts1(Product products1) {
		getProducts1().add(products1);
		products1.setUnit1(this);

		return products1;
	}

	public Product removeProducts1(Product products1) {
		getProducts1().remove(products1);
		products1.setUnit1(null);

		return products1;
	}

	public List<Product> getProducts2() {
		return this.products2;
	}

	public void setProducts2(List<Product> products2) {
		this.products2 = products2;
	}

	public Product addProducts2(Product products2) {
		getProducts2().add(products2);
		products2.setUnit2(this);

		return products2;
	}

	public Product removeProducts2(Product products2) {
		getProducts2().remove(products2);
		products2.setUnit2(null);

		return products2;
	}

}