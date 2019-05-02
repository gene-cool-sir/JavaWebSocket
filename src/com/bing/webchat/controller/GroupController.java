package com.bing.webchat.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.Group;
import com.bing.webchat.service.GroupService;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.StringUtil;

/*
 * 群组的Controller，控制群组的创建，更新和删除
 * 
 * */

@Controller
public class GroupController {
	@Resource 
	private GroupService groupService;
	//@Resource 
	//private UserGroupRelationService userGroupRelationService;
	@Resource 
	private UserService userService;
	
	@RequestMapping(value="/createGroup",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createGroup(Group group,HttpSession session){
		// String groupName,String groupIntroduction,int groupCreaterId
		String groupId =StringUtil.getGuid();
		CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
		group.setCreateBy(user.getId());
		group.setDelFlag("0");
		Date date = new Date();
		group.setCreateTime(new Date());
		group.setGroupUserCount(1);
		group.setGroupMembers(user.getId() + ",");
		groupService.addGroup(group);
		Map<String,Object> resoult = new HashMap<String,Object>();
		resoult.put("group", group);
		return resoult;
	}
	
	@RequestMapping(value="/findGroupById",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> findGroupById(String id){
		Group group = groupService.getGroup(id);
		String JsonGroup = JSONArray.toJSONString(group,SerializerFeature.UseSingleQuotes);
		Map<String,Object> resoult = new HashMap<String,Object>();
		resoult.put("resoult", JsonGroup);
		return resoult;
	}
	
	@RequestMapping(value="/getGroupList",method=RequestMethod.POST)
	@ResponseBody
	public String getGroupList(String userId){
		JSONObject resoult = new JSONObject();
		try {
			List<Group> listGroup = groupService.getGroupList(userId);
			resoult.put("message", "success");
			resoult.put("listGroup", listGroup);
		} catch (Exception e) {
			e.printStackTrace();
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
	
	
	private static SerializeConfig mapping = new SerializeConfig();  
	private static String dateFormat;  
	static {  
	    dateFormat = "yyyy-MM-dd HH:mm:ss";  
	    mapping.put(Timestamp.class, new SimpleDateFormatSerializer(dateFormat));  
	}
}
