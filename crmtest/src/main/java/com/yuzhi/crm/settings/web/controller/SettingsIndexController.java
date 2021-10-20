package com.yuzhi.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "settings")
public class SettingsIndexController {

    @RequestMapping(value = "toIndex.do")
    public String toIndex(){

        return "/settings/index";
    }
}
