package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@NamedQuery(name = "VatBusinessRegister.findAll", query = "SELECT v FROM VatBusinessRegister v ORDER BY v.vatregisterid")
public class VatBusinessRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer vatregisterid;

    private String countrystate;

    private String trnno;

    @Temporal(TemporalType.TIMESTAMP)
    private Date wef;

    @ManyToOne
    @JoinColumn(name = "countryno")
    private Country countryno;

    @ManyToOne
    @JoinColumn(name = "vatcategoryid")
    private VatCategory vatcategoryid;

    @ManyToOne
    @JoinColumn(name = "vataccounttypeid")
    private VatAccountType vataccounttypeid;

    @ManyToOne
    @JoinColumn(name = "accountid")
    private Account accountid;

    @ManyToOne
    @JoinColumn(name = "partnerid")
    private BusinessPartner partnerid;

    public VatBusinessRegister() {
    }

    /**
     * @return the vatregisterid
     */
    public Integer getVatregisterid() {
        return vatregisterid;
    }

    /**
     * @param vatregisterid the vatregisterid to set
     */
    public void setVatregisterid(Integer vatregisterid) {
        this.vatregisterid = vatregisterid;
    }

    /**
     * @return the state
     */
    public String getCountryState() {
        return countrystate;
    }

    /**
     * @param state the state to set
     */
    public void setCountryState(String state) {
        this.countrystate = state;
    }

    /**
     * @return the trnno
     */
    public String getTrnno() {
        return trnno;
    }

    /**
     * @param trnno the trnno to set
     */
    public void setTrnno(String trnno) {
        this.trnno = trnno;
    }

    /**
     * @return the wef
     */
    public Date getWef() {
        return wef;
    }

    /**
     * @param wef the wef to set
     */
    public void setWef(Date wef) {
        this.wef = wef;
    }

    /**
     * @return the countryno
     */
    public Country getCountryno() {
        return countryno;
    }

    /**
     * @param countryno the countryno to set
     */
    public void setCountryno(Country countryno) {
        this.countryno = countryno;
    }

    /**
     * @return the vatcategoryid
     */
    public VatCategory getVatcategoryid() {
        return vatcategoryid;
    }

    /**
     * @param vatcategoryid the vatcategoryid to set
     */
    public void setVatcategoryid(VatCategory vatcategoryid) {
        this.vatcategoryid = vatcategoryid;
    }

    /**
     * @return the vataccounttypeid
     */
    public VatAccountType getVataccounttypeid() {
        return vataccounttypeid;
    }

    /**
     * @param vataccounttypeid the vataccounttypeid to set
     */
    public void setVataccounttypeid(VatAccountType vataccounttypeid) {
        this.vataccounttypeid = vataccounttypeid;
    }

    /**
     * @return the accountid
     */
    public Account getAccountid() {
        return accountid;
    }

    /**
     * @param accountid the accountid to set
     */
    public void setAccountid(Account accountid) {
        this.accountid = accountid;
    }

    @Override
    public String toString() {
        return "VatBusinessRegister{" + "vatregisterid=" + vatregisterid + ", trnno=" + trnno + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.vatregisterid);
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
        final VatBusinessRegister other = (VatBusinessRegister) obj;
        if (!Objects.equals(this.vatregisterid, other.vatregisterid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the partnerid
     */
    public BusinessPartner getPartnerid() {
        return partnerid;
    }

    /**
     * @param partnerid the partnerid to set
     */
    public void setPartnerid(BusinessPartner partnerid) {
        this.partnerid = partnerid;
    }

}
