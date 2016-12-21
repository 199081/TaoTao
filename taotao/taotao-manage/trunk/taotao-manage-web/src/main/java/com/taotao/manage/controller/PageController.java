package com.taotao.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通用页面跳转逻辑
 * 
 */
@RequestMapping("page")
@Controller
public class PageController {

    @RequestMapping("{pageName}")
    public String toPage(@PathVariable("pageName") String pageName) {
        return pageName;
    }

}
