package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.EmailTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailInterface extends JpaRepository<EmailTable, Long> {

}