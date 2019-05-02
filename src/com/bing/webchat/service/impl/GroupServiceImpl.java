package com.bing.webchat.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bing.webchat.dao.CurrentUserDao;
import com.bing.webchat.dao.GroupDao;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.Group;
import com.bing.webchat.entity.GroupExample;
import com.bing.webchat.entity.GroupExample.Criteria;
import com.bing.webchat.service.GroupService;
import com.bing.webchat.utils.MatchUtil;

@Service("groupService")
public class GroupServiceImpl implements GroupService{
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private CurrentUserDao currentUserDao;
	
	@Override
	public void addGroup(Group group) {
		groupDao.insertSelective(group);
	}

	@Override
	public Group getGroup(String id) {
		GroupExample example = new GroupExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		 List<Group> list = groupDao.selectByExampleWithBLOBs(example);
		 if (!MatchUtil.isEmpty(list)) {
			 return list.get(0);
		 }else {
			 return null;
		 }
	}

	@Override
	public List<Group> getAllGroup(String userId) {
		GroupExample example = new GroupExample();
		Criteria criteria = example.createCriteria();
		criteria.andCreateByEqualTo(userId);
		return groupDao.selectByExampleWithBLOBs(example);
	}

	@Override
	public List<CurrentUser> getAllUser(String groupId) {
		GroupExample example = new GroupExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(groupId);
		List<Group> list = groupDao.selectByExampleWithBLOBs(example);
		if (!MatchUtil.isEmpty(list)) {
			Group group = list.get(0);
			String groupMembers = group.getGroupMembers();
			// 获取组成员iD
			String[] split = groupMembers.split(",");
			List<CurrentUser> userList = new ArrayList<CurrentUser>();
			for (String id : split) {
				userList.add(currentUserDao.selectByPrimaryKey(id));
			}
			return userList;
		}
		return null;
	}

	@Override
	public void updateGroup(Group group) {
		
		groupDao.updateByExampleSelective(group, null);
		
	}

	@Override
	public List<Group> getGroupList(String currentId) {
		GroupExample example = new GroupExample();
		Criteria criteria = example.createCriteria();
		criteria.andDelFlagEqualTo("0");
		List<Group> list = groupDao.selectByExampleWithBLOBs(example);
		List<Group> listGroup = new ArrayList<Group>();
		for (Group group : list) {
			String groupMembers = group.getGroupMembers();
			if (!MatchUtil.isEmpty(groupMembers) && groupMembers.contains(currentId)) {
				listGroup.add(group);
			}
		}
		return listGroup;
	}

}
