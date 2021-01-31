/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.repo;

import com.smb.erp.entity.PrintReport;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Burhani152
 */
@Repository
public interface PrintReportRepository extends BaseRepository<PrintReport, Long> {

    @Query("SELECT b FROM PrintReport as b WHERE b.bdinfoid.bdinfoid=:bdinfoid ORDER BY b.defaultreport, b.reportid")
    List<PrintReport> findReportByBdinfoid(@Param("bdinfoid") Integer bdinfoid);

}
