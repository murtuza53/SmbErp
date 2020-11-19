/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.AccDoc;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface AccDocRepository extends BaseRepository<AccDoc, String>{
    
    @Query("SELECT a FROM AccDoc as a WHERE a.docno LIKE :prefix% ORDER by a.docdate desc")
    List<AccDoc> findByAccDocByPrefix(@Param("prefix") String prefix);

    AccDoc findByRefno(String refno);
}
