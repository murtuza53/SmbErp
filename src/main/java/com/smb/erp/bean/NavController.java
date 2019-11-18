package com.smb.erp.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class NavController implements Serializable {
    private static final long serialVersionUID = 1L;
    private String page = "dashboard";

    public String getPage() {
        return page;
    }

    public void goToPage(String page) {
        this.page = page;
    }
}
