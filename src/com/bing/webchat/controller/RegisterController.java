package com.bing.webchat.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.StringUtil;

@Controller
public class RegisterController {
	
	@Autowired 
	private UserService userService;

	
	@RequestMapping(value="/register")
	public String register(){
		return "register";
	}
	@RequestMapping(value="/main")
	public String main(){
		return "main";
	}
	
	@RequestMapping(value="/doRegister",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doRegister(CurrentUser currentUser){
		String resoult="fail";
		currentUser.setDelFlag("0");
		currentUser.setIsOnline("1");// 刚开始,不在线
		// 用户名不存在, 则保存
		if(userService.findUser(currentUser)==null){
			currentUser.setId(StringUtil.getGuid());
			userService.addUser(currentUser);
			resoult = "success";
		}
		else{
			resoult = "exist";
		}
		Map<String, Object> resoults = new HashMap<String,Object>();
		resoults.put("resoult", resoult);
		return resoults;
	}
}
