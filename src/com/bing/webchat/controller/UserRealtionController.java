package com.bing.webchat.controller;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONObject;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.UserRelation;
import com.bing.webchat.service.UserRelationService;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.MatchUtil;
import com.bing.webchat.utils.StringUtil;

@Controller
public class UserRealtionController {
	
	@Resource 
	private UserService userService;
	@Resource 
	private UserRelationService userRelationService;
	
	@RequestMapping(value="/addFriend",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addFriend(String userId,HttpSession session){
		CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
		UserRelation userRelation = new UserRelation();
		userRelation.setId(StringUtil.getGuid());
		userRelation.setUserId(user.getId());
		userRelation.setUserFriendId(userId);
		userRelation.setDelFlag("1");// 发送好友请求,此时为1,等待对方同意后为0 
		userRelation.setCreateTime(new Date());
		userRelation.setCreateBy(userRelation.getUserId());
		userRelationService.addUserRelation(userRelation);
		Map<String,Object> resoult = new HashMap<String,Object>();
		resoult.put("resoult", "success");
		return resoult;
	}
	
	/**
	 * 删除好友
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/deleteFriend",method=RequestMethod.POST)
	@ResponseBody
	public String removeRelation(String friendId,HttpSession session){
		JSONObject resoult = new JSONObject();
		try {
			CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
			userRelationService.deleteUserRelation(friendId,user.getId());
			resoult.put("message", "success");
		} catch (Exception e) {
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
	/**
	 * 模糊搜索用户信息
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/searchFriendInfo",method=RequestMethod.POST)
	@ResponseBody
	public String searchFriendInfo(String keyword,HttpSession session){
		JSONObject resoult = new JSONObject();
		try {
			CurrentUser user = (CurrentUser) session.getAttribute("currentUser");
			List<CurrentUser> searchUserList = userService.searchBykeyWord(keyword,user.getId());
			resoult.put("searchUserList", searchUserList);
			resoult.put("message", "success");
		} catch (Exception e) {
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
	/**
	 * 获取用户好友请求
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/lookFriendListrequest",method=RequestMethod.POST)
	@ResponseBody
	public String lookFriendListrequest(String userId){
		JSONObject resoult = new JSONObject();
		try {
			List<UserRelation> userRelationList = userRelationService.getRequestFriend(userId);
			resoult.put("userRelationList", userRelationList);
			resoult.put("message", "success");
		} catch (Exception e) {
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
	/**
	 * 同意用户好友请求
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/isOrnotRecode",method=RequestMethod.POST)
	public void isOrnotRecode(String relationId){
		try {
			userRelationService.updateUserRelation(relationId);
		} catch (Exception e) {
		}
	}
	/**
	 * 同意用户好友请求
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/deleteRelation",method=RequestMethod.POST)
	public void deleteRelation(String relationId){
		try {
			userRelationService.deleteUserRelation(relationId);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 获取在线好友
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/getOnlineFriendList",method=RequestMethod.POST)
	@ResponseBody
	public String getOnlineFriendList(String userId){
		JSONObject resoult = new JSONObject();
		try {
			List<CurrentUser> friendList = userRelationService.getAllFriends(userId);
			List<CurrentUser> currentOnlineList = new ArrayList<CurrentUser>();
			if(!MatchUtil.isEmpty(friendList)) {
				for (CurrentUser user : friendList) {
					if ("0".equals(user.getIsOnline())) { // 在线
						currentOnlineList.add(user);
					}
				}
			}
			resoult.put("message", "success");
			resoult.put("friendList", currentOnlineList);
		}catch (Exception e) {
			e.printStackTrace();
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
	
	/**
	 * 获取所有好友
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/getAllFriendList",method=RequestMethod.POST)
	@ResponseBody
	public String getAllFriendList(String userId){
		JSONObject resoult = new JSONObject();
		try {
			List<CurrentUser> friendList = userRelationService.getAllFriends(userId);
			resoult.put("message", "success");
			resoult.put("friendList", friendList);
		}catch (Exception e) {
			e.printStackTrace();
			resoult.put("message", "fail");
		}
		return resoult.toString();
	}
}
