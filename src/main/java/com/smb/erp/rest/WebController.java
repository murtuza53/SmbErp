/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author FatemaLaptop
 */
@Controller
public class WebController {
    
    @RequestMapping(value = "/")
    public String index() {
        return "forward:/index.xhtml";
    }
}
