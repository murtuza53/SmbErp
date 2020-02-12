package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the producttype database table.
 *
 */
@Entity
@NamedQuery(name = "ProductType.findAll", query = "SELECT p FROM ProductType p")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int producttypeid;

    private String description;

    private String name;

    //bi-directional many-to-one association to Product
    @OneToMany(mappedBy = "producttype")
    private List<Product> products;

    public ProductType() {
    }

    public int getProducttypeid() {
        return this.producttypeid;
    }

    public void setProducttypeid(int producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product addProduct(Product product) {
        getProducts().add(product);
        product.setProducttype(this);

        return product;
    }

    public Product removeProduct(Product product) {
        getProducts().remove(product);
        product.setProducttype(null);

        return product;
    }

    @Override
    public String toString() {
        return "ProductType{" + "producttypeid=" + producttypeid + ", description=" + description + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.producttypeid;
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
        final ProductType other = (ProductType) obj;
        if (this.producttypeid != other.producttypeid) {
            return false;
        }
        return true;
    }

}
