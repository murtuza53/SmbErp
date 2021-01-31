/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Webpage;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface WebpageRepository extends BaseRepository<Webpage, Long> {
    
    @Query("SELECT b FROM Webpage as b WHERE b.moduleid.moduleid=:moduleid ORDER BY b.menuorder")
    List<Webpage> findWebpageByModuleid(@Param("moduleid") long moduleid);
}
