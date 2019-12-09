package com.smb.erp.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.smb.erp.entity.ProductCategory;
import com.smb.erp.repo.ProductCategoryRepository;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "productCategoryController")
@ViewScoped
public class ProductCategoryController extends AbstractController<ProductCategory> {

    //@Autowired
    ProductCategoryRepository pdrepo;
    
    List<ProductCategory> leafNodes;

    @Autowired
    public ProductCategoryController(ProductCategoryRepository repo) {
        this.pdrepo = repo;
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
        if(leafNodes==null){
            leafNodes = pdrepo.findCategoryLeafNodes();
        }
        return leafNodes;
    }

    public List<ProductCategory> completeFilter(String criteria) {
        return pdrepo.findCategoryLeafNodesByCriteria(criteria);
    }
}
