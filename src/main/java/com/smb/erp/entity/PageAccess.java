package com.smb.erp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.util.Objects;

/**
 * The persistent class for the brand database table.
 *
 */
@Entity
@Table(name = "pageaccess")
public class PageAccess implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessid = 0l;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date wef;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expireson;

    private Boolean c = true;

    private Boolean r = true;

    private Boolean u = true;

    private Boolean d = true;

    @ManyToOne
    @JoinColumn(name = "roleid")
    private EmpRole roleid;

    @ManyToOne
    @JoinColumn(name = "pageid")
    private Webpage pageid;

    @ManyToOne
    @JoinColumn(name = "moduleid")
    private Module moduleid;

    public PageAccess() {
    }

    /**
     * @return the accessid
     */
    public Long getAccessid() {
        return accessid;
    }

    /**
     * @param accessid the accessid to set
     */
    public void setAccessid(Long accessid) {
        this.accessid = accessid;
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
     * @return the expireson
     */
    public Date getExpireson() {
        return expireson;
    }

    /**
     * @param expireson the expireson to set
     */
    public void setExpireson(Date expireson) {
        this.expireson = expireson;
    }

    /**
     * @return the c
     */
    public Boolean getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(Boolean c) {
        this.c = c;
    }

    /**
     * @return the r
     */
    public Boolean getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(Boolean r) {
        this.r = r;
    }

    /**
     * @return the u
     */
    public Boolean getU() {
        return u;
    }

    /**
     * @param u the u to set
     */
    public void setU(Boolean u) {
        this.u = u;
    }

    /**
     * @return the d
     */
    public Boolean getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(Boolean d) {
        this.d = d;
    }

    /**
     * @return the roleid
     */
    public EmpRole getRoleid() {
        return roleid;
    }

    /**
     * @param roleid the roleid to set
     */
    public void setRoleid(EmpRole roleid) {
        this.roleid = roleid;
    }

    /**
     * @return the pageid
     */
    public Webpage getPageid() {
        return pageid;
    }

    /**
     * @param pageid the pageid to set
     */
    public void setPageid(Webpage pageid) {
        this.pageid = pageid;
    }

    /**
     * @return the moduleid
     */
    public Module getModuleid() {
        return moduleid;
    }

    /**
     * @param moduleid the moduleid to set
     */
    public void setModuleid(Module moduleid) {
        this.moduleid = moduleid;
    }

    @Override
    public String toString() {
        return "PageAccess{" + "accessid=" + accessid + ", c=" + c + ", r=" + r + ", u=" + u + ", d=" + d + ", roleid=" + roleid + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.accessid);
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
        final PageAccess other = (PageAccess) obj;
        if (!Objects.equals(this.accessid, other.accessid)) {
            return false;
        }
        return true;
    }

}
