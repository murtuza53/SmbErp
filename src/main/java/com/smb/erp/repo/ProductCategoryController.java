package com.smb.erp.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smb.erp.entity.ProductCategory;

@RestController
@RequestMapping({"/prodcat"})
public class ProductCategoryController implements Serializable {

	@Autowired
	ProductCategoryRepository pdrepo;

	@GetMapping
	public List<ProductCategory> getAll(){
	  return pdrepo.findAll();
	}
	
	public ProductCategory getRootNode() {
		Optional<ProductCategory> obj = pdrepo.findById(new Long(1));
		if(obj.isPresent()) {
			return obj.get();
		}
		return null;
	}
	
	public List<ProductCategory> getProductCategoryByParent(Long id){
		return pdrepo.findByParentId(id);
	}
}
