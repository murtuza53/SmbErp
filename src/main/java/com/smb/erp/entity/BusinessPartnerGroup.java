package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The persistent class for the companygroup database table.
 *
 */
@Entity
@Table(name = "businesspartnergroup")
public class BusinessPartnerGroup implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    private Long bpgroupid = 0l;

    private String groupname;

    private String fulladdress;

    private String trnno;

    private String tel;

    private String fax;

    private String email;


    public BusinessPartnerGroup() {
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
     * @return the bpgroupid
     */
    public Long getBpgroupid() {
        return bpgroupid;
    }

    /**
     * @param bpgroupid the bpgroupid to set
     */
    public void setBpgroupid(Long bpgroupid) {
        this.bpgroupid = bpgroupid;
    }

    /**
     * @return the groupname
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * @param groupname the groupname to set
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    /**
     * @return the fulladdress
     */
    public String getFulladdress() {
        return fulladdress;
    }

    /**
     * @param fulladdress the fulladdress to set
     */
    public void setFulladdress(String fulladdress) {
        this.fulladdress = fulladdress;
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
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return bpgroupid + " [" + bpgroupid + ']';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.bpgroupid);
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
        final BusinessPartnerGroup other = (BusinessPartnerGroup) obj;
        if (!Objects.equals(this.bpgroupid, other.bpgroupid)) {
            return false;
        }
        return true;
    }

}
