package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the company database table.
 *
 */
@Entity
@NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c ORDER BY c.companyname")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyid = 0;

    private String addresss;

    private String companyname;

    private String crno;

    private String fax;

    private String mob;

    @Temporal(TemporalType.DATE)
    private Date openingdate;

    private String phone;

    //bi-directional many-to-one association to BusDoc
    //@OneToMany(mappedBy = "company")
    //private List<BusDoc> busdocs;

    //bi-directional many-to-one association to CompanyGroup
    @ManyToOne
    @JoinColumn(name = "comgroupid")
    private CompanyGroup companygroup;

    //bi-directional many-to-one association to Dept
    @OneToMany(mappedBy = "company")
    private List<Dept> depts;

    //bi-directional many-to-one association to Emp
    @OneToMany(mappedBy = "company")
    private List<Emp> emps;

    //bi-directional many-to-one association to Warehouse
    @OneToMany(mappedBy = "company")
    private List<Branch> branches;

    public Company() {
    }

    public Integer getCompanyid() {
        return this.companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public String getAddresss() {
        return this.addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getCompanyname() {
        return this.companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
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

    /*public List<BusDoc> getBusdocs() {
        return this.busdocs;
    }

    public void setBusdocs(List<BusDoc> busdocs) {
        this.busdocs = busdocs;
    }

    public BusDoc addBusdoc(BusDoc busdoc) {
        getBusdocs().add(busdoc);
        busdoc.setCompany(this);

        return busdoc;
    }

    public BusDoc removeBusdoc(BusDoc busdoc) {
        getBusdocs().remove(busdoc);
        busdoc.setCompany(null);

        return busdoc;
    }*/

    public CompanyGroup getCompanygroup() {
        return this.companygroup;
    }

    public void setCompanygroup(CompanyGroup companygroup) {
        this.companygroup = companygroup;
    }

    public List<Dept> getDepts() {
        return this.depts;
    }

    public void setDepts(List<Dept> depts) {
        this.depts = depts;
    }

    public Dept addDept(Dept dept) {
        getDepts().add(dept);
        dept.setCompany(this);

        return dept;
    }

    public Dept removeDept(Dept dept) {
        getDepts().remove(dept);
        dept.setCompany(null);

        return dept;
    }

    public List<Emp> getEmps() {
        return this.emps;
    }

    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }

    public Emp addEmp(Emp emp) {
        getEmps().add(emp);
        emp.setCompany(this);

        return emp;
    }

    public Emp removeEmp(Emp emp) {
        getEmps().remove(emp);
        emp.setCompany(null);

        return emp;
    }

    public List<Branch> getBranches() {
        return this.branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return companyname + " [" + companyid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.companyid;
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
        final Company other = (Company) obj;
        if (this.companyid != other.companyid) {
            return false;
        }
        return true;
    }

}
