package com.hanon.scheduler.interfaces;

import com.hanon.scheduler.table.UploadTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadInterface extends JpaRepository<UploadTable, Long> {

}