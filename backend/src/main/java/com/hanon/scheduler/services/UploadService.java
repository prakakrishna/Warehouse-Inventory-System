package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.UploadInterface;
import com.hanon.scheduler.table.UploadTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UploadService {
    @Autowired
    UploadInterface repository;

    public Optional<UploadTable> findById(Long id) {
        return repository.findById(id);
    }

    public List<UploadTable> getAll() {
        return repository.findAll();
    }

    public void create(UploadTable uploadFile) {
        repository.save(uploadFile);
    }

}
