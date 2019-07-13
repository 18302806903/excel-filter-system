package org.spring.springboot.service;

import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {

    void writeExcelPOI(InputStream is, HttpServletResponse response);

    FilterObject getFilterObjectById(int id);
    List<FilterObject> getAllFilterObject();

    List<FilterDetail> getFilterDetailsByObjectId(int objectId);
    List<FilterDetail> getAllFilterDetails();


}
