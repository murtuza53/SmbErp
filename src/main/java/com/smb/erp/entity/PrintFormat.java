package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the printformat database table.
 * 
 */
@Entity
@Table(name = "printformat")
@NamedQuery(name="PrintFormat.findAll", query="SELECT p FROM PrintFormat p")
public class PrintFormat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int printno;

	private byte defaultprint;

	private double extranumber1;

	private double extranumber2;

	private double extranumber3;

	private double extranumber4;

	private double extranumber5;

	private String extratext1;

	private String extratext2;

	private String extratext3;

	private String extratext4;

	private String extratext5;

	private double footersize;

	private double headersize;

	private String linkedto;

	private double pageheight;

	private int pageorientation;

	private double pagewidth;

	private int printmode;

	private String printpagefooter;

	private String printpageheader;

	private String printsummary;

	private String reportname;

	private String reporttitle;

	@Lob
	private String xmllayout;

	public PrintFormat() {
	}

	public int getPrintno() {
		return this.printno;
	}

	public void setPrintno(int printno) {
		this.printno = printno;
	}

	public byte getDefaultprint() {
		return this.defaultprint;
	}

	public void setDefaultprint(byte defaultprint) {
		this.defaultprint = defaultprint;
	}

	public double getExtranumber1() {
		return this.extranumber1;
	}

	public void setExtranumber1(double extranumber1) {
		this.extranumber1 = extranumber1;
	}

	public double getExtranumber2() {
		return this.extranumber2;
	}

	public void setExtranumber2(double extranumber2) {
		this.extranumber2 = extranumber2;
	}

	public double getExtranumber3() {
		return this.extranumber3;
	}

	public void setExtranumber3(double extranumber3) {
		this.extranumber3 = extranumber3;
	}

	public double getExtranumber4() {
		return this.extranumber4;
	}

	public void setExtranumber4(double extranumber4) {
		this.extranumber4 = extranumber4;
	}

	public double getExtranumber5() {
		return this.extranumber5;
	}

	public void setExtranumber5(double extranumber5) {
		this.extranumber5 = extranumber5;
	}

	public String getExtratext1() {
		return this.extratext1;
	}

	public void setExtratext1(String extratext1) {
		this.extratext1 = extratext1;
	}

	public String getExtratext2() {
		return this.extratext2;
	}

	public void setExtratext2(String extratext2) {
		this.extratext2 = extratext2;
	}

	public String getExtratext3() {
		return this.extratext3;
	}

	public void setExtratext3(String extratext3) {
		this.extratext3 = extratext3;
	}

	public String getExtratext4() {
		return this.extratext4;
	}

	public void setExtratext4(String extratext4) {
		this.extratext4 = extratext4;
	}

	public String getExtratext5() {
		return this.extratext5;
	}

	public void setExtratext5(String extratext5) {
		this.extratext5 = extratext5;
	}

	public double getFootersize() {
		return this.footersize;
	}

	public void setFootersize(double footersize) {
		this.footersize = footersize;
	}

	public double getHeadersize() {
		return this.headersize;
	}

	public void setHeadersize(double headersize) {
		this.headersize = headersize;
	}

	public String getLinkedto() {
		return this.linkedto;
	}

	public void setLinkedto(String linkedto) {
		this.linkedto = linkedto;
	}

	public double getPageheight() {
		return this.pageheight;
	}

	public void setPageheight(double pageheight) {
		this.pageheight = pageheight;
	}

	public int getPageorientation() {
		return this.pageorientation;
	}

	public void setPageorientation(int pageorientation) {
		this.pageorientation = pageorientation;
	}

	public double getPagewidth() {
		return this.pagewidth;
	}

	public void setPagewidth(double pagewidth) {
		this.pagewidth = pagewidth;
	}

	public int getPrintmode() {
		return this.printmode;
	}

	public void setPrintmode(int printmode) {
		this.printmode = printmode;
	}

	public String getPrintpagefooter() {
		return this.printpagefooter;
	}

	public void setPrintpagefooter(String printpagefooter) {
		this.printpagefooter = printpagefooter;
	}

	public String getPrintpageheader() {
		return this.printpageheader;
	}

	public void setPrintpageheader(String printpageheader) {
		this.printpageheader = printpageheader;
	}

	public String getPrintsummary() {
		return this.printsummary;
	}

	public void setPrintsummary(String printsummary) {
		this.printsummary = printsummary;
	}

	public String getReportname() {
		return this.reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getReporttitle() {
		return this.reporttitle;
	}

	public void setReporttitle(String reporttitle) {
		this.reporttitle = reporttitle;
	}

	public String getXmllayout() {
		return this.xmllayout;
	}

	public void setXmllayout(String xmllayout) {
		this.xmllayout = xmllayout;
	}

}