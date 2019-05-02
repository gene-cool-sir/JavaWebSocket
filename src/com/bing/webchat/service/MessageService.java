package com.bing.webchat.service;

import java.util.List;

import com.bing.webchat.entity.Message;

public interface MessageService {

	List<Message> getMessageUnReceive(String userId,String toId);

	void updateMessage(Message message);

	void addMessage(Message message,List<String> asList );

	void addMessageToId(Message message);

	void deleteMessage(Message message);

	List<Message> findGroupUnReceive(String groupId);

}
