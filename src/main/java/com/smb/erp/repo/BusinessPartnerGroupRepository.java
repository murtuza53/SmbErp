/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Branch;
import com.smb.erp.entity.BusinessPartnerGroup;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BusinessPartnerGroupRepository extends BaseRepository<BusinessPartnerGroup, Long> {
    
    @Query("SELECT b FROM BusinessPartnerGroup as b WHERE b.groupname LIKE %:criteria% ORDER BY b.groupname")
    List<BusinessPartnerGroup> findGroupByCriteria(@Param("criteria") String criteria);
    
}
