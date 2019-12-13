package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.ChildTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChildInterface extends JpaRepository<ChildTable, Long> {
    @Query(value = "select * from child p where p.is_active = 'Y' and p.component = :component", nativeQuery = true)
    ChildTable getByComponent(@Param("component") String component);
    @Query(value = "select count(1) from child c where c.is_active = 'Y' and c.component = :component", nativeQuery = true)
    Integer isComponentExists(@Param("component") String component);
}
