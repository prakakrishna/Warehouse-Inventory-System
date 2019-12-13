package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.ParentInterface;
import com.hanon.scheduler.table.ParentTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
    @Autowired
    ParentInterface repository;

    public ParentTable getByParentItem(String parent_item) {
        return repository.getByParentitem(parent_item);
    }
    public Integer isParentItemExists(String parent_item) {
        return repository.isParentItemExists(parent_item);
    }
    public List<ParentTable> getAll() {
        return repository.findAll();
    }
    public Optional<ParentTable> getById(Long parent_id) {
        return repository.findById(parent_id);
    }
    public void create(ParentTable plan) {
        repository.save(plan);
    }
}
