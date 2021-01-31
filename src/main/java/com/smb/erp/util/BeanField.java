/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Burhani152
 */
public class BeanField implements Serializable{
    
    private String property;
    private Class type;

    public BeanField() {
    }

    public BeanField(String property, Class type) {
        this.property = property;
        this.type = type;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the type
     */
    public Class getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Class type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BeanField{" + "property=" + property + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.property);
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
        final BeanField other = (BeanField) obj;
        if (!Objects.equals(this.property, other.property)) {
            return false;
        }
        return true;
    }
    
}
