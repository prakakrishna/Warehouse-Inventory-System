package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.PlanTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanInterface extends JpaRepository<PlanTable, Long> {
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.upload_id = :uploadId", nativeQuery = true)
    List<PlanTable> getPlanByUploadId(@Param("uploadId") Integer upload_id);
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.parent_id = :parentId", nativeQuery = true)
    List<PlanTable> getPlanByParentid(@Param("parentId") Long parent_id);
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.parent_id in (:parentIds)", nativeQuery = true)
    List<PlanTable> getPlanByParentids(@Param("parentIds") List<Long> parent_ids);
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.supplier_id = :supplierId", nativeQuery = true)
    List<PlanTable> getPlanBySupplierId(@Param("supplierId") Long supplier_id);
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.child_id = :childId", nativeQuery = true)
    List<PlanTable> getPlanByChildId(@Param("childId") Long child_id);
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.parent_id = :parentId and p.supplier_id = :supplierId", nativeQuery = true)
    List<PlanTable> getPlanByParentIdSupplierId(@Param("parentId") Long parent_id, @Param("supplierId") Long supplier_id);

    @Query(value = "select * from plan p where p.is_active = 'Y' and p.parent_id in (:parentId) and p.supplier_id = :supplierId", nativeQuery = true)
    List<PlanTable> getPlanByParentIdsSupplierId(@Param("parentId") List<Long> parent_id, @Param("supplierId") Long supplier_id);
    @Query(value = "select * from plan p where p.is_active = 'Y' and p.child_id in (:childId) and p.parent_id in (:parentId) and p.supplier_id = :supplierId", nativeQuery = true)
    List<PlanTable> getPlanByParentIdsChildIdsSupplierId(@Param("parentId") List<Long> parent_id, @Param("childId") List<Long> child_id, @Param("supplierId") Long supplier_id);


    @Query(value = "select * from plan p where p.is_active = 'Y' and p.parent_id in (:parentIds) and p.supplier_id in (:supplierIds)", nativeQuery = true)
    List<PlanTable> getPlanByParentIdsSupplierIds(@Param("parentIds") List <Long> parent_ids, @Param("supplierIds") List <Long> supplier_ids);

    @Query(value = "select count(1) from plan p where p.parent_id = :parent_id and p.child_id = :child_id and p.level = :level and p.is_active = 'Y'", nativeQuery = true)
    Integer isPlanExists(@Param("parent_id") Long parent_id, @Param("child_id") Long child_id, @Param("level") String level);
    /*List<PlanTable> findPlanByParentIdChildId(@Param("parentId") Long parent_id, @Param("childId") Long child_id);
    List<PlanTable> findPlanByParentIdChildIdSupplierId(@Param("parentId") Long parent_id, @Param("childId") Long child_id, @Param("supplierId") Long supplier_id);
*/

    /*@Query(value = "select * from plan where is_active = 'Y' and upload_id = :uploadId", nativeQuery = true)
    List<PlanTable> findByUploadId(@Param("uploadId") Long id);

    @Query(value = "select * from plan where is_active = 'Y' and upload_id = :uploadId and parent_id = :parentItem", nativeQuery = true)
    List<PlanTable> findByParentItem(@Param("uploadId") Long id, @Param("parentItem") String parent_id);

    @Query(value = "select * from plan where is_active = 'Y' and upload_id = :uploadId and parent_id = :parentItem and supplier_id = :supplierId", nativeQuery = true)
    List<PlanTable> findByParentItemSupplier(@Param("uploadId") int id, @Param("parentItem") Long parent_id, @Param("supplierId") Long supplier_id);
    */
}