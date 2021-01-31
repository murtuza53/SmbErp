package com.smb.erp.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.entity.VatCategory;
import com.smb.erp.entity.VatProductRegister;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.VatCategoryRepository;
import com.smb.erp.rest.ProductRestController;
import com.smb.erp.util.JsfUtil;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;

@Named
@SessionScoped
public class ProductTreeViewController implements Serializable {

    @Autowired
    ProductCategoryController pccontroller;

    @Autowired
    TabViewController tabController;

    @Autowired
    ProductRestController pdcontroller;
    
    @Autowired
    VatCategoryRepository vatcatRepo;
    
    @Autowired
    ProductRepository prodRepo;

    @Autowired
    TabViewController mainTabs;
    
    @Autowired
    TableKeyController keyCon;

    private TreeNode root;

    private String treeName;

    private TreeNode selectedNode;

    private Product selectedProduct;
    
    private ProductCategory selectedCategory;

    private List<Product> prodList;

    private HashMap<String, DocumentTab<Product>> productMap = new HashMap<String, DocumentTab<Product>>();
    
    private DefaultMenuModel vatMenuModel = new DefaultMenuModel();

    @PostConstruct
    public void init() {
        refreshCategoryTree();
        for(VatCategory cat: vatcatRepo.findAll()){
            DefaultMenuItem item = new DefaultMenuItem();
            item.setValue(cat);
            item.setCommand("#{productTreeViewController.registerProductVat("+cat.getVatcategoryid()+")}");
            item.setUpdate("prodtable, growl");
            getVatMenuModel().getElements().add(item);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public void addNode(ProductCategory pc, TreeNode parent) {
        DefaultTreeNode n = new DefaultTreeNode("CAT",pc, parent);

        if(selectedNode!=null && ((ProductCategory)selectedNode.getData()).getProdcatId().longValue()==pc.getProdcatId().longValue()){
            selectedNode = n;
            parent.setExpanded(true);
        }
        
        if (pc.getProdcatId() == 1) {
            n.setExpanded(true);
        }

        if (pc.getProdcategories() != null) {
            for (ProductCategory p : pc.getProdcategories()) {
                addNode(p, n);
            }
        }
    }

    public DocumentTab<Product> getDocumentTab(String windowId) {
        return productMap.get(windowId);
    }

    public void newTab() throws IOException {
        Product p = new Product();
        p.setCreatedon(new Date());
        p.setProdcategory((ProductCategory) getSelectedNode().getData());
        DocumentTab<Product> tab = new DocumentTab<Product>(p, "New Product", "product", DocumentTab.MODE.NEW);
        mainTabs.add(tab);
    }

    public void editTab() throws IOException {
        DocumentTab<Product> tab = new DocumentTab<Product>(getSelectedProduct(), "Edit - " + getSelectedProduct().getProductname(), "inventory/product", DocumentTab.MODE.NEW);
        mainTabs.add(tab);
    }

    public void newTab_In_newBtab() throws IOException {
        String windowId = new Date().getTime() + "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //Flash flash = facesContext.getExternalContext().getFlash();
        //flash.clear();
        //flash.put("category", (ProductCategory) getSelectedNode().getData());
        //flash.put("mode", "new");
        //flash.put("windowid", windowId);
        //flash.setKeepMessages(true);
        //flash.setRedirect(true);

        Product p = new Product();
        p.setCreatedon(new Date());
        p.setProdcategory((ProductCategory) getSelectedNode().getData());
        setSelectedProduct(p);
        DocumentTab<Product> tab = new DocumentTab<Product>(p, "New Product", "product", DocumentTab.MODE.NEW);
        productMap.put(windowId, tab);
        
        facesContext.getExternalContext().redirect("product.xhtml?windowid=" + windowId);
    }

    public void editTab_In_newBtab() throws IOException {
        String windowId = new Date().getTime() + "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //Flash flash = facesContext.getExternalContext().getFlash();
        //flash.clear();
        //flash.put("product", getSelectedProduct());
        //flash.put("mode", "edit");
        //flash.put("windowid", windowId);
        //flash.setKeepMessages(true);
        //flash.setRedirect(true);

        DocumentTab<Product> tab = new DocumentTab<Product>(getSelectedProduct(), "Edit - " + getSelectedProduct().getProductname(), "product", DocumentTab.MODE.EDIT);
        tab.setData(getSelectedProduct());
        productMap.put(windowId, tab);

        facesContext.getExternalContext().redirect("product.xhtml?windowid=" + windowId);
    }

    public void cloneTab_In_newBtab() throws IOException {
        String windowId = new Date().getTime() + "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //Flash flash = facesContext.getExternalContext().getFlash();
        //flash.clear();
        //setSelectedProduct(getSelectedProduct().clone());
        //flash.put("product", getSelectedProduct());
        //flash.put("mode", "edit");
        //flash.put("windowid", windowId);
        //flash.setKeepMessages(true);
        //flash.setRedirect(true);

        DocumentTab<Product> tab = new DocumentTab<Product>(getSelectedProduct(), "New Product", "product", DocumentTab.MODE.NEW);
        tab.setData(getSelectedProduct());
        productMap.put(windowId, tab);

        facesContext.getExternalContext().redirect("product.xhtml?windowid=" + windowId);
    }

    public void refreshCategoryTree() {
        ProductCategory rpc = pccontroller.getRootNode();
        root = new DefaultTreeNode("CAT","Root", null);
        addNode(rpc, root);
    }

    public void refreshProductList() {
        ProductCategory pc = (ProductCategory) selectedNode.getData();
        prodList = pdcontroller.getProductByCategory(pc.getProdcatId());
    }

    public void onNodeSelect(NodeSelectEvent event) {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        //FacesContext.getCurrentInstance().addMessage(null, message);
        //selectedNode = event.getTreeNode();
        if(selectedNode!=null){
            selectedCategory = (ProductCategory)selectedNode.getData();
        }
        refreshProductList();
        setSelectedProduct(null);
        //PrimeFaces.current().ajax().update("toolbar");
    }

    public boolean getNewCategoryDisabled() {
        if (getSelectedNode() == null) {
            //System.out.println("getNewCategoryDisabled: " + getSelectedNode());
            return true;
        } else if (((ProductCategory) getSelectedNode().getData()).getProdcatId() > 1099) {
            //System.out.println("getNewCategoryDisabled: " + ((ProductCategory)getSelectedNode().getData()).getProdcatId());
            return true;
        }

        //System.out.println("getNewCategoryDisabled:" + false);
        return false;
    }

    public boolean getEditCategoryDisabled() {
        if (getSelectedNode() == null) {
            //System.out.println("getEditCategoryDisabled: " + getSelectedNode());
            return true;
        } else if (((ProductCategory) getSelectedNode().getData()).getProdcatId() == 1) {
            //System.out.println("getEditCategoryDisabled: " + ((ProductCategory)getSelectedNode().getData()).getProdcatId());
            return true;
        }
        //System.out.println("getEditCategoryDisabled: " + false);
        return false;
    }

    public boolean getProductEditDisabled() {
        if (getSelectedProduct() == null) {
            //System.out.println("getProductEditDisabled: " + true);
            return true;
        }
        //System.out.println("getProductEditDisabled: " + false);
        return false;
    }

    public boolean getProductNewDisabled() {
        if (getSelectedNode() == null) {
            return true;
        } else if (((ProductCategory) getSelectedNode().getData()).getProdcatId() < 2000) {
            return true;
        }
        return false;
    }
    
    public void registerProductVat(long vatcatid){
        if(selectedProduct!=null){
            VatCategory vc = vatcatRepo.getOne(vatcatid);
            VatProductRegister vpr = new VatProductRegister();
            if(selectedProduct.getVatregisterid()!=null){
                vpr = selectedProduct.getVatregisterid();
            }
            vpr.setProducttype("Products");
            vpr.setVatcategoryid(vc);
            vpr.setWef(new Date());
            selectedProduct.setVatregisterid(vpr);
            prodRepo.save(selectedProduct);
            System.out.println(selectedProduct + " updated with registerProductVat: " + vc);
            JsfUtil.addSuccessMessage(selectedProduct + " registered with " + vc);
        }
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
        // refreshProductList();
    }

    public List<Product> getProdList() {
        return prodList;
    }

    public void setProdList(List<Product> prodList) {
        this.prodList = prodList;
    }

    public Product getSelectedProduct() {
        //System.out.println(new Date() + " => getSelectedProduct: " + selectedProduct);
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /*public void createNewProduct() {
        tabController.addTabProduct(new Product(), "New Product", DocumentTab.MODE.NEW);
    }*/
    public String getHeaderTitle() {
        if (selectedProduct == null || selectedProduct.getProductid() == null || selectedProduct.getProductid().longValue() == 0) {
            return "New Product";
        }
        return "Edit Product";
    }

    public String getHeaderCatTitle(){
        if(selectedCategory==null){
            return "";
        }
        if(selectedCategory.getProdcatId()>0){
            return "Edit " + selectedCategory.getCatname();
        }
        return "New Product Category";
    }

    public void prepareCreateNewCategory(){
        //System.out.println("prepareCreateNewCategory: " + selectedCategory);
        ProductCategory newcat = new ProductCategory();
        newcat.setProdcategory(selectedCategory);
        newcat.setProdcatId(0l);
        selectedCategory = newcat;
    }
    
    public void saveCategory(){
        pccontroller.save(selectedCategory);
        //JsfUtil.addSuccessMessage(selectedCategory.getCatname() + " saved");
    }
    
    public void prepareCreateNewProduct() {
        selectedProduct = new Product();
        selectedProduct.setProductid(0l);
        selectedProduct.setProductname("Testing");
        if (getSelectedNode() != null) {
            if (getSelectedNode().getData() != null) {
                selectedProduct.setProdcategory((ProductCategory) getSelectedNode().getData());
            }
        }
        //System.out.println(new Date() + " => prepareCreateNewProduct: " + selectedProduct);
    }

    /**
     * @return the vatMenuModel
     */
    public DefaultMenuModel getVatMenuModel() {
        return vatMenuModel;
    }

    /**
     * @param vatMenuModel the vatMenuModel to set
     */
    public void setVatMenuModel(DefaultMenuModel vatMenuModel) {
        this.vatMenuModel = vatMenuModel;
    }

    /**
     * @return the selectedCategory
     */
    public ProductCategory getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(ProductCategory selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

}
