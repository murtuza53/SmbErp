package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the pricelist database table.
 * 
 */
@Entity
@Table(name = "pricelist")
@NamedQuery(name="PriceList.findAll", query="SELECT p FROM PriceList p")
public class PriceList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String pricelistno;

	@Temporal(TemporalType.DATE)
	private Date fromdate;

	private double mdp;

	private double mp;

	private double mpp;

	private String pricelistname;

	private double rdp;

	private double rp;

	private double rpp;

	@Temporal(TemporalType.DATE)
	private Date todate;

	private double wdp;

	private double wp;

	private double wpp;

	//bi-directional many-to-one association to Brand
	@ManyToOne
	@JoinColumn(name="brandid")
	private Brand brand;

	//bi-directional many-to-one association to BusinessPartner
	@ManyToOne
	@JoinColumn(name="partnerid")
	private BusinessPartner businesspartner;

	//bi-directional many-to-one association to ProductCategry
	@ManyToOne
	@JoinColumn(name="prodcatId")
	private ProductCategory prodcategory;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="productid")
	private Product product;

	public PriceList() {
	}

	public String getPricelistno() {
		return this.pricelistno;
	}

	public void setPricelistno(String pricelistno) {
		this.pricelistno = pricelistno;
	}

	public Date getFromdate() {
		return this.fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public double getMdp() {
		return this.mdp;
	}

	public void setMdp(double mdp) {
		this.mdp = mdp;
	}

	public double getMp() {
		return this.mp;
	}

	public void setMp(double mp) {
		this.mp = mp;
	}

	public double getMpp() {
		return this.mpp;
	}

	public void setMpp(double mpp) {
		this.mpp = mpp;
	}

	public String getPricelistname() {
		return this.pricelistname;
	}

	public void setPricelistname(String pricelistname) {
		this.pricelistname = pricelistname;
	}

	public double getRdp() {
		return this.rdp;
	}

	public void setRdp(double rdp) {
		this.rdp = rdp;
	}

	public double getRp() {
		return this.rp;
	}

	public void setRp(double rp) {
		this.rp = rp;
	}

	public double getRpp() {
		return this.rpp;
	}

	public void setRpp(double rpp) {
		this.rpp = rpp;
	}

	public Date getTodate() {
		return this.todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public double getWdp() {
		return this.wdp;
	}

	public void setWdp(double wdp) {
		this.wdp = wdp;
	}

	public double getWp() {
		return this.wp;
	}

	public void setWp(double wp) {
		this.wp = wp;
	}

	public double getWpp() {
		return this.wpp;
	}

	public void setWpp(double wpp) {
		this.wpp = wpp;
	}

	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public BusinessPartner getBusinesspartner() {
		return this.businesspartner;
	}

	public void setBusinesspartner(BusinessPartner businesspartner) {
		this.businesspartner = businesspartner;
	}

	public ProductCategory getProdcategory() {
		return this.prodcategory;
	}

	public void setProdcategory(ProductCategory prodcategory) {
		this.prodcategory = prodcategory;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}