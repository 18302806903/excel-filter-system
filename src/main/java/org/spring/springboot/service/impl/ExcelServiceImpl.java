package org.spring.springboot.service.impl;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.dao.FilterDetailDao;
import org.spring.springboot.dao.FilterObjectDao;
import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;
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

    @Override
    public void writeExcelPOI(InputStream is, HttpServletResponse response) {
        try {
            XSSFWorkbook xwb = new XSSFWorkbook(is);
            XSSFSheet xSheet = xwb.getSheetAt(0);
            int totalRows = xSheet.getLastRowNum();
            for (int i = 0; i <= totalRows; i++){
                if(xSheet.getRow(i)==null){
                    continue;
                }
                for (int j = 0; j <=  xSheet.getRow(i).getPhysicalNumberOfCells(); j++) {
                    if(xSheet.getRow(i).getCell(j)==null){
                        continue;
                    }
                    String cellStr = xSheet.getRow(i).getCell(j).toString();
//                    System.err.println(cellStr);
                    XSSFCell xCell=xSheet.getRow(i).getCell(j);
                    List<FilterDetail> filterDetails = this.getAllFilterDetails();

                    if (filterDetails == null){
                        break;
                    }
                    filterDetails.sort((a,b) -> StringUtil.sort(String.valueOf(a.getPriority()), String.valueOf(b.getPriority())));
                    for (FilterDetail filterDetail : filterDetails){
                        String filter = filterDetail.getFilter();
                        if (filter != null){
                            if (cellStr.contains(filter)){
                                String replaceStr = cellStr.replace(filter,"");
                                xCell.setCellValue(replaceStr);
                            }
                        }
                    }
//                    filterObjects.stream().forEach(filterObject ->{
//                        List<FilterDetail> filterDetails = filterObject.getFilterDetailList();
//                        if (filterDetails == null){
//                            return;
//                        }
//                        filterDetails.sort((a,b) -> StringUtil.sort(String.valueOf(a.getPriority()), String.valueOf(b.getPriority())));
//                        filterDetails.stream().forEach(filterDetail -> {
//                            String filter = filterDetail.getFilter();
////                            System.err.println(filter);
//                            if (filter != null){
//                                if (cellStr.contains(filter)){
//                                    String replaceStr = cellStr.replace(filter,"");
//                                    xCell.setCellValue(replaceStr);
//                                }
//                            }
//                        });
//                    });
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
    public List<FilterDetail> getFilterDetailsByObjectId(int objectId) {
        return filterDetailDao.getFilterDetailsByObjectId(objectId);
    }

    public List<FilterDetail> getAllFilterDetails(){
        return filterDetailDao.getAllFilterDetails();
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

    private void doFilter(String cellStr, XSSFCell xCell){
        // 若条件匹配修改对象
        if (cellStr.contains(",China")){
            xCell.setCellValue(cellStr.replace(",China",""));
        } else if (cellStr.contains("<br>Place Of Origin:China (Mainland)")){
            xCell.setCellValue(cellStr.replace("<br>Place Of Origin:China (Mainland)",""));
        } else if (cellStr.contains("Place Of Origin:China (Mainland),")){
            xCell.setCellValue(cellStr.replace("Place Of Origin:China (Mainland),",""));
        } else if (cellStr.contains("China")){
            xCell.setCellValue(cellStr.replace("China",""));
        }
    }

}
