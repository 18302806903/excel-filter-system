package org.spring.springboot.service;

import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;
import org.spring.springboot.domain.TransformLog;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {

    void writeExcelPOI(InputStream is, HttpServletResponse response, String excelName);

    FilterObject getFilterObjectById(int id);
    List<FilterObject> getAllFilterObject();
    int saveFilterObject(FilterObject filterObject);
    int removeFilterObject(int id);

    List<FilterDetail> getFilterDetailsByObjectId(int objectId);
    List<FilterDetail> getAllFilterDetails();
    FilterDetail getFilterDetailsById(int id);
    int saveFilterDetail(FilterDetail filterDetail);
    int removeFilterDetail(int id);

    List<TransformLog> getTransformLogByExcelName(String excelName);
    int saveTransformLog(TransformLog transformLog);


}
