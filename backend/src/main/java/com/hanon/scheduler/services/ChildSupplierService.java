package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.ChildSupplierInterface;
import com.hanon.scheduler.table.ChildSupplierTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildSupplierService {
    @Autowired
    ChildSupplierInterface repository;

    public List<ChildSupplierTable> getBySupplierId(Long supplier_id) {
        return repository.getBySupplierId(supplier_id);
    }

    public List<ChildSupplierTable> getByChildId(Long child_id) {
        return repository.getByChildId(child_id);
    }
    public Integer isExists(Long child_id, Long supplier_id) {
        return repository.isExists(child_id, supplier_id);
    }
    public List<ChildSupplierTable> getAll() {
        return repository.findAll();
    }
    public void create(ChildSupplierTable plan) {
        repository.save(plan);
    }

}
