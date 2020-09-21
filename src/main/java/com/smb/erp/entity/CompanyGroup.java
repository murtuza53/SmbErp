package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the companygroup database table.
 *
 */
@Entity
@Table(name = "companygroup")
@NamedQuery(name = "CompanyGroup.findAll", query = "SELECT c FROM CompanyGroup c ORDER BY c.groupname")
public class CompanyGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comgroupid = 0;

    private String addresss;

    private String crno;

    private String fax;

    private String groupname;

    private String mob;

    @Temporal(TemporalType.DATE)
    private Date openingdate;

    private String phone;

    //bi-directional many-to-one association to Company
    @OneToMany(mappedBy = "companygroup")
    private List<Company> companies;

    public CompanyGroup() {
    }

    public Integer getComgroupid() {
        return this.comgroupid;
    }

    public void setComgroupid(Integer comgroupid) {
        this.comgroupid = comgroupid;
    }

    public String getAddresss() {
        return this.addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getCrno() {
        return this.crno;
    }

    public void setCrno(String crno) {
        this.crno = crno;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getGroupname() {
        return this.groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getMob() {
        return this.mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public Date getOpeningdate() {
        return this.openingdate;
    }

    public void setOpeningdate(Date openingdate) {
        this.openingdate = openingdate;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Company> getCompanies() {
        return this.companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public Company addCompany(Company company) {
        getCompanies().add(company);
        company.setCompanygroup(this);

        return company;
    }

    public Company removeCompany(Company company) {
        getCompanies().remove(company);
        company.setCompanygroup(null);

        return company;
    }

    @Override
    public String toString() {
        return "CompanyGroup{" + "comgroupid=" + comgroupid + ", groupname=" + groupname + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.comgroupid);
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
        final CompanyGroup other = (CompanyGroup) obj;
        if (!Objects.equals(this.comgroupid, other.comgroupid)) {
            return false;
        }
        return true;
    }

}
