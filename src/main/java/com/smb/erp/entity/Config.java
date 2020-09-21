package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the config database table.
 * 
 */
@Entity
@Table(name = "config")
@NamedQuery(name="Config.findAll", query="SELECT c FROM Config c")
public class Config implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Lob
	private byte[] propertyblobvalue;

	@Lob
	private byte[] propertyclass;

	private String propertyname;

	private String propertytype;

	private String propertyvalue;

	public Config() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getPropertyblobvalue() {
		return this.propertyblobvalue;
	}

	public void setPropertyblobvalue(byte[] propertyblobvalue) {
		this.propertyblobvalue = propertyblobvalue;
	}

	public byte[] getPropertyclass() {
		return this.propertyclass;
	}

	public void setPropertyclass(byte[] propertyclass) {
		this.propertyclass = propertyclass;
	}

	public String getPropertyname() {
		return this.propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	public String getPropertytype() {
		return this.propertytype;
	}

	public void setPropertytype(String propertytype) {
		this.propertytype = propertytype;
	}

	public String getPropertyvalue() {
		return this.propertyvalue;
	}

	public void setPropertyvalue(String propertyvalue) {
		this.propertyvalue = propertyvalue;
	}

}