package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the busdocinfo database table.
 * 
 */
@Entity
@NamedQuery(name="BusDocInfo.findAll", query="SELECT b FROM BusDocInfo b")
public class BusDocInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int bdinfoid;

	private String abbreviation;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	private String docname;

	private String doctype;

	private String menuname;

	private String transactiontype;

	//bi-directional many-to-one association to BusDoc
	@OneToMany(mappedBy="busdocinfo")
	private List<BusDoc> busdocs;

	//bi-directional many-to-one association to ConvertTo
	@OneToMany(mappedBy="busdocinfo")
	private List<ConvertTo> converttos;

	//bi-directional many-to-one association to DocConversion
	@OneToMany(mappedBy="busdocinfo")
	private List<DocConversion> docconversions;

	public BusDocInfo() {
	}

	public int getBdinfoid() {
		return this.bdinfoid;
	}

	public void setBdinfoid(int bdinfoid) {
		this.bdinfoid = bdinfoid;
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

	public String getDocname() {
		return this.docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public String getDoctype() {
		return this.doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getMenuname() {
		return this.menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getTransactiontype() {
		return this.transactiontype;
	}

	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	public List<BusDoc> getBusdocs() {
		return this.busdocs;
	}

	public void setBusdocs(List<BusDoc> busdocs) {
		this.busdocs = busdocs;
	}

	public BusDoc addBusdoc(BusDoc busdoc) {
		getBusdocs().add(busdoc);
		busdoc.setBusdocinfo(this);

		return busdoc;
	}

	public BusDoc removeBusdoc(BusDoc busdoc) {
		getBusdocs().remove(busdoc);
		busdoc.setBusdocinfo(null);

		return busdoc;
	}

	public List<ConvertTo> getConverttos() {
		return this.converttos;
	}

	public void setConverttos(List<ConvertTo> converttos) {
		this.converttos = converttos;
	}

	public ConvertTo addConvertto(ConvertTo convertto) {
		getConverttos().add(convertto);
		convertto.setBusdocinfo(this);

		return convertto;
	}

	public ConvertTo removeConvertto(ConvertTo convertto) {
		getConverttos().remove(convertto);
		convertto.setBusdocinfo(null);

		return convertto;
	}

	public List<DocConversion> getDocconversions() {
		return this.docconversions;
	}

	public void setDocconversions(List<DocConversion> docconversions) {
		this.docconversions = docconversions;
	}

	public DocConversion addDocconversion(DocConversion docconversion) {
		getDocconversions().add(docconversion);
		docconversion.setBusdocinfo(this);

		return docconversion;
	}

	public DocConversion removeDocconversion(DocConversion docconversion) {
		getDocconversions().remove(docconversion);
		docconversion.setBusdocinfo(null);

		return docconversion;
	}

}