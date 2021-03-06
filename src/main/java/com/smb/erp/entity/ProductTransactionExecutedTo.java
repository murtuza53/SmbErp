package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the prodtransexecutedto database table.
 * 
 */
@Entity
@Table(name="prodtransexecutedto")
@NamedQuery(name="ProductTransactionExecutedTo.findAll", query="SELECT p FROM ProductTransactionExecutedTo p")
public class ProductTransactionExecutedTo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String exectedqtyid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	private double executedqty;

	private BigInteger toprodtransid;

	//bi-directional many-to-one association to ProductTransaction
	@ManyToOne
	@JoinColumn(name="prodtransid")
	private ProductTransaction prodtransaction;

	public ProductTransactionExecutedTo() {
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

	public BigInteger getToprodtransid() {
		return this.toprodtransid;
	}

	public void setToprodtransid(BigInteger toprodtransid) {
		this.toprodtransid = toprodtransid;
	}

	public ProductTransaction getProdtransaction() {
		return this.prodtransaction;
	}

	public void setProdtransaction(ProductTransaction prodtransaction) {
		this.prodtransaction = prodtransaction;
	}

}