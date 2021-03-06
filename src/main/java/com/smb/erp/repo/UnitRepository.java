/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Unit;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface UnitRepository extends BaseRepository<Unit, Long>{
    
    List<Unit> findByOrderByUnitname();
    
    List<Unit> findByOrderByUnitsym();
}
