package org.spring.springboot.domain;


import java.util.List;

public class FilterObject {

    private Long id;
    private String name;
    private String desc;
    private List<FilterDetail> filterDetailList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<FilterDetail> getFilterDetailList() {
        return filterDetailList;
    }

    public void setFilterDetailList(List<FilterDetail> filterDetailList) {
        this.filterDetailList = filterDetailList;
    }
}
