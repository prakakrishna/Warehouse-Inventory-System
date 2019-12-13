package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="parent")
/*@NamedNativeQueries({
        @NamedNativeQuery(name = "getByParentitem", query = "select * from parent p where p.is_active = 'Y' and p.parent_item = :parent"),
        @NamedNativeQuery(name = "isParentItemExists", query = "select count(1) from parent p where p.parent_item = :parent_item"),
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "findIfExists", query = "select count(1) from parent where parent_item = :parentItem"),
})*/
public class ParentTable {
    @Id
    @GeneratedValue(generator = "parent_seq")
    @SequenceGenerator(name="parent_seq", sequenceName = "PARENT_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="parent_item")
    String parent_item;
    //@OneToOne(fetch = FetchType.LAZY, mappedBy = "parent")
    //PlanTable plan;
    @Column(name="end_item_desc")
    String end_item_desc;
    @Column(name="program")
    String program;
    @Column(name="commodity")
    String commodity;

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
    public String getParent_item() {
        return parent_item;
    }
    public void setParent_item(String parent_item) {
        this.parent_item = parent_item;
    }
    public String getEnd_item_desc() {
        return end_item_desc;
    }
    public void setEnd_item_desc(String end_item_desc) {
        this.end_item_desc = end_item_desc;
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
    public String getProgram() {
        return program;
    }
    public void setProgram(String program) {
        this.program = program;
    }
    public String getCommodity() {
        return commodity;
    }
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }
    /*public PlanTable getPlan() {
        return plan;
    }
    public void setPlan(PlanTable plan) {
        this.plan = plan;
    }*/

}
