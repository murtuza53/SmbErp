package com.smb.erp.controller;

import java.util.Date;

public class Tab {
	
	private String id = "tab-"+new Date().getTime();
	private String title;
    private String page;
    private boolean closable;

    public Tab(String title, String page) {
        this(title, page, true);
    }

    public Tab(String title, String page, boolean closable) {
        this.title = title;
        this.page = page;
        this.closable = closable;
        //System.out.println("CREATED_TAB: " + id);
    }

    public String getId() {
    	return id;
    }
    
    public String getTitle() {
        return title;
    }

    public String getPage() {
        return page;
    }
    
    public boolean getClosable() {
    	return closable;
    }
    
    public void setClosable(boolean closable) {
    	this.closable = closable;
    }

}
