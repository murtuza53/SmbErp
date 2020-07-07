package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the ppdetails database table.
 * 
 */
@Entity
@Table(name="ppdetails")
@NamedQuery(name="PartialPaymentDetail.findAll", query="SELECT p FROM PartialPaymentDetail p")
public class PartialPaymentDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String pplineno;

	private double amount;

	private BigInteger position;

	@Temporal(TemporalType.TIMESTAMP)
	private Date transdate;

	private BigInteger version;

	//bi-directional many-to-one association to BusDoc
	@ManyToOne
	@JoinColumn(name="fpino")
	private BusDoc busdoc1;

	//bi-directional many-to-one association to BusDoc
	@ManyToOne
	@JoinColumn(name="jcino")
	private BusDoc busdoc2;

	//bi-directional many-to-one association to BusDoc
	@ManyToOne
	@JoinColumn(name="jclpino")
	private BusDoc busdoc3;

	//bi-directional many-to-one association to LedgerLine
	@ManyToOne
	@JoinColumn(name="llno")
	private LedgerLine ledline;

	//bi-directional many-to-one association to BusDoc
	@ManyToOne
	@JoinColumn(name="lpino")
	private BusDoc busdoc4;

	//bi-directional many-to-one association to BusDoc
	@ManyToOne
	@JoinColumn(name="sino")
	private BusDoc busdoc5;

	public PartialPaymentDetail() {
	}

	public String getPplineno() {
		return this.pplineno;
	}

	public void setPplineno(String pplineno) {
		this.pplineno = pplineno;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public BigInteger getPosition() {
		return this.position;
	}

	public void setPosition(BigInteger position) {
		this.position = position;
	}

	public Date getTransdate() {
		return this.transdate;
	}

	public void setTransdate(Date transdate) {
		this.transdate = transdate;
	}

	public BigInteger getVersion() {
		return this.version;
	}

	public void setVersion(BigInteger version) {
		this.version = version;
	}

	public BusDoc getBusdoc1() {
		return this.busdoc1;
	}

	public void setBusdoc1(BusDoc busdoc1) {
		this.busdoc1 = busdoc1;
	}

	public BusDoc getBusdoc2() {
		return this.busdoc2;
	}

	public void setBusdoc2(BusDoc busdoc2) {
		this.busdoc2 = busdoc2;
	}

	public BusDoc getBusdoc3() {
		return this.busdoc3;
	}

	public void setBusdoc3(BusDoc busdoc3) {
		this.busdoc3 = busdoc3;
	}

	public LedgerLine getLedline() {
		return this.ledline;
	}

	public void setLedline(LedgerLine ledline) {
		this.ledline = ledline;
	}

	public BusDoc getBusdoc4() {
		return this.busdoc4;
	}

	public void setBusdoc4(BusDoc busdoc4) {
		this.busdoc4 = busdoc4;
	}

	public BusDoc getBusdoc5() {
		return this.busdoc5;
	}

	public void setBusdoc5(BusDoc busdoc5) {
		this.busdoc5 = busdoc5;
	}

}