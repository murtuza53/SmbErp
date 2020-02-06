package com.smb.erp.controller;

import com.smb.erp.entity.Product;

public class DocumentTab<T> extends Tab {

    enum MODE {
        NEW, EDIT, NONE, LIST, REPORT;
    }

    private T data;
    private MODE documentMode;
    private boolean modified;
    private Object controller;

    public DocumentTab(String title, String page, boolean closable) {
        super(title, page, closable);
        documentMode = MODE.NONE;
    }

    public DocumentTab(T object, String title, String page, MODE mode) {
        super(title, page);
        this.data = object;
        this.documentMode = mode;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean getModified() {
        return modified;
    }

    public MODE getDocumentMode() {
        return documentMode;
    }
    
    public void setController(Object con){
        this.controller = con;
    }
    
    public Object getController(){
        return controller;
    }

    public ProductController getProductController(){
        return (ProductController)controller;
    }
    
    public static DocumentTab<Product> createProductController(Product data, String title, String page, MODE mode){
        DocumentTab<Product> dt = new DocumentTab<Product>(data, title, page, mode);
        //ProductController pc = new ProductController(data);
        //dt.setController(pc);
        return dt;
    }
}
