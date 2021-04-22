package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the dept database table.
 *
 */
@Entity
@Table(name = "dept")
@NamedQuery(name = "Dept.findAll", query = "SELECT d FROM Dept d")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String deptid;

    private String abbreviation;

    private String deptname;

    private String description;

    //bi-directional many-to-one association to Company
    @ManyToOne
    @JoinColumn(name = "companyid")
    private Company company;

    //bi-directional many-to-one association to Emp
    //@OneToMany(mappedBy="dept")
    //private List<Emp> emps;
    public Dept() {
    }

    public String getDeptid() {
        return this.deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDeptname() {
        return this.deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    /*public List<Emp> getEmps() {
		return this.emps;
	}

	public void setEmps(List<Emp> emps) {
		this.emps = emps;
	}

	public Emp addEmp(Emp emp) {
		getEmps().add(emp);
		emp.setDept(this);

		return emp;
	}

	public Emp removeEmp(Emp emp) {
		getEmps().remove(emp);
		emp.setDept(null);

		return emp;
	}*/
}
