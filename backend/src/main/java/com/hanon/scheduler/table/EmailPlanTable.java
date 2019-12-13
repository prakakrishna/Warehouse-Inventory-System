package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="email_plan")
public class EmailPlanTable {
    @Id
    @GeneratedValue(generator = "emailplan_seq")
    @SequenceGenerator(name="emailplan_seq", sequenceName = "EMAILPLAN_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="d1")
    Double d1;
    @Column(name="d2")
    Double d2;
    @Column(name="d3")
    Double d3;
    @Column(name="email_id")
    Long email_id;
    @Column(name="parent_id")
    Long parent_id;
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
    @OneToOne()
    @JoinColumn(name="email_id", insertable = false, updatable = false)
    EmailTable email;
    @OneToOne()
    @JoinColumn(name="parent_id", insertable = false, updatable = false)
    ParentTable parent;


    public Long getId() {
        return id;
    }
    public void setId(Long d1) {
        this.id = id;
    }
    public Double getD1() {
        return d1;
    }
    public void setD1(Double d1) {
        this.d1 = d1;
    }
    public Double getD2() {
        return d2;
    }
    public void setD2(Double d2) {
        this.d2 = d2;
    }
    public Double getD3() {
        return d3;
    }
    public void setD3(Double d3) {
        this.d3 = d3;
    }
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
    public Long getEmail_id() {
        return email_id;
    }
    public void setEmail_id(Long email_id) {
        this.email_id = email_id;
    }
    public Long getParent_id() {
        return parent_id;
    }
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
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
    public EmailTable getEmail() {
        return email;
    }
    public void setEmail(EmailTable email) {
        this.email = email;
    }
    public ParentTable getParent() {
        return parent;
    }
    public void setParent(ParentTable parent) {
        this.parent = parent;
    }
}
