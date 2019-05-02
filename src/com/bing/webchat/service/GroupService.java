package com.bing.webchat.service;

import java.util.List;

import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.Group;

public interface GroupService {

	void addGroup(Group group);

	Group getGroup(String id);

	List<Group> getAllGroup(String userId);

	List<CurrentUser> getAllUser(String groupId);

	void updateGroup(Group group);

	List<Group> getGroupList(String currentId);

}
