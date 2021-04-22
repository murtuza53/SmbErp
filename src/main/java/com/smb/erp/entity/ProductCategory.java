package com.smb.erp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * The persistent class for the prodcategry database table.
 *
 */
@Entity
@Table(name = "prodcategory")
@NamedQuery(name = "ProductCategory.findAll", query = "SELECT p FROM ProductCategory p ORDER BY p.catname")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodcatId = new Date().getTime();

    private String catname;

    private String description;

    private String groupcode;

    //bi-directional many-to-one association to PriceList
    @OneToMany(mappedBy = "prodcategory")
    private List<PriceList> pricelists;

    //bi-directional many-to-one association to ProductCategry
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parentid")
    private ProductCategory prodcategory;

    //bi-directional many-to-one association to ProductCategry
    @JsonIgnore
    @OneToMany(mappedBy = "prodcategory")
    private List<ProductCategory> prodcategories;

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
        pricelist.setProdcategory(this);

        return pricelist;
    }

    public PriceList removePricelist(PriceList pricelist) {
        getPricelists().remove(pricelist);
        pricelist.setProdcategory(null);

        return pricelist;
    }

    public ProductCategory getProdcategory() {
        return this.prodcategory;
    }

    public void setProdcategory(ProductCategory prodcategory) {
        this.prodcategory = prodcategory;
    }

    public List<ProductCategory> getProdcategories() {
        return this.prodcategories;
    }

    public void setProdcategories(List<ProductCategory> prodcategories) {
        this.prodcategories = prodcategories;
    }

    public ProductCategory addProdcategory(ProductCategory prodcategory) {
        getProdcategories().add(prodcategory);
        prodcategory.setProdcategory(this);

        return prodcategory;
    }

    public ProductCategory removeProdcategry(ProductCategory prodcategry) {
        getProdcategories().remove(prodcategry);
        prodcategry.setProdcategory(null);

        return prodcategry;
    }

    /*public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product addProduct(Product product) {
        getProducts().add(product);
        product.setProdcategory(this);

        return product;
    }

    public Product removeProduct(Product product) {
        getProducts().remove(product);
        product.setProdcategory(null);

        return product;
    }*/

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
