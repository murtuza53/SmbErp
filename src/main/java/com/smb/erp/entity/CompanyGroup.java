package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the companygroup database table.
 * 
 */
@Entity
@NamedQuery(name="CompanyGroup.findAll", query="SELECT c FROM CompanyGroup c")
public class CompanyGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int comgroupid;

	private String addresss;

	private String crno;

	private String fax;

	private String groupname;

	private String mob;

	@Temporal(TemporalType.DATE)
	private Date openingdate;

	private String phone;

	//bi-directional many-to-one association to Company
	@OneToMany(mappedBy="companygroup")
	private List<Company> companies;

	public CompanyGroup() {
	}

	public int getComgroupid() {
		return this.comgroupid;
	}

	public void setComgroupid(int comgroupid) {
		this.comgroupid = comgroupid;
	}

	public String getAddresss() {
		return this.addresss;
	}

	public void setAddresss(String addresss) {
		this.addresss = addresss;
	}

	public String getCrno() {
		return this.crno;
	}

	public void setCrno(String crno) {
		this.crno = crno;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getMob() {
		return this.mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public Date getOpeningdate() {
		return this.openingdate;
	}

	public void setOpeningdate(Date openingdate) {
		this.openingdate = openingdate;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<Company> getCompanies() {
		return this.companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public Company addCompany(Company company) {
		getCompanies().add(company);
		company.setCompanygroup(this);

		return company;
	}

	public Company removeCompany(Company company) {
		getCompanies().remove(company);
		company.setCompanygroup(null);

		return company;
	}

}