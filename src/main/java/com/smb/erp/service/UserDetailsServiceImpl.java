/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.entity.Emp;
import com.smb.erp.repo.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author Burhani152
 */
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    private EmpRepository empRepo;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Emp emp = empRepo.findEmpByUsername(username);
         
        if (emp == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
         
        return new EmpUserDetails(emp);
    }
}
