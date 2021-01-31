package com.smb.erp.entity;

import java.io.Serializable;
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
@Table(name = "module")
public class Module implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleid;

    private String modulename;

    private String icon;

    private Boolean active = true;

    private String url;

    @OneToMany(mappedBy = "moduleid", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(FetchMode.SUBSELECT)
    private List<Webpage> webpages;

    public Module() {
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
     * @return the moduleid
     */
    public Long getModuleid() {
        return moduleid;
    }

    /**
     * @param moduleid the moduleid to set
     */
    public void setModuleid(Long moduleid) {
        this.moduleid = moduleid;
    }

    /**
     * @return the modulename
     */
    public String getModulename() {
        return modulename;
    }

    /**
     * @param modulename the modulename to set
     */
    public void setModulename(String modulename) {
        this.modulename = modulename;
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
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return modulename + " [" + moduleid + ']';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.moduleid);
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
        final Module other = (Module) obj;
        if (!Objects.equals(this.moduleid, other.moduleid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the webpages
     */
    public List<Webpage> getWebpages() {
        return webpages;
    }

    /**
     * @param webpages the webpages to set
     */
    public void setWebpages(List<Webpage> webpages) {
        this.webpages = webpages;
    }

}
