package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.ChildInterface;
import com.hanon.scheduler.table.ChildTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChildService {
    @Autowired
    ChildInterface repository;

    public ChildTable getByComponent(String component) {
        return repository.getByComponent(component);
    }
    public Integer isComponentExists(String component) {
        return repository.isComponentExists(component);
    }
    public Optional<ChildTable> findById(Long child_id) {
        return repository.findById(child_id);
    }

    public List<ChildTable> getAll() {
        return repository.findAll();
    }
    public void create(ChildTable plan) {
        repository.save(plan);
    }
}
