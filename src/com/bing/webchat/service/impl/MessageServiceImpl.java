package com.bing.webchat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bing.webchat.dao.MessageDao;
import com.bing.webchat.entity.Message;
import com.bing.webchat.entity.MessageExample;
import com.bing.webchat.entity.MessageExample.Criteria;
import com.bing.webchat.service.MessageService;
import com.bing.webchat.utils.MatchUtil;
import com.bing.webchat.utils.StringUtil;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired 
	private MessageDao messageDao;
	
	@Override
	public List<Message> getMessageUnReceive(String userId,String toId) {
		MessageExample message = new MessageExample();
		Criteria criteria = message.createCriteria();
		// 用户id
		criteria.andToIdEqualTo(userId);
		// 发送者id
		criteria.andFromIdEqualTo(toId);
		// 未读状态
		criteria.andStatusEqualTo("0");
		// 有效消息
		criteria.andDelFlagEqualTo("0");
		message.setOrderByClause(" create_time desc ");
		return messageDao.selectByExampleWithBLOBs(message);
	}

	@Override
	public void updateMessage(Message message) {
		messageDao.updateByPrimaryKeySelective(message);
	}

	@Override
	public void addMessage(Message message, List<String> asList) {
		for (String toId :asList) {
			if (!MatchUtil.isEmpty(toId)) {
			message.setId(StringUtil.getGuid());
			message.setToId(toId);
			messageDao.insertSelective(message);
			}
		}
	}

	@Override
	public void addMessageToId(Message message) {
		try {
			message.setId(StringUtil.getGuid());
			messageDao.insertSelective(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMessage(Message message) {
		messageDao.deleteByPrimaryKey(message.getId());
	}

	@Override
	public List<Message> findGroupUnReceive(String groupId) {
		MessageExample message = new MessageExample();
		Criteria criteria = message.createCriteria();
		criteria.andGroupIdEqualTo(groupId);
		// 有效消息
		criteria.andDelFlagEqualTo("0");
		return messageDao.selectByExampleWithBLOBs(message);
	}
}
