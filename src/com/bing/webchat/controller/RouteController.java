package com.bing.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 页面跳转
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "")
public class RouteController {

    @RequestMapping(value = "")
    public String index() {
        return "redirect:/user/login";
    }


    /**
     * 聊天主页
     */
    @RequestMapping(value = "/chat")
    public String getIndex(){
      return "index";
    }
    @RequestMapping(value = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/help")
    public String help() {
        return "help";
    }
    @RequestMapping(value = "/info")
    public String info() {
    	return "info-setting";
    }

    @RequestMapping(value = "/information")
    public String information() {
        return "information";
    }
    
    @RequestMapping(value = "/friendSetting")
    public String friendSetting() {
        return "friendSetting";
    }
    
    @RequestMapping(value = "/groupSetting")
    public String groupSetting() {
        return "groupSetting";
    }
    @RequestMapping(value = "/systemSetting")
    public String systemSetting() {
    	return "system-setting";
    }

}
