

function initPersion() {
	// 初始化当前用户的在线好友列表
	var currentUserId = $("#currentUserId").val();
	getOnlineFriendList(currentUserId);
	getGroupList(currentUserId);
	getAllFriendList(currentUserId);
}

/**
 * 
 * 
 * @param userId 当前用户
 * @returns 当前用户的所有在线好友
 */
function getOnlineFriendList(userId) {
	$.ajax({
		async : false, //设置同步
		type : 'POST',
		url : path+'/getOnlineFriendList',
		data : {"userId": userId},
		dataType : 'json',
		success : function(resoult) {
			$("#collapse-navonline").empty();
			if (resoult.message == "success") {
				var listFriend =  resoult.friendList;
				if (listFriend != undefined && listFriend != '') {
					var appendStr = "";
					for (var i=0; i < listFriend.length; i++) {
						appendStr += "<li ><a href=\"javascript:void(0)\" onclick=\"addChat('"+listFriend[i].id+"',this)\" >"+listFriend[i].nickname+" <button type=\"button\" class=\"am-btn am-btn-xs am-btn-primary am-round\"><span class=\"am-icon-phone\"></span><span class='chattypeinfo' chattypeinfo='"+listFriend[i].id+"'>点击私聊</span></button></a></li>"
					}
					$("#collapse-navonline").append(appendStr);
					$("#onlinenum").html(listFriend.length);
				}
			} else {
				layer.msg('获取好友列表失败', {
				icon : 2,
				zIndex : 20000001,
				time : 2000
			});
			}
		}
	});
}

/**
 * 获取所有好友列表
 * 在线: 绿色: 不在线: 灰色
 * @param userId
 * @returns
 */
function getAllFriendList(userId) {
	$("#collapse-navallFriend").empty();
	$.ajax({
		async : false, //设置同步
		type : 'POST',
		url : path+'/getAllFriendList',
		data : {"userId":userId},
		dataType : 'json',
		success : function(resoult) {
			if (resoult.message == "success") {
				var listFriend =  resoult.friendList;
				if (listFriend != undefined && listFriend != '') {
					var appendStr = "";
					for (var i=0; i < listFriend.length; i++) {
						if (listFriend[i].isOnline == "0") {// 在线, 颜色加绿色
							appendStr += "<li><a onclick=\"addChat('"+listFriend[i].id+"',this)\"  >"+listFriend[i].nickname+" <button type=\"button\" class=\"am-btn am-btn-xs am-btn-primary am-round\"><span class=\"am-icon-phone\"></span><span class='chattypeinfo' chattypeinfo='"+listFriend[i].id+"'>点击私聊</span></button></a></li>"
						} else { // 不在线,灰色
							appendStr += "<li  class=\"am-link-muted\"><a onclick=\"addChat('"+listFriend[i].id+"',this)\" >"+listFriend[i].nickname+" <button type=\"button\" class=\"am-btn am-btn-xs am-btn-primary am-round\"><span class=\"am-icon-phone\"></span><span class='chattypeinfo' chattypeinfo='"+listFriend[i].id+"'>点击私聊</span></button></a></li>"
						}
					}
					$("#collapse-navallFriend").append(appendStr);
					$("#allFriendNum").html(listFriend.length);
				}
			} else {
				layer.msg('获取好友列表失败', {
				icon : 2,
				zIndex : 20000001,
				time : 2000
			});
			}
		}
	});
}

/**
 * 获取当前用户所在群列表
 * @param userId
 * @returns
 */
function getGroupList(userId) {
	$("#collapse-navgroup").empty();
	$.ajax({
		async : false, //设置同步
		type : 'POST',
		url : path+'/getGroupList',
		data : {"userId":userId},
		dataType : 'json',
		success : function(resoult) {
			if (resoult.message == "success") {
				var listGroup =  resoult.listGroup;
				if (listGroup != undefined && listGroup != '') {
					var appendStr = "";
					for (var i=0; i < listGroup.length; i++) {
						appendStr += "<li ><a onclick=\"LookGroupMembersChat('"+listGroup[i].id+"',this)\" class=\"am-u-sm-12\" >"+listGroup[i].groupName+" [成员数 "+listGroup[i].groupUserCount +"]<button type=\"button\" class=\"am-u-sm-12 am-btn am-btn-xs am-btn-primary am-round\"><span class=\"am-icon-phone\"></span><span class='chattypeinfomany' chattypeinfomany='"+listGroup[i].id+"'>点击群聊</span></button></a></li>"
					}
					$("#collapse-navgroup").append(appendStr);
					$("#groupNum").html(listGroup.length);
				}
			} else {
				layer.msg('获取群列表失败', {
				icon : 2,
				zIndex : 20000001,
				time : 2000
			});
			}
		}
	});
}
/**
 * 添加私聊
 */
function addChat(toId,obj){
	$("#toIds").val(toId);
	var text = $(obj).find(".chattypeinfo").text();
	text = text.replace(/^\s+|\s+$/g,"");
	if ( text != "正在私聊") {
		 $("#chat").html("");
	}
	eachText($(".chattypeinfo"),"点击私聊");
	eachText($(".chattypeinfomany"),"点击群聊");
	$("#tuling").text("未上线").removeClass("am-btn-success").addClass("am-btn-danger");
	$(obj).find(".chattypeinfo").text("正在私聊");
	$("#chatType").val("4");
	$("#message").val("初始化..");
	// 初始化未读消息
	sendMessage();
	$("#chatType").val("1");
	$("#message").val("");
	
}

function LookGroupMembersChat(groupId,obj) {
	$("#toIds").val(groupId);
	var text = $(obj).find(".chattypeinfomany").text();
	text = text.replace(/^\s+|\s+$/g,"");
	if (text != "正在群聊") {
		 $("#chat").html("");
	}
	eachText($(".chattypeinfo"),"点击私聊");
	eachText($(".chattypeinfomany"),"点击群聊");
	$("#tuling").text("未上线").removeClass("am-btn-success").addClass("am-btn-danger");
	$(obj).find(".chattypeinfomany").text("正在群聊");
	$("#chatType").val("5");
	$("#message").val("初始化..");
	// 初始化未读消息
	sendMessage();
	$("#chatType").val("2");
	$("#message").val("");
}
// 谁知所有未某个状态: 点击私聊/点击群聊
function eachText(obj,message) {
	for(var i=0; i<$(obj).length; i++) {
		$($(obj)[i]).text(message);
	}
}
// 是否清空面板
function eachText2(obj,message) {
	for(var i=0; i<$(obj).length; i++) {
		if ($($(obj)[i]).text() ==message ){
			 $("#chat").html("");
			 break;
		}
	}
}

function changeText(value) {
	if (value == "1") {
		$("#sendto .butext").text("私聊");
		 layer.msg('请选择右侧私聊的好友', {
	            offset: 0
	      });
	} else {
		$("#sendto .butext").text("群聊");
		layer.msg('请选择右侧群', {
            offset: 0
      });
	}
	$("#sendto").dropdown('close');
}