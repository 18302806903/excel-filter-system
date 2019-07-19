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
import java.util.Set;

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
                String replaceText = filterDetail.getReplaceText();
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

                            boolean isContainsFilter = StringUtils.deleteWhitespace(cellStr.toLowerCase()).contains(StringUtils.deleteWhitespace(filter.toLowerCase()).replace(" ",""));
                            // 完全匹配包含
                            if (isContainsFilter){
                                String finalFilter = "(?i)"+filter;
                                String replaceResultText = null;
                                if (!StringUtil.checkNull(replaceText)){
                                    replaceResultText = cellStr.replaceAll(finalFilter,replaceText);
                                } else {
                                    replaceResultText = cellStr.replaceAll(finalFilter,"");
                                }

                                xCell.setCellValue(replaceResultText);
                                saveTransformLogsAudit(excelName,cellStr, filter, replaceText, replaceResultText, i, j);
                            } else {
                                // 特殊匹配包含，是否包含 *
                                if (filter.contains("*")){
                                    if (filter.startsWith("*")){

                                    } else if(filter.endsWith("*")){
                                        String[] arrays = filter.split("\\*");
                                        String finalFilter = cellStr.substring(cellStr.indexOf(arrays[0]));;
                                        String replaceResultText = cellStr.replaceAll(finalFilter, "");
                                        xCell.setCellValue(replaceResultText);
                                        saveTransformLogsAudit(excelName,cellStr, filter, replaceText, replaceResultText, i, j);
                                    } else {
                                        // * 在中间，
                                        String[] arrays = filter.split("\\*");
                                        if (arrays.length != 2){
                                            // 包含多个* 错误
                                        } else {
                                            if (cellStr.contains(arrays[0])){
                                                String containStr =cellStr.substring(cellStr.indexOf(arrays[0]));
                                                if (cellStr.contains(containStr)){
                                                    String replaceResultText = StringUtil.subRangeString(cellStr, arrays[0], arrays[1]);
                                                    xCell.setCellValue(replaceResultText);
                                                    saveTransformLogsAudit(excelName,cellStr, filter, replaceText, replaceResultText, i, j);
                                                }
                                            }
                                        }
                                    }
                                } else if (filter.contains("?")){
                                    if (filter.endsWith("?")){
                                        String[] arrays = filter.split("\\?");
                                        if (cellStr.contains(arrays[0])){
                                            String[] cellArrays = cellStr.split(arrays[0]);
                                            String replaceResultText = cellArrays[0] + arrays[0] + 9 + cellArrays[1].substring(1);
                                            xCell.setCellValue(replaceResultText);
                                            saveTransformLogsAudit(excelName,cellStr, filter, replaceText, replaceResultText, i, j);
                                        }

                                    }
                                }
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

    @Override
    public Set<TransformLog> getSimpleTransformLogByExcelName(String excelName) {
        return transformLogDao.getSimpleTransformLogByExcelName(excelName);
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

    private void saveTransformLogsAudit(String excelName,String cellStr, String filter, String replaceText, String replaceResultText, int i, int j){
        TransformLog transformLog = new TransformLog();
        transformLog.setExcelName(excelName);
        transformLog.setFilter(filter);
        transformLog.setReplaceText(replaceText);
        transformLog.setLocation(StringUtil.getExcelColIndexToStr(1+j)+ String.valueOf(1+i));
        transformLog.setTextBefore(cellStr);
        transformLog.setTextAfter(replaceResultText);
        if (StringUtils.equals(cellStr, replaceResultText)){
            transformLog.setSuccess(false);
        } else {
            transformLog.setSuccess(true);
        }
        saveTransformLog(transformLog);
    }
}
