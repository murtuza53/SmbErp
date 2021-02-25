/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.PageAccess;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface PageAccessRepository extends BaseRepository<PageAccess, Long> {

    @Query("SELECT b FROM PageAccess as b WHERE b.pageid.moduleid.moduleid=:moduleid ORDER BY b.pageid.menuorder")
    List<PageAccess> findAccessByModule(@Param("moduleid") long moduleid);
    
    @Query("SELECT b FROM PageAccess as b WHERE b.roleid.roleid=:roleid ORDER BY b.pageid.menuorder")
    List<PageAccess> findAccessByRole(@Param("roleid") long roleid);
    
    @Query("SELECT b FROM PageAccess as b WHERE b.roleid.roleid=:roleid ORDER BY b.pageid.moduleid.modulename")
    List<PageAccess> findAccessByRoleOrderByModule(@Param("roleid") long roleid);

}
