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
import com.smb.erp.rest.ProductRestController;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.springframework.web.context.annotation.SessionScope;

@Named
@SessionScope
public class ProductTreeViewController implements Serializable {

    @Autowired
    ProductCategoryController pccontroller;

    @Autowired
    TabViewController tabController;

    @Autowired
    ProductRestController pdcontroller;
    
    @Autowired
    TabViewController mainTabs;

    private TreeNode root;

    private String treeName;

    private TreeNode selectedNode;

    private Product selectedProduct;

    private List<Product> prodList;
    
    private HashMap<String, DocumentTab<Product>> productMap = new HashMap<String, DocumentTab<Product>>();

    @PostConstruct
    public void init() {
        if (root == null) {
            ProductCategory rpc = pccontroller.getRootNode();
            root = new DefaultTreeNode("Root", null);
            addNode(rpc, root);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public void addNode(ProductCategory pc, TreeNode parent) {
        DefaultTreeNode n = new DefaultTreeNode(pc, parent);

        if (pc.getProdcatId() == 1) {
            n.setExpanded(true);
        }

        if (pc.getProdcategries() != null) {
            for (ProductCategory p : pc.getProdcategries()) {
                addNode(p, n);
            }
        }
    }

    public DocumentTab<Product> getDocumentTab(String windowId){
        return productMap.get(windowId);
    }
    
    public void newTab() throws IOException {
        Product p = new Product();
        p.setCreatedon(new Date());
        p.setProdcategry((ProductCategory)getSelectedNode().getData());
        DocumentTab<Product> tab = new DocumentTab<Product>(p, "New Product", "inventory/product", DocumentTab.MODE.NEW);
        mainTabs.add(tab);
    }

    public void editTab() throws IOException {
        DocumentTab<Product> tab = new DocumentTab<Product>(getSelectedProduct(), "Edit - " + getSelectedProduct().getProductname(), "inventory/product", DocumentTab.MODE.NEW);
        mainTabs.add(tab);
    }
    
    public void newTab_In_newBtab() throws IOException {
        String windowId = new Date().getTime()+"";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.clear();
        flash.put("category", (ProductCategory)getSelectedNode().getData());
        flash.put("mode", "new");
        flash.put("windowid", windowId);
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        facesContext.getExternalContext().redirect("inventory/product.xhtml");
    }

    public void editTab_In_newBtab() throws IOException {
        String windowId = new Date().getTime()+"";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.clear();
        flash.put("product", getSelectedProduct());
        flash.put("mode", "edit");
        flash.put("windowid", windowId);
        flash.setKeepMessages(true);
        flash.setRedirect(true);
        facesContext.getExternalContext().redirect("inventory/product.xhtml");
    }
    
    public void refreshProductList() {
        ProductCategory pc = (ProductCategory) selectedNode.getData();
        prodList = pdcontroller.getProductByCategory(pc.getProdcatId());
    }

    public void onNodeSelect(NodeSelectEvent event) {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        //FacesContext.getCurrentInstance().addMessage(null, message);
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

    public void prepareCreateNewProduct() {
        selectedProduct = new Product();
        selectedProduct.setProductid(0l);
        selectedProduct.setProductname("Testing");
        if (getSelectedNode() != null) {
            if (getSelectedNode().getData() != null) {
                selectedProduct.setProdcategry((ProductCategory) getSelectedNode().getData());
            }
        }
        //System.out.println(new Date() + " => prepareCreateNewProduct: " + selectedProduct);
    }

}
