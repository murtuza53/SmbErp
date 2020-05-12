package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The persistent class for the busdocinfo database table.
 *
 */
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
@Entity
@NamedQuery(name = "BusDocInfo.findAll", query = "SELECT b FROM BusDocInfo b")
public class BusDocInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bdinfoid;

    private String abbreviation;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String docname;

    private String doctype; //SALES, PURCHASE

    private String menuname;

    private String transactiontype; //NON_INVENTORY, INVENTORY_ONLY, INVENTORY_ACCOUNT //BusDocTransactionType.NON_INVENTORY.value()

    private String prefix;

    private String suffix;

    //bi-directional many-to-one association to BusDoc
    @OneToMany(mappedBy = "busdocinfo")
    private List<BusDoc> busdocs;

    //bi-directional many-to-one association to ConvertTo
    @OneToMany(mappedBy = "busdocinfo")
    private List<ConvertTo> converttos;

    //bi-directional many-to-one association to DocConversion
    @OneToMany(mappedBy = "busdocinfo")
    private List<DocConversion> docconversions;

    public BusDocInfo() {
    }

    public Integer getBdinfoid() {
        return this.bdinfoid;
    }

    public void setBdinfoid(Integer bdinfoid) {
        this.bdinfoid = bdinfoid;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Date getCreatedon() {
        return this.createdon;
    }

    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    public String getDocname() {
        return this.docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDoctype() {
        return this.doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getMenuname() {
        return this.menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getTransactiontype() {
        return this.transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public List<BusDoc> getBusdocs() {
        return this.busdocs;
    }

    public void setBusdocs(List<BusDoc> busdocs) {
        this.busdocs = busdocs;
    }

    public BusDoc addBusdoc(BusDoc busdoc) {
        getBusdocs().add(busdoc);
        busdoc.setBusdocinfo(this);

        return busdoc;
    }

    public BusDoc removeBusdoc(BusDoc busdoc) {
        getBusdocs().remove(busdoc);
        busdoc.setBusdocinfo(null);

        return busdoc;
    }

    public List<ConvertTo> getConverttos() {
        return this.converttos;
    }

    public void setConverttos(List<ConvertTo> converttos) {
        this.converttos = converttos;
    }

    public ConvertTo addConvertto(ConvertTo convertto) {
        getConverttos().add(convertto);
        convertto.setBusdocinfo(this);

        return convertto;
    }

    public ConvertTo removeConvertto(ConvertTo convertto) {
        getConverttos().remove(convertto);
        convertto.setBusdocinfo(null);

        return convertto;
    }

    public List<DocConversion> getDocconversions() {
        return this.docconversions;
    }

    public void setDocconversions(List<DocConversion> docconversions) {
        this.docconversions = docconversions;
    }

    public DocConversion addDocconversion(DocConversion docconversion) {
        getDocconversions().add(docconversion);
        docconversion.setBusdocinfo(this);

        return docconversion;
    }

    public DocConversion removeDocconversion(DocConversion docconversion) {
        getDocconversions().remove(docconversion);
        docconversion.setBusdocinfo(null);

        return docconversion;
    }

    @Override
    public String toString() {
        return "BusDocInfo{" + "bdinfoid=" + bdinfoid + ", abbreviation=" + abbreviation + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.bdinfoid);
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
        final BusDocInfo other = (BusDocInfo) obj;
        if (!Objects.equals(this.bdinfoid, other.bdinfoid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
