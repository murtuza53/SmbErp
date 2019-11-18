package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the country database table.
 * 
 */
@Entity
@NamedQuery(name="Country.findAll", query="SELECT c FROM Country c")
public class Country implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String countryno;

	private String countryname;

	private String countrysym;

	private String currency;

	private String currencysym;

	private byte defcountry;

	private double rate;

	//bi-directional many-to-one association to AccDoc
	@OneToMany(mappedBy="country")
	private List<AccDoc> accdocs;

	//bi-directional many-to-one association to BusinessPartner
	@OneToMany(mappedBy="country")
	private List<BusinessPartner> businesspartners;

	//bi-directional many-to-one association to Product
	@OneToMany(mappedBy="country")
	private List<Product> products;

	public Country() {
	}

	public String getCountryno() {
		return this.countryno;
	}

	public void setCountryno(String countryno) {
		this.countryno = countryno;
	}

	public String getCountryname() {
		return this.countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getCountrysym() {
		return this.countrysym;
	}

	public void setCountrysym(String countrysym) {
		this.countrysym = countrysym;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencysym() {
		return this.currencysym;
	}

	public void setCurrencysym(String currencysym) {
		this.currencysym = currencysym;
	}

	public byte getDefcountry() {
		return this.defcountry;
	}

	public void setDefcountry(byte defcountry) {
		this.defcountry = defcountry;
	}

	public double getRate() {
		return this.rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public List<AccDoc> getAccdocs() {
		return this.accdocs;
	}

	public void setAccdocs(List<AccDoc> accdocs) {
		this.accdocs = accdocs;
	}

	public AccDoc addAccdoc(AccDoc accdoc) {
		getAccdocs().add(accdoc);
		accdoc.setCountry(this);

		return accdoc;
	}

	public AccDoc removeAccdoc(AccDoc accdoc) {
		getAccdocs().remove(accdoc);
		accdoc.setCountry(null);

		return accdoc;
	}

	public List<BusinessPartner> getBusinesspartners() {
		return this.businesspartners;
	}

	public void setBusinesspartners(List<BusinessPartner> businesspartners) {
		this.businesspartners = businesspartners;
	}

	public BusinessPartner addBusinesspartner(BusinessPartner businesspartner) {
		getBusinesspartners().add(businesspartner);
		businesspartner.setCountry(this);

		return businesspartner;
	}

	public BusinessPartner removeBusinesspartner(BusinessPartner businesspartner) {
		getBusinesspartners().remove(businesspartner);
		businesspartner.setCountry(null);

		return businesspartner;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product addProduct(Product product) {
		getProducts().add(product);
		product.setCountry(this);

		return product;
	}

	public Product removeProduct(Product product) {
		getProducts().remove(product);
		product.setCountry(null);

		return product;
	}

}