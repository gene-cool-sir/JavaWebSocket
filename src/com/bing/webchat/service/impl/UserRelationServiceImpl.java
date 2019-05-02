package com.bing.webchat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bing.webchat.dao.CurrentUserDao;
import com.bing.webchat.dao.UserRelationDao;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.CurrentUserExample;
import com.bing.webchat.entity.UserRelation;
import com.bing.webchat.entity.UserRelationExample;
import com.bing.webchat.entity.UserRelationExample.Criteria;
import com.bing.webchat.service.UserRelationService;
import com.bing.webchat.utils.MatchUtil;

@Service("userRelationService")
public class UserRelationServiceImpl implements UserRelationService{

	@Autowired
	private UserRelationDao userRelationDao;
	@Autowired
	private CurrentUserDao currentUserDao;
	@Override
	public void addUserRelation(UserRelation userRelation) {
		userRelationDao.insertSelective(userRelation);		
	}
	@Override
	public void deleteUserRelation(String friendId, String currentuserId) {
		List<UserRelation> list = userRelationDao.selectByExample(null);
		for (UserRelation relation : list ) {
			String userFriendId = relation.getUserFriendId();
			String userId = relation.getUserId();
			if ((friendId.equals(userFriendId) && currentuserId.equals(userId))
					|| (friendId.equals(userId) && currentuserId.equals(userFriendId))) {
					userRelationDao.deleteByPrimaryKey(relation.getId());
					}
		}
	}
	@Override
	public List<CurrentUser> getAllFriends(String userId) {
		UserRelationExample example = new UserRelationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		// 0: 互为好友了, 1: 不是好友
		criteria.andDelFlagEqualTo("0");
		// 当前用户添加的好友
		List<UserRelation> list = userRelationDao.selectByExample(example);
		// 其他用户添加自己为好友
		UserRelationExample example2 = new UserRelationExample();
		Criteria criteria2 = example2.createCriteria();
		criteria2.andUserFriendIdEqualTo(userId);
		criteria2.andDelFlagEqualTo("0");
		List<UserRelation> list2 = userRelationDao.selectByExample(example2);
		List<String> friendIds = new ArrayList<String>();
		for (UserRelation relation : list ) {
			friendIds.add(relation.getUserFriendId());
		}
		for (UserRelation relation : list2 ) {
			if (!friendIds.contains(relation.getUserId()))
			friendIds.add(relation.getUserId());
		}
		if (!MatchUtil.isEmpty(friendIds)) {
			CurrentUserExample example1 = new CurrentUserExample();
			com.bing.webchat.entity.CurrentUserExample.Criteria createCriteria = example1.createCriteria();
			createCriteria.andIdIn(friendIds);
			example1.setOrderByClause(" is_online asc");
			return currentUserDao.selectByExample(example1);
		}
		return null;
	}
	@Override
	public List<UserRelation> getRequestFriend(String userId) {
		UserRelationExample example = new UserRelationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserFriendIdEqualTo(userId);
		// 0: 互为好友了, 1: 不是好友
		criteria.andDelFlagEqualTo("1");
		// 当前用户添加的好友
		List<UserRelation> selectByExample = userRelationDao.selectByExample(example);
		for (UserRelation relation :selectByExample) {
			CurrentUser user = currentUserDao.selectByPrimaryKey(relation.getUserId());
			relation.setCreateBy(user.getNickname());
		}
		return selectByExample;
	}
	@Override
	public void updateUserRelation(String id) {
		UserRelation example = new UserRelation();
		example.setId(id);
		// 0: 互为好友了, 1: 不是好友
		example.setDelFlag("0");
		// 当前用户添加的好友
		userRelationDao.updateByPrimaryKeySelective(example);
		
	}
	@Override
	public void deleteUserRelation(String relationId) {
		userRelationDao.deleteByPrimaryKey(relationId);
	}

}
