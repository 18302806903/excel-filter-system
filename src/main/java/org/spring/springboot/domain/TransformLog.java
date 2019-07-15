package org.spring.springboot.domain;


public class TransformLog {

    private Long id;
    private String excelName;
    private String filter;
    private String location;
    private String textBefore;
    private String textAfter;
    private int success;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTextBefore() {
        return textBefore;
    }

    public void setTextBefore(String text_before) {
        this.textBefore = text_before;
    }

    public String getTextAfter() {
        return textAfter;
    }

    public void setTextAfter(String text_after) {
        this.textAfter = text_after;
    }

    public boolean isSuccess() {
        return success==1?true:false;
    }

    public void setSuccess(boolean success) {
        this.success = success==true?1:0;
    }
}
