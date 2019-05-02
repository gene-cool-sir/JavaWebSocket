package com.bing.webchat.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.WordDefined;

/**
 * 用户登录与注销
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(CurrentUser currentUser, HttpSession session, RedirectAttributes attributes,
                       HttpServletRequest request) {
    	currentUser.setDelFlag("0");
        CurrentUser user = userService.findUser(currentUser);
        if (user == null) {
            attributes.addFlashAttribute("error", WordDefined.LOGIN_PASSWORD_ERROR);
            return "redirect:/user/login";
        } else {
        	// 更新用户登录状态
        	user.setIsOnline("0");
        	user.setUpdateTime(new Date());
        	userService.updateUser(user);
            attributes.addFlashAttribute("message", WordDefined.LOGIN_SUCCESS);
            session.setAttribute("currentUser", user);
            session.setAttribute("login_status", true);
            return "redirect:/chat";
        }
    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession session, RedirectAttributes attributes) {
    	CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
    	user.setIsOnline("1");
    	user.setUpdateTime(new Date());
    	userService.updateUser(user);
        session.removeAttribute("currentUser");
        session.removeAttribute("login_status");
        return "redirect:/user/login";
    }
    
//    @RequestMapping(value = "/login12", method = RequestMethod.GET)
//    public String login() {
//        return "login12";
//    }

    @RequestMapping(value = "/login12", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(CurrentUser currentUser, HttpSession session,
                      HttpServletRequest request) {
    	Map<String, Object> resoults = new HashMap<String,Object>();
		String resoult="fail";
    	currentUser.setDelFlag("0");
        CurrentUser user = userService.findUser(currentUser);
        if (user == null) {
        	resoult = "unexist";
        } else {
            if (user.getPassword().equals(currentUser.getPassword())) {
            	// 更新用户登录状态
            	user.setIsOnline("0");
            	user.setUpdateTime(new Date());
            	userService.updateUser(user);
                session.setAttribute("currentUser", user);
                session.setAttribute("login_status", true);
                resoult = "success";
            } else {
            	resoult = "wrong";
            }
        }
        resoults.put("resoult", resoult);
        return resoults;
    }
    
}
