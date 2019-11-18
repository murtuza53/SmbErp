package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the prodtransexecutedfrom database table.
 * 
 */
@Entity
@Table(name="prodtransexecutedfrom")
@NamedQuery(name="ProductTransactionExecutedFrom.findAll", query="SELECT p FROM ProductTransactionExecutedFrom p")
public class ProductTransactionExecutedFrom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String exectedqtyid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	private double executedqty;

	private BigInteger fromprodtransid;

	//bi-directional many-to-one association to ProductTransaction
	@ManyToOne
	@JoinColumn(name="prodtransid")
	private ProductTransaction prodtransaction;

	public ProductTransactionExecutedFrom() {
	}

	public String getExectedqtyid() {
		return this.exectedqtyid;
	}

	public void setExectedqtyid(String exectedqtyid) {
		this.exectedqtyid = exectedqtyid;
	}

	public Date getCreatedon() {
		return this.createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public double getExecutedqty() {
		return this.executedqty;
	}

	public void setExecutedqty(double executedqty) {
		this.executedqty = executedqty;
	}

	public BigInteger getFromprodtransid() {
		return this.fromprodtransid;
	}

	public void setFromprodtransid(BigInteger fromprodtransid) {
		this.fromprodtransid = fromprodtransid;
	}

	public ProductTransaction getProdtransaction() {
		return this.prodtransaction;
	}

	public void setProdtransaction(ProductTransaction prodtransaction) {
		this.prodtransaction = prodtransaction;
	}

}