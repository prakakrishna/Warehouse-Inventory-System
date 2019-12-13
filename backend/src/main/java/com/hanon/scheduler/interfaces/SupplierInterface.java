package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.SupplierTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierInterface extends JpaRepository<SupplierTable, Long> {
    @Query(value = "select * from supplier p where p.is_active = 'Y' and p.supplier = :supplier", nativeQuery = true)
    SupplierTable findBySupplier(@Param("supplier") String supplier);
    @Query(value = "select count(1) from supplier s where s.supplier = :supplier and s.is_active = 'Y'", nativeQuery = true)
    Integer isSupplierExists(@Param("supplier") String supplier);
    @Query(value = "select * from supplier s where s.is_active = 'Y' order by supplier", nativeQuery = true)
    List<SupplierTable> getAll();
}
