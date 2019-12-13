package com.hanon.scheduler.services;

import com.hanon.scheduler.interfaces.EmailPlanInterface;
import com.hanon.scheduler.table.EmailPlanTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmailPlanService {
    @Autowired
    EmailPlanInterface repository;

    @Autowired
    ChildService childService;
    @Autowired
    SupplierService supplierService;
    @Autowired
    ParentService parentService;


    public EmailPlanTable getById(Long id) {
        return repository.findById(id).get();
    }

    public List<EmailPlanTable> getPlanBySupplierId(java.math.BigInteger supplier_id, Long email_id) {
        return repository.getPlanBySupplierId(supplier_id, email_id);
    }

    public List<EmailPlanTable> getPlanByEmailId(Long email_id) {
        return repository.getPlanByEmailId(email_id);
    }
    public List<EmailPlanTable> getByEmailId(Long email_id) {
        return repository.getByEmailId(email_id);
    }

    public EmailPlanTable searchPlan(Long id, Long parent_id, Long email_id, Long child_id, Long supplier_id) {
        return repository.searchPlan(id, parent_id, email_id, child_id, supplier_id);
    }

    public List<EmailPlanTable> getPlanByChildId(Long child_id) {
        return repository.getPlanByChildId(child_id);
    }
    public List<EmailPlanTable> getPlanByParentIdSupplierId(Long parent_id, Long supplier_id) {
        return repository.getPlanByParentIdSupplierId(parent_id, supplier_id);
    }
    public List<EmailPlanTable> getPlanByParentIdsSupplierIds(List <Long> parent_ids, List <Long> supplier_ids) {
        return repository.getPlanByParentIdsSupplierIds(parent_ids, supplier_ids);
    }
    public List<java.math.BigInteger> getSupplierIds(Long email_id) {
        return repository.getSupplierIds(email_id);
    }

    public List<EmailPlanTable> stagePlan(Long parent_id, Long d1, Long d2, Long d3, Long email_id) {
        List<Object[]> list = repository.stagePlan(parent_id, d1, d2, d3);
        ArrayList<EmailPlanTable> emailPlanTables = new ArrayList<EmailPlanTable>();

        for (Object[] l: list) {
            EmailPlanTable emailPlanTable = new EmailPlanTable();
            emailPlanTable.setIs_active('Y');
            emailPlanTable.setCreated_date(new Date());
            emailPlanTable.setEmail_id(email_id);
            emailPlanTable.setParent_id(Long.valueOf(l[0].toString()));
            emailPlanTable.setChild_id(Long.valueOf(l[1].toString()));
            emailPlanTable.setSupplier_id(Long.valueOf(l[2].toString()));
            emailPlanTable.setD1(Double.parseDouble(l[3].toString()));
            emailPlanTable.setD2(Double.parseDouble(l[4].toString()));
            emailPlanTable.setD3(Double.parseDouble(l[5].toString()));
            emailPlanTable.setParent(parentService.getById(Long.valueOf(l[0].toString())).get());
            emailPlanTable.setChild(childService.findById(Long.valueOf(l[1].toString())).get());
            emailPlanTable.setSupplier(supplierService.getById(Long.valueOf(l[2].toString())).get());
            emailPlanTables.add(emailPlanTable);
        }
        return emailPlanTables;
    }

    /*public List<EmailPlanTable> findPlanByParentIdChildId(Long parent_id, Long child_id) {
        return repository.findPlanByParentIdChildId(parent_id, child_id);
    }
    public List<EmailPlanTable> findPlanByParentIdChildIdSupplierId(Long parent_id, Long child_id, Long supplier_id) {
        return repository.findPlanByParentIdChildIdSupplierId(parent_id, child_id, supplier_id);
    }*/

    public List<EmailPlanTable> getAll() {
        return repository.findAll();
    }

    public void create(EmailPlanTable plan) {
        repository.save(plan);
    }

    public void update(EmailPlanTable plan) { repository.save(plan); }

}
