package com.smb.erp.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductCategory;

@Named
@SessionScoped
public class TabViewController implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<DocumentTab> tabs;
	private DocumentTab selectedTab;
	private Integer activeTabIndex = 0;
	private TabView tabView;
	private Map<String, Object> tabBeans = new HashMap<String, Object>();	

	@PostConstruct
	public void init() {
		tabs = new LinkedList<DocumentTab>();
		add("Dashboard", "dashboard", false);
		System.out.println("TOTAL_TABS: " + tabs.size());
	}

//	public void add(String title, String page) {
//		DocumentTab t = new DocumentTab(title, page);
//		tabs.add(t);
//		activeTabIndex = tabs.size() - 1;
//		selectedTab = t;
//	}

	public void add(String title, String page, boolean closable) {
		DocumentTab t = new DocumentTab(title, page, closable);
		tabs.add(t);
		activeTabIndex = tabs.size() - 1;
		selectedTab = t;
	}
	
	public void add(DocumentTab<?> tab) {
		tabs.add(tab);
		activeTabIndex = tabs.size()-1;
                selectedTab = tab;
	}

	public void addClosable(String title, String page) {
		int i = findTab(title);
		if (i > -1) {
			activeTabIndex = i;
		} else {
			tabs.add(new DocumentTab(title, page, true));
			activeTabIndex = tabs.size() - 1;
		}
	}

	public int findTab(String title) {
		int index = -1;
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getTitle().equalsIgnoreCase(title)) {
				return i;
			}
		}
		return index;
	}

	public void remove(Tab tab) {
		tabs.remove(tab);
	}

	public void removeTabById(String id) {
		Tab tab = null;
		for (Tab t : tabs) {
			if (t.getId().equalsIgnoreCase(id)) {
				tab = t;
			}
		}

		int index = tabs.indexOf(tab);
		tabs.remove(tab);
		if (index >= tabs.size()) {
			activeTabIndex = tabs.size() - 1;
		} else {
			activeTabIndex = index;
		}
	}

	public List<DocumentTab> getTabs() {
		return tabs;
	}

	public void onTabChange(TabChangeEvent event) {
		activeTabIndex = ((TabView) event.getSource()).getIndex();
		selectedTab = getTabs().get(activeTabIndex);
		FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onTabClose(TabCloseEvent event) {
		removeTabById(event.getTab().getId());
		FacesMessage msg = new FacesMessage("Tab Closed", "Closed tab: " + event.getTab().getTitle());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public DocumentTab getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(DocumentTab selectedTab) {
		this.selectedTab = selectedTab;
	}

	public Integer getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(Integer activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public TabView getTabView() {
		return tabView;
	}

	public void setTabView(TabView tabView) {
		this.tabView = tabView;
	}
	
	//add tab for category
	public void addTabCategory(ProductCategory pc, String title, DocumentTab.MODE mode) {
		DocumentTab<ProductCategory> dt = new DocumentTab<ProductCategory>(pc, title, "pcategory", mode);
                selectedTab = dt;
		add(dt);
	}

	public void addTabProduct(Product prod, String title, DocumentTab.MODE mode) {
		//DocumentTab<Product> dt = new DocumentTab<Product>(prod, title, "product", mode);
                DocumentTab<Product> dt = DocumentTab.createProductController(prod, title, "product", mode);
                System.out.println("New Product: " + dt.getData());
                System.out.println("Product_Tab_Id: " + dt.getId());
		add(dt);
		System.out.println("New Product Tab Added: " + tabs.size());
	}
}
