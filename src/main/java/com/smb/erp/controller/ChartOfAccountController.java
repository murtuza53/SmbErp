/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Account;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Burhani152
 */
@Named
@ViewScoped
public class ChartOfAccountController implements Serializable {

    @Autowired
    AccountController accController;

    @Autowired
    AccountRepository accRepo;

    private TreeNode root;

    private String treeName;

    private TreeNode selectedNode;

    private Account selectedAccount;

    private List<Account> accList;

    public ChartOfAccountController() {

    }

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("ROOT", "Root", null);
        //refreshCoa();
    }

    public TreeNode getRoot() {
        //if (root == null) {
        //    refreshCoa();
        //}
        return root;
    }

    public void refreshCoa() {
        Account ra = accRepo.getOne("1");
        root = new DefaultTreeNode("ROOT", "Root", null);
        addNode(ra, root);
    }

    public void save() {
        if (getSelectedAccount().getAccountid() == null) {
            selectedAccount.setAccountid(accController.getAccountNextNo(selectedAccount.getParentid()));
        }
        accRepo.save(selectedAccount);
        JsfUtil.addSuccessMessage("New Account added successfuly");
    }

    public void addNode(Account acc, TreeNode parent) {
        DefaultTreeNode n = new DefaultTreeNode(acc.getNodetype(), acc, parent);

        if (getSelectedNode() != null && ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase(acc.getAccountid())) {
            setSelectedNode(n);
            parent.setExpanded(true);
        }

        if (acc.getAccountid().equalsIgnoreCase("1")) {
            n.setExpanded(true);
        }

        List<Account> childrens = accRepo.findAccountByParent(acc.getAccountid());
        if (childrens != null) {
            for (Account a : childrens) {
                addNode(a, n);
            }
        }
    }

    public void onNodeSelect(NodeSelectEvent event) {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        //FacesContext.getCurrentInstance().addMessage(null, message);
        refreshAccountList();
        setSelectedAccount((Account) selectedNode.getData());
        //PrimeFaces.current().ajax().update("toolbar");
    }

    public void refreshAccountList() {
        //ProductCategory pc = (ProductCategory) selectedNode.getData();
        //prodList = pdcontroller.getProductByCategory(pc.getProdcatId());
    }

    public void createNewAccountGroup() {
        selectedAccount = new Account();
        selectedAccount.setNodetype("GROUP");
        System.out.println("createNewAccountGroup: " + selectedNode.getData());
        selectedAccount.setParentid((Account) selectedNode.getData());
    }

    public void editAccountGroup() {
        selectedAccount = (Account) selectedNode.getData();
    }

    public void createNewAccount() {
        selectedAccount = new Account();
        selectedAccount.setNodetype("ACCOUNT");
        selectedAccount.setParentid((Account) selectedNode.getData());
    }

    public void editAccount() {
        selectedAccount = (Account) selectedNode.getData();
    }

    public void cloneAccount() {
        selectedAccount = new Account();
        Account acc = (Account) selectedNode.getData();
        selectedAccount.setParentid(acc.getParentid());
        selectedAccount.setAccounttype(acc.getAccounttype());
        selectedAccount.setAccountname(acc.getAccountname());
        selectedAccount.setNodetype(acc.getNodetype());
    }

    public boolean getNewGroupDisabled() {
        if (getSelectedNode() == null) {
            //System.out.println("getNewCategoryDisabled: " + getSelectedNode());
            return true;
        } else if (((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("171110")
                || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("171111")
                || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("211110")
                || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("211111")) {
            return true;
        } else if (((Account) getSelectedNode().getData()).getNodetype().equalsIgnoreCase("GROUP")
                || ((Account) getSelectedNode().getData()).getNodetype().equalsIgnoreCase("ROOT")) {
            //System.out.println("getNewCategoryDisabled: " + ((ProductCategory)getSelectedNode().getData()).getProdcatId());
            return false;
        }

        //System.out.println("getNewCategoryDisabled:" + false);
        return true;
    }

    public boolean getEditGroupDisabled() {
        if (getSelectedNode() != null) {
            //System.out.println("getEditCategoryDisabled: " + getSelectedNode());
            if (((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("171110")
                    || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("171111")
                    || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("211110")
                    || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("211111")) {
                return true;
            } else if (((Account) getSelectedNode().getData()).getNodetype().equalsIgnoreCase("GROUP")) {
                return false;
            }
        }
        //System.out.println("getEditCategoryDisabled: " + false);
        return true;
    }

    public boolean getNewAccountDisabled() {
        if (getSelectedNode() == null) {
            //System.out.println("getNewCategoryDisabled: " + getSelectedNode());
            return true;
        } else if (((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("171110")
                || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("171111")
                || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("211110")
                || ((Account) getSelectedNode().getData()).getAccountid().equalsIgnoreCase("211111")) {
            return true;
        } else if (((Account) getSelectedNode().getData()).getNodetype().equalsIgnoreCase("GROUP")) {
            //System.out.println("getNewCategoryDisabled: " + ((ProductCategory)getSelectedNode().getData()).getProdcatId());
            return false;
        }

        //System.out.println("getNewCategoryDisabled:" + false);
        return true;
    }

    public boolean getEditAccountDisabled() {
        if (getSelectedNode() != null) {
            //System.out.println("getEditCategoryDisabled: " + getSelectedNode());
            if (((Account) getSelectedNode().getData()).getAccountid().startsWith("171110")
                    || ((Account) getSelectedNode().getData()).getAccountid().startsWith("171111")
                    || ((Account) getSelectedNode().getData()).getAccountid().startsWith("211110")
                    || ((Account) getSelectedNode().getData()).getAccountid().startsWith("211111")) {
                return true;
            } else if (((Account) getSelectedNode().getData()).getNodetype().equalsIgnoreCase("ACCOUNT")) {
                return false;
            }
        }
        //System.out.println("getEditCategoryDisabled: " + false);
        return true;
    }

    public boolean getCloneAccountDisabled() {
        if (getSelectedNode() != null) {
            //System.out.println("getEditCategoryDisabled: " + getSelectedNode());
            if (((Account) getSelectedNode().getData()).getAccountid().startsWith("171110")
                    || ((Account) getSelectedNode().getData()).getAccountid().startsWith("171111")
                    || ((Account) getSelectedNode().getData()).getAccountid().startsWith("211110")
                    || ((Account) getSelectedNode().getData()).getAccountid().startsWith("211111")) {
                return true;
            } else if (((Account) getSelectedNode().getData()).getNodetype().equalsIgnoreCase("ACCOUNT")) {
                return false;
            }
        }
        //System.out.println("getEditCategoryDisabled: " + false);
        return true;
    }

    /**
     * @return the treeName
     */
    public String getTreeName() {
        return treeName;
    }

    /**
     * @param treeName the treeName to set
     */
    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    /**
     * @return the selectedNode
     */
    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @param selectedNode the selectedNode to set
     */
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
     * @return the selectedAccount
     */
    public Account getSelectedAccount() {
        return selectedAccount;
    }

    /**
     * @param selectedAccount the selectedAccount to set
     */
    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    /**
     * @return the accList
     */
    public List<Account> getAccList() {
        return accList;
    }

    /**
     * @param accList the accList to set
     */
    public void setAccList(List<Account> accList) {
        this.accList = accList;
    }
}
