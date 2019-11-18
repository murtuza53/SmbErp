package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the dimensionmapping database table.
 * 
 */
@Entity
@NamedQuery(name="DimensionMapping.findAll", query="SELECT d FROM DimensionMapping d")
public class DimensionMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int dvtid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	private String mappedto;

	//bi-directional many-to-one association to Dimension
	@ManyToOne
	@JoinColumn(name="dimensionid")
	private Dimension dimension;

	//bi-directional many-to-one association to DimensionMapValue
	@OneToMany(mappedBy="dimensionmapping")
	private List<DimensionMapValue> dimensionmapvalues;

	public DimensionMapping() {
	}

	public int getDvtid() {
		return this.dvtid;
	}

	public void setDvtid(int dvtid) {
		this.dvtid = dvtid;
	}

	public Date getCreatedon() {
		return this.createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public String getMappedto() {
		return this.mappedto;
	}

	public void setMappedto(String mappedto) {
		this.mappedto = mappedto;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public List<DimensionMapValue> getDimensionmapvalues() {
		return this.dimensionmapvalues;
	}

	public void setDimensionmapvalues(List<DimensionMapValue> dimensionmapvalues) {
		this.dimensionmapvalues = dimensionmapvalues;
	}

	public DimensionMapValue addDimensionmapvalue(DimensionMapValue dimensionmapvalue) {
		getDimensionmapvalues().add(dimensionmapvalue);
		dimensionmapvalue.setDimensionmapping(this);

		return dimensionmapvalue;
	}

	public DimensionMapValue removeDimensionmapvalue(DimensionMapValue dimensionmapvalue) {
		getDimensionmapvalues().remove(dimensionmapvalue);
		dimensionmapvalue.setDimensionmapping(null);

		return dimensionmapvalue;
	}

}