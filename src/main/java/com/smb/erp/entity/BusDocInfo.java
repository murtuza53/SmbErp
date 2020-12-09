package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;

/**
 * The persistent class for the busdocinfo database table.
 *
 */
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "busdocinfo")
//@NamedQuery(name = "BusDocInfo.findAll", query = "SELECT b FROM BusDocInfo b")
public class BusDocInfo implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bdinfoid = (int) new Date().getTime();

    private String abbreviation;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdon;

    private String docname;

    private String doctype; //SALES, PURCHASE

    private String menuname;

    private String transactiontype; //NON_INVENTORY, INVENTORY //BusDocTransactionType.NON_INVENTORY.value()
    
    private String accounttype;     //Non, Receivable, Payable

    private String prefix;

    private String suffix;
    
    private String doclisturl;
    
    private String docediturl;

    private String extra1label;

    private String extra1value;

    private String extra2label;

    private String extra2value;

    private String extra3label;

    private String extra3value;

    private String extra4label;

    private String extra4value;

    private String extra5label;

    private String extra5value;

    private String extra6label;

    private String extra6value;

    private String extra7label;

    private String extra7value;

    private String extra8label;

    private String extra8value;

    private String extra9label;

    private String extra9value;

    private String extra10label;

    private String extra10value;

    private String extra11label;

    private String extra11value;

    private String extra12label;

    private String extra12value;

    private String extra13label;

    private String extra13value;

    private String extra14label;

    private String extra14value;

    private String extra15label;

    private String extra15value;

    private String extra16label;

    private String extra16value;

    private String extra17label;

    private String extra17value;

    private String extra18label;

    private String extra18value;

    private String extra19label;

    private String extra19value;

    private String extra20label;

    private String extra20value;

    private String extra21label;

    private String extra21value;

    private String extra22label;

    private String extra22value;

    private String extra23label;

    private String extra23value;

    private String extra24label;

    private String extra24value;

    private String extra25label;

    private String extra25value;

    //bi-directional many-to-one association to BusDoc
    //@OneToMany(mappedBy = "busdocinfo")
    //@Fetch(FetchMode.SUBSELECT)
    //private List<BusDoc> busdocs;
    //bi-directional many-to-one association to BusDoc
    @OneToMany(mappedBy = "bdinfoid", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<AccountTransactionType> transtypeid;

    @OneToMany(mappedBy = "bdinfoid", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<CashRegister> cashregiserid;

    //bi-directional many-to-one association to ConvertTo
    //@OneToMany(mappedBy = "busdocinfo")
    //@Fetch(FetchMode.SUBSELECT)
    //private List<ConvertTo> converttos;
    //bi-directional many-to-one association to DocConversion
    //@OneToMany(mappedBy = "busdocinfo")
    //@Fetch(FetchMode.SUBSELECT)
    //private List<DocConversion> docconversions;
    //@Nullable
    @ManyToMany(mappedBy = "convertfrom", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<BusDocInfo> convertto = new LinkedList<>();

    //@Nullable
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "docconversion",
            joinColumns = {
                @JoinColumn(name = "convertto")},
            inverseJoinColumns = {
                @JoinColumn(name = "convertfrom")})
    private List<BusDocInfo> convertfrom = new LinkedList<>();

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

    public List<AccountTransactionType> getTranstypeid() {
        return transtypeid;
    }

    public void setTranstypeid(List<AccountTransactionType> transtypeid) {
        this.transtypeid = transtypeid;
    }

    public AccountTransactionType addTranstypeid(AccountTransactionType tti) {
        getTranstypeid().add(tti);
        tti.setBdinfoid(this);

        return tti;
    }

    public AccountTransactionType removeTranstypeid(AccountTransactionType tti) {
        getTranstypeid().remove(tti);
        tti.setBdinfoid(null);

        return tti;
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

    /*
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
     */
    @Override
    public String toString() {
        return "BusDocInfo{" + "bdinfoid=" + getBdinfoid() + ", abbreviation=" + getPrefix()+ '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getBdinfoid());
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
     * @return the extra1label
     */
    public String getExtra1label() {
        return extra1label;
    }

    /**
     * @param extra1label the extra1label to set
     */
    public void setExtra1label(String extra1label) {
        this.extra1label = extra1label;
    }

    /**
     * @return the extra1value
     */
    public String getExtra1value() {
        return extra1value;
    }

    /**
     * @param extra1value the extra1value to set
     */
    public void setExtra1value(String extra1value) {
        this.extra1value = extra1value;
    }

    /**
     * @return the extra2label
     */
    public String getExtra2label() {
        return extra2label;
    }

    /**
     * @param extra2label the extra2label to set
     */
    public void setExtra2label(String extra2label) {
        this.extra2label = extra2label;
    }

    /**
     * @return the extra2value
     */
    public String getExtra2value() {
        return extra2value;
    }

    /**
     * @param extra2value the extra2value to set
     */
    public void setExtra2value(String extra2value) {
        this.extra2value = extra2value;
    }

    /**
     * @return the extra3label
     */
    public String getExtra3label() {
        return extra3label;
    }

    /**
     * @param extra3label the extra3label to set
     */
    public void setExtra3label(String extra3label) {
        this.extra3label = extra3label;
    }

    /**
     * @return the extra3value
     */
    public String getExtra3value() {
        return extra3value;
    }

    /**
     * @param extra3value the extra3value to set
     */
    public void setExtra3value(String extra3value) {
        this.extra3value = extra3value;
    }

    /**
     * @return the extra4label
     */
    public String getExtra4label() {
        return extra4label;
    }

    /**
     * @param extra4label the extra4label to set
     */
    public void setExtra4label(String extra4label) {
        this.extra4label = extra4label;
    }

    /**
     * @return the extra4value
     */
    public String getExtra4value() {
        return extra4value;
    }

    /**
     * @param extra4value the extra4value to set
     */
    public void setExtra4value(String extra4value) {
        this.extra4value = extra4value;
    }

    /**
     * @return the extra5label
     */
    public String getExtra5label() {
        return extra5label;
    }

    /**
     * @param extra5label the extra5label to set
     */
    public void setExtra5label(String extra5label) {
        this.extra5label = extra5label;
    }

    /**
     * @return the extra5value
     */
    public String getExtra5value() {
        return extra5value;
    }

    /**
     * @param extra5value the extra5value to set
     */
    public void setExtra5value(String extra5value) {
        this.extra5value = extra5value;
    }

    /**
     * @return the extra6label
     */
    public String getExtra6label() {
        return extra6label;
    }

    /**
     * @param extra6label the extra6label to set
     */
    public void setExtra6label(String extra6label) {
        this.extra6label = extra6label;
    }

    /**
     * @return the extra6value
     */
    public String getExtra6value() {
        return extra6value;
    }

    /**
     * @param extra6value the extra6value to set
     */
    public void setExtra6value(String extra6value) {
        this.extra6value = extra6value;
    }

    /**
     * @return the extra7label
     */
    public String getExtra7label() {
        return extra7label;
    }

    /**
     * @param extra7label the extra7label to set
     */
    public void setExtra7label(String extra7label) {
        this.extra7label = extra7label;
    }

    /**
     * @return the extra7value
     */
    public String getExtra7value() {
        return extra7value;
    }

    /**
     * @param extra7value the extra7value to set
     */
    public void setExtra7value(String extra7value) {
        this.extra7value = extra7value;
    }

    /**
     * @return the extra8label
     */
    public String getExtra8label() {
        return extra8label;
    }

    /**
     * @param extra8label the extra8label to set
     */
    public void setExtra8label(String extra8label) {
        this.extra8label = extra8label;
    }

    /**
     * @return the extra8value
     */
    public String getExtra8value() {
        return extra8value;
    }

    /**
     * @param extra8value the extra8value to set
     */
    public void setExtra8value(String extra8value) {
        this.extra8value = extra8value;
    }

    /**
     * @return the extra9label
     */
    public String getExtra9label() {
        return extra9label;
    }

    /**
     * @param extra9label the extra9label to set
     */
    public void setExtra9label(String extra9label) {
        this.extra9label = extra9label;
    }

    /**
     * @return the extra9value
     */
    public String getExtra9value() {
        return extra9value;
    }

    /**
     * @param extra9value the extra9value to set
     */
    public void setExtra9value(String extra9value) {
        this.extra9value = extra9value;
    }

    /**
     * @return the extra10label
     */
    public String getExtra10label() {
        return extra10label;
    }

    /**
     * @param extra10label the extra10label to set
     */
    public void setExtra10label(String extra10label) {
        this.extra10label = extra10label;
    }

    /**
     * @return the extra10value
     */
    public String getExtra10value() {
        return extra10value;
    }

    /**
     * @param extra10value the extra10value to set
     */
    public void setExtra10value(String extra10value) {
        this.extra10value = extra10value;
    }

    /**
     * @return the extra11label
     */
    public String getExtra11label() {
        return extra11label;
    }

    /**
     * @param extra11label the extra11label to set
     */
    public void setExtra11label(String extra11label) {
        this.extra11label = extra11label;
    }

    /**
     * @return the extra11value
     */
    public String getExtra11value() {
        return extra11value;
    }

    /**
     * @param extra11value the extra11value to set
     */
    public void setExtra11value(String extra11value) {
        this.extra11value = extra11value;
    }

    /**
     * @return the extra12label
     */
    public String getExtra12label() {
        return extra12label;
    }

    /**
     * @param extra12label the extra12label to set
     */
    public void setExtra12label(String extra12label) {
        this.extra12label = extra12label;
    }

    /**
     * @return the extra12value
     */
    public String getExtra12value() {
        return extra12value;
    }

    /**
     * @param extra12value the extra12value to set
     */
    public void setExtra12value(String extra12value) {
        this.extra12value = extra12value;
    }

    /**
     * @return the extra13label
     */
    public String getExtra13label() {
        return extra13label;
    }

    /**
     * @param extra13label the extra13label to set
     */
    public void setExtra13label(String extra13label) {
        this.extra13label = extra13label;
    }

    /**
     * @return the extra13value
     */
    public String getExtra13value() {
        return extra13value;
    }

    /**
     * @param extra13value the extra13value to set
     */
    public void setExtra13value(String extra13value) {
        this.extra13value = extra13value;
    }

    /**
     * @return the extra14label
     */
    public String getExtra14label() {
        return extra14label;
    }

    /**
     * @param extra14label the extra14label to set
     */
    public void setExtra14label(String extra14label) {
        this.extra14label = extra14label;
    }

    /**
     * @return the extra14value
     */
    public String getExtra14value() {
        return extra14value;
    }

    /**
     * @param extra14value the extra14value to set
     */
    public void setExtra14value(String extra14value) {
        this.extra14value = extra14value;
    }

    /**
     * @return the extra15label
     */
    public String getExtra15label() {
        return extra15label;
    }

    /**
     * @param extra15label the extra15label to set
     */
    public void setExtra15label(String extra15label) {
        this.extra15label = extra15label;
    }

    /**
     * @return the extra15value
     */
    public String getExtra15value() {
        return extra15value;
    }

    /**
     * @param extra15value the extra15value to set
     */
    public void setExtra15value(String extra15value) {
        this.extra15value = extra15value;
    }

    /**
     * @return the extra16label
     */
    public String getExtra16label() {
        return extra16label;
    }

    /**
     * @param extra16label the extra16label to set
     */
    public void setExtra16label(String extra16label) {
        this.extra16label = extra16label;
    }

    /**
     * @return the extra16value
     */
    public String getExtra16value() {
        return extra16value;
    }

    /**
     * @param extra16value the extra16value to set
     */
    public void setExtra16value(String extra16value) {
        this.extra16value = extra16value;
    }

    /**
     * @return the extra17label
     */
    public String getExtra17label() {
        return extra17label;
    }

    /**
     * @param extra17label the extra17label to set
     */
    public void setExtra17label(String extra17label) {
        this.extra17label = extra17label;
    }

    /**
     * @return the extra17value
     */
    public String getExtra17value() {
        return extra17value;
    }

    /**
     * @param extra17value the extra17value to set
     */
    public void setExtra17value(String extra17value) {
        this.extra17value = extra17value;
    }

    /**
     * @return the extra18label
     */
    public String getExtra18label() {
        return extra18label;
    }

    /**
     * @param extra18label the extra18label to set
     */
    public void setExtra18label(String extra18label) {
        this.extra18label = extra18label;
    }

    /**
     * @return the extra18value
     */
    public String getExtra18value() {
        return extra18value;
    }

    /**
     * @param extra18value the extra18value to set
     */
    public void setExtra18value(String extra18value) {
        this.extra18value = extra18value;
    }

    /**
     * @return the extra19label
     */
    public String getExtra19label() {
        return extra19label;
    }

    /**
     * @param extra19label the extra19label to set
     */
    public void setExtra19label(String extra19label) {
        this.extra19label = extra19label;
    }

    /**
     * @return the extra19value
     */
    public String getExtra19value() {
        return extra19value;
    }

    /**
     * @param extra19value the extra19value to set
     */
    public void setExtra19value(String extra19value) {
        this.extra19value = extra19value;
    }

    /**
     * @return the extra20label
     */
    public String getExtra20label() {
        return extra20label;
    }

    /**
     * @param extra20label the extra20label to set
     */
    public void setExtra20label(String extra20label) {
        this.extra20label = extra20label;
    }

    /**
     * @return the extra20value
     */
    public String getExtra20value() {
        return extra20value;
    }

    /**
     * @param extra20value the extra20value to set
     */
    public void setExtra20value(String extra20value) {
        this.extra20value = extra20value;
    }

    /**
     * @return the extra21label
     */
    public String getExtra21label() {
        return extra21label;
    }

    /**
     * @param extra21label the extra21label to set
     */
    public void setExtra21label(String extra21label) {
        this.extra21label = extra21label;
    }

    /**
     * @return the extra21value
     */
    public String getExtra21value() {
        return extra21value;
    }

    /**
     * @param extra21value the extra21value to set
     */
    public void setExtra21value(String extra21value) {
        this.extra21value = extra21value;
    }

    /**
     * @return the extra22label
     */
    public String getExtra22label() {
        return extra22label;
    }

    /**
     * @param extra22label the extra22label to set
     */
    public void setExtra22label(String extra22label) {
        this.extra22label = extra22label;
    }

    /**
     * @return the extra22value
     */
    public String getExtra22value() {
        return extra22value;
    }

    /**
     * @param extra22value the extra22value to set
     */
    public void setExtra22value(String extra22value) {
        this.extra22value = extra22value;
    }

    /**
     * @return the extra23label
     */
    public String getExtra23label() {
        return extra23label;
    }

    /**
     * @param extra23label the extra23label to set
     */
    public void setExtra23label(String extra23label) {
        this.extra23label = extra23label;
    }

    /**
     * @return the extra23value
     */
    public String getExtra23value() {
        return extra23value;
    }

    /**
     * @param extra23value the extra23value to set
     */
    public void setExtra23value(String extra23value) {
        this.extra23value = extra23value;
    }

    /**
     * @return the extra24label
     */
    public String getExtra24label() {
        return extra24label;
    }

    /**
     * @param extra24label the extra24label to set
     */
    public void setExtra24label(String extra24label) {
        this.extra24label = extra24label;
    }

    /**
     * @return the extra24value
     */
    public String getExtra24value() {
        return extra24value;
    }

    /**
     * @param extra24value the extra24value to set
     */
    public void setExtra24value(String extra24value) {
        this.extra24value = extra24value;
    }

    /**
     * @return the extra25label
     */
    public String getExtra25label() {
        return extra25label;
    }

    /**
     * @param extra25label the extra25label to set
     */
    public void setExtra25label(String extra25label) {
        this.extra25label = extra25label;
    }

    /**
     * @return the extra25value
     */
    public String getExtra25value() {
        return extra25value;
    }

    /**
     * @param extra25value the extra25value to set
     */
    public void setExtra25value(String extra25value) {
        this.extra25value = extra25value;
    }

    /**
     * @return the converto
     */
    public List<BusDocInfo> getConvertto() {
        return convertto;
    }

    /**
     * @param converto the converto to set
     */
    public void setConverto(List<BusDocInfo> converto) {
        this.convertto = convertto;
    }

    public void addConvertFrom(BusDocInfo docinfo) {
        convertfrom.add(docinfo);
    }

    /**
     * @return the convertfrom
     */
    public List<BusDocInfo> getConvertfrom() {
        return convertfrom;
    }

    /**
     * @param convertfrom the convertfrom to set
     */
    public void setConvertfrom(List<BusDocInfo> convertfrom) {
        this.convertfrom = convertfrom;
    }

    /**
     * @return the cashregiserid
     */
    public List<CashRegister> getCashregiserid() {
        return cashregiserid;
    }

    /**
     * @param cashregiserid the cashregiserid to set
     */
    public void setCashregiserid(List<CashRegister> cashregiserid) {
        this.cashregiserid = cashregiserid;
    }

    public CashRegister addCashregisterid(CashRegister cr) {
        getCashregiserid().add(cr);
        cr.setBdinfoid(this);

        return cr;
    }

    public CashRegister removeCashregisterid(CashRegister cr) {
        getCashregiserid().remove(cr);
        cr.setBdinfoid(null);

        return cr;
    }
    
    public boolean hasCashRegister(){
        if(getCashregiserid()==null || getCashregiserid().isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * @return the doclisturl
     */
    public String getDoclisturl() {
        return doclisturl;
    }

    /**
     * @param doclisturl the doclisturl to set
     */
    public void setDoclisturl(String doclisturl) {
        this.doclisturl = doclisturl;
    }

    /**
     * @return the docediturl
     */
    public String getDocediturl() {
        return docediturl;
    }

    /**
     * @param docediturl the docediturl to set
     */
    public void setDocediturl(String docediturl) {
        this.docediturl = docediturl;
    }

    /**
     * @return the accounttype
     */
    public String getAccounttype() {
        return accounttype;
    }

    /**
     * @param accounttype the accounttype to set
     */
    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

}
