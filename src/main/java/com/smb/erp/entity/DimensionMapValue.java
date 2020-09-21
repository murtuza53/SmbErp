package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the dimensionmapvalue database table.
 * 
 */
@Entity
@Table(name = "dimensionmapvalue")
@NamedQuery(name="DimensionMapValue.findAll", query="SELECT d FROM DimensionMapValue d")
public class DimensionMapValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int dmvid;

	private String refId1;

	private BigInteger refId2;

	private String value;

	//bi-directional many-to-one association to DimensionMapping
	@ManyToOne
	@JoinColumn(name="dvtid")
	private DimensionMapping dimensionmapping;

	public DimensionMapValue() {
	}

	public int getDmvid() {
		return this.dmvid;
	}

	public void setDmvid(int dmvid) {
		this.dmvid = dmvid;
	}

	public String getRefId1() {
		return this.refId1;
	}

	public void setRefId1(String refId1) {
		this.refId1 = refId1;
	}

	public BigInteger getRefId2() {
		return this.refId2;
	}

	public void setRefId2(BigInteger refId2) {
		this.refId2 = refId2;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DimensionMapping getDimensionmapping() {
		return this.dimensionmapping;
	}

	public void setDimensionmapping(DimensionMapping dimensionmapping) {
		this.dimensionmapping = dimensionmapping;
	}

}