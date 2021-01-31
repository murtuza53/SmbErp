package com.smb.erp.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the authentication database table.
 *
 */
@Entity
@Table(name = "authentication")
@NamedQuery(name = "Authentication.findAll", query = "SELECT a FROM Authentication a")
public class Authentication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    private String password;

    public Authentication() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
