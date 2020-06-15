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
    private Date createdon;

    private String description;

    private String extra1;

    private Double extra10=0.0;

    private String extra2;

    private String extra3;

    private String extra4;

    private String extra5;

    private Double extra6=0.0;

    private Double extra7=0.0;

    private Double extra8=0.0;

    private Double extra9=0.0;

    private String manualdocno;

    private byte markdeleted;

    private String refno;

    private Double totalAmount = 0.0;
    
    private Double discount = 0.0;
    
    private Double grossamount = 0.0;
    
    private Double totalvat = 0.0;
    
    private Double netamount = 0.0;

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
    @ManyToOne
    @JoinColumn(name = "companyid")
    private Company company;

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

    public Double getExtra10() {
        return this.extra10;
    }

    public void setExtra10(Double extra10) {
        this.extra10 = extra10;
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

    public Double getExtra6() {
        return this.extra6;
    }

    public void setExtra6(Double extra6) {
        this.extra6 = extra6;
    }

    public Double getExtra7() {
        return this.extra7;
    }

    public void setExtra7(Double extra7) {
        this.extra7 = extra7;
    }

    public Double getExtra8() {
        return this.extra8;
    }

    public void setExtra8(Double extra8) {
        this.extra8 = extra8;
    }

    public Double getExtra9() {
        return this.extra9;
    }

    public void setExtra9(Double extra9) {
        this.extra9 = extra9;
    }

    public String getManualdocno() {
        return this.manualdocno;
    }

    public void setManualdocno(String manualdocno) {
        this.manualdocno = manualdocno;
    }

    public byte getMarkdeleted() {
        return this.markdeleted;
    }

    public void setMarkdeleted(byte markdeleted) {
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

    public BusinessPartner getBusinesspartner() {
        return this.businesspartner;
    }

    public void setBusinesspartner(BusinessPartner businesspartner) {
        this.businesspartner = businesspartner;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

        /**
     * @return the totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

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
     * @return the grossamount
     */
    public Double getGrossamount() {
        return grossamount;
    }

    /**
     * @param grossamount the grossamount to set
     */
    public void setGrossamount(Double grossamount) {
        this.grossamount = grossamount;
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

    /**
     * @return the netamount
     */
    public Double getNetamount() {
        return netamount;
    }

    /**
     * @param netamount the netamount to set
     */
    public void setNetamount(Double netamount) {
        this.netamount = netamount;
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
    
    public void refreshTotal(){
        if(getProductTransactions()!=null){
            totalAmount = getProductTransactions().stream().mapToDouble(x->x.getTotal()).sum();
            if(totalAmount==0){
                totalAmount = getProductTransactions().stream().mapToDouble(x->x.getLineTotal()).sum();
            }
            grossamount = totalAmount - discount;
            totalvat = getProductTransactions().stream().mapToDouble(x->x.getVatamount()).sum();
            netamount = grossamount + totalvat;
        }
    }

}
