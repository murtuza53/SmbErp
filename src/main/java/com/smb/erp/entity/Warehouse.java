package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the warehouse database table.
 * 
 */
@Entity
@NamedQuery(name="Warehouse.findAll", query="SELECT w FROM Warehouse w")
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int warehouseid;

	private String abbreviation;

	private String addresss;

	private String crno;

	private String fax;

	private String mob;

	private String name;

	@Temporal(TemporalType.DATE)
	private Date openingdate;

	private String phone;

	//bi-directional many-to-one association to ProductTransaction
	@OneToMany(mappedBy="warehouse")
	private List<ProductTransaction> prodtransactions;

	//bi-directional many-to-one association to Company
	@ManyToOne
	@JoinColumn(name="companyid")
	private Company company;

	public Warehouse() {
	}

	public int getWarehouseid() {
		return this.warehouseid;
	}

	public void setWarehouseid(int warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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

	public String getMob() {
		return this.mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<ProductTransaction> getProdtransactions() {
		return this.prodtransactions;
	}

	public void setProdtransactions(List<ProductTransaction> prodtransactions) {
		this.prodtransactions = prodtransactions;
	}

	public ProductTransaction addProdtransaction(ProductTransaction prodtransaction) {
		getProdtransactions().add(prodtransaction);
		prodtransaction.setWarehouse(this);

		return prodtransaction;
	}

	public ProductTransaction removeProdtransaction(ProductTransaction prodtransaction) {
		getProdtransactions().remove(prodtransaction);
		prodtransaction.setWarehouse(null);

		return prodtransaction;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.warehouseid;
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
        final Warehouse other = (Warehouse) obj;
        if (this.warehouseid != other.warehouseid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Warehouse{" + "abbreviation=" + abbreviation + '}';
    }

}