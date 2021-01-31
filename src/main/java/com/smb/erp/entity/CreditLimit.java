package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * The persistent class for the creditlimit database table.
 *
 */
@Entity
@Table(name = "creditlimit")
@NamedQuery(name = "CreditLimit.findAll", query = "SELECT c FROM CreditLimit c")
public class CreditLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Integer creditlimitno = (int)new Date().getTime();

    private Double amountfc = 0.0;

    private Double amountlc = 0.0;

    private Integer daysduration = 0;

    private Double rate = 1.0;

    //bi-directional many-to-one association to BusinessPartner
    //@OneToMany(mappedBy="creditlimit")
    //private List<BusinessPartner> businesspartners;
    public CreditLimit() {
    }

    public CreditLimit(int no) {
        this.creditlimitno = no;
    }

    public Integer getCreditlimitno() {
        return this.creditlimitno;
    }

    public void setCreditlimitno(Integer creditlimitno) {
        this.creditlimitno = creditlimitno;
    }

    public Double getAmountfc() {
        return this.amountfc;
    }

    public void setAmountfc(Double amountfc) {
        this.amountfc = amountfc;
    }

    public Double getAmountlc() {
        return this.amountlc;
    }

    public void setAmountlc(Double amountlc) {
        this.amountlc = amountlc;
    }

    public Integer getDaysduration() {
        return this.daysduration;
    }

    public void setDaysduration(Integer daysduration) {
        this.daysduration = daysduration;
    }

    public Double getRate() {
        return this.rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    /*public List<BusinessPartner> getBusinesspartners() {
		return this.businesspartners;
	}

	public void setBusinesspartners(List<BusinessPartner> businesspartners) {
		this.businesspartners = businesspartners;
	}

	public BusinessPartner addBusinesspartner(BusinessPartner businesspartner) {
		getBusinesspartners().add(businesspartner);
		businesspartner.setCreditlimit(this);

		return businesspartner;
	}

	public BusinessPartner removeBusinesspartner(BusinessPartner businesspartner) {
		getBusinesspartners().remove(businesspartner);
		businesspartner.setCreditlimit(null);

		return businesspartner;
	}*/
    @Override
    public String toString() {
        return "CreditLimit{" + "creditlimitno=" + creditlimitno + ", amountfc=" + amountfc + ", amountlc=" + amountlc + ", daysduration=" + daysduration + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.creditlimitno);
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
        final CreditLimit other = (CreditLimit) obj;
        if (!Objects.equals(this.creditlimitno, other.creditlimitno)) {
            return false;
        }
        return true;
    }

}
