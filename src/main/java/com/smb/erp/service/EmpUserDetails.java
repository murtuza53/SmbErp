/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.service;

import com.smb.erp.entity.Emp;
import com.smb.erp.entity.EmpRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Burhani152
 */
public class EmpUserDetails implements UserDetails {

    private Emp emp;

    public EmpUserDetails(Emp user){
        this.emp = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<EmpRole> roles = emp.getEmproles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getRolename()));
        });

        return authorities;
    }

    @Override
    public String getPassword() {
        return emp.getAuthentication().getPassword();
    }

    @Override
    public String getUsername() {
        return emp.getAuthentication().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emp.isActive();
    }

}
