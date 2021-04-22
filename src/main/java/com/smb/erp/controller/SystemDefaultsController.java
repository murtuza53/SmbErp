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
import static com.smb.erp.util.SystemConfig.DATE_FORMAT_PATTERN;
import static com.smb.erp.util.SystemConfig.DECIMAL_FORMAT_PATTERN;
import static com.smb.erp.util.SystemConfig.INTEGER_FORMAT_PATTERN;
import static com.smb.erp.util.SystemConfig.MYSQL_DATE_PATTERN;
import static com.smb.erp.util.SystemConfig.PRINT_LETTERHEAD_IMAGE;
import static com.smb.erp.util.SystemConfig.TIME_FORMAT_PATTERN;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

        SystemConfig.TIME_FORMAT_PATTERN = getSystemPropertyValue("DefaultTimeFormat");
        SystemConfig.DATE_FORMAT_PATTERN = getSystemPropertyValue("DefaultDateFormat");
        SystemConfig.DECIMAL_FORMAT_PATTERN = getSystemPropertyValue("DefaultDecimalFormat");
        SystemConfig.INTEGER_FORMAT_PATTERN = getSystemPropertyValue("DefaultIntegerFormat");
        SystemConfig.MYSQL_DATE_PATTERN = getSystemPropertyValue("DefaultMysqlDateTimeFormat");
        SystemConfig.DECIMAL_TEXT_ALIGN = getSystemPropertyValue("DecimalNumberAlignment");
        SystemConfig.INTEGER_TEXT_ALIGN = getSystemPropertyValue("IntegerNumberAlignment");

        SystemConfig.TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_PATTERN);
        SystemConfig.DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        SystemConfig.MYSQL_DATE_FORMAT = new SimpleDateFormat(MYSQL_DATE_PATTERN);
        SystemConfig.DECIMAL_FORMAT = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
        SystemConfig.INTEGER_FORMAT = new DecimalFormat(INTEGER_FORMAT_PATTERN);
    }

    public Account getDefaultAccount(String propertyname) {
        //Optional<Account> acc = accountRepo.findById(propertyTable.get("Account." + propertyname));
        //return acc.get();
        return accountRepo.getOne(propertyTable.get("Account." + propertyname));
    }

    public Account resolveAccount(Account account) {
        //System.out.println("resolveAccount: " + account);
        if (account.getNodetype().equalsIgnoreCase("INTERNAL_ACCOUNT_SYSTEM")) {
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

    public boolean isSystemProperty(String property) {
        SystemDefaults sys = getByPropertyname(property);
        return sys != null;
    }

    public String getSystemPropertyValue(String property) {
        SystemDefaults sys = getByPropertyname(property);
        if (sys == null) {
            return "Invalid Property - " + property;
        }
        return sys.getValue();
    }

    public Object getSystemPropertyAsObject(String property) {
        SystemDefaults sys = getByPropertyname(property);
        if (sys == null) {
            return "Invalid Property - " + property;
        }
        if (sys.getReferredclass().equalsIgnoreCase("String") || sys.getReferredclass().equalsIgnoreCase("VatCategory")) {
            return sys.getValue();
        } else if (sys.getReferredclass().equalsIgnoreCase("Account")) {
            return getDefaultAccount(property);
        } else if (sys.getReferredclass().equalsIgnoreCase("List")) {
            return getAsList(property);
        }
        return sys.getValue();
    }

    public static <T, U> List<U>
            convertStringListToIntList(List<T> listOfString,
                    Function<T, U> function) {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public String getSystemButtonStyle() {
        return systemRepo.findByPropertyname("SystemButtonStyle").getValue();
    }

    public String getToolbarButtonStyle() {
        return systemRepo.findByPropertyname("ToolbarButtonStyle").getValue();
    }

    public String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return SystemConfig.DATE_FORMAT.format(date);
    }

    public String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        return SystemConfig.TIME_FORMAT.format(date);
    }

    public String formatMysqlDate(Date date) {
        if (date == null) {
            return "";
        }
        return SystemConfig.MYSQL_DATE_FORMAT.format(date);
    }

    public String formatDecimal(Double value) {
        if (value == null) {
            return "";
        }
        return SystemConfig.DECIMAL_FORMAT.format(value);
    }

    public String formatDecimal(Float value) {
        if (value == null) {
            return "";
        }
        return SystemConfig.DECIMAL_FORMAT.format(value);
    }

    public String formatDecimal(Long value) {
        if (value == null) {
            return "";
        }
        return SystemConfig.DECIMAL_FORMAT.format(value);
    }

    public String formatNumber(Long value) {
        if (value == null) {
            return "";
        }
        return SystemConfig.INTEGER_FORMAT.format(value);
    }

    public String formatNumber(Integer value) {
        if (value == null) {
            return "";
        }
        return SystemConfig.INTEGER_FORMAT.format(value);
    }
}
