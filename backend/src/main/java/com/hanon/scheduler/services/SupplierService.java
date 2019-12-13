package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.SupplierInterface;
import com.hanon.scheduler.table.SupplierTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    SupplierInterface repository;

    public SupplierTable getBySupplier(String supplier) {
        return repository.findBySupplier(supplier);
    }
    public Integer isSupplierExists(String supplier) {
        return repository.isSupplierExists(supplier);
    }
    public List<SupplierTable> getAll() {
        return repository.getAll();
    }
    public Optional<SupplierTable> getById(Long supplier_id) {
        return repository.findById(supplier_id);
    }
    public void create(SupplierTable plan) {
        repository.save(plan);
    }
    public void update(SupplierTable plan) {
        repository.save(plan);
    }
}
