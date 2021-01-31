/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.controller;

import com.smb.erp.UserSession;
import com.smb.erp.entity.Account;
import com.smb.erp.entity.SystemDefaults;
import com.smb.erp.repo.AccountRepository;
import com.smb.erp.repo.SystemDefaultsRepository;
import com.smb.erp.util.StringUtils;
import com.smb.erp.util.SystemConfig;
import static com.smb.erp.util.SystemConfig.PRINT_LETTERHEAD_IMAGE;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author Burhani152
 */
@Named(value = "systemDefaultsController")
@SessionScope
public class SystemDefaultsController extends AbstractController<SystemDefaults> {

    SystemDefaultsRepository systemRepo;

    HashMap<String, String> propertyTable;

    @Autowired
    AccountRepository accountRepo;
    
    @Autowired
    UserSession userSession;

    @Autowired
    public SystemDefaultsController(SystemDefaultsRepository repo) {
        super(SystemDefaults.class, repo);
        this.systemRepo = repo;
    }

    @PostConstruct
    public void init() {
        refresh();
    }

    public void refresh() {
        propertyTable = new HashMap<String, String>();

        List<SystemDefaults> sds = findAllData();
        System.out.println("SystemDefaultsController:" + sds);
        for (SystemDefaults sd : sds) {
            if (!propertyTable.containsKey(sd.getReferredclass() + "." + sd.getPropertyname())) {
                propertyTable.put(sd.getReferredclass() + "." + sd.getPropertyname(), sd.getValue());
            }
        }
        System.out.println("SystemDefaultsController: " + propertyTable);
                try {
            System.out.println("Loading LetterHead: " + getByPropertyname("LetterHeadLocation").getValue()
                    + userSession.getLoggedInCompany().getCompanyid() + ".png");
            PRINT_LETTERHEAD_IMAGE = ImageIO.read(new File(getByPropertyname("LetterHeadLocation").getValue()
                    + userSession.getLoggedInCompany().getCompanyid() + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(SystemConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Account getDefaultAccount(String propertyname) {
        return accountRepo.getOne(propertyTable.get("Account." + propertyname));
    }

    public Account resolveAccount(Account account){
        System.out.println("resolveAccount: " + account);
        if(account.getNodetype().equalsIgnoreCase("INTERNAL_ACCOUNT_SYSTEM")){
            return resolveAccount(getDefaultAccount(account.getAccountname()));
        }
        return account;
    }
    
    public String getDefaultList(String propertyname) {
        return propertyTable.get("List." + propertyname);
    }

    public List<String> getAsList(String propertyname) {
        //System.out.println("getAsList => " + propertyname + " => " + propertyTable.get("List." + propertyname));
        return StringUtils.tokensToList(propertyTable.get("List." + propertyname));
        /*SystemDefaults sd = systemRepo.findByPropertyname(propertyname);
        if(sd!=null){
            String type = sd.getValuetype();
            if(type.equalsIgnoreCase("String")){
                return StringUtils.tokensToList(sd.getValue());
            } else {
                return convertStringListToIntList(StringUtils.tokensToList(sd.getValue()), Integer::parseInt);
            }
        }*/
    }
    
    public List<Integer> getAsListInteger(String propertyname) {
        return convertStringListToIntList(StringUtils.tokensToList(propertyTable.get("List." + propertyname)), Integer::parseInt);
    }    
    
    public List<String> getAsList(String propertyname, String firstValue) {
        return StringUtils.tokensToList(propertyTable.get("List." + propertyname), firstValue);
    }

    public List<SystemDefaults> findAllData() {
        return systemRepo.findAll();
    }

    public SystemDefaults getByPropertyname(String propertyName) {
        return systemRepo.findByPropertyname(propertyName);
    }

    public static <T, U> List<U>
            convertStringListToIntList(List<T> listOfString,
                    Function<T, U> function) {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }
            
    public String getSystemButtonStyle(){
        return systemRepo.findByPropertyname("SystemButtonStyle").getValue();
    }

    public String getToolbarButtonStyle(){
        return systemRepo.findByPropertyname("ToolbarButtonStyle").getValue();
    }
    
    public String formatDate(Date date){
        return SystemConfig.DATE_FORMAT.format(date);
    }
}
