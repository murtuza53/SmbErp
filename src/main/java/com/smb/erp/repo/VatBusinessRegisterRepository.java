/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.VatAccountType;
import com.smb.erp.entity.VatBusinessRegister;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface VatBusinessRegisterRepository extends BaseRepository<VatBusinessRegister, Integer> {
    
}
