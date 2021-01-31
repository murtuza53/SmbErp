/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Module;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface ModuleRepository extends BaseRepository<Module, Long> {

    @Query("SELECT b FROM Module as b WHERE b.active=1 ORDER BY b.modulename")
    List<Module> findActiveAll();
    
}
