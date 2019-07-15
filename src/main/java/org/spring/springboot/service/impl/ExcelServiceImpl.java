package org.spring.springboot.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.dao.FilterDetailDao;
import org.spring.springboot.dao.FilterObjectDao;
import org.spring.springboot.dao.TransformLogDao;
import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;
import org.spring.springboot.domain.TransformLog;
import org.spring.springboot.service.ExcelService;
import org.spring.springboot.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private FilterObjectDao filterObjectDao;

    @Autowired
    private FilterDetailDao filterDetailDao;

    @Autowired
    private TransformLogDao transformLogDao;

    @Override
    public void writeExcelPOI(InputStream is, HttpServletResponse response, String excelName) {
        try {
            XSSFWorkbook xwb = new XSSFWorkbook(is);
            XSSFSheet xSheet = xwb.getSheetAt(0);
            List<FilterDetail> filterDetails = this.getAllFilterDetails();
            if (filterDetails == null){
                return;
            }
            filterDetails.sort((a,b) -> StringUtil.sort(String.valueOf(a.getPriority()), String.valueOf(b.getPriority())));
            for (FilterDetail filterDetail : filterDetails){
                String filter = filterDetail.getFilter();
                if (filter != null){
                    int totalRows = xSheet.getLastRowNum();
                    for (int i = 0; i <= totalRows; i++){
                        if(xSheet.getRow(i)==null){
                            continue;
                        }
                        for (int j = 0; j <=  xSheet.getRow(i).getLastCellNum(); j++) {
                            if(xSheet.getRow(i).getCell(j)==null){
                                continue;
                            }
                            String cellStr = xSheet.getRow(i).getCell(j).toString();
                            XSSFCell xCell=xSheet.getRow(i).getCell(j);

                            if (StringUtils.deleteWhitespace(cellStr.toLowerCase()).contains(StringUtils.deleteWhitespace(filter.toLowerCase()).replace(" ",""))){
                                if (filter.equalsIgnoreCase("lady")){
                                    System.err.println("hello");
                                }
                                String finalFilter = "(?i)"+filter;
                                String replaceStr = cellStr.replaceAll(finalFilter,"");
                                xCell.setCellValue(replaceStr);

                                TransformLog transformLog = new TransformLog();
                                transformLog.setExcelName(excelName);
                                transformLog.setFilter(filter);
                                transformLog.setLocation(StringUtil.getExcelColIndexToStr(1+j)+ String.valueOf(1+i));
                                transformLog.setTextBefore(cellStr);
                                transformLog.setTextAfter(replaceStr);
                                if (StringUtils.equals(cellStr, replaceStr)){
                                    transformLog.setSuccess(false);
                                } else {
                                    transformLog.setSuccess(true);
                                }
                                saveTransformLog(transformLog);
                            }
                        }
                    }
                }
            }
            OutputStream out = response.getOutputStream();
            xwb.write(out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FilterObject getFilterObjectById(int id) {
        return filterObjectDao.getFilterObjectById(id);
    }

    @Override
    public List<FilterObject> getAllFilterObject() {
        return filterObjectDao.getAllFilterObject();
    }

    @Override
    public int saveFilterObject(FilterObject filterObject) {
        return filterObjectDao.saveFilterObject(filterObject);
    }

    @Override
    public List<FilterDetail> getFilterDetailsByObjectId(int objectId) {
        return filterDetailDao.getFilterDetailsByObjectId(objectId);
    }

    @Override
    public List<FilterDetail> getAllFilterDetails(){
        return filterDetailDao.getAllFilterDetails();
    }

    @Override
    public int removeFilterObject(int id) {
        return filterObjectDao.removeFilterObject(id);
    }

    @Override
    public int saveFilterDetail(FilterDetail filterDetail) {
        return filterDetailDao.saveFilterDetail(filterDetail);
    }

    @Override
    public int removeFilterDetail(int id) {
        return filterDetailDao.removeFilterDetail(id);
    }

    @Override
    public FilterDetail getFilterDetailsById(int id) {
        return filterDetailDao.getFilterDetailsById(id);
    }

    @Override
    public List<TransformLog> getTransformLogByExcelName(String excelName) {
        return transformLogDao.getTransformLogByExcelName(excelName);
    }

    @Override
    public int saveTransformLog(TransformLog transformLog) {
        return transformLogDao.saveTransformLog(transformLog);
    }

    private String getValue(XSSFCell xCell) {
        if (xCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(xCell.getBooleanCellValue());
        } else if (xCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {

            return String.valueOf(xCell.getNumericCellValue());
        } else {

            return String.valueOf(xCell.getStringCellValue());
        }
    }
}
