package com.smb.erp.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.rest.ProductRestController;

@Named
@ConversationScoped
public class ProductTreeViewController implements Serializable {

	@Autowired
	ProductCategoryController pccontroller;

	@Autowired
	TabViewController tabController;
	
	@Autowired
	ProductRestController pdcontroller;

	private TreeNode root;

	private String treeName;

	private TreeNode selectedNode;
	
	private Product selectedProduct;

	private List<Product> prodList;

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
		if(getSelectedNode()==null) {
			//System.out.println("getNewCategoryDisabled: " + getSelectedNode());
			return true;
		} else if(((ProductCategory)getSelectedNode().getData()).getProdcatId()>1099) {
			//System.out.println("getNewCategoryDisabled: " + ((ProductCategory)getSelectedNode().getData()).getProdcatId());
			return true;
		}
		
		//System.out.println("getNewCategoryDisabled:" + false);
		return false;
	}
	
	public boolean getEditCategoryDisabled() {
		if(getSelectedNode()==null) {
			//System.out.println("getEditCategoryDisabled: " + getSelectedNode());
			return true;
		} else if(((ProductCategory)getSelectedNode().getData()).getProdcatId()==1) {
			//System.out.println("getEditCategoryDisabled: " + ((ProductCategory)getSelectedNode().getData()).getProdcatId());
			return true;
		}
		//System.out.println("getEditCategoryDisabled: " + false);
		return false;
	}
	
	public boolean getProductEditDisabled() {
		if(getSelectedProduct()==null) {
			//System.out.println("getProductEditDisabled: " + true);
			return true;
		} 
		//System.out.println("getProductEditDisabled: " + false);
		return false;
	}
	
	public boolean getProductNewDisabled() {
		if(getSelectedNode()==null) {
			return true;
		} else if(((ProductCategory)getSelectedNode().getData()).getProdcatId()<2000) {
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
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public void createNewProduct() {
		tabController.addTabProduct(new Product(), "New Product", DocumentTab.MODE.NEW);
	}
	
}
