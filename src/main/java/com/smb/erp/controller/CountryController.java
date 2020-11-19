/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.entity.Country;
import com.smb.erp.repo.CountryRepository;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author FatemaLaptop
 */
@Named(value = "countryController")
@ViewScoped
public class CountryController extends AbstractController<Country>{
    
    CountryRepository countryRepo;
    
    private List<Country> countryList;
    
    @Autowired
    public CountryController(CountryRepository repo){
        this.countryRepo = repo;
    }
    
    public List<Country> getCountryAll(){
        if(countryList==null){
            countryList = countryRepo.findByOrderByCountrynameAsc();
        }
        return countryList;
    }
    
    public List<Country> getCountryAllDefaultFirst(){
        if(countryList==null){
            countryList = countryRepo.findByOrderByCountrynameAscDefaultFirst();
        }
        return countryList;
    }

    public List<Country> getCurrencyAll(){
        return countryRepo.findByOrderByCurrencysymAsc();
    }
    
    public Country findCountryDefault(){
        return countryRepo.findCountryDefault();
    }
}
