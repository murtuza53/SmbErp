package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the convertto database table.
 * 
 */
@Entity
@NamedQuery(name="ConvertTo.findAll", query="SELECT c FROM ConvertTo c")
public class ConvertTo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int converttoid;

	//bi-directional many-to-one association to BusDocInfo
	@ManyToOne
	@JoinColumn(name="bdinfoid")
	private BusDocInfo busdocinfo;

	//bi-directional many-to-one association to DocConversion
	//@ManyToOne
	//@JoinColumn(name="docconid")
	//private DocConversion docconversion;

	public ConvertTo() {
	}

	public int getConverttoid() {
		return this.converttoid;
	}

	public void setConverttoid(int converttoid) {
		this.converttoid = converttoid;
	}

	public BusDocInfo getBusdocinfo() {
		return this.busdocinfo;
	}

	public void setBusdocinfo(BusDocInfo busdocinfo) {
		this.busdocinfo = busdocinfo;
	}

	/*public DocConversion getDocconversion() {
		return this.docconversion;
	}

	public void setDocconversion(DocConversion docconversion) {
		this.docconversion = docconversion;
	}*/

}