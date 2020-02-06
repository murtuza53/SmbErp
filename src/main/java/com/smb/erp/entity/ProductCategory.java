package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the prodcategry database table.
 *
 */
@Entity
@Table(name = "prodcategry")
@NamedQuery(name = "ProductCategory.findAll", query = "SELECT p FROM ProductCategory p")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long prodcatId;

    private String catname;

    private String description;

    private String groupcode;

    //bi-directional many-to-one association to PriceList
    @OneToMany(mappedBy = "prodcategry")
    private List<PriceList> pricelists;

    //bi-directional many-to-one association to ProductCategry
    @ManyToOne
    @JoinColumn(name = "parentid")
    private ProductCategory prodcategry;

    //bi-directional many-to-one association to ProductCategry
    @OneToMany(mappedBy = "prodcategry")
    private List<ProductCategory> prodcategries;

    //bi-directional many-to-one association to Product
    @OneToMany(mappedBy = "prodcategry")
    private List<Product> products;

    public ProductCategory() {
    }

    public Long getProdcatId() {
        return this.prodcatId;
    }

    public void setProdcatId(Long prodcatId) {
        this.prodcatId = prodcatId;
    }

    public String getCatname() {
        return this.catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PriceList> getPricelists() {
        return this.pricelists;
    }

    public void setPricelists(List<PriceList> pricelists) {
        this.pricelists = pricelists;
    }

    public PriceList addPricelist(PriceList pricelist) {
        getPricelists().add(pricelist);
        pricelist.setProdcategry(this);

        return pricelist;
    }

    public PriceList removePricelist(PriceList pricelist) {
        getPricelists().remove(pricelist);
        pricelist.setProdcategry(null);

        return pricelist;
    }

    public ProductCategory getProdcategry() {
        return this.prodcategry;
    }

    public void setProdcategry(ProductCategory prodcategry) {
        this.prodcategry = prodcategry;
    }

    public List<ProductCategory> getProdcategries() {
        return this.prodcategries;
    }

    public void setProdcategries(List<ProductCategory> prodcategries) {
        this.prodcategries = prodcategries;
    }

    public ProductCategory addProdcategry(ProductCategory prodcategry) {
        getProdcategries().add(prodcategry);
        prodcategry.setProdcategry(this);

        return prodcategry;
    }

    public ProductCategory removeProdcategry(ProductCategory prodcategry) {
        getProdcategries().remove(prodcategry);
        prodcategry.setProdcategry(null);

        return prodcategry;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product addProduct(Product product) {
        getProducts().add(product);
        product.setProdcategry(this);

        return product;
    }

    public Product removeProduct(Product product) {
        getProducts().remove(product);
        product.setProdcategry(null);

        return product;
    }

    @Override
    public String toString() {
        return getCatname() + " [" + getProdcatId() + "]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.prodcatId);
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
        final ProductCategory other = (ProductCategory) obj;
        if (!Objects.equals(this.prodcatId, other.prodcatId)) {
            return false;
        }
        return true;
    }

    public String getGroupcode() {
        return groupcode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }
}
