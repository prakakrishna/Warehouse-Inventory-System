package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="email")
public class EmailTable {
    @Id
    @GeneratedValue(generator = "email_seq")
    @SequenceGenerator(name="email_seq", sequenceName = "EMAIL_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="description")
    String description;
    @Column(name="created_date")
    Date created_date;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getCreated_date() {
        return created_date;
    }
    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}












