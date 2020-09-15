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

    @Query("SELECT a FROM BusDoc as a WHERE a.docno LIKE %:prefix% ORDER by a.docdate desc")
    List<BusDoc> findByBusDocByPrefix(@Param("prefix") String prefix);

    @Query("SELECT a FROM BusDoc as a WHERE a.docno LIKE %:prefix% AND a.businesspartner.partnerid = :partnerid ORDER by a.docdate desc")
    List<BusDoc> findByBusDocByPrefixAndBusinessPartner(@Param("prefix") String prefix, @Param("partnerid") int partnerid);
    
    //@Query(value="SELECT * FROM busdoc as a WHERE a.docno LIKE '%:prefix%' AND a.partnerid=:partnerid ORDER by a.docdate desc", nativeQuery = true)
    //List<BusDoc> findByBusDocByPrefixAndBusinessPartner(@Param("prefix") String prefix, @Param("partnerid") int partnerid);
    
}
