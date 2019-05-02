package com.bing.webchat.service;

import java.util.List;

import com.bing.webchat.entity.CurrentUser;

public interface UserService {
	/**
	    *   通过用户id查询用户信息
	 * @param userid
	 * @return
	 */
	CurrentUser findUserById(String userid);
	/**
	 * 修改用户信息
	 * @param user
	 */
	void updateUser(CurrentUser user);
	/**
	 * 查询用户信息
	 * @param currentUser
	 * @return
	 */
	CurrentUser findUser(CurrentUser currentUser);
	
	/***
	 * 添加注册用户
	 * @param currentUser
	 */
	void addUser(CurrentUser currentUser);
	List<CurrentUser> searchBykeyWord(String keyword, String string);

}
