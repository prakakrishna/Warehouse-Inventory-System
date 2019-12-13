package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.EmailPlanTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmailPlanInterface extends JpaRepository<EmailPlanTable, Long> {
    @Query(value = "select distinct supplier_id from email_plan p where p.is_active = 'Y' and p.email_id = :emailId", nativeQuery = true)
    List<java.math.BigInteger> getSupplierIds(@Param("emailId") Long email_id);
    @Query(value = "select * from email_plan p where p.is_active = 'Y' and p.parent_id = :parentId", nativeQuery = true)
    List<EmailPlanTable> getPlanByParentid(@Param("parentId") Long parent_id);
    @Query(value = "select * from email_plan p where p.is_active = 'Y' and p.id = :Id and p.parent_id = :parentId and p.email_id = :emailId and p.supplier_id = :supplierId and p.child_id = :childId", nativeQuery = true)
    EmailPlanTable searchPlan(@Param("parentId") Long parent_id, @Param("Id") Long id, @Param("emailId") Long email_id, @Param("childId") Long child_id, @Param("supplierId") Long supplier_id);
    @Query(value = "select max(id) as id, child_id, max(created_date) as created_date, sum(d1) as d1, sum(d2) as d2, sum(d3) as d3, max(email_id) as email_id, max(is_active) as is_active, supplier_id, min(parent_id) as parent_id from email_plan p where p.is_active = 'Y' and p.supplier_id = :supplierId and p.email_id = :emailId group by child_id, supplier_id order by child_id, supplier_id", nativeQuery = true)
    List<EmailPlanTable> getPlanBySupplierId(@Param("supplierId") java.math.BigInteger supplier_id, @Param("emailId") Long email_id);
    @Query(value = "select max(id) as id, child_id, max(created_date) as created_date, sum(d1) as d1, sum(d2) as d2, sum(d3) as d3, max(email_id) as email_id, max(is_active) as is_active, supplier_id, min(parent_id) as parent_id from email_plan p where p.is_active = 'Y' and p.email_id = :emailId group by child_id, supplier_id order by child_id, supplier_id", nativeQuery = true)
    List<EmailPlanTable> getByEmailId(@Param("emailId") Long email_id);
    @Query(value = "select * from email_plan p where p.is_active = 'Y' and email_id = :emailId order by child_id, supplier_id, parent_id", nativeQuery = true)
    List<EmailPlanTable> getPlanByEmailId(@Param("emailId") Long email_id);

    @Query(value = "select * from parent_child p where p.is_active = 'Y' and p.child_id = :childId", nativeQuery = true)
    List<EmailPlanTable> getPlanByChildId(@Param("childId") Long child_id);
    @Query(value = "select * from parent_child p where p.is_active = 'Y' and p.parent_id = :parentId and p.supplier_id = :supplierId", nativeQuery = true)
    List<EmailPlanTable> getPlanByParentIdSupplierId(@Param("parentId") Long parent_id, @Param("supplierId") Long supplier_id);

    @Query(value = "select * from parent_child p where p.is_active = 'Y' and p.parent_id in (:parentIds) and p.supplier_id in (:supplierIds)", nativeQuery = true)
    List<EmailPlanTable> getPlanByParentIdsSupplierIds(@Param("parentIds") List<Long> parent_ids, @Param("supplierIds") List<Long> supplier_ids);

    @Query(value = "select distinct p.id as parent_id, c.id as child_id, s.id as supplier_id, sum(:d1 * l.usage) d1, sum(:d2 * l.usage) d2, sum(:d3 * l.usage) d3 from plan l, parent p, supplier s, child c, child_supplier cs where p.id = l.parent_id and p.id = :parentId and l.child_id = c.id and s.id = cs.supplier_id and c.id = cs.child_id and p.is_active = 'Y' and l.is_active = 'Y' and c.is_active = 'Y' and cs.is_active = 'Y' group by p.id, c.id, s.id", nativeQuery = true)
    List<Object[]> stagePlan(@Param("parentId") Long parent_id, @Param("d1") Long d1, @Param("d2") Long d2, @Param("d3") Long d3);



    /*List<EmailPlanTable> findPlanByParentIdChildId(@Param("parentId") Long parent_id, @Param("childId") Long child_id);
    List<EmailPlanTable> findPlanByParentIdChildIdSupplierId(@Param("parentId") Long parent_id, @Param("childId") Long child_id, @Param("supplierId") Long supplier_id);
*/

    /*@Query(value = "select * from parent_child where is_active = 'Y' and upload_id = :uploadId", nativeQuery = true)
    List<EmailPlanTable> findByUploadId(@Param("uploadId") Long id);

    @Query(value = "select * from parent_child where is_active = 'Y' and upload_id = :uploadId and parent_id = :parentItem", nativeQuery = true)
    List<EmailPlanTable> findByParentItem(@Param("uploadId") Long id, @Param("parentItem") String parent_id);

    @Query(value = "select * from parent_child where is_active = 'Y' and upload_id = :uploadId and parent_id = :parentItem and supplier_id = :supplierId", nativeQuery = true)
    List<EmailPlanTable> findByParentItemSupplier(@Param("uploadId") int id, @Param("parentItem") Long parent_id, @Param("supplierId") Long supplier_id);
    */
}