package org.spring.springboot.controller;

import org.spring.springboot.domain.City;
import org.spring.springboot.domain.FilterDetail;
import org.spring.springboot.service.CityService;
import org.spring.springboot.service.impl.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private ExcelServiceImpl excelService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mav) {
        mav = new ModelAndView("bookList");
        List<FilterDetail> filterDetailList = excelService.getAllFilterDetails();
        mav.addObject("bookList",filterDetailList);
        return mav;
    }

}
