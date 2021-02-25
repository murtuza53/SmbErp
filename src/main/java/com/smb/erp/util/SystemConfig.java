/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import java.awt.Image;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Burhani152
 */
public class SystemConfig implements Serializable{
    
    //@Autowired
    //SystemDefaultsController sysCon;
    
    //@Autowired
    //UserSession userSession;
    
    public static String TIME_FORMAT_PATTERN = "HH:mm:ss";
    public static String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    public static String DECIMAL_FORMAT_PATTERN = "#,##0.000";
    public static String INTEGER_FORMAT_PATTERN = "######0";
    public static String MYSQL_DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_PATTERN);
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    public static SimpleDateFormat MYSQL_DATE_FORMAT = new SimpleDateFormat(MYSQL_DATE_PATTERN);
    public static DecimalFormat DECIMAL_FORMAT = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
    public static DecimalFormat INTEGER_FORMAT = new DecimalFormat(INTEGER_FORMAT_PATTERN);
    public static String DECIMAL_TEXT_ALIGN = "right";
    public static String INTEGER_TEXT_ALIGN = "center";
    public static String CURRENCY_SYMBOL = "BD";
    public static String[] YELLOW_COLORS = {"#ffffcc", "#ffff99", "#ffff66"};
    public static String[] GREEN_COLORS = {"#ffffcc", "#ffff99", "#ffff66"};
    public static String[] BLUE_COLORS = {"#e6f7ff", "#cceeff", "#b3e6ff"};
    public static String[] PURPLE_COLORS = {"#ffccff", "#ff99ff", "#ff66ff"};
    public static String[] RED_COLORS = {"#ffd9cc", "#ff9999", "#ff6666"};
    public static String[] GRAY_COLORS = {"#f2f2f2", "#e6e6e6", "#d9d9d9"};
    public static String[][] COLORS = new String[][]{YELLOW_COLORS,     //yellow
                                                     GREEN_COLORS,     //green
                                                     BLUE_COLORS,     //blue
                                                     PURPLE_COLORS,     //purpulish
                                                     RED_COLORS,     //red
                                                     GRAY_COLORS};    //gray
    public static Image PRINT_LETTERHEAD_IMAGE = null;

    //private static SystemConfig instance;
    
    /*static {
        try {
            PRINT_LETTERHEAD_IMAGE = ImageIO.read(new File("D:\\Murtuza\\OneDrive\\MTC\\Stationary\\MTC LetterHead.png"));
            
        } catch (IOException ex) {
            Logger.getLogger(SystemConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    /*public static SystemConfig getInstance(){
        if(instance==null){
            instance = new SystemConfig();
        }
        return instance;
    }
    
    public Image getLetterHeadImage(){
        if(PRINT_LETTERHEAD_IMAGE==null){
            try {
                PRINT_LETTERHEAD_IMAGE = ImageIO.read(new File(sysCon.getByPropertyname("LetterHeadLocation").getValue()
                        + userSession.getLoggedInCompany().getCompanyid() + ".png"));
            } catch (IOException ex) {
                Logger.getLogger(SystemConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return PRINT_LETTERHEAD_IMAGE;
    }*/
    
    public String getDateFormatPattern(){
        return DATE_FORMAT_PATTERN;
    }
    
    public String getDecimalFormatString(){
        return DECIMAL_FORMAT_PATTERN;
    }    
}
