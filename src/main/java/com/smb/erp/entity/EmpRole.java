package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@Table(name = "emprole")
public class EmpRole implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleid = 0l;

    private String rolename;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date wef;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expires;
    
    private String successurl;
    
    private Integer level;

    @ManyToMany(mappedBy="emproles", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Emp> emps;

    public EmpRole() {
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
     * @return the roleid
     */
    public Long getRoleid() {
        return roleid;
    }

    /**
     * @param roleid the roleid to set
     */
    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }

    /**
     * @return the rolename
     */
    public String getRolename() {
        return rolename;
    }

    /**
     * @param rolename the rolename to set
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    /**
     * @return the createdon
     */
    public Date getCreatedon() {
        return createdon;
    }

    /**
     * @param createdon the createdon to set
     */
    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
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
     * @return the expires
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * @param expires the expires to set
     */
    public void setExpires(Date expires) {
        this.expires = expires;
    }

    /**
     * @return the successurl
     */
    public String getSuccessurl() {
        return successurl;
    }

    /**
     * @param successurl the successurl to set
     */
    public void setSuccessurl(String successurl) {
        this.successurl = successurl;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return rolename + " [" + "roleid=" + roleid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.roleid);
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
        final EmpRole other = (EmpRole) obj;
        if (!Objects.equals(this.roleid, other.roleid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the emps
     */
    public List<Emp> getEmps() {
        return emps;
    }

    /**
     * @param emps the emps to set
     */
    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }


}
