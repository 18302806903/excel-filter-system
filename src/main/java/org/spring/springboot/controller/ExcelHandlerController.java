package org.spring.springboot.controller;

import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.domain.FilterObject;
import org.spring.springboot.domain.TransformLog;
import org.spring.springboot.service.impl.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ExcelHandlerController {

    @Autowired
    private ExcelServiceImpl excelService;

    @RequestMapping(value = "/filter/object", method = RequestMethod.GET)
    public ModelAndView filterObjectView(ModelAndView mav) {
        mav = new ModelAndView("filterObject");
        List<FilterObject> filterObjects = excelService.getAllFilterObject();
        mav.addObject("filterObject", filterObjects);
        return mav;
    }

    @RequestMapping(value = "/filter/object/remove/{objectId}", method = RequestMethod.GET)
    public String RemoveFilterObjectView(ModelAndView mav, @PathVariable String objectId) {
        int result = excelService.removeFilterObject(Integer.valueOf(objectId));
        return "redirect:/filter/object";
    }

    @RequestMapping(value = "/filter/detail/{objectId}", method = RequestMethod.GET)
    public ModelAndView filterDetailView(ModelAndView mav, @PathVariable String objectId) {
        mav = new ModelAndView("filterDetail");
        List<FilterDetail> filterDetails = excelService.getFilterDetailsByObjectId(Integer.valueOf(objectId));
        mav.addObject("filterDetail", filterDetails);
        mav.addObject("objectId", objectId);
        return mav;
    }

    @RequestMapping(value = "/filter/detail/create/{objectId}", method = RequestMethod.GET)
    public ModelAndView createFilterDetailView(@PathVariable String objectId) {
        ModelAndView mav = new ModelAndView("filterDetailCreate");
        mav.addObject("objectId",objectId);
        return mav;
    }

    @RequestMapping(value = "/filter/detail/create", method = RequestMethod.POST)
    public String createDetailObject(FilterDetail filterDetail) {
        String objectId = String.valueOf(filterDetail.getObjectId());
        int result = excelService.saveFilterDetail(filterDetail);
        return "redirect:/filter/detail/" + objectId;
    }

    @RequestMapping(value = "/filter/detail/remove/{id}", method = RequestMethod.GET)
    public String removeDetailObjectView(@PathVariable String id) {
        FilterDetail filterDetail = excelService.getFilterDetailsById(Integer.valueOf(id));
        int result = excelService.removeFilterDetail(Integer.valueOf(id));
        return "redirect:/filter/detail/" + filterDetail.getObjectId();
    }

    @RequestMapping(value = "/filter/object/create", method = RequestMethod.GET)
    public ModelAndView createFilterObjectView(ModelAndView mav) {
        mav = new ModelAndView("filterObjectCreate");
        return mav;
    }

    @RequestMapping(value = "/filter/object/create", method = RequestMethod.POST)
    public String createFilterObject(FilterObject filterObject) {
        int result = excelService.saveFilterObject(filterObject);
        return "redirect:/filter/object";
    }

    @RequestMapping(value = "/filter/result/search", method = RequestMethod.GET)
    public String filterSearchView() {
        return "filterSearch";
    }

    @PostMapping(value = "/filter/result/search")
    public ModelAndView filterSearchResultView(TransformLog transformLog) {
        ModelAndView mav = new ModelAndView("filterSearchResult");
        List<TransformLog> transformLogList = excelService.getTransformLogByExcelName(transformLog.getExcelName());
        mav.addObject("transformLog", transformLogList);
        return mav;
    }

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
            String outputExcelName = "transform-"+dateFormat.format(new Date())+"-"+fileName;
            response.addHeader("Content-Disposition","attachment;fileName=transform-" +dateFormat.format
                    (new Date())+"-"+new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            excelService.writeExcelPOI(file.getInputStream(), response, outputExcelName);
        } catch (IOException e){
            System.err.println(e);
        }
        return "success";
    }
}
