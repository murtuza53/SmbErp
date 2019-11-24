package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the brand database table.
 * 
 */
@Entity
@NamedQuery(name="Brand.findAll", query="SELECT b FROM Brand b")
public class Brand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long brandid;

	private String abbreviation;

	private String brandname;

	private String description;

	private String logofile;

	//bi-directional many-to-one association to PriceList
	@OneToMany(mappedBy="brand")
	private List<PriceList> pricelists;

	//bi-directional many-to-one association to Product
	@OneToMany(mappedBy="brand")
	private List<Product> products;

	public Brand() {
	}

	public Long getBrandid() {
		return this.brandid;
	}

	public void setBrandid(Long brandid) {
		this.brandid = brandid;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getBrandname() {
		return this.brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogofile() {
		return this.logofile;
	}

	public void setLogofile(String logofile) {
		this.logofile = logofile;
	}

	public List<PriceList> getPricelists() {
		return this.pricelists;
	}

	public void setPricelists(List<PriceList> pricelists) {
		this.pricelists = pricelists;
	}

	public PriceList addPricelist(PriceList pricelist) {
		getPricelists().add(pricelist);
		pricelist.setBrand(this);

		return pricelist;
	}

	public PriceList removePricelist(PriceList pricelist) {
		getPricelists().remove(pricelist);
		pricelist.setBrand(null);

		return pricelist;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product addProduct(Product product) {
		getProducts().add(product);
		product.setBrand(this);

		return product;
	}

	public Product removeProduct(Product product) {
		getProducts().remove(product);
		product.setBrand(null);

		return product;
	}

}