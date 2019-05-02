package com.bing.webchat.service;

import java.util.List;

import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.UserRelation;

public interface UserRelationService {

	void addUserRelation(UserRelation userRelation);

	void deleteUserRelation(String friendId, String userId);

	List<CurrentUser> getAllFriends(String userId);

	List<UserRelation> getRequestFriend(String userId);

	void updateUserRelation(String userId);

	void deleteUserRelation(String relationId);

}
