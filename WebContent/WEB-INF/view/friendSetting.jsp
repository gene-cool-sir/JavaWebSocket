<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>哎 聊 | 个人信息</title>
    <jsp:include page="include/commonfile.jsp"/>
</head>
<body>
<jsp:include page="include/header.jsp"/>
<div class="am-cf admin-main">
    <jsp:include page="include/sidebar.jsp"/>
	<input type="hidden" value="${currentUser.id}" id="currentIdFF" />
    <!-- content start -->
    <div class="admin-content">
        <div class="am-cf am-padding">
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">好友设置</strong> / <small>info</small></div>
        </div>
         <div data-am-widget="list_news" class="am-list-news am-list-news-default" >
		<button type="button" class="am-btn am-btn-success" id="searchFriend">搜索好友[搜索出匹配好友]</button>
     	<button type="button" class="am-btn am-btn-success" onclick="addFriend()">添加好友[先搜索]</button>
		<button type="button" class="am-btn am-btn-warning" onclick="deleteFrinedList()">删除好友[选择的好友]</button>
		<button type="button" class="am-btn am-btn-warning" onclick="currentFriendList()">刷新[查看当前好友]</button>
		<button type="button" class="am-btn am-btn-warning" onclick="lookFriendList()">好友请求[查看]</button>
		<hr>
  	<!--列表标题-->
    <div class="am-list-news-hd am-cf">
        <a href="##" class="">
          <h2>好友列表</h2>
        </a>
    </div>

 	 <div class="am-list-news-bd">
  	 	 <ul class="am-list confirm-list" id="friendAllList">
	
  		</ul>
 	 </div>
<div class="am-modal am-modal-confirm" tabindex="-1" id="deletefriend-confirm">
  <div class="am-modal-dialog">
    <div class="am-modal-hd friendName">删除好友</div>
    <div class="am-modal-bd">
    	  你，确定要删除该好友吗？
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" data-am-modal-confirm>确定</span>
    </div>
  </div>
</div>

<div class="am-modal am-modal-prompt" tabindex="-1" id="searchFriend-prompt">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">搜索好友</div>
    <div class="am-modal-bd">
      <input type="text" class="am-modal-prompt-input" id="friendInfo">
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" data-am-modal-confirm>提交</span>
    </div>
  </div>
</div>

<div class="am-modal am-modal-confirm" tabindex="-1" id="requestfriend-confirm">
  <div class="am-modal-dialog">
    <div class="am-modal-hd friendName">请求好友</div>
    <div class="am-modal-bd">
    	  你，确定同意该好友吗？点击确定,同意该请求, 点击删除,不同意!!!!!!!
    </div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" data-am-modal-cancel>删除</span>
      <span class="am-modal-btn" data-am-modal-confirm>确定</span>
    </div>
  </div>
</div>
    </div>
</div>
<a href="#" class="am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}">
    <span class="am-icon-btn am-icon-th-list"></span>
</a>
<jsp:include page="include/footer.jsp"/>

<script type="text/javascript">
$(document).ready(function(){
	currentFriendList();
	
	$('#searchFriend').on('click', function() {
	    $('#searchFriend-prompt').modal({
	      relatedTarget: this,
	      onConfirm: function(e) {
	    	  var keyword = $("#friendInfo").val();
	    	  if (keyword != "") {
	        	searchFriendInfo(keyword);
	    	  }
	      },
	      onCancel: function(e) {
	        //alert('不想说!');
	      }
	    });
	  });
});

function addFriend() {
	var obj= $(".isOrNotCheck");
	var count = 0;
	var flag = true;
	var currentUse = "";
	for (var i=0; i<$(obj).length; i++) {
		var curobj = $($(obj)[i]);
		if ($(curobj).prop("checked")) {
			currentUse =$(curobj).val();
			count +=1;
			var addType = $(curobj).parent().parent().parent().find(".addType").attr("addType");
			if (addType == "0") {
				layer.msg('已经添加过好友,请重新选择为添加的用户', {
					icon : 2,
					zIndex : 20000001,
					time : 2000
				});
				return;
			}
			if (addType == "1") {
				layer.msg('正在好友请求中,请求已经发送', {
					icon : 2,
					zIndex : 20000001,
					time : 2000
				});
				return;
			}
			if (addType == "3") {
				layer.msg('有人给你发送了好友请求,请在好友请求中查看', {
					icon : 2,
					zIndex : 20000001,
					time : 2000
				});
				return;
			}
			flag = false;
		}
	}
	if (count >1) {
		layer.msg('一次添加一个用户', {
			icon : 2,
			zIndex : 20000001,
			time : 2000
		});
		return;
	}
	if (flag) {
		layer.msg('请勾选需要添加的用户', {
			icon : 2,
			zIndex : 20000001,
			time : 2000
		});
		return;
	}
	// 添加用户
	$.ajax({
		async : false, //设置同步
		type : 'POST',
		url : path+'/addFriend',
		data : {"userId":currentUse},
		dataType : 'json',
		success : function(resoult) {
			if (resoult.resoult == "success") {
				layer.msg('添加好友已经发送,等待对方同意', {
					icon : 2,
					zIndex : 20000001,
					time : 2000
				});
				// 刷新好友列表
				currentFriendList();
			}else {
				layer.msg('添加好友请求失败', {
					icon : 2,
					zIndex : 20000001,
					time : 2000
				});
			}
		}
	});
}

 
function searchFriendInfo(keyword) {
	$.ajax({
		async : false, //设置同步
		type : 'POST',
		url : path+'/searchFriendInfo',
		data : {"keyword":keyword},
		dataType : 'json',
		success : function(resoult) {
			$("#friendAllList").empty();
			if (resoult.message == "success") {
				var listFriend =  resoult.searchUserList;
				if (listFriend != undefined && listFriend != "") {
					var appendStr = "";
					for (var i=0; i < listFriend.length; i++) {
						appendStr += "<li class=\"am-g am-list-item-dated\">"
						appendStr += "<div class=\"am-checkbox am-g-8\">"
						appendStr += "<label>"
						appendStr += "<input class=\"isOrNotCheck\" type=\"checkbox\" value='"+listFriend[i].id+"' >"
						appendStr += " <a href=\""+path+"/infomation?id="+listFriend[i].id+"\" class=\"am-list-item-hd \">"+listFriend[i].nickname+"</a>"
						appendStr += "</label></div>"
						appendStr +="<div style=\"float:right\" class=\"am-u-sm-4\">"
						if (listFriend[i].status =='0'){
							appendStr += "<span addType=\"0\" class=\" addType am-text-success\">[是 好 友]</span"
						} else if  (listFriend[i].status =='1'){
							appendStr += "<span addType=\"1\" class=\"addType am-text-warning\">[正在好友请求中]</span"
						} else if  (listFriend[i].status =='2'){
							appendStr += "<span addType=\"2\" class=\"addType am-text-warning\">[不是好友关系,请选择添加]</span"
						}else if  (listFriend[i].status =='3'){
							appendStr += "<span addType=\"3\" class=\"addType am-text-warning\">[有人给你发送了好友请求,请在好友请求中查看]</span"
						}
						appendStr += "</div>";
						appendStr += "</li>"
					}
					$("#friendAllList").append(appendStr);
				} else {
						$("#friendAllList").append("没有匹配的用户信息...........")
				}
			}
		}
	})
}
  
  /**
  当前好友
  */
		function currentFriendList() {
			var userId = $("#currentIdFF").val();
			$.ajax({
				async : false, //设置同步
				type : 'POST',
				url : path+'/getAllFriendList',
				data : {"userId":userId},
				dataType : 'json',
				success : function(resoult) {
					$("#friendAllList").empty();
					if (resoult.message == "success") {
						var listFriend =  resoult.friendList;
						if (listFriend != undefined && listFriend != "") {
							var appendStr = "";
							for (var i=0; i < listFriend.length; i++) {
								appendStr += "<li class=\"am-g am-list-item-dated\">"
								appendStr += "<span class=\"am-checkbox am-u-sm-8\">"
								appendStr += "<label>"
								appendStr += "<input class=\"isOrNotCheck\" type=\"checkbox\" value='"+listFriend[i].id+"' >"
								appendStr += " <a href=\""+path+"/infomation?id="+listFriend[i].id+"\" class=\"am-list-item-hd \">"+listFriend[i].nickname+"</a>"
								appendStr += "</label></span>"
								appendStr +="<div style=\"float:right\" class=\"am-u-sm-4\">"
								//if (listFriend[i].delFlag =='0'){
									appendStr += "<span addType=\"0\" class=\"am-text-success\">[是 好 友]</span"
								//} else {
								//	appendStr += "<span addType=\"1\" class=\"am-text-warning\">[未 添 加]</span"
								//}
								appendStr +="</div>"
								appendStr += "<i alt=\"点击删除好友\"class=\"am-icon-close\" currentId='"+listFriend[i].id+"'></i>";
								appendStr += "</li>"
							}
							$("#friendAllList").append(appendStr);
							$('#friendAllList').find('.am-icon-close').on('click', function() {
								var curId = $(this).attr("currentId");
						      $('#deletefriend-confirm').modal({
						        relatedTarget: this,
						        onConfirm: function(options) {
						          deleteFriend(curId);
						        },
						        // closeOnConfirm: false,
						        onCancel: function() {
						          // 取消
						        }
						      });
						    });
						} 
					} else {
						$("#friendAllList").append("没有好友信息...........")
					}
				}
			});
		}
		
		function deleteFriend(friendId) {
			$.ajax({
				async : false, //设置同步
				type : 'POST',
				url : path+'/deleteFriend',
				data : {"friendId":friendId},
				dataType : 'json',
				success : function(resoult) {
					if (resoult.message == "success") {
						layer.msg('删除好友列表成功', {
							icon : 2,
							zIndex : 20000001,
							time : 2000
						});
						// 刷新好友列表
						currentFriendList();
					}else {
						layer.msg('删除好友列表失败', {
							icon : 2,
							zIndex : 20000001,
							time : 2000
						});
					}
				}
			})
		}
		
		function lookFriendList() {
			var userId = $("#currentIdFF").val();
			$.ajax({
				async : false, //设置同步
				type : 'POST',
				url : path+'/lookFriendListrequest',
				data : {"userId":userId},
				dataType : 'json',
				success : function(resoult) {
					$("#friendAllList").empty();
					if (resoult.message == "success") {
						var listFriend =  resoult.userRelationList;
						if (listFriend != undefined && listFriend != "") {
							var appendStr = "";
							for (var i=0; i < listFriend.length; i++) {
								appendStr += "<li class=\"am-g am-list-item-dated\">"
								appendStr += "<div class=\"am-checkbox am-g-8 \">"
								appendStr += "<label>"
								appendStr += "<input class=\"isOrNotCheck\" type=\"checkbox\" value='"+listFriend[i].id+"' >"
								appendStr += " <a href=\"javascript:void(0)\" class=\"am-list-item-hd \">"+listFriend[i].createBy+"发起请求</a>"
								appendStr += "</label></div>"
								appendStr +="<div style=\"float:right\" class=\"am-u-sm-4\">"
								appendStr += "<i class=\"am-text-success\" currentId='"+listFriend[i].id+"' onclick=\"isOrnotRecode('"+listFriend[i].id+"')\">点击同意</i>";
								appendStr += "</div>";
								appendStr += "</li>"
							}
							$("#friendAllList").append(appendStr);
						} else {
							$("#friendAllList").append("没有好友请求信息...........")
						}
					}
				}
			})
		}
	function isOrnotRecode(relationId) {
		$('#requestfriend-confirm').modal({
	        relatedTarget: this,
	        onConfirm: function(options) {
	        	$.ajax({
					async : false, //设置同步
					type : 'POST',
					url : path+'/isOrnotRecode',
					data : {"relationId":relationId},
					dataType : 'json',
					success : function(resoult) {
						layer.msg('同意好友成功', {
							icon : 2,
							zIndex : 20000001,
							time : 2000
						});
						// 刷新好友列表
						currentFriendList();
			        }
	        	});
			}
	         ,
	        // closeOnConfirm: false,
	        onCancel: function() {
	         // alert('不同意, 暂时未开发,给出提示');
	        	$.ajax({
					async : false, //设置同步
					type : 'POST',
					url : path+'/deleteRelation',
					data : {"relationId":relationId},
					dataType : 'json',
					success : function(resoult) {
						layer.msg('不同意', {
							icon : 2,
							zIndex : 20000001,
							time : 2000
						});
						// 刷新好友列表
						currentFriendList();
			        }
	        	});
	        }
	      });
	}
</script>
</body>
</html>
