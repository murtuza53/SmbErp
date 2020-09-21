package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the dimensionvalue database table.
 * 
 */
@Entity
@Table(name = "dimensionvalue")
@NamedQuery(name="DimensionValue.findAll", query="SELECT d FROM DimensionValue d")
public class DimensionValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int dvid;

	private String abbreviation;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	private String value;

	//bi-directional many-to-one association to Dimension
	@ManyToOne
	@JoinColumn(name="dimensionid")
	private Dimension dimension;

	public DimensionValue() {
	}

	public int getDvid() {
		return this.dvid;
	}

	public void setDvid(int dvid) {
		this.dvid = dvid;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Date getCreatedon() {
		return this.createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

}