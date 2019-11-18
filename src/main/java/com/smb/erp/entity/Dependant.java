package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the dependant database table.
 * 
 */
@Entity
@NamedQuery(name="Dependant.findAll", query="SELECT d FROM Dependant d")
public class Dependant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String dependantno;

	private String contactno;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dob;

	private String email;

	private String gender;

	private String name;

	private BigInteger pdetailsno;

	private String relation;

	//bi-directional many-to-one association to Emp
	@ManyToOne
	@JoinColumn(name="empno")
	private Emp emp;

	public Dependant() {
	}

	public String getDependantno() {
		return this.dependantno;
	}

	public void setDependantno(String dependantno) {
		this.dependantno = dependantno;
	}

	public String getContactno() {
		return this.contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getPdetailsno() {
		return this.pdetailsno;
	}

	public void setPdetailsno(BigInteger pdetailsno) {
		this.pdetailsno = pdetailsno;
	}

	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Emp getEmp() {
		return this.emp;
	}

	public void setEmp(Emp emp) {
		this.emp = emp;
	}

}