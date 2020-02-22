package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tablekeys database table.
 * 
 */
@Entity
@Table(name="tablekeys")
@NamedQuery(name="TableKey.findAll", query="SELECT t FROM TableKey t")
public class TableKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String tablename;

	private String nextvalue;

	public TableKey() {
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getNextvalue() {
		return this.nextvalue;
	}

	public void setNextvalue(String nextvalue) {
		this.nextvalue = nextvalue;
	}

}