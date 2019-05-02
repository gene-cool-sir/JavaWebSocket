package com.bing.webchat.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.Group;
import com.bing.webchat.service.GroupService;
import com.bing.webchat.service.UserService;


@Controller
public class UserGroupRelationController {
	@Autowired 
	private GroupService groupService;
	@Autowired 
	private UserService userService;
	
	@RequestMapping(value="/getUserGroups",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getUserGroups(String userId){
		List<Group> list = new ArrayList<Group>();
		list = groupService.getAllGroup(userId);
		String groups = JSONArray.toJSONString(list,SerializerFeature.UseSingleQuotes);
		Map<String,Object> resoult = new HashMap<String,Object>();
		resoult.put("groups",groups);
		return  resoult;
	}
	
	@RequestMapping(value="/getGroupUsers",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getGroupUsers(String groupId){
		List<CurrentUser> userList = new ArrayList<CurrentUser>();
		userList = groupService.getAllUser(groupId);
		String users = JSONArray.toJSONString(userList);
		String userGroups = JSONArray.toJSONString(userList,SerializerFeature.UseSingleQuotes);
		Map<String,Object> resoult = new HashMap<String,Object>();
		resoult.put("userGroups", userGroups);
		resoult.put("users",users);
		return  resoult;
	}
	
	@RequestMapping(value="/addGroupUsers",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addGroupUsers(String groupId,String userId){
	
		Group group = groupService.getGroup(groupId);
		group.setGroupMembers(group.getGroupMembers()+ userId + ",");
		group.setGroupUserCount(group.getGroupUserCount() + 1);
		groupService.updateGroup(group);
		Map<String,Object> resoult = new HashMap<String,Object>(); 
		resoult.put("resoult", "success"); return resoult;
	}
	
	private static SerializeConfig mapping = new SerializeConfig();  
	private static String dateFormat;  
	static {  
	    dateFormat = "yyyy-MM-dd HH:mm:ss";  
	    mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));  
	}
}
