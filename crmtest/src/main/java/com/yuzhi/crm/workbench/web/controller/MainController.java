package com.yuzhi.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping(value = "/workbench/main")
public class MainController {

    @RequestMapping(value = "toIndex.do")
    public String toIndex(){

        return "/workbench/main/index";
    }
}
