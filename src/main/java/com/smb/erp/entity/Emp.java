package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the emp database table.
 *
 */
@Entity
@NamedQuery(name = "Emp.findAll", query = "SELECT e FROM Emp e")
public class Emp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String empid;

    private String addressstate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;

    @Temporal(TemporalType.TIMESTAMP)
    private Date doj;

    private String emailaddress;

    private String empname;

    private String fulladdress;

    private String gender;

    private String mobileno1;

    private String mobileno2;

    private String phoneno;

    private String post;

    private double salary;

    //bi-directional many-to-one association to AccDoc
    @OneToMany(mappedBy = "emp")
    private List<AccDoc> accdocs;

    //bi-directional many-to-one association to Asset
    @OneToMany(mappedBy = "emp")
    private List<Asset> assets;

    //bi-directional many-to-one association to BusDoc
    @OneToMany(mappedBy = "emp1")
    private List<BusDoc> busdocs1;

    //bi-directional many-to-one association to BusDoc
    @OneToMany(mappedBy = "emp2")
    private List<BusDoc> busdocs2;

    //bi-directional many-to-one association to Dependant
    @OneToMany(mappedBy = "emp")
    private List<Dependant> dependants;

    //bi-directional many-to-one association to Dept
    @ManyToOne
    @JoinColumn(name = "deptid")
    private Dept dept;

    //bi-directional many-to-one association to Emp
    @ManyToOne
    @JoinColumn(name = "managerid")
    private Emp emp;

    //bi-directional many-to-one association to Emp
    @OneToMany(mappedBy = "emp")
    private List<Emp> emps;

    //bi-directional many-to-one association to Authentication
    @ManyToOne
    @JoinColumn(name = "username")
    private Authentication authentication;

    //bi-directional many-to-one association to Company
    @ManyToOne
    @JoinColumn(name = "companyid")
    private Company company;

    public Emp() {
    }

    public String getEmpid() {
        return this.empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getAddressstate() {
        return this.addressstate;
    }

    public void setAddressstate(String addressstate) {
        this.addressstate = addressstate;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDoj() {
        return this.doj;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public String getEmailaddress() {
        return this.emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getEmpname() {
        return this.empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getFulladdress() {
        return this.fulladdress;
    }

    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileno1() {
        return this.mobileno1;
    }

    public void setMobileno1(String mobileno1) {
        this.mobileno1 = mobileno1;
    }

    public String getMobileno2() {
        return this.mobileno2;
    }

    public void setMobileno2(String mobileno2) {
        this.mobileno2 = mobileno2;
    }

    public String getPhoneno() {
        return this.phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPost() {
        return this.post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<AccDoc> getAccdocs() {
        return this.accdocs;
    }

    public void setAccdocs(List<AccDoc> accdocs) {
        this.accdocs = accdocs;
    }

    public AccDoc addAccdoc(AccDoc accdoc) {
        getAccdocs().add(accdoc);
        accdoc.setEmp(this);

        return accdoc;
    }

    public AccDoc removeAccdoc(AccDoc accdoc) {
        getAccdocs().remove(accdoc);
        accdoc.setEmp(null);

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
        asset.setEmp(this);

        return asset;
    }

    public Asset removeAsset(Asset asset) {
        getAssets().remove(asset);
        asset.setEmp(null);

        return asset;
    }

    public List<BusDoc> getBusdocs1() {
        return this.busdocs1;
    }

    public void setBusdocs1(List<BusDoc> busdocs1) {
        this.busdocs1 = busdocs1;
    }

    public BusDoc addBusdocs1(BusDoc busdocs1) {
        getBusdocs1().add(busdocs1);
        busdocs1.setEmp1(this);

        return busdocs1;
    }

    public BusDoc removeBusdocs1(BusDoc busdocs1) {
        getBusdocs1().remove(busdocs1);
        busdocs1.setEmp1(null);

        return busdocs1;
    }

    public List<BusDoc> getBusdocs2() {
        return this.busdocs2;
    }

    public void setBusdocs2(List<BusDoc> busdocs2) {
        this.busdocs2 = busdocs2;
    }

    public BusDoc addBusdocs2(BusDoc busdocs2) {
        getBusdocs2().add(busdocs2);
        busdocs2.setEmp2(this);

        return busdocs2;
    }

    public BusDoc removeBusdocs2(BusDoc busdocs2) {
        getBusdocs2().remove(busdocs2);
        busdocs2.setEmp2(null);

        return busdocs2;
    }

    public List<Dependant> getDependants() {
        return this.dependants;
    }

    public void setDependants(List<Dependant> dependants) {
        this.dependants = dependants;
    }

    public Dependant addDependant(Dependant dependant) {
        getDependants().add(dependant);
        dependant.setEmp(this);

        return dependant;
    }

    public Dependant removeDependant(Dependant dependant) {
        getDependants().remove(dependant);
        dependant.setEmp(null);

        return dependant;
    }

    public Dept getDept() {
        return this.dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Emp getEmp() {
        return this.emp;
    }

    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    public List<Emp> getEmps() {
        return this.emps;
    }

    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }

    public Emp addEmp(Emp emp) {
        getEmps().add(emp);
        emp.setEmp(this);

        return emp;
    }

    public Emp removeEmp(Emp emp) {
        getEmps().remove(emp);
        emp.setEmp(null);

        return emp;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Emp{" + "empid=" + empid + ", empname=" + empname + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.empid);
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
        final Emp other = (Emp) obj;
        if (!Objects.equals(this.empid, other.empid)) {
            return false;
        }
        return true;
    }

}
