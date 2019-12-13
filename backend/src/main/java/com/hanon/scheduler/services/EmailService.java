package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.EmailInterface;
import com.hanon.scheduler.table.EmailTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    EmailInterface repository;

    public Optional<EmailTable> findById(Long id) {
        return repository.findById(id);
    }

    public List<EmailTable> getAll() {
        return repository.findAll();
    }

    public void create(EmailTable emailTable) {
        repository.save(emailTable);
    }

}
