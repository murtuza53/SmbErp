package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the dimension database table.
 * 
 */
@Entity
@Table(name = "dimension")
@NamedQuery(name="Dimension.findAll", query="SELECT d FROM Dimension d")
public class Dimension implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int dimensionid;

	private String createdon;

	private String description;

	private String name;

	//bi-directional many-to-one association to DimensionMapping
	@OneToMany(mappedBy="dimension")
	private List<DimensionMapping> dimensionmappings;

	//bi-directional many-to-one association to DimensionValue
	@OneToMany(mappedBy="dimension")
	private List<DimensionValue> dimensionvalues;

	public Dimension() {
	}

	public int getDimensionid() {
		return this.dimensionid;
	}

	public void setDimensionid(int dimensionid) {
		this.dimensionid = dimensionid;
	}

	public String getCreatedon() {
		return this.createdon;
	}

	public void setCreatedon(String createdon) {
		this.createdon = createdon;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DimensionMapping> getDimensionmappings() {
		return this.dimensionmappings;
	}

	public void setDimensionmappings(List<DimensionMapping> dimensionmappings) {
		this.dimensionmappings = dimensionmappings;
	}

	public DimensionMapping addDimensionmapping(DimensionMapping dimensionmapping) {
		getDimensionmappings().add(dimensionmapping);
		dimensionmapping.setDimension(this);

		return dimensionmapping;
	}

	public DimensionMapping removeDimensionmapping(DimensionMapping dimensionmapping) {
		getDimensionmappings().remove(dimensionmapping);
		dimensionmapping.setDimension(null);

		return dimensionmapping;
	}

	public List<DimensionValue> getDimensionvalues() {
		return this.dimensionvalues;
	}

	public void setDimensionvalues(List<DimensionValue> dimensionvalues) {
		this.dimensionvalues = dimensionvalues;
	}

	public DimensionValue addDimensionvalue(DimensionValue dimensionvalue) {
		getDimensionvalues().add(dimensionvalue);
		dimensionvalue.setDimension(this);

		return dimensionvalue;
	}

	public DimensionValue removeDimensionvalue(DimensionValue dimensionvalue) {
		getDimensionvalues().remove(dimensionvalue);
		dimensionvalue.setDimension(null);

		return dimensionvalue;
	}

}