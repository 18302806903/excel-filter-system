package org.spring.springboot.domain;


public class FilterDetail {

    private Long id;
    private String name;
    private String description;
    private String filter;
    private String replaceText;
    private Integer priority;
    private Long objectId;
//    private FilterObject filterObject;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getReplaceText() {
        return replaceText;
    }

    public void setReplaceText(String replaceText) {
        this.replaceText = replaceText;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

//    public FilterObject getFilterObject() {
//        return filterObject;
//    }
//
//    public void setFilterObject(FilterObject filterObject) {
//        this.filterObject = filterObject;
//    }
}
