package com.smb.erp.repo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductCategory;

@RestController
@RequestMapping({"/prod"})
public class ProductRestController implements Serializable {

	@Autowired
	ProductRepository pdrepo;

	@GetMapping
	public List<Product> getAll(){
	  return pdrepo.findAll();
	}
	
	public List<Product> getProductByCategory(Long id){
		return pdrepo.findByCategoryId(id);
	}
}
