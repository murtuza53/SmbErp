package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@Table(name = "payterms")
public class PayTerms implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paytermsid = 0l;

    private String paytype;
    
    private String validitydays;
    
    private Integer creditperiod;

    private String deliveryterms;

    private String deliverydays;

    private String extra1;

    private String extra2;

    private String extra3;

    public PayTerms() {
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @return the paytermsid
     */
    public Long getPaytermsid() {
        return paytermsid;
    }

    /**
     * @param paytermsid the paytermsid to set
     */
    public void setPaytermsid(Long paytermsid) {
        this.paytermsid = paytermsid;
    }

    /**
     * @return the paytype
     */
    public String getPaytype() {
        return paytype;
    }

    /**
     * @param paytype the paytype to set
     */
    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    /**
     * @return the validitydays
     */
    public String getValiditydays() {
        return validitydays;
    }

    /**
     * @param validitydays the validitydays to set
     */
    public void setValiditydays(String validitydays) {
        this.validitydays = validitydays;
    }

    /**
     * @return the creditperiod
     */
    public Integer getCreditperiod() {
        return creditperiod;
    }

    /**
     * @param creditperiod the creditperiod to set
     */
    public void setCreditperiod(Integer creditperiod) {
        this.creditperiod = creditperiod;
    }

    /**
     * @return the deliveryterms
     */
    public String getDeliveryterms() {
        return deliveryterms;
    }

    /**
     * @param deliveryterms the deliveryterms to set
     */
    public void setDeliveryterms(String deliveryterms) {
        this.deliveryterms = deliveryterms;
    }

    /**
     * @return the deliverydays
     */
    public String getDeliverydays() {
        return deliverydays;
    }

    /**
     * @param deliverydays the deliverydays to set
     */
    public void setDeliverydays(String deliverydays) {
        this.deliverydays = deliverydays;
    }

    /**
     * @return the extra1
     */
    public String getExtra1() {
        return extra1;
    }

    /**
     * @param extra1 the extra1 to set
     */
    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    /**
     * @return the extra2
     */
    public String getExtra2() {
        return extra2;
    }

    /**
     * @param extra2 the extra2 to set
     */
    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    /**
     * @return the extra3
     */
    public String getExtra3() {
        return extra3;
    }

    /**
     * @param extra3 the extra3 to set
     */
    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.paytermsid);
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
        final PayTerms other = (PayTerms) obj;
        if (!Objects.equals(this.paytermsid, other.paytermsid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return paytype;
    }

}
