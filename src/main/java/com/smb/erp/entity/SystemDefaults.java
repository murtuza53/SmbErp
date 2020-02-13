package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 * The persistent class for the config database table.
 *
 */
@Entity
@NamedQuery(name = "SystemDefaults.findAll", query = "SELECT a FROM SystemDefaults a ORDER BY a.propertyname")
public class SystemDefaults implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer defaultid;

    private String propertyname;

    private String value;

    private String valuetype;

    private String description;
    
    private String referredclass;

    public SystemDefaults() {
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
     * @return the defaultid
     */
    public Integer getDefaultid() {
        return defaultid;
    }

    /**
     * @param defaultid the defaultid to set
     */
    public void setDefaultid(Integer defaultid) {
        this.defaultid = defaultid;
    }

    /**
     * @return the propertyname
     */
    public String getPropertyname() {
        return propertyname;
    }

    /**
     * @param propertyname the propertyname to set
     */
    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the valuetype
     */
    public String getValuetype() {
        return valuetype;
    }

    /**
     * @param valuetype the valuetype to set
     */
    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.defaultid);
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
        final SystemDefaults other = (SystemDefaults) obj;
        if (!Objects.equals(this.defaultid, other.defaultid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getPropertyname() + ": " + getValue();
    }

    /**
     * @return the referredclass
     */
    public String getReferredclass() {
        return referredclass;
    }

    /**
     * @param referredclass the referredclass to set
     */
    public void setReferredclass(String referredclass) {
        this.referredclass = referredclass;
    }

}
