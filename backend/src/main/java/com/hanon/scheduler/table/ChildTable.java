package com.hanon.scheduler.table;

import javax.persistence.*;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;

@Entity
@Table(name="child")
/*@NamedNativeQueries({
        @NamedNativeQuery(name = "getByComponent", query = "select * from child p where p.is_active = 'Y' and p.component = :component"),
        @NamedNativeQuery(name = "isComponentExists", query = "select count(1) from child c where c.component = :component"),
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "findIfExists", query = "select count(1) from child where parent_item = :component"),
})*/

public class ChildTable {
    @Id
    @GeneratedValue(generator = "child_seq")
    @SequenceGenerator(name="child_seq", sequenceName = "CHILD_SEQ")
    @Column(name="id")
    Long id;
    @Column(name="component")
    String component;
    @Column(name="component_desc")
    String component_desc;
    @Column(name="component2_desc")
    String component2_desc;
    @Column(name="uom")
    String uom;
    @Column(name="item_type")
    String item_type;
    @Column(name="cost_total")
    Double cost_total;
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
    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = component;
    }
    public String getComponent_desc() {
        return component_desc;
    }
    public void setComponent_desc(String component_desc) {
        this.component_desc = component_desc;
    }
    public String getComponent2_desc() {
        return component2_desc;
    }
    public void setComponent2_desc(String component2_desc) {
        this.component2_desc = component2_desc;
    }

    public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
        this.uom = uom;
    }
    public String getItem_type() {
        return item_type;
    }
    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }
    public Double getCost_total() {
        return cost_total;
    }
    public void setCost_total(Double cost_total) {
        this.cost_total = cost_total;
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
