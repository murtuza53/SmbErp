package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * The persistent class for the branch database table.
 *
 */
@Entity
@Table(name = "branch")
@NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch b ORDER BY b.branchname")
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchid = new Date().getTime();

    private String abbreviation;

    private String branchname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date crexpirydate;

    private String crno;

    private String emailaddress;

    private String fax;

    private String footer;

    private String header;

    private String location;

    private String logo;

    private String tel1;

    private String tel2;

    private String website;

    //bi-directional many-to-one association to Company
    @ManyToOne
    @JoinColumn(name="companyid")
    private Company company;
    
    @Transient
    private String companyAddress;
    
    @Transient
    private String vatNo;

    @Transient
    private String companyName;
    
    public Branch() {
    }

    public Branch(String abbreviation, String branchname) {
        this.abbreviation = abbreviation;
        this.branchname = branchname;
    }

    public Long getBranchid() {
        return this.branchid;
    }

    public void setBranchid(Long branchid) {
        this.branchid = branchid;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getBranchname() {
        return this.branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public Date getCrexpirydate() {
        return this.crexpirydate;
    }

    public void setCrexpirydate(Date crexpirydate) {
        this.crexpirydate = crexpirydate;
    }

    public String getCrno() {
        return this.crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getEmailaddress() {
        return this.emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTel1() {
        return this.tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return this.tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /*public List<AccDoc> getAccdocs() {
        return this.accdocs;
    }

    public void setAccdocs(List<AccDoc> accdocs) {
        this.accdocs = accdocs;
    }

    public AccDoc addAccdoc(AccDoc accdoc) {
        getAccdocs().add(accdoc);
        accdoc.setBranch(this);

        return accdoc;
    }

    public AccDoc removeAccdoc(AccDoc accdoc) {
        getAccdocs().remove(accdoc);
        accdoc.setBranch(null);

        return accdoc;
    }

    public List<Asset> getAssets() {
        return this.assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public Asset addAsset(Asset asset) {
        getAssets().add(asset);
        asset.setBranchBean(this);

        return asset;
    }

    public Asset removeAsset(Asset asset) {
        getAssets().remove(asset);
        asset.setBranchBean(null);

        return asset;
    }

    public List<LederLine> getLedlines() {
        return this.ledlines;
    }

    public void setLedlines(List<LederLine> ledlines) {
        this.ledlines = ledlines;
    }

    public LederLine addLedline(LederLine ledline) {
        getLedlines().add(ledline);
        ledline.setBranch(this);

        return ledline;
    }

    public LederLine removeLedline(LederLine ledline) {
        getLedlines().remove(ledline);
        ledline.setBranch(null);

        return ledline;
    }*/

    @Override
    public String toString() {
        return branchname + " [" + branchid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.branchid);
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
        final Branch other = (Branch) obj;
        if (!Objects.equals(this.branchid, other.branchid)) {
            return false;
        }
        return true;
    }

    /*public List<ProductTransaction> getProdtransactions() {
        return prodtransactions;
    }

    public void setProdtransactions(List<ProductTransaction> prodtransactions) {
        this.prodtransactions = prodtransactions;
    }*/

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * @return the companyAddress
     */
    public String getCompanyAddress() {
        return getCompany().getAddresss();
    }

    /**
     * @param companyAddress the companyAddress to set
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    /**
     * @return the vatNo
     */
    public String getVatNo() {
        return getCompany().getVatno();
    }

    /**
     * @param vatNo the vatNo to set
     */
    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return getCompany().getCompanyname();
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
