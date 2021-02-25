/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.dynamic.report;

import java.util.HashMap;

/**
 *
 * @author Burhani152
 */
public class ColorSchemeManager {
    public static HashMap<String, ColorScheme> themes = new HashMap();
    
    static{
        ColorScheme scheme = new ColorScheme();
        scheme.setSchemeName("Daisy Blue");
        scheme.setPrimaryColor("#00296b");
        scheme.setSecondaryColor("#00509d");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#ffd500");
        themes.put(scheme.getSchemeName(), scheme);
        
        scheme = new ColorScheme();
        scheme.setSchemeName("Olive");
        scheme.setPrimaryColor("#797d62");
        scheme.setSecondaryColor("#9b9b7a");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#ffcb69");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Maroon");
        scheme.setPrimaryColor("#6f1d1b");
        scheme.setSecondaryColor("#bb9457");
        scheme.setTextPrimaryColor("#432818");
        scheme.setInversePrimaryColor("#ffe6a7");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Teal");
        scheme.setPrimaryColor("#555b6e");
        scheme.setSecondaryColor("#89b0ae");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#faf9f9");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Sunset");
        scheme.setPrimaryColor("#f25c54");
        scheme.setSecondaryColor("#f4845f");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#ffffff");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Rainbow");
        scheme.setPrimaryColor("#cdb4db");
        scheme.setSecondaryColor("#ffc8dd");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#000000");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Green");
        scheme.setPrimaryColor("#137547");
        scheme.setSecondaryColor("#5bba6f");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#ffffff");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Rajestan Royal");
        scheme.setPrimaryColor("#12306e");
        scheme.setSecondaryColor("#eba4c3");
        scheme.setTextPrimaryColor("#060e2d");
        scheme.setInversePrimaryColor("#ffffff");
        themes.put(scheme.getSchemeName(), scheme);
        
        scheme = new ColorScheme();
        scheme.setSchemeName("Maimoon Scheme");
        scheme.setPrimaryColor("#2a2a86");
        scheme.setSecondaryColor("#9e9e9e");
        scheme.setTextPrimaryColor("#060e2d");
        scheme.setInversePrimaryColor("#ffffff");
        themes.put(scheme.getSchemeName(), scheme);

        scheme = new ColorScheme();
        scheme.setSchemeName("Printed Stationary");
        scheme.setPrimaryColor("#000000");
        scheme.setSecondaryColor("#ffffff");
        scheme.setTextPrimaryColor("#000000");
        scheme.setInversePrimaryColor("#ffffff");
        themes.put(scheme.getSchemeName(), scheme);
        
    }
}
