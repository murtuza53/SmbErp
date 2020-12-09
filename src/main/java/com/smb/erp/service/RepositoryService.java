/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.entity.Brand;
import com.smb.erp.entity.Country;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductAccount;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.entity.ProductType;
import com.smb.erp.entity.Unit;
import com.smb.erp.entity.VatProductRegister;
import com.smb.erp.repo.BaseRepository;
import com.smb.erp.repo.BrandRepository;
import com.smb.erp.repo.CountryRepository;
import com.smb.erp.repo.ProductAccountRepository;
import com.smb.erp.repo.ProductCategoryRepository;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.ProductTypeRepository;
import com.smb.erp.repo.UnitRepository;
import com.smb.erp.repo.VatProductRegisterRepository;
import com.smb.erp.util.Utils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FatemaLaptop
 */
@Service
public class RepositoryService implements Serializable {

    @Autowired
    ProductRepository prodRepo;

    @Autowired
    ProductCategoryRepository catRepo;

    @Autowired
    UnitRepository unitRepo;

    @Autowired
    BrandRepository brandRepo;

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    ProductTypeRepository ptRepo;

    @Autowired
    VatProductRegisterRepository vprRepo;

    @Autowired
    ProductAccountRepository paRepo;

    HashMap<String, BaseRepository> repositories = new HashMap<>();
    HashMap<String, Class> primaryTypes = new HashMap<>();

    public RepositoryService() {
    }

    @PostConstruct
    public void init() {
        repositories.put(Product.class.getName(), prodRepo);
        repositories.put(ProductCategory.class.getName(), catRepo);
        repositories.put(Unit.class.getName(), unitRepo);
        repositories.put(Brand.class.getName(), brandRepo);
        repositories.put(Country.class.getName(), countryRepo);
        repositories.put(ProductType.class.getName(), ptRepo);
        repositories.put(VatProductRegister.class.getName(), vprRepo);
        repositories.put(ProductAccount.class.getName(), paRepo);

        primaryTypes.put(Product.class.getName(), Long.class);
        primaryTypes.put(ProductCategory.class.getName(), Long.class);
        primaryTypes.put(Unit.class.getName(), Long.class);
        primaryTypes.put(Brand.class.getName(), Long.class);
        primaryTypes.put(Country.class.getName(), Long.class);
        primaryTypes.put(ProductType.class.getName(), Long.class);
        primaryTypes.put(VatProductRegister.class.getName(), Long.class);
        primaryTypes.put(ProductAccount.class.getName(), Long.class);
    }

    public void printRepositories() {
        System.out.println(repositories);
    }

    public BaseRepository get(Class clz) {
        //System.out.println("RepositoryService.get: " + clz.getName());
        return repositories.get(clz.getName());
    }

    public Object getBeanValue(Object value, Class clz) {
        //System.out.println("getBeanValue.repoService:" + repoService);
        BaseRepository repo = get(clz);
        //System.out.println("getBeanValue.repo:" + repo);
        Object bean = null;
        if (repo != null) {
            //System.out.println(clz.getName() + " Found Repo: " + repo.getOne(Integer.parseInt(value)));
            try {
                Optional op = repo.findById(Utils.convertValue(primaryTypes.get(clz.getName()), value.toString()));
                if (op.isPresent()) {
                    bean = op.get();
                }
            } catch (Throwable ex) {
                System.out.println(clz.getName() + " cannot find Data for " + value);
                //ex.printStackTrace();
                return null;
            }
        }
        //System.out.println("getBeanValue: " + clz + "\t" + value + "\t" + bean);
        return bean;
    }

    public Object saveBean(Object bean, Class clz) {
        BaseRepository repo = get(clz);
        try {
            repo.save(bean);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
        return bean;
    }
}
