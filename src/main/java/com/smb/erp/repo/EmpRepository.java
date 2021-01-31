/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Emp;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface EmpRepository extends BaseRepository<Emp, Long> {

    List<Emp> findByOrderByEmpnameAsc();

    @Query("SELECT e FROM Emp as e WHERE e.company.companyid=:companyid ORDER BY e.empname ASC")
    List<Emp> findEmpByCompany(@Param("companyid") Long companyid);

    @Query("SELECT e FROM Emp as e WHERE e.authentication.username=:username")
    Emp findEmpByUsername(@Param("username") String username);
    
}
