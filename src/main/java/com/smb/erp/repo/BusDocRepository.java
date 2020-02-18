/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.BusDoc;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BusDocRepository extends BaseRepository<BusDoc, String> {

    @Query("SELECT a FROM BusDoc as a WHERE a.businesspartner.partnerid=:partnerid AND a.docno LIKE :criteria")
    List<BusDoc> findByBusinessPartnerBySearchCriteria(@Param("partnerid") String partnerid, @Param("criteria") String criteria);
    
}
