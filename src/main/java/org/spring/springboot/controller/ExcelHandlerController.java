package org.spring.springboot.controller;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.spring.springboot.dao.FilterDetailDao;
import org.spring.springboot.dao.FilterObjectDao;
import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;
import org.spring.springboot.service.CityService;
import org.spring.springboot.service.impl.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class ExcelHandlerController {

    @Autowired
    private ExcelServiceImpl excelService;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView uploadView(ModelAndView mav) {
        mav = new ModelAndView("upload");
        return mav;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response ) {
        try {
            response.setContentType("application/force-download");// 设置强制下载不打开
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = file.getOriginalFilename();
            response.addHeader("Content-Disposition","attachment;fileName=transform-" +dateFormat.format
                    (new Date())+"-"+new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            excelService.writeExcelPOI(file.getInputStream(), response);
        } catch (IOException e){
            System.err.println(e);
        }
        return "success";
    }







}
