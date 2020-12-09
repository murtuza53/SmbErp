/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.BusinessPartner;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BusinessPartnerRepository extends BaseRepository<BusinessPartner, Long> {
    
    @Query("SELECT b FROM BusinessPartner as b WHERE (b.companyname LIKE %:criteria% OR b.email1 LIKE %:criteria%) "
            + "AND b.companytypes LIKE %:bustype% ORDER BY b.companyname")
    List<BusinessPartner> findBusinessPartnerByTypeBySearchCriteria(@Param("criteria") String criteria, @Param("bustype") String bustype);

    @Query("SELECT b FROM BusinessPartner as b WHERE b.companyname LIKE %:criteria% OR b.email1 LIKE %:criteria% ORDER BY b.companyname")
    List<BusinessPartner> findBusinessPartnerBySearchCriteria(@Param("criteria") String criteria);

}
