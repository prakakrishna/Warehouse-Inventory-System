package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.PlanInterface;
import com.hanon.scheduler.table.PlanTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    @Autowired
    PlanInterface repository;

    public List<PlanTable> getPlanByUploadId(Integer upload_id) {
        return repository.getPlanByUploadId(upload_id);
    }

    public List<PlanTable> getPlanByParentid(Long parent_id) {
        return repository.getPlanByParentid(parent_id);
    }
    public List<PlanTable> getPlanByParentids(List<Long> parent_ids) {
        return repository.getPlanByParentids(parent_ids);
    }

    public List<PlanTable> getPlanBySupplierId(Long supplier_id) {
        return repository.getPlanBySupplierId(supplier_id);
    }

    public List<PlanTable> getPlanByChildId(Long child_id) {
        return repository.getPlanByChildId(child_id);
    }
    public List<PlanTable> getPlanByParentIdSupplierId(Long parent_id, Long supplier_id) {
        return repository.getPlanByParentIdSupplierId(parent_id, supplier_id);
    }
    public List<PlanTable> getPlanByParentIdsSupplierId(List<Long> parent_id, Long supplier_id) {
        return repository.getPlanByParentIdsSupplierId(parent_id, supplier_id);
    }
    public List<PlanTable> getPlanByParentIdsChildIdsSupplierId(List<Long> parent_id, List<Long> child_id, Long supplier_id) {
        return repository.getPlanByParentIdsChildIdsSupplierId(parent_id, child_id, supplier_id);
    }public List<PlanTable> getPlanByParentIdsSupplierIds(List <Long> parent_ids, List <Long> supplier_ids) {
        return repository.getPlanByParentIdsSupplierIds(parent_ids, supplier_ids);
    }
    public Integer isPlanExists(Long parent_id, Long child_id, String level) {
        return repository.isPlanExists(parent_id, child_id, level);
    }
    /*public List<PlanTable> findPlanByParentIdChildId(Long parent_id, Long child_id) {
        return repository.findPlanByParentIdChildId(parent_id, child_id);
    }
    public List<PlanTable> findPlanByParentIdChildIdSupplierId(Long parent_id, Long child_id, Long supplier_id) {
        return repository.findPlanByParentIdChildIdSupplierId(parent_id, child_id, supplier_id);
    }*/

    public List<PlanTable> getAll() {
        return repository.findAll();
    }

    public void create(PlanTable plan) {
        repository.save(plan);
    }

}
