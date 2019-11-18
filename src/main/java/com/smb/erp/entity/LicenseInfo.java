package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the licenseinfo database table.
 * 
 */
@Entity
@NamedQuery(name="LicenseInfo.findAll", query="SELECT l FROM LicenseInfo l")
public class LicenseInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private double clientversion;

	private String companyname;

	private String contactperson;

	private double dbversion;

	private String email;

	private String fax;

	private String licensecode;

	@Temporal(TemporalType.TIMESTAMP)
	private Date licensefromdate;

	private String licensestatus;

	@Temporal(TemporalType.TIMESTAMP)
	private Date licensetodate;

	private String mobile;

	private double serverversion;

	private String tel;

	public LicenseInfo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getClientversion() {
		return this.clientversion;
	}

	public void setClientversion(double clientversion) {
		this.clientversion = clientversion;
	}

	public String getCompanyname() {
		return this.companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getContactperson() {
		return this.contactperson;
	}

	public void setContactperson(String contactperson) {
		this.contactperson = contactperson;
	}

	public double getDbversion() {
		return this.dbversion;
	}

	public void setDbversion(double dbversion) {
		this.dbversion = dbversion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getLicensecode() {
		return this.licensecode;
	}

	public void setLicensecode(String licensecode) {
		this.licensecode = licensecode;
	}

	public Date getLicensefromdate() {
		return this.licensefromdate;
	}

	public void setLicensefromdate(Date licensefromdate) {
		this.licensefromdate = licensefromdate;
	}

	public String getLicensestatus() {
		return this.licensestatus;
	}

	public void setLicensestatus(String licensestatus) {
		this.licensestatus = licensestatus;
	}

	public Date getLicensetodate() {
		return this.licensetodate;
	}

	public void setLicensetodate(Date licensetodate) {
		this.licensetodate = licensetodate;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public double getServerversion() {
		return this.serverversion;
	}

	public void setServerversion(double serverversion) {
		this.serverversion = serverversion;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}