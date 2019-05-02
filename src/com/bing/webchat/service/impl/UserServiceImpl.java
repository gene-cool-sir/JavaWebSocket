package com.bing.webchat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bing.webchat.dao.CurrentUserDao;
import com.bing.webchat.dao.UserRelationDao;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.CurrentUserExample;
import com.bing.webchat.entity.CurrentUserExample.Criteria;
import com.bing.webchat.entity.UserRelation;
import com.bing.webchat.entity.UserRelationExample;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.MatchUtil;
import com.bing.webchat.utils.StringUtil;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private CurrentUserDao currentUserDao;
	@Autowired
	private UserRelationDao userRelationDao;

	@Override
	public CurrentUser findUserById(String userid) {
		
		return currentUserDao.selectByPrimaryKey(userid);
	}

	@Override
	public void updateUser(CurrentUser user) {
		currentUserDao.updateByPrimaryKeySelective(user);
		
	}

	@Override
	public CurrentUser findUser(CurrentUser currentUser) {
		CurrentUserExample criteriaExample = new CurrentUserExample();
		Criteria criteria = criteriaExample.createCriteria();
		if (!MatchUtil.isEmpty(currentUser)) {
			if (!MatchUtil.isEmpty(currentUser.getUsername())) {
				criteria.andUsernameEqualTo(currentUser.getUsername());
			}
			if (!MatchUtil.isEmpty(currentUser.getPassword())) {
				criteria.andPasswordEqualTo(currentUser.getPassword());
			}
			List<CurrentUser> selectByExample = currentUserDao.selectByExample(criteriaExample);
			if ( selectByExample != null && !selectByExample.isEmpty()) {
				return selectByExample.get(0);
			} else {
				return null;
			}
		}
		return null;
		
	}

	@Override
	public void addUser(CurrentUser currentUser) {
		currentUser.setId(StringUtil.getGuid());
		currentUserDao.insert(currentUser);
	}

	@Override
	public List<CurrentUser> searchBykeyWord(String keyword,String userId) {
		
		UserRelationExample example = new UserRelationExample();
		com.bing.webchat.entity.UserRelationExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		// 0: 互为好友了, 1: 不是好友
		//criteria.andDelFlagEqualTo("0");
		// 当前用户添加的好友
		List<UserRelation> list = userRelationDao.selectByExample(example);
		// 其他用户添加自己为好友
		UserRelationExample example2 = new UserRelationExample();
		com.bing.webchat.entity.UserRelationExample.Criteria criteria2 = example2.createCriteria();
		criteria2.andUserFriendIdEqualTo(userId);
		//criteria2.andDelFlagEqualTo("0");
		List<UserRelation> list2 = userRelationDao.selectByExample(example2);
		List<String> friendIds = new ArrayList<String>();
		for (UserRelation relation : list ) {
			list2.add(relation);
			//friendIds.add(relation.getUserFriendId());
		}
		  CurrentUserExample criteriaExample = new CurrentUserExample(); 
		  Criteria criteria3 = criteriaExample.createCriteria(); 
		  criteria3.andDelFlagEqualTo("0");
		  criteria3.andNicknameLike("%" + keyword + "%"); 
		  List<CurrentUser> list3 = currentUserDao.selectByExample(criteriaExample);
		  for (CurrentUser usr : list3) {
			  if (!MatchUtil.isEmpty(list2)) {
				  boolean flag = true;
				  for (UserRelation relation : list2 ) {
					  if (usr.getId().equals(relation.getUserFriendId())) {
						  if ("0".equals(relation.getDelFlag())) {
							  usr.setStatus("0"); // 已经和当前用户是好友关系
						  } else if ("1".equals(relation.getDelFlag())) {
							  usr.setStatus("1"); // 发起好友请求关系
						  }
						  flag = false;
					  }
					  if (usr.getId().equals(relation.getUserId())) { // 别人添加自己
						  if ("0".equals(relation.getDelFlag())) {
							  usr.setStatus("0"); // 已经和当前用户是好友关系
						  } else if ("1".equals(relation.getDelFlag())) {
							  usr.setStatus("3"); // 请在好友请求中查看,是否同意请求
						  }
						  flag = false;
					  }
				  }
				  if (flag ) {
					  usr.setStatus("2"); // 已经和当前用户不是好友关系
				  }
			  }
		  }
		  return list3;
		 
	}
	
	

}
