package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.ParentTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParentInterface extends JpaRepository<ParentTable, Long> {
    @Query(value = "select * from parent p where p.is_active = 'Y' and p.parent_item = :parent", nativeQuery = true)
    ParentTable getByParentitem(@Param("parent") String parent_item);
    @Query(value = "select count(1) from parent p where p.parent_item = :parent_item", nativeQuery = true)
    Integer isParentItemExists(@Param("parent_item") String parent_item);
}
