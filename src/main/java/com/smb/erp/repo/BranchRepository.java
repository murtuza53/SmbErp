/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Branch;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BranchRepository extends BaseRepository<Branch, Integer> {
    
    @Query("SELECT b FROM Branch as b WHERE b.company.companyid=:companyid ORDER BY b.branchname")
    List<Branch> findBranchByCompanyId(@Param("companyid") int companyid);
    
}
