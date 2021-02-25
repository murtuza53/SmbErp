package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the branch database table.
 *
 */
@Entity
@Table(name = "webpage")
public class Webpage implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pageid;

    private String title;

    private String icon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String listurl;

    private String pageurl;

    private Boolean active = true;

    private Integer menuorder = 0;

    //bi-directional many-to-one association to Company
    @ManyToOne
    @JoinColumn(name = "moduleid")
    private Module moduleid;

    @OneToMany(mappedBy = "pageid", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<PrintReport> reportid;

    public Webpage() {
    }

    /**
     * @return the pageid
     */
    public Long getPageid() {
        return pageid;
    }

    /**
     * @param pageid the pageid to set
     */
    public void setPageid(Long pageid) {
        this.pageid = pageid;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
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
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
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
        return title + " [" + ']';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.pageid);
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
        final Webpage other = (Webpage) obj;
        if (!Objects.equals(this.pageid, other.pageid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the listurl
     */
    public String getListurl() {
        return listurl;
    }

    /**
     * @param listurl the listurl to set
     */
    public void setListurl(String listurl) {
        this.listurl = listurl;
    }

    /**
     * @return the pageurl
     */
    public String getPageurl() {
        return pageurl;
    }

    /**
     * @param pageurl the pageurl to set
     */
    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }

    /**
     * @return the menuorder
     */
    public Integer getMenuorder() {
        return menuorder;
    }

    /**
     * @param menuorder the menuorder to set
     */
    public void setMenuorder(Integer menuorder) {
        this.menuorder = menuorder;
    }

    public String getJsfId() {
        StringBuffer sb = new StringBuffer();
        return sb.append(getModuleid()).append("_").append(getPageid()).toString();
    }

    /**
     * @return the reportid
     */
    public List<PrintReport> getReportid() {
        return reportid;
    }

    /**
     * @param reportid the reportid to set
     */
    public void setReportid(List<PrintReport> reportid) {
        this.reportid = reportid;
    }

    public PrintReport addReportid(PrintReport rpt) {
        getReportid().add(rpt);
        rpt.setPageid(this);

        return rpt;
    }

    public PrintReport removeReportid(PrintReport rpt) {
        getReportid().remove(rpt);
        rpt.setPageid(null);

        return rpt;
    }
}
