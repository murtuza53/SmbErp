/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.VatProductRegister;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface VatProductRegisterRepository extends BaseRepository<VatProductRegister, Integer> {

    @Query("SELECT a FROM VatProductRegister as a WHERE a.vatregisterid = :vatregisterid")
    VatProductRegister findById(@Param("vatregisterid") String vatregisterid);

}
