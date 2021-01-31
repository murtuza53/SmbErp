/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.entity.Account;
import com.smb.erp.entity.Branch;
import com.smb.erp.entity.Brand;
import com.smb.erp.entity.BusDoc;
import com.smb.erp.entity.BusDocExpense;
import com.smb.erp.entity.BusDocInfo;
import com.smb.erp.entity.BusinessPartner;
import com.smb.erp.entity.ContactPerson;
import com.smb.erp.entity.Country;
import com.smb.erp.entity.CreditLimit;
import com.smb.erp.entity.Emp;
import com.smb.erp.entity.PartialPaymentDetail;
import com.smb.erp.entity.PayTerms;
import com.smb.erp.entity.Product;
import com.smb.erp.entity.ProductAccount;
import com.smb.erp.entity.ProductCategory;
import com.smb.erp.entity.ProductType;
import com.smb.erp.entity.Unit;
import com.smb.erp.entity.VatAccountType;
import com.smb.erp.entity.VatCategory;
import com.smb.erp.entity.VatProductRegister;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.BaseRepository;
import com.smb.erp.repo.BranchRepository;
import com.smb.erp.repo.BrandRepository;
import com.smb.erp.repo.BusDocExpenseRepository;
import com.smb.erp.repo.BusDocInfoRepository;
import com.smb.erp.repo.BusDocRepository;
import com.smb.erp.repo.BusinessPartnerRepository;
import com.smb.erp.repo.ContactPersonRepository;
import com.smb.erp.repo.CountryRepository;
import com.smb.erp.repo.CreditLimitRepository;
import com.smb.erp.repo.EmpRepository;
import com.smb.erp.repo.PartialPaymentDetailRepository;
import com.smb.erp.repo.PayTermsRepository;
import com.smb.erp.repo.ProductAccountRepository;
import com.smb.erp.repo.ProductCategoryRepository;
import com.smb.erp.repo.ProductRepository;
import com.smb.erp.repo.ProductTypeRepository;
import com.smb.erp.repo.UnitRepository;
import com.smb.erp.repo.VatAccountTypeRepository;
import com.smb.erp.repo.VatCategoryRepository;
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

    @Autowired
    BusinessPartnerRepository bpRepo;

    @Autowired
    VatCategoryRepository vcRepo;

    @Autowired
    VatAccountTypeRepository vatRepo;

    @Autowired
    CreditLimitRepository clRepo;

    @Autowired
    AccountRepository acRepo;

    @Autowired
    ContactPersonRepository cpRepo;

    @Autowired
    EmpRepository empRepo;

    @Autowired
    BusDocInfoRepository bdInfo;

    @Autowired
    BranchRepository brnachRepo;

    @Autowired
    BusDocExpenseRepository bdeRepo;

    @Autowired
    PartialPaymentDetailRepository ppdRepo;

    @Autowired
    BusDocRepository busdocRepo;

    @Autowired
    PayTermsRepository paytermsRepo;

    HashMap<String, BaseRepository> repositories = new HashMap<>();
    HashMap<String, Class> primaryTypes = new HashMap<>();

    public RepositoryService() {
    }

    @PostConstruct
    public void init() {
        addRepository(Product.class.getName(), prodRepo, Long.class);
        addRepository(ProductCategory.class.getName(), catRepo, Long.class);
        addRepository(Unit.class.getName(), unitRepo, Long.class);
        addRepository(Brand.class.getName(), brandRepo, Long.class);
        addRepository(Branch.class.getName(), brnachRepo, Long.class);
        addRepository(Country.class.getName(), countryRepo, Long.class);
        addRepository(ProductType.class.getName(), ptRepo, Long.class);
        addRepository(VatProductRegister.class.getName(), vprRepo, Long.class);
        addRepository(ProductAccount.class.getName(), paRepo, Long.class);
        addRepository(BusinessPartner.class.getName(), bpRepo, Long.class);
        addRepository(CreditLimit.class.getName(), clRepo, Integer.class);
        addRepository(VatCategory.class.getName(), vcRepo, Long.class);
        addRepository(VatAccountType.class.getName(), vatRepo, Long.class);
        addRepository(Account.class.getName(), acRepo, String.class);

        addRepository(ContactPerson.class.getName(), cpRepo, Long.class);
        addRepository(Emp.class.getName(), empRepo, Long.class);
        addRepository(BusDocInfo.class.getName(), bdInfo, Integer.class);
        addRepository(BusDocExpense.class.getName(), bdeRepo, Integer.class);
        addRepository(PartialPaymentDetail.class.getName(), ppdRepo, Long.class);
        addRepository(BusDoc.class.getName(), busdocRepo, String.class);
        addRepository(PayTerms.class.getName(), paytermsRepo, Long.class);
    }

    public void addRepository(String className, BaseRepository repo, Class primaryIdCls) {
        repositories.put(className, repo);
        primaryTypes.put(className, primaryIdCls);
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
            try {
                //System.out.println(clz.getName() + " => " + value + " Found Repo: " + Utils.convertValue(primaryTypes.get(clz.getName()), value.toString()) + " => " + repo);
                Optional op = repo.findById(Utils.convertValue(primaryTypes.get(clz.getName()), value.toString()));
                if (op.isPresent()) {
                    //System.out.println("VALLUE_FROM_REPOSITORY: " + op.get());
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
