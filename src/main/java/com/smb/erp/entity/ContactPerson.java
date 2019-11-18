package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the contactperson database table.
 * 
 */
@Entity
@NamedQuery(name="ContactPerson.findAll", query="SELECT c FROM ContactPerson c")
public class ContactPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String cpid;

	private String contactpersonname;

	private String email1;

	private String email2;

	private String mobile1;

	private String mobile2;

	private String post;

	//bi-directional many-to-one association to BusDoc
	@OneToMany(mappedBy="contactperson")
	private List<BusDoc> busdocs;

	//bi-directional many-to-one association to BusinessPartner
	@ManyToOne
	@JoinColumn(name="partnerid")
	private BusinessPartner businesspartner;

	public ContactPerson() {
	}

	public String getCpid() {
		return this.cpid;
	}

	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getContactpersonname() {
		return this.contactpersonname;
	}

	public void setContactpersonname(String contactpersonname) {
		this.contactpersonname = contactpersonname;
	}

	public String getEmail1() {
		return this.email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getMobile1() {
		return this.mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	public String getMobile2() {
		return this.mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public String getPost() {
		return this.post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public List<BusDoc> getBusdocs() {
		return this.busdocs;
	}

	public void setBusdocs(List<BusDoc> busdocs) {
		this.busdocs = busdocs;
	}

	public BusDoc addBusdoc(BusDoc busdoc) {
		getBusdocs().add(busdoc);
		busdoc.setContactperson(this);

		return busdoc;
	}

	public BusDoc removeBusdoc(BusDoc busdoc) {
		getBusdocs().remove(busdoc);
		busdoc.setContactperson(null);

		return busdoc;
	}

	public BusinessPartner getBusinesspartner() {
		return this.businesspartner;
	}

	public void setBusinesspartner(BusinessPartner businesspartner) {
		this.businesspartner = businesspartner;
	}

}