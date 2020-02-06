package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the accdoc database table.
 *
 */
@Entity
@NamedQuery(name = "AccDoc.findAll", query = "SELECT a FROM AccDoc a")
public class AccDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String docno;

    private String dcol;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date docdate;

    private String extra1;

    private double extra10;

    private byte extra11;

    private byte extra12;

    private String extra2;

    private String extra3;

    private String extra4;

    private String extra5;

    private double extra6;

    private double extra7;

    private double extra8;

    private double extra9;

    private String manualno;

    private String refno;

    //bi-directional many-to-one association to Branch
    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    //bi-directional many-to-one association to BusinessPartner
    @ManyToOne
    @JoinColumn(name = "companyno")
    private BusinessPartner businesspartner;

    //bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "countryno")
    private Country country;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "empno")
    private Emp emp;

    //bi-directional many-to-one association to LederLine
    @OneToMany(mappedBy = "accdoc")
    private List<LederLine> ledlines;

    public AccDoc() {
    }

    public String getDocno() {
        return this.docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getDcol() {
        return this.dcol;
    }

    public void setDcol(String dcol) {
        this.dcol = dcol;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDocdate() {
        return this.docdate;
    }

    public void setDocdate(Date docdate) {
        this.docdate = docdate;
    }

    public String getExtra1() {
        return this.extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public double getExtra10() {
        return this.extra10;
    }

    public void setExtra10(double extra10) {
        this.extra10 = extra10;
    }

    public byte getExtra11() {
        return this.extra11;
    }

    public void setExtra11(byte extra11) {
        this.extra11 = extra11;
    }

    public byte getExtra12() {
        return this.extra12;
    }

    public void setExtra12(byte extra12) {
        this.extra12 = extra12;
    }

    public String getExtra2() {
        return this.extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return this.extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return this.extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getExtra5() {
        return this.extra5;
    }

    public void setExtra5(String extra5) {
        this.extra5 = extra5;
    }

    public double getExtra6() {
        return this.extra6;
    }

    public void setExtra6(double extra6) {
        this.extra6 = extra6;
    }

    public double getExtra7() {
        return this.extra7;
    }

    public void setExtra7(double extra7) {
        this.extra7 = extra7;
    }

    public double getExtra8() {
        return this.extra8;
    }

    public void setExtra8(double extra8) {
        this.extra8 = extra8;
    }

    public double getExtra9() {
        return this.extra9;
    }

    public void setExtra9(double extra9) {
        this.extra9 = extra9;
    }

    public String getManualno() {
        return this.manualno;
    }

    public void setManualno(String manualno) {
        this.manualno = manualno;
    }

    public String getRefno() {
        return this.refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BusinessPartner getBusinesspartner() {
        return this.businesspartner;
    }

    public void setBusinesspartner(BusinessPartner businesspartner) {
        this.businesspartner = businesspartner;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Emp getEmp() {
        return this.emp;
    }

    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    public List<LederLine> getLedlines() {
        return this.ledlines;
    }

    public void setLedlines(List<LederLine> ledlines) {
        this.ledlines = ledlines;
    }

    public LederLine addLedline(LederLine ledline) {
        getLedlines().add(ledline);
        ledline.setAccdoc(this);

        return ledline;
    }

    public LederLine removeLedline(LederLine ledline) {
        getLedlines().remove(ledline);
        ledline.setAccdoc(null);

        return ledline;
    }

    @Override
    public String toString() {
        return "AccDoc{" + "docno=" + docno + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.docno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccDoc other = (AccDoc) obj;
        if (!Objects.equals(this.docno, other.docno)) {
            return false;
        }
        return true;
    }

}
