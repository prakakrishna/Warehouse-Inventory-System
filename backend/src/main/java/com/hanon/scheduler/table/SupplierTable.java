package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="supplier")
/*@NamedNativeQueries({
        @NamedNativeQuery(name = "findBySupplier", query = "select * from supplier p where p.is_active = 'Y' and p.supplier = :supplier"),
        @NamedNativeQuery(name = "isSupplierExists", query = "select count(1) from supplier s where s.supplier = :supplier"),
})*/
public class SupplierTable {
    @Id
    @GeneratedValue(generator = "supplier_seq")
    @SequenceGenerator(name="supplier_seq", sequenceName = "SUPPLIER_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="supplier")
    String supplier;
    @Column(name="name")
    String name;
    @Column(name="email")
    String email;
    @Column(name="is_active")
    Character is_active;
    @Column(name="created_date")
    Date created_date;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
}
