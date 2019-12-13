package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.ChildSupplierTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildSupplierInterface extends JpaRepository<ChildSupplierTable, Long> {
    @Query(value = "select * from child_supplier p where p.is_active = 'Y' and p.child_id = :childId", nativeQuery = true)
    List<ChildSupplierTable> getByChildId(@Param("childId") Long child_id);
    @Query(value = "select * from child_supplier p where p.is_active = 'Y' and p.supplier_id = :supplierId", nativeQuery = true)
    List<ChildSupplierTable> getBySupplierId(@Param("supplierId") Long supplier_id);

    @Query(value = "select count(1) from child_supplier p where p.child_id = :child_id and p.supplier_id = :supplier_id and p.is_active = 'Y'", nativeQuery = true)
    Integer isExists(@Param("child_id") Long child_id, @Param("supplier_id") Long supplier_id);

    @Query(value = "select * from child_supplier s where s.is_active = 'Y' order by child_id", nativeQuery = true)
    List<ChildSupplierTable> getAll();
}