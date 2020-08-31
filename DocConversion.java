package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the docconversion database table.
 * 
 */
@Entity
@NamedQuery(name="DocConversion.findAll", query="SELECT d FROM DocConversion d")
public class DocConversion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int docconid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	//bi-directional many-to-one association to ConvertTo
	@OneToMany(mappedBy="docconversion")
	private List<ConvertTo> converttos;

	//bi-directional many-to-one association to BusDocInfo
	@ManyToOne
	@JoinColumn(name="bdinfoid")
	private BusDocInfo busdocinfo;

	public DocConversion() {
	}

	public int getDocconid() {
		return this.docconid;
	}

	public void setDocconid(int docconid) {
		this.docconid = docconid;
	}

	public Date getCreatedon() {
		return this.createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public List<ConvertTo> getConverttos() {
		return this.converttos;
	}

	public void setConverttos(List<ConvertTo> converttos) {
		this.converttos = converttos;
	}

	public ConvertTo addConvertto(ConvertTo convertto) {
		getConverttos().add(convertto);
		convertto.setDocconversion(this);

		return convertto;
	}

	public ConvertTo removeConvertto(ConvertTo convertto) {
		getConverttos().remove(convertto);
		convertto.setDocconversion(null);

		return convertto;
	}

	public BusDocInfo getBusdocinfo() {
		return this.busdocinfo;
	}

	public void setBusdocinfo(BusDocInfo busdocinfo) {
		this.busdocinfo = busdocinfo;
	}

}