package com.smb.erp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.smb.erp.entity.ProductCategory;
import com.smb.erp.repo.ProductCategoryRepository;
import com.smb.erp.util.JsfUtil;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named
@SessionScoped
public class ProductCategoryController extends AbstractController<ProductCategory> {

    //@Autowired
    ProductCategoryRepository pdrepo;

    @Autowired
    TableKeyController keyCon;

    List<ProductCategory> leafNodes;

    @Autowired
    public ProductCategoryController(ProductCategoryRepository repo) {
        this.pdrepo = repo;
    }

    @Override
    public void save() {
        if (getSelected().getProdcatId() == 0) {
            getSelected().setProdcatId(keyCon.getProductCategoryNextId());
        }
        pdrepo.save(getSelected());
        JsfUtil.addSuccessMessage(getSelected().getCatname() + " saved");

    }
    
    public void save(ProductCategory cat){
        //System.out.println("ProductCategoryController.save: " + cat);
        setSelected(cat);
        this.save();
    }

    //@GetMapping
    public List<ProductCategory> getAll() {
        return pdrepo.findAll();
    }

    public ProductCategory getRootNode() {
        Optional<ProductCategory> obj = pdrepo.findById(new Long(1));
        if (obj.isPresent()) {
            return obj.get();
        }
        return null;
    }

    public List<ProductCategory> getProductCategoryByParent(Long id) {
        return pdrepo.findByParentId(id);
    }

    public List<ProductCategory> getCategoryLeafNodes() {
        if (leafNodes == null) {
            leafNodes = pdrepo.findCategoryLeafNodes();
        }
        return leafNodes;
    }

    public SelectItem[] getSelectableCategoryLeafNodes() {
        return JsfUtil.getSelectItems(getCategoryLeafNodes());
    }

    public List<ProductCategory> completeFilter(String criteria) {
        return pdrepo.findCategoryLeafNodesByCriteria(criteria);
    }
}
