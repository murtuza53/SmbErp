package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the dbversion database table.
 * 
 */
@Entity
@Table(name = "dbversion")
@NamedQuery(name="DbVersion.findAll", query="SELECT d FROM DbVersion d")
public class DbVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int dbVersionno;

	@Temporal(TemporalType.DATE)
	private Date datecreated;

	private String fileurl;

	private String updatetype;

	public DbVersion() {
	}

	public int getDbVersionno() {
		return this.dbVersionno;
	}

	public void setDbVersionno(int dbVersionno) {
		this.dbVersionno = dbVersionno;
	}

	public Date getDatecreated() {
		return this.datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public String getFileurl() {
		return this.fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public String getUpdatetype() {
		return this.updatetype;
	}

	public void setUpdatetype(String updatetype) {
		this.updatetype = updatetype;
	}

}