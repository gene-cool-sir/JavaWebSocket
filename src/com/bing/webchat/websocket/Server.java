package com.bing.webchat.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bing.webchat.entity.CurrentUser;
import com.bing.webchat.entity.Group;
import com.bing.webchat.entity.Message;
import com.bing.webchat.service.GroupService;
import com.bing.webchat.service.MessageService;
import com.bing.webchat.service.UserRelationService;
import com.bing.webchat.service.UserService;
import com.bing.webchat.utils.MatchUtil;

/*
 * f服务器类
 */
@ServerEndpoint(value="/chatServer",configurator=HttpSessionConfigurator.class)
public class Server {
	private static final String Session = null;
	//静态变量，用于记录当前在线人数
	 private static int onlineCount = 0;
	 //存放每个客户端对应的Server对象，可以考虑使用Map来代替，key作为用户标识
	 private static CopyOnWriteArraySet<Server> server = new CopyOnWriteArraySet<Server>();
	 //表示与某个用户的连接会话，通过它给客户端发送数据
 	 @SuppressWarnings("unused")
	 private Session session;
	 //用户id
	 private String userId;
	 //request的session，用于获取用户信息
	 private HttpSession httpSession;
	 //在线列表
	 @SuppressWarnings("rawtypes")
	 private static List list = new ArrayList<>();
	 //用户名和websocket的session绑定的路由表
	 @SuppressWarnings("rawtypes")
	 private static Map routeTable = new HashMap<>();
	 
	 //这里无法注入service，只能通过getBean的方式来获取service
	 private MessageService messageService=(MessageService)ContextLoader.getCurrentWebApplicationContext().getBean("messageService");
	 private UserService userService=(UserService)ContextLoader.getCurrentWebApplicationContext().getBean("userService");
	 private GroupService groupService=(GroupService)ContextLoader.getCurrentWebApplicationContext().getBean("groupService");
	 private UserRelationService userRelationService=(UserRelationService)ContextLoader.getCurrentWebApplicationContext().getBean("userRelationService");
	 /**
	     * 连接建立成功调用的方法
	     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	     * @param config 获取HttpSession的参数
	     */
	    @SuppressWarnings("unchecked")
		@OnOpen
	    public void onOpen(Session session, EndpointConfig config){
	        this.session = session;
	        //加入server中
	        if (!server.contains(this)) {
	        	server.add(this);     	
	        }
	        //在线人数一
	        addOnlineCount();
	        //获取httpSession
	        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
	        //获取当前登录用户的id
	        CurrentUser user = (CurrentUser)httpSession.getAttribute("currentUser");
	        this.userId = user.getId();
	        //将用户id加入在线列表(这个主要是给控制台看的)
	        list.add(userId);
	        //将用户的在线状态置0
	        user.setIsOnline("0");
	        user.setUpdateTime(new Date());
	        userService.updateUser(user);
	        //将用户名和session绑定到路由表
	        routeTable.put(userId, session);
	        
	        // 通知消息,通知在线好友上线了
		    Message mess = new Message();
		    mess.setType("3");
		    mess.setContent("[" + user.getNickname() + "]已经上线");
		    mess.setCreateTime(new Date());
		    noticFriendIsOnline(getMessage(mess,null,null));
	    }
	    
	    /**
	            * 通知好友以上线/下线
	     */
	    public void noticFriendIsOnline(String message) {
	    	List<CurrentUser> friendList = userRelationService.getAllFriends(userId);
	    	if (!MatchUtil.isEmpty(friendList)) {
				for (CurrentUser user : friendList) {
					if ("0".equals(user.getIsOnline())) { // 在线
						Session session = (Session)routeTable.get(user.getId());
						if (MatchUtil.isEmpty(session)) {// 不在线(意外关闭连接)
							 //将用户的在线状态置1
					        user.setIsOnline("1");
					        user.setUpdateTime(new Date());
					        userService.updateUser(user);
					      //从server中删除当前对象
					        server.remove(this);
					        //在线人数减1
					        subOnlineCount();
					        //从在线列表移除这个用户
					        list.remove(userId);
						} else {
							singleSend(message,(Session)routeTable.get(user.getId())); //广播
						}
					}
				}
	    	}
	    }

	    /**
	     * 连接关闭调用的方法
	     */
	    @OnClose
	    public void onClose(){
	    	 //将用户的在线状态设为1
	        CurrentUser user = (CurrentUser)httpSession.getAttribute("currentUser");
	        Message mess = new Message();
	        mess.setType("3");
  	    	mess.setContent("[" + user.getNickname() +"]已经下线");
  	    	mess.setCreateTime(new Date());
  	    	String message = getMessage(mess,null,null);
  	    	// 通知好友已经下线
  	    	noticFriendIsOnline(message);
	    	//从server中删除当前对象
	        server.remove(this);
	        //在线人数减1
	        subOnlineCount();
	        //从在线列表移除这个用户
	        list.remove(userId);
	        user.setIsOnline("1");
	        userService.updateUser(user);
	        //从路由表中删除当前用户
	        routeTable.remove(userId);
	    }

	    /**
	     * 
	     * 接收客户端的jsonMessage,存储数据库并且判断当前聊天用户是否在线，
	     * 在线则服务器直接转发并且记录已经转发，不在线则记录未转发，
	     * 然后将这条数据存储至数据库
	     * 
	     */
	    @OnMessage
	    public void onMessage(String jsonMessage) throws IOException {
	    	System.out.println("我收到了客户端的消息:"+jsonMessage);
	    	 CurrentUser user = (CurrentUser)httpSession.getAttribute("currentUser");
		    Message message = new Message();
			//存储数据库
			JSONObject jsonObjectMessage = JSON.parseObject(jsonMessage);
			message.setFromId(jsonObjectMessage.getString("from"));
			message.setContent(jsonObjectMessage.getString("content"));
			message.setType(jsonObjectMessage.getString("type"));
			message.setCreateTime(new Date());
			//发送给自己,不过发之前先判断一下是普通消息还是通知性消息，通知性消息不必给自己发回去了，服务器直接转发
			if("0".equals(jsonObjectMessage.getString("type"))){ // 单聊聊
				String self = String.valueOf(jsonObjectMessage.get("from"));
				singleSend(getMessage(message,user.getNickname(),null),(Session) routeTable.get(self));
			}
			if("1".equals(jsonObjectMessage.getString("type"))){ // 单聊聊
				String toUsersId =  jsonObjectMessage.getString("to");
				CurrentUser toUser = null;
				// 查询用户
				if (!MatchUtil.isEmpty(toUsersId)) {
					toUser= userService.findUserById(toUsersId);
					String self = String.valueOf(jsonObjectMessage.get("from"));
					// 先发送给自己
					singleSend(getMessage(message,user.getNickname(),toUser.getNickname()),(Session) routeTable.get(self));
					// 判断好友是否在线,好友在线, 则再次发送给好友
					Session singleSession = (Session)routeTable.get(toUsersId);
					if(!MatchUtil.isEmpty(singleSession)) {
						singleSend(getMessage(message,user.getNickname(),toUser.getNickname()),singleSession);
					} else {
						message.setStatus("0");// 未读
						message.setToId(toUsersId); // 发给谁的
						message.setDelFlag("0");
						message.setCreateTime(new Date());
			        	messageService.addMessageToId(message);
					}
				}
			}
			if("2".equals(jsonObjectMessage.getString("type"))){// 发给某个群
				String groupId =  jsonObjectMessage.getString("to");
				String self = String.valueOf(jsonObjectMessage.get("from"));
				singleSend(getMessage(message,user.getNickname(),null),(Session) routeTable.get(self));
				// 存储群信息, 别的用户登录可以查看该信息 // 为每个成员添加一条是否已读状态信息(除了自己)
				// 在哪个群发的
				message.setGroupId(groupId);
				// 获取该群里的所有用户
				List<CurrentUser> allUser = groupService.getAllUser(groupId);
				if (!MatchUtil.isEmpty(allUser)) {
					// 保存该群发消息,给其他用户查看
					StringBuffer sb = new StringBuffer();
					for (CurrentUser cuu: allUser) {
						sb.append(cuu.getId()).append(",");
					}
					message.setToId(sb.toString());
					message.setCreateTime(new Date());
					message.setDelFlag("0");
					message.setStatus("0");
					messageService.addMessageToId(message);
				}
			}
			// 下线通知
			if ("3".equals(jsonObjectMessage.getString("type"))) {
				onClose();
			}
			// 点击某个人进行私聊发送信息之前,查看是否有未读的信息
			if ("4".equals(jsonObjectMessage.getString("type"))) {
				String toUsersId =  jsonObjectMessage.getString("to");
				singleChatMessage(user,toUsersId,message);
			}
			// 点击某个群的时候进行聊发送信息之前,查看是否有未读的信息
			if ("5".equals(jsonObjectMessage.getString("type"))) {
				String groupId =  jsonObjectMessage.getString("to");
				groupChatMessage(user,groupId,message);
			}
	}
	    
	    /**
         * 处理未读的单聊信息
  */
 public  void singleChatMessage(CurrentUser user,String toId,Message messages) {
 	 List<Message> messageList = new ArrayList<Message>();
     // 未读单聊的信息
     messageList =messageService.getMessageUnReceive(user.getId(),toId);
	 if(messageList!=null){
	    String fromnickName = userService.findUserById(toId).getNickname();
     	for(int i=0;i<messageList.size();i++){
	        	Message message = (Message)messageList.get(i);
	        	message.setType("1");
	        	String jsonMessage = getMessage(message,fromnickName,user.getNickname());
	        	singleSend(jsonMessage,(Session) routeTable.get(userId));
	        	// 更改已读
	        	//message.setStatus("1");
	        	//messageService.updateMessage(message);
	        	// 已经读取过了 , 删除数据库中数据,避免过多, (如果要保留所有信息, 需重新扩展)
	        	messageService.deleteMessage(message);
	        }
	    } else {
	    	//messages.setType("1");
	    	//singleSend(getMessage(messages,user.getNickname(),null),(Session) routeTable.get(user.getId()));
	    }
 }
 
 /**
  	* 群聊未读信息处理
  * @param user
  */
 public void groupChatMessage(CurrentUser user,String groupId,Message messages) {
 	List<Message> messageList = messageService.findGroupUnReceive(groupId);
 	if (!MatchUtil.isEmpty(messageList)) {
	 	for (Message message : messageList) {
		 	CurrentUser fromUser = userService.findUserById(message.getFromId());
		 	if (message.getToId().contains(user.getId())) {
		 		message.setType("2");
		 		String jsonMessage = getMessage(message,fromUser.getNickname(),null);
		 		singleSend(jsonMessage,(Session)routeTable.get(user.getId())); //广播
		 		// 更新信息
		 		String[] split = message.getToId().split(",");
		 		String toIds = "";
		 		for (String toId : split) {
		 			if (!toId.equals(user.getId())) {
		 				toIds += toId +",";
		 			}
		 		}
		 		if (MatchUtil.isEmpty(toIds)) {
		 			// 所有成员都看过了, 删除
		 			messageService.deleteMessage(message);
		 		} else {
		 			// 更新
		 			message.setToId(toIds);
		 			messageService.updateMessage(message);
		 		}
		 	}
	 	}
 	} else {
 		//messages.setType("5");
	   // singleSend(getMessage(messages,user.getNickname(),null),(Session) routeTable.get(user.getId()));
 	}
 }

	    /**
	     * 发生错误时调用
	     * @param error
	     */
	    @OnError
	    public void onError(Throwable error){
	        error.printStackTrace();
	    }

	    /**
	     * 广播消息
	     * @param message
	     */
	    public void broadcast(String message){
	    	System.out.println("I will broadcast this message"+message);
	    	for(Server chat: server){
	            try {
	                chat.session.getBasicRemote().sendText(message);
	            } catch (IOException e) {
	                e.printStackTrace();
	                continue;
	            }
	        }
	    }

	    /**
	     * 对特定用户发送消息
	     * @param message
	     * @param session
	     */
	    public void singleSend(String message, Session session){
	        try {
	            session.getBasicRemote().sendText(message);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 根据Message实体组装Json格式的数据返回给前台
	     */
		public String getMessage(Message message,String fromUserName, String toUserName){
			//使用JSONObject方法构建Json数据
	        JSONObject jsonObjectMessage = new JSONObject();
	        // 消息发送者
	        if(!MatchUtil.isEmpty(message.getFromId()))
	        	jsonObjectMessage.put("from", message.getFromId());
	        if(!MatchUtil.isEmpty(message.getFromId()))
	        	jsonObjectMessage.put("fromUserName", fromUserName);
	        // 消息接受者
	        if(!MatchUtil.isEmpty(message.getToId()))
	        	jsonObjectMessage.put("to", new String[] {message.getToId()});
	        // 消息内容
	        if(!MatchUtil.isEmpty(message.getContent()))
	        	jsonObjectMessage.put("content", message.getContent());
	        // 消息类型 1 单聊; 2 群聊; 3. 广播提示信息 
	        if(!MatchUtil.isEmpty(message.getType()))
	        	jsonObjectMessage.put("type", message.getType());
	        if(!MatchUtil.isEmpty(message.getCreateTime()))
	        	jsonObjectMessage.put("time", dateFormat(message.getCreateTime()));
	        if(!MatchUtil.isEmpty(toUserName))
	        	jsonObjectMessage.put("toUserName", toUserName);
	        return jsonObjectMessage.toString();
	    }

	    public int getOnlineCount() {
	        return onlineCount;
	    }

	    public void addOnlineCount() {
	        Server.onlineCount++;
	    }

	    public void subOnlineCount() {
	        Server.onlineCount--;
	    }
	    
	    /**
	             * 日期格式
	     */
	    public String dateFormat(Date date) {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String format = sdf.format(date);
	    	return format;
	    }

}

