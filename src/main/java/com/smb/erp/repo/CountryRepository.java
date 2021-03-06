/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.Country;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface CountryRepository extends BaseRepository<Country, Long> {

    List<Country> findByOrderByCountrynameAsc();

    List<Country> findByOrderByCountrysymAsc();

    List<Country> findByOrderByCurrencysymAsc();

    @Query("SELECT c FROM Country as c WHERE c.defcountry=true")
    Country findCountryDefault();

}
