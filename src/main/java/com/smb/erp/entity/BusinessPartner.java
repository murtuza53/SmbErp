package com.smb.erp.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * The persistent class for the businesspartner database table.
 *
 */
@Entity
@Table(name = "businesspartner")
@NamedQuery(name = "BusinessPartner.findAll", query = "SELECT b FROM BusinessPartner b ORDER BY b.companyname")
public class BusinessPartner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long partnerid;

    private String address;

    private String companyname;

    private String companytypes;        //Customer, Supplier

    private String creditstatus;        //Cash, 30 Days, 60 Days, 90 Days, PDC

    private String description;

    private String email1;

    private String email2;

    private String fax1;

    private String fax2;

    private boolean lporequired;

    private String pbone2;

    private String phone1;

    private String pobox;

    private String website;

    private String shopno;

    private String buildingno;

    private String roadno;

    private String blockno;

    private String town;

    private String area;

    @XmlTransient
    @Transient
    public static final String CUSTOMER = "Customer";

    @XmlTransient
    @Transient
    public static final String SUPPLIER = "Supplier";

    //bi-directional many-to-one association to AccDoc
    //@OneToMany(mappedBy="businesspartner")
    //private List<AccDoc> accdocs;
    //bi-directional many-to-one association to Account
    @OneToMany(mappedBy = "businesspartner", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Account> accounts;
    //bi-directional many-to-one association to BusDoc
    //@OneToMany(mappedBy="businesspartner")
    //private List<BusDoc> busdocs;
    //bi-directional many-to-one association to Country
    @ManyToOne
    @JoinColumn(name = "countryno")
    private Country country;

    //bi-directional many-to-one association to CreditLimit
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "creditlimitno")
    private CreditLimit creditlimit;

    //bi-directional many-to-one association to ContactPerson
    @OneToMany(mappedBy = "businesspartner", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ContactPerson> contactpersons;
    //bi-directional many-to-one association to PriceList
    //@OneToMany(mappedBy="businesspartner")
    //private List<PriceList> pricelists;
    @OneToMany(mappedBy = "partnerid", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<VatBusinessRegister> businessRegisters;

    public BusinessPartner() {
    }

    public Long getPartnerid() {
        return this.partnerid;
    }

    public void setPartnerid(Long partnerid) {
        this.partnerid = partnerid;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyname() {
        return this.companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanytypes() {
        return this.companytypes;
    }

    public void setCompanytypes(String companytypes) {
        this.companytypes = companytypes;
    }

    public String getCreditstatus() {
        return this.creditstatus;
    }

    public void setCreditstatus(String creditstatus) {
        this.creditstatus = creditstatus;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail1() {
        return this.email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return this.email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getFax1() {
        return this.fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public String getFax2() {
        return this.fax2;
    }

    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    public boolean getLporequired() {
        return this.lporequired;
    }

    public void setLporequired(boolean lporequired) {
        this.lporequired = lporequired;
    }

    public String getPbone2() {
        return this.pbone2;
    }

    public void setPbone2(String pbone2) {
        this.pbone2 = pbone2;
    }

    public String getPhone1() {
        return this.phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPobox() {
        return this.pobox;
    }

    public void setPobox(String pobox) {
        this.pobox = pobox;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /*public List<AccDoc> getAccdocs() {
		return this.accdocs;
	}

	public void setAccdocs(List<AccDoc> accdocs) {
		this.accdocs = accdocs;
	}

	public AccDoc addAccdoc(AccDoc accdoc) {
		getAccdocs().add(accdoc);
		accdoc.setBusinesspartner(this);

		return accdoc;
	}

	public AccDoc removeAccdoc(AccDoc accdoc) {
		getAccdocs().remove(accdoc);
		accdoc.setBusinesspartner(null);

		return accdoc;
	}
    
    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account addAccount(Account account) {
        getAccounts().add(account);
        account.setBusinesspartner(this);

        return account;
    }

    public Account removeAccount(Account account) {
        getAccounts().remove(account);
        account.setBusinesspartner(null);

        return account;
    }

    public List<BusDoc> getBusdocs() {
		return this.busdocs;
	}

	public void setBusdocs(List<BusDoc> busdocs) {
		this.busdocs = busdocs;
	}

	public BusDoc addBusdoc(BusDoc busdoc) {
		getBusdocs().add(busdoc);
		busdoc.setBusinesspartner(this);

		return busdoc;
	}

	public BusDoc removeBusdoc(BusDoc busdoc) {
		getBusdocs().remove(busdoc);
		busdoc.setBusinesspartner(null);

		return busdoc;
	}*/
    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public CreditLimit getCreditlimit() {
        return this.creditlimit;
    }

    public void setCreditlimit(CreditLimit creditlimit) {
        this.creditlimit = creditlimit;
    }

    public List<ContactPerson> getContactpersons() {
        return this.contactpersons;
    }

    public void setContactpersons(List<ContactPerson> contactpersons) {
        this.contactpersons = contactpersons;
    }

    public ContactPerson addContactperson(ContactPerson contactperson) {
        getContactpersons().add(contactperson);
        contactperson.setBusinesspartner(this);

        return contactperson;
    }

    public ContactPerson removeContactperson(ContactPerson contactperson) {
        getContactpersons().remove(contactperson);
        contactperson.setBusinesspartner(null);

        return contactperson;
    }

    /*public List<PriceList> getPricelists() {
		return this.pricelists;
	}

	public void setPricelists(List<PriceList> pricelists) {
		this.pricelists = pricelists;
	}

	public PriceList addPricelist(PriceList pricelist) {
		getPricelists().add(pricelist);
		pricelist.setBusinesspartner(this);

		return pricelist;
	}

	public PriceList removePricelist(PriceList pricelist) {
		getPricelists().remove(pricelist);
		pricelist.setBusinesspartner(null);

		return pricelist;
	}*/
    public boolean isLocalCompany() {
        if (getCountry() == null) {
            return true;
        }
        return getCountry().getDefcountry();
    }

    public boolean isForeignCompany() {
        if (getCountry() == null) {
            return false;
        }
        return !getCountry().getDefcountry();
    }

    public boolean isCustomer() {
        return getCompanytypes().contains(CUSTOMER);
    }

    public boolean isSupplier() {
        return getCompanytypes().contains(SUPPLIER);
    }

    public static String[] getAvailableCompanyTypes() {
        return new String[]{CUSTOMER, SUPPLIER};
    }

    public String[] getCompanyTypesArray() {
        if (getCompanytypes() == null) {
            return null;
        }
        StringTokenizer tokens = new StringTokenizer(getCompanytypes(), ",");
        List<String> list = new LinkedList<>();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.length() > 0) {
                list.add(token);
            }
        }
        String[] types = new String[list.size()];
        list.toArray(types);
        return types;
    }

    public void setCompanyTypesArray(String[] types) {
        if (types == null) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i == types.length - 1) {
                buffer.append(types[i]);
            } else {
                buffer.append(types[i]).append(",");
            }
        }
        setCompanytypes(buffer.toString());
    }

    public String getCurrency() {
        if (getCountry() == null) {
            return "LC";
        }

        return getCountry().getCurrencysym();
    }

    @Override
    public String toString() {
        return companyname + " [" + partnerid + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.partnerid);
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
        final BusinessPartner other = (BusinessPartner) obj;
        if (!Objects.equals(this.partnerid, other.partnerid)) {
            return false;
        }
        return true;
    }

    /**
     * @return the shopno
     */
    public String getShopno() {
        return shopno;
    }

    /**
     * @param shopno the shopno to set
     */
    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    /**
     * @return the buildingno
     */
    public String getBuildingno() {
        return buildingno;
    }

    /**
     * @param buildingno the buildingno to set
     */
    public void setBuildingno(String buildingno) {
        this.buildingno = buildingno;
    }

    /**
     * @return the roadno
     */
    public String getRoadno() {
        return roadno;
    }

    /**
     * @param roadno the roadno to set
     */
    public void setRoadno(String roadno) {
        this.roadno = roadno;
    }

    /**
     * @return the blockno
     */
    public String getBlockno() {
        return blockno;
    }

    /**
     * @param blockno the blockno to set
     */
    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    /**
     * @return the town
     */
    public String getTown() {
        return town;
    }

    /**
     * @param town the town to set
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return the businessRegisters
     */
    public List<VatBusinessRegister> getBusinessRegisters() {
        return businessRegisters;
    }

    /**
     * @param businessRegisters the businessRegisters to set
     */
    public void setBusinessRegisters(List<VatBusinessRegister> businessRegisters) {
        this.businessRegisters = businessRegisters;
    }

    public VatBusinessRegister getCurrentVatRegister() {
        if (getBusinessRegisters() != null && getBusinessRegisters().size() > 0) {
            return getBusinessRegisters().get(getBusinessRegisters().size() - 1);
        }
        return null;
    }

    /**
     * @return the accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
    //shopno,buildingno,roadno,blockno,town,area
    
    public String getAddressLine1(){
        StringBuilder b = new StringBuilder();
        if(getShopno()!=null){
            b.append("Shop: " + getShopno().trim());
        }
        if(getBuildingno()!=null){
            b.append(" Building: " + getBuildingno().trim());
        }
        if(getRoadno()!=null){
            b.append(" Road: " + getRoadno().trim());
        }
        return b.toString().trim();
    }
    
    public String getAddressLine2(){
        StringBuilder b = new StringBuilder();
        if(getBlockno()!=null){
            b.append("Block: " + getBlockno().trim());
        }
        if(getTown()!=null){
            b.append(" Town: " + getTown().trim());
        }
        if(getArea()!=null){
            b.append(" Area: " + getArea().trim());
        }
        return b.toString().trim();
    }
}
