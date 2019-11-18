package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the asset database table.
 * 
 */
@Entity
@NamedQuery(name="Asset.findAll", query="SELECT a FROM Asset a")
public class Asset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String assetno;

	private String assetdescription;

	@Temporal(TemporalType.TIMESTAMP)
	private Date datepurchased;

	@Temporal(TemporalType.TIMESTAMP)
	private Date datesold;

	private BigInteger deplife;

	private double purvalue;

	private double soldvalue;

	//bi-directional many-to-one association to Branch
	@ManyToOne
	@JoinColumn(name="branch")
	private Branch branchBean;

	//bi-directional many-to-one association to Emp
	@ManyToOne
	@JoinColumn(name="createdby")
	private Emp emp;

	//bi-directional many-to-one association to LederLine
	@ManyToOne
	@JoinColumn(name="ledgerlineno")
	private LederLine ledline;

	public Asset() {
	}

	public String getAssetno() {
		return this.assetno;
	}

	public void setAssetno(String assetno) {
		this.assetno = assetno;
	}

	public String getAssetdescription() {
		return this.assetdescription;
	}

	public void setAssetdescription(String assetdescription) {
		this.assetdescription = assetdescription;
	}

	public Date getDatepurchased() {
		return this.datepurchased;
	}

	public void setDatepurchased(Date datepurchased) {
		this.datepurchased = datepurchased;
	}

	public Date getDatesold() {
		return this.datesold;
	}

	public void setDatesold(Date datesold) {
		this.datesold = datesold;
	}

	public BigInteger getDeplife() {
		return this.deplife;
	}

	public void setDeplife(BigInteger deplife) {
		this.deplife = deplife;
	}

	public double getPurvalue() {
		return this.purvalue;
	}

	public void setPurvalue(double purvalue) {
		this.purvalue = purvalue;
	}

	public double getSoldvalue() {
		return this.soldvalue;
	}

	public void setSoldvalue(double soldvalue) {
		this.soldvalue = soldvalue;
	}

	public Branch getBranchBean() {
		return this.branchBean;
	}

	public void setBranchBean(Branch branchBean) {
		this.branchBean = branchBean;
	}

	public Emp getEmp() {
		return this.emp;
	}

	public void setEmp(Emp emp) {
		this.emp = emp;
	}

	public LederLine getLedline() {
		return this.ledline;
	}

	public void setLedline(LederLine ledline) {
		this.ledline = ledline;
	}

}