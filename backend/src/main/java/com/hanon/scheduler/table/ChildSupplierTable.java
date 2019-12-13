package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="child_supplier")
public class ChildSupplierTable {
    @Id
    @GeneratedValue(generator = "child_supplier_seq")
    @SequenceGenerator(name="child_supplier_seq", sequenceName = "CHILDSUP_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="child_id")
    Long child_id;
    @Column(name="supplier_id")
    Long supplier_id;
    @Column(name="is_active")
    Character is_active;
    @Column(name="created_date")
    Date created_date;

    @OneToOne()
    @JoinColumn(name="child_id", insertable = false, updatable = false)
    ChildTable child;
    @OneToOne()
    @JoinColumn(name="supplier_id", insertable = false, updatable = false)
    SupplierTable supplier;

    public Long getChild_id() {
        return child_id;
    }
    public void setChild_id(Long child_id) {
        this.child_id = child_id;
    }
    public Long getSupplier_id() {
        return supplier_id;
    }
    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
    }
    public Character getIs_active() {
        return is_active;
    }
    public void setIs_active(Character is_active) {
        this.is_active = is_active;
    }
    public Date getCreated_date() {
        return created_date;
    }
    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public ChildTable getChild() {
        return child;
    }
    public void setChild(ChildTable child) {
        this.child = child;
    }
    public SupplierTable getSupplier() {
        return supplier;
    }
    public void setSupplier(SupplierTable supplier) {
        this.supplier = supplier;
    }
}
