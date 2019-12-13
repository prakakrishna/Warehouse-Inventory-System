package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="plan")
public class PlanTable {
    @Id
    @GeneratedValue(generator = "parent_child_seq")
    @SequenceGenerator(name="parent_child_seq", sequenceName = "PARENT_CHILD_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="upload_id")
    Integer upload_id;
    @Column(name="seq")
    Long seq;
    @Column(name="parent_id")
    Long parent_id;
    @Column(name="child_id")
    Long child_id;
    @Column(name="level")
    String level;
    @Column(name="usage")
    Double usage;
    /*@Column(name="supplier_id")
    Long supplier_id;*/
    @Column(name="is_active")
    Character is_active;
    @Column(name="created_date")
    Date created_date;

    @OneToOne()
    @JoinColumn(name="parent_id", insertable = false, updatable = false)
    ParentTable parent;

    @OneToOne()
    @JoinColumn(name="child_id", insertable = false, updatable = false)
    ChildTable child;
    /*@OneToOne()
    @JoinColumn(name="supplier_id", insertable = false, updatable = false)
    SupplierTable supplier;*/


    public Integer getUpload_id() {
        return upload_id;
    }
    public void setUpload_id(Integer upload_id) {
        this.upload_id = upload_id;
    }
    public Long getSeq() {
        return seq;
    }
    public void setSeq(Long seq) {
        this.seq = seq;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public Long getParent_id() {
        return parent_id;
    }
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }
    public Long getChild_id() {
        return child_id;
    }
    public void setChild_id(Long child_id) {
        this.child_id = child_id;
    }
    public Double getUsage() {
        return usage;
    }
    public void setUsage(Double usage) {
        this.usage = usage;
    }
    /*public Long getSupplier_id() {
        return supplier_id;
    }
    public void setSupplier_id(Long supplier_id) {
        this.supplier_id = supplier_id;
    }*/
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

    public ParentTable getParent() {
        return parent;
    }
    public void setParent(ParentTable parent) {
        this.parent = parent;
    }
    public ChildTable getChild() {
        return child;
    }
    public void setChild(ChildTable child) {
        this.child = child;
    }
    /*public SupplierTable getSupplier() {
        return supplier;
    }
    public void setSupplier(SupplierTable supplier) {
        this.supplier = supplier;
    }*/

}
