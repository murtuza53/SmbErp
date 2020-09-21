package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the busdoc database table.
 *
 */
@Entity
@Table(name = "busdoc")
@NamedQuery(name = "BusDoc.findAll", query = "SELECT b FROM BusDoc b")
public class BusDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String docno;

    @Temporal(TemporalType.TIMESTAMP)
    private Date docdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String description;

    private String extra1;

    private String extra2;

    private String extra3;

    private String extra4;

    private String extra5;

    private String extra6;

    private String extra7;

    private String extra8;

    private String extra9;

    private String extra10;

    private Double extra11 = 0.0;

    private Double extra12 = 0.0;

    private Double extra13 = 0.0;

    private Double extra14 = 0.0;

    private Double extra15 = 0.0;

    private Double extra16 = 0.0;

    private Double extra17 = 0.0;

    private Double extra18 = 0.0;

    private Double extra19 = 0.0;

    private Double extra20 = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra21;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra22;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra23;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra24;

    @Temporal(TemporalType.TIMESTAMP)
    private Date extra25;

    private String manualdocno;

    private boolean markdeleted;

    private String refno;

    private Double subtotal = 0.0;

    private Double discount = 0.0;

    private Double totalamount = 0.0;

    private Double totalvat = 0.0;

    private Double grandtotal = 0.0;
    
    private Double totalcost = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedon;

    //bi-directional many-to-one association to ContactPerson
    @ManyToOne
    @JoinColumn(name = "cpid")
    private ContactPerson contactperson;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "empid")
    private Emp emp1;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "inaccountid")
    private Emp emp2;

    //bi-directional many-to-one association to BusDoc
    @ManyToOne
    @JoinColumn(name = "fromdocno")
    private BusDoc busdoc;

    //bi-directional many-to-one association to BusDoc
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "paytermsid")
    private PayTerms paytermsid;

    //bi-directional many-to-one association to BusDoc
    //@OneToMany(mappedBy="busdoc")
    //private List<BusDoc> busdocs;
    @OneToMany(mappedBy = "busdoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductTransaction> productTransactions;

    //bi-directional many-to-one association to BusDocInfo
    @ManyToOne
    @JoinColumn(name = "busdocinfoid")
    private BusDocInfo busdocinfo;

    //bi-directional many-to-one association to BusinessPartner
    @ManyToOne
    @JoinColumn(name = "partnerid")
    private BusinessPartner businesspartner;

    //bi-directional many-to-one association to Company
    //@ManyToOne
    //@JoinColumn(name = "companyid")
    //private Company company;
    @ManyToOne
    @JoinColumn(name = "branchno")
    private Branch branch;

    //bi-directional many-to-one association to PartialPaymentDetail
    //@OneToMany(mappedBy="busdoc1")
    //private List<PartialPaymentDetail> ppdetails1;
    //bi-directional many-to-one association to PartialPaymentDetail
    //@OneToMany(mappedBy="busdoc2")
    //private List<PartialPaymentDetail> ppdetails2;
    //bi-directional many-to-one association to PartialPaymentDetail
    //@OneToMany(mappedBy="busdoc3")
    //private List<PartialPaymentDetail> ppdetails3;
    //bi-directional many-to-one association to PartialPaymentDetail
    //@OneToMany(mappedBy="busdoc4")
    //private List<PartialPaymentDetail> ppdetails4;
    //bi-directional many-to-one association to PartialPaymentDetail
    //@OneToMany(mappedBy="busdoc5")
    //private List<PartialPaymentDetail> ppdetails5;
    public BusDoc() {
    }

    public String getDocno() {
        return this.docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public Date getCreatedon() {
        return this.createdon;
    }

    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra1() {
        return this.extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
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

    public String getExtra6() {
        return this.extra6;
    }

    public void setExtra6(String extra6) {
        this.extra6 = extra6;
    }

    public String getExtra7() {
        return this.extra7;
    }

    public void setExtra7(String extra7) {
        this.extra7 = extra7;
    }

    public String getExtra8() {
        return this.extra8;
    }

    public void setExtra8(String extra8) {
        this.extra8 = extra8;
    }

    public String getExtra9() {
        return this.extra9;
    }

    public void setExtra9(String extra9) {
        this.extra9 = extra9;
    }

    public String getExtra10() {
        return this.extra10;
    }

    public void setExtra10(String extra10) {
        this.extra10 = extra10;
    }

    /**
     * @return the extra11
     */
    public Double getExtra11() {
        return extra11;
    }

    /**
     * @param extra11 the extra11 to set
     */
    public void setExtra11(Double extra11) {
        this.extra11 = extra11;
    }

    /**
     * @return the extra12
     */
    public Double getExtra12() {
        return extra12;
    }

    /**
     * @param extra12 the extra12 to set
     */
    public void setExtra12(Double extra12) {
        this.extra12 = extra12;
    }

    /**
     * @return the extra13
     */
    public Double getExtra13() {
        return extra13;
    }

    /**
     * @param extra13 the extra13 to set
     */
    public void setExtra13(Double extra13) {
        this.extra13 = extra13;
    }

    /**
     * @return the extra14
     */
    public Double getExtra14() {
        return extra14;
    }

    /**
     * @param extra14 the extra14 to set
     */
    public void setExtra14(Double extra14) {
        this.extra14 = extra14;
    }

    /**
     * @return the extra15
     */
    public Double getExtra15() {
        return extra15;
    }

    /**
     * @param extra15 the extra15 to set
     */
    public void setExtra15(Double extra15) {
        this.extra15 = extra15;
    }

    /**
     * @return the extra16
     */
    public Double getExtra16() {
        return extra16;
    }

    /**
     * @param extra16 the extra16 to set
     */
    public void setExtra16(Double extra16) {
        this.extra16 = extra16;
    }

    /**
     * @return the extra17
     */
    public Double getExtra17() {
        return extra17;
    }

    /**
     * @param extra17 the extra17 to set
     */
    public void setExtra17(Double extra17) {
        this.extra17 = extra17;
    }

    /**
     * @return the extra18
     */
    public Double getExtra18() {
        return extra18;
    }

    /**
     * @param extra18 the extra18 to set
     */
    public void setExtra18(Double extra18) {
        this.extra18 = extra18;
    }

    /**
     * @return the extra19
     */
    public Double getExtra19() {
        return extra19;
    }

    /**
     * @param extra19 the extra19 to set
     */
    public void setExtra19(Double extra19) {
        this.extra19 = extra19;
    }

    /**
     * @return the extra20
     */
    public Double getExtra20() {
        return extra20;
    }

    /**
     * @param extra20 the extra20 to set
     */
    public void setExtra20(Double extra20) {
        this.extra20 = extra20;
    }

    /**
     * @return the extra21
     */
    public Date getExtra21() {
        return extra21;
    }

    /**
     * @param extra21 the extra21 to set
     */
    public void setExtra21(Date extra21) {
        this.extra21 = extra21;
    }

    /**
     * @return the extra22
     */
    public Date getExtra22() {
        return extra22;
    }

    /**
     * @param extra22 the extra22 to set
     */
    public void setExtra22(Date extra22) {
        this.extra22 = extra22;
    }

    /**
     * @return the extra23
     */
    public Date getExtra23() {
        return extra23;
    }

    /**
     * @param extra23 the extra23 to set
     */
    public void setExtra23(Date extra23) {
        this.extra23 = extra23;
    }

    /**
     * @return the extra24
     */
    public Date getExtra24() {
        return extra24;
    }

    /**
     * @param extra24 the extra24 to set
     */
    public void setExtra24(Date extra24) {
        this.extra24 = extra24;
    }

    /**
     * @return the extra25
     */
    public Date getExtra25() {
        return extra25;
    }

    /**
     * @param extra25 the extra25 to set
     */
    public void setExtra25(Date extra25) {
        this.extra25 = extra25;
    }

    public String getManualdocno() {
        return this.manualdocno;
    }

    public void setManualdocno(String manualdocno) {
        this.manualdocno = manualdocno;
    }

    public boolean getMarkdeleted() {
        return this.markdeleted;
    }

    public void setMarkdeleted(boolean markdeleted) {
        this.markdeleted = markdeleted;
    }

    public String getRefno() {
        return this.refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public Date getUpdatedon() {
        return this.updatedon;
    }

    public void setUpdatedon(Date updatedon) {
        this.updatedon = updatedon;
    }

    public ContactPerson getContactperson() {
        return this.contactperson;
    }

    public void setContactperson(ContactPerson contactperson) {
        this.contactperson = contactperson;
    }

    public Emp getEmp1() {
        return this.emp1;
    }

    public void setEmp1(Emp emp1) {
        this.emp1 = emp1;
    }

    public Emp getEmp2() {
        return this.emp2;
    }

    public void setEmp2(Emp emp2) {
        this.emp2 = emp2;
    }

    public BusDoc getBusdoc() {
        return this.busdoc;
    }

    public void setBusdoc(BusDoc busdoc) {
        this.busdoc = busdoc;
    }

    /*public List<BusDoc> getBusdocs() {
		return this.busdocs;
	}

	public void setBusdocs(List<BusDoc> busdocs) {
		this.busdocs = busdocs;
	}

	public BusDoc addBusdoc(BusDoc busdoc) {
		getBusdocs().add(busdoc);
		busdoc.setBusdoc(this);

		return busdoc;
	}

	public BusDoc removeBusdoc(BusDoc busdoc) {
		getBusdocs().remove(busdoc);
		busdoc.setBusdoc(null);

		return busdoc;
	}*/
    public BusDocInfo getBusdocinfo() {
        return this.busdocinfo;
    }

    public void setBusdocinfo(BusDocInfo busdocinfo) {
        this.busdocinfo = busdocinfo;
    }

    /**
     * @return the paytermsid
     */
    public PayTerms getPaytermsid() {
        return paytermsid;
    }

    /**
     * @param paytermsid the paytermsid to set
     */
    public void setPaytermsid(PayTerms paytermsid) {
        this.paytermsid = paytermsid;
    }

    public BusinessPartner getBusinesspartner() {
        return this.businesspartner;
    }

    public void setBusinesspartner(BusinessPartner businesspartner) {
        this.businesspartner = businesspartner;
    }

    /*public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }*/

    /**
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * @return the totalvat
     */
    public Double getTotalvat() {
        return totalvat;
    }

    /**
     * @param totalvat the totalvat to set
     */
    public void setTotalvat(Double totalvat) {
        this.totalvat = totalvat;
    }

    /*public List<PartialPaymentDetail> getPpdetails1() {
		return this.ppdetails1;
	}

	public void setPpdetails1(List<PartialPaymentDetail> ppdetails1) {
		this.ppdetails1 = ppdetails1;
	}

	public PartialPaymentDetail addPpdetails1(PartialPaymentDetail ppdetails1) {
		getPpdetails1().add(ppdetails1);
		ppdetails1.setBusdoc1(this);

		return ppdetails1;
	}

	public PartialPaymentDetail removePpdetails1(PartialPaymentDetail ppdetails1) {
		getPpdetails1().remove(ppdetails1);
		ppdetails1.setBusdoc1(null);

		return ppdetails1;
	}

	public List<PartialPaymentDetail> getPpdetails2() {
		return this.ppdetails2;
	}

	public void setPpdetails2(List<PartialPaymentDetail> ppdetails2) {
		this.ppdetails2 = ppdetails2;
	}

	public PartialPaymentDetail addPpdetails2(PartialPaymentDetail ppdetails2) {
		getPpdetails2().add(ppdetails2);
		ppdetails2.setBusdoc2(this);

		return ppdetails2;
	}

	public PartialPaymentDetail removePpdetails2(PartialPaymentDetail ppdetails2) {
		getPpdetails2().remove(ppdetails2);
		ppdetails2.setBusdoc2(null);

		return ppdetails2;
	}

	public List<PartialPaymentDetail> getPpdetails3() {
		return this.ppdetails3;
	}

	public void setPpdetails3(List<PartialPaymentDetail> ppdetails3) {
		this.ppdetails3 = ppdetails3;
	}

	public PartialPaymentDetail addPpdetails3(PartialPaymentDetail ppdetails3) {
		getPpdetails3().add(ppdetails3);
		ppdetails3.setBusdoc3(this);

		return ppdetails3;
	}

	public PartialPaymentDetail removePpdetails3(PartialPaymentDetail ppdetails3) {
		getPpdetails3().remove(ppdetails3);
		ppdetails3.setBusdoc3(null);

		return ppdetails3;
	}

	public List<PartialPaymentDetail> getPpdetails4() {
		return this.ppdetails4;
	}

	public void setPpdetails4(List<PartialPaymentDetail> ppdetails4) {
		this.ppdetails4 = ppdetails4;
	}

	public PartialPaymentDetail addPpdetails4(PartialPaymentDetail ppdetails4) {
		getPpdetails4().add(ppdetails4);
		ppdetails4.setBusdoc4(this);

		return ppdetails4;
	}

	public PartialPaymentDetail removePpdetails4(PartialPaymentDetail ppdetails4) {
		getPpdetails4().remove(ppdetails4);
		ppdetails4.setBusdoc4(null);

		return ppdetails4;
	}

	public List<PartialPaymentDetail> getPpdetails5() {
		return this.ppdetails5;
	}

	public void setPpdetails5(List<PartialPaymentDetail> ppdetails5) {
		this.ppdetails5 = ppdetails5;
	}

	public PartialPaymentDetail addPpdetails5(PartialPaymentDetail ppdetails5) {
		getPpdetails5().add(ppdetails5);
		ppdetails5.setBusdoc5(this);

		return ppdetails5;
	}

	public PartialPaymentDetail removePpdetails5(PartialPaymentDetail ppdetails5) {
		getPpdetails5().remove(ppdetails5);
		ppdetails5.setBusdoc5(null);

		return ppdetails5;
	}*/
    /**
     * @return the productTransactions
     */
    public List<ProductTransaction> getProductTransactions() {
        return productTransactions;
    }

    /**
     * @param productTransactions the productTransactions to set
     */
    public void setProductTransactions(List<ProductTransaction> productTransactions) {
        this.productTransactions = productTransactions;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.docno);
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
        final BusDoc other = (BusDoc) obj;
        if (!Objects.equals(this.docno, other.docno)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return docno;
    }

    public void refreshTotal() {
        if (getProductTransactions() != null) {
            setSubtotal((Double) getProductTransactions().stream().mapToDouble(x -> x.getSubtotal()).sum());
            if (getSubtotal() == 0) {
                setSubtotal((Double) getProductTransactions().stream().mapToDouble(x -> x.getLineSubtotal()).sum());
            }
            setTotalamount((Double) getSubtotal() - discount);
            totalvat = getProductTransactions().stream().mapToDouble(x -> x.getVatamount()).sum();
            setGrandtotal((Double) getTotalamount() + totalvat);
        }
    }

    /**
     * @return the subtotal
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the totalamount
     */
    public Double getTotalamount() {
        return totalamount;
    }

    /**
     * @param totalamount the totalamount to set
     */
    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

    /**
     * @return the grandtotal
     */
    public Double getGrandtotal() {
        return grandtotal;
    }

    /**
     * @param grandtotal the grandtotal to set
     */
    public void setGrandtotal(Double grandtotal) {
        this.grandtotal = grandtotal;
    }

    /**
     * @return the totalcost
     */
    public Double getTotalcost() {
        return totalcost;
    }

    /**
     * @param totalcost the totalcost to set
     */
    public void setTotalcost(Double totalcost) {
        this.totalcost = totalcost;
    }

    /**
     * @return the branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return the docdate
     */
    public Date getDocdate() {
        return docdate;
    }

    /**
     * @param docdate the docdate to set
     */
    public void setDocdate(Date docdate) {
        this.docdate = docdate;
        System.out.println(docno + ": " + this.docdate);
    }
    
    public String getTitleBadge(){
        if(getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.SALES.getValue())){
            return "status-instock";
        } else if (getBusdocinfo().getDoctype().equalsIgnoreCase(BusDocType.PURCHASE.getValue())){
            return "status-outofstock";
        }
        return "status-lowstock";
    }

}
