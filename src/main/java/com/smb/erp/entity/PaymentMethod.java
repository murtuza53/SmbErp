package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the paymethod database table.
 * 
 */
@Entity
@Table(name="paymethod")
@NamedQuery(name="PaymentMethod.findAll", query="SELECT p FROM PaymentMethod p")
public class PaymentMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String paymentno;

	private String chequename;

	@Temporal(TemporalType.TIMESTAMP)
	private Date paydate;

	private double paymentamount;

	private String paytype;

	private String refdetails;

	private String refno;

	public PaymentMethod() {
	}

	public String getPaymentno() {
		return this.paymentno;
	}

	public void setPaymentno(String paymentno) {
		this.paymentno = paymentno;
	}

	public String getChequename() {
		return this.chequename;
	}

	public void setChequename(String chequename) {
		this.chequename = chequename;
	}

	public Date getPaydate() {
		return this.paydate;
	}

	public void setPaydate(Date paydate) {
		this.paydate = paydate;
	}

	public double getPaymentamount() {
		return this.paymentamount;
	}

	public void setPaymentamount(double paymentamount) {
		this.paymentamount = paymentamount;
	}

	public String getPaytype() {
		return this.paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getRefdetails() {
		return this.refdetails;
	}

	public void setRefdetails(String refdetails) {
		this.refdetails = refdetails;
	}

	public String getRefno() {
		return this.refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

}