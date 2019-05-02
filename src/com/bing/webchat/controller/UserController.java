package com.bing.webchat.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.MatchUtil;
import com.bing.webchat.utils.UploadUtil;


@Controller
public class UserController {
	
	@Autowired 
	private UserService userService;
	
	/**
	 * 更新用户是否上线状态
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/changeUserIsOnline",method=RequestMethod.POST)
	@ResponseBody
	public String changeUserIsOnline(String userId){
		JSONObject resoult = new JSONObject();
		try {
			CurrentUser user = userService.findUserById(userId);
			user.setIsOnline("1");//下线了
			user.setUpdateTime(new Date());
			userService.updateUser(user);
			resoult.put("message", "success");
		}catch (Exception e) {
			e.printStackTrace();
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
	
	 /**
     * 显示个人信息编辑页面
     * @param userid
     * @param sessionid
     * @return
     */
    @RequestMapping(value = "/config")
    public String setting(HttpSession session){
        CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
        user = userService.findUserById(user.getId());
        session.setAttribute("currentUser", user);
        return "info-setting";
    }
	
    /**
     * 更改用户基本信息
     * @param currentUser
     * @param attributes
     * @return
     */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public String updateUser(CurrentUser currentUser,HttpSession session, RedirectAttributes attributes){
		try {
			 userService.updateUser(currentUser);
			 attributes.addFlashAttribute("message", "["+currentUser.getNickname()+"]资料更新成功!");
		}catch (Exception e) {
			e.printStackTrace();
			 attributes.addFlashAttribute("error", "["+currentUser.getNickname()+"]资料更新失败!");
		}
		 return "redirect:/config";
	}
	
	 /**
     * 修改密码
     * @param userid
     * @param oldpass
     * @param newpass
     * @return
     */
    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    public String changePassword(String oldpass, String newpass, RedirectAttributes attributes,
                                 HttpSession session, HttpServletRequest request){
    	CurrentUser user = (CurrentUser)session.getAttribute("currentUser");
        if(oldpass.equals(user.getPassword())){
            user.setPassword(newpass);
            userService.updateUser(user);
           attributes.addFlashAttribute("message", "["+user.getNickname()+"]密码更新成功!");
        }else{
        	attributes.addFlashAttribute("error", "["+user.getNickname()+"]密码更新失败!");
        }
        return "redirect:/config";
    }

    /**
     * 头像上传
     * @param userid
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload")
    public String upload(MultipartFile file, HttpSession session,HttpServletRequest request, UploadUtil uploadUtil,
                         RedirectAttributes attributes){
    	CurrentUser user = (CurrentUser)session.getAttribute("currentUser");
        try{
            String fileurl = uploadUtil.upload(request, "upload", user.getNickname());
            user.setProfilehead(fileurl);
            userService.updateUser(user);
                attributes.addFlashAttribute("message", "["+user.getNickname()+"]头像更新成功!");
        } catch (Exception e){
        	attributes.addFlashAttribute("error", "["+user.getNickname()+"]头像更新失败!");
        }
        return "redirect:/config";
    }

    /**
     * 获取用户头像
     * @param userid
     */
    @RequestMapping(value = "/head")
    public void head(HttpSession session, HttpServletRequest request, HttpServletResponse response){
    	CurrentUser user = (CurrentUser)session.getAttribute("currentUser");
        try {
            String path = user.getProfilehead();
            if (!MatchUtil.isEmpty(path)) {
	            String rootPath = request.getSession().getServletContext().getRealPath("/");
	            String picturePath = rootPath + path;
	            response.setContentType("image/jpeg; charset=UTF-8");
	            ServletOutputStream outputStream = response.getOutputStream();
	            FileInputStream inputStream = new FileInputStream(picturePath);
	            byte[] buffer = new byte[1024];
	            int i = -1;
	            while ((i = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, i);
	            }
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();
	            outputStream = null;
            }
        } catch (FileNotFoundException e) {
           // e.printStackTrace();
        	System.out.println("路径不存在");
        } catch (IOException e) {
            //e.printStackTrace();
        	System.out.println("图片不存在");
        }
    }
    
    @RequestMapping(value = "/infomation")
    public String info(String id,HttpServletRequest request) {
    	CurrentUser findUserById = userService.findUserById(id);
    	request.setAttribute("currentUser",findUserById);
    	return "info-setting";
    }
}
