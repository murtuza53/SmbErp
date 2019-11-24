/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Brand;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface BrandRepository extends BaseRepository<Brand, Long>{
    
    List<Brand> findByOrderByBrandnameAsc();
    
    List<Brand> findByBrandnameContainsOrAbbreviationContainsAllIgnoreCaseOrderByBrandnameAsc(String brandnamePart, String abbreviationPart);
}
