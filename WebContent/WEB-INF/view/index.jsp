<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>哎 聊 | 聊天</title>
    <jsp:include page="include/commonfile.jsp"/>
    <script src="${ctx}/static/plugins/sockjs/sockjs.js"></script>
</head>
<body>
<jsp:include page="include/header.jsp"/>
<div class="am-cf admin-main">
    <jsp:include page="include/sidebar.jsp"/>

    <!-- content start -->
    <div class="admin-content">
   		<!-- 存放发送人的Id集合 -->
        <input type ="hidden" id ="toIds" name="toIds" />
        <input type ="hidden" id ="chatType" name="chatType" />
        <input type ="hidden" id ="currentUserId" name="currentUserId" value="${currentUser.id }" />
        <input type ="hidden" id ="currentUserName" name="currentUserName" value="${currentUser.nickname }" />
        <div class="" style="width: 80%;float:left;">
            <!-- 聊天区 -->
            <div class="am-scrollable-vertical" id="chat-view" style="height: 510px;">
                <ul class="am-comments-list am-comments-list-flip" id="chat">
                </ul>
            </div>
            <!-- 按钮区 -->
            <div class="am-btn-group am-btn-group-xs" style="float:right;">
                <button class="am-btn am-btn-default" type="button" onclick="getConnection()"><span class="am-icon-plug"></span> 连接</button>
                <button class="am-btn am-btn-default" type="button" onclick="closeConnection()"><span class="am-icon-remove"></span> 断开</button>
                <button class="am-btn am-btn-default" type="button" onclick="checkConnection()"><span class="am-icon-bug"></span> 检查</button>
                <button class="am-btn am-btn-default" type="button" onclick="clearConsole()"><span class="am-icon-trash-o"></span> 清屏</button>
                <button class="am-btn am-btn-default" type="button" onclick="sendMessage()"><span class="am-icon-commenting"></span> 发送</button>
            </div>
             <!-- 接收者 -->
            <div class="" style="float: left">
                <div id="sendto" class="am-dropdown am-dropdown-up" data-am-dropdown>
	                <button  class="am-btn am-btn-xs am-dropdown-toggle sendtoBtn" data-am-dropdown-toggle><span class="butext">聊天类型</span> <span class="am-icon-caret-down"></span></button>
					  <ul class="am-dropdown-content">
					    <li class="am-dropdown-header">发送类型</li>
					    <li><a href="javascript:void(0)" class="sendto" onclick="changeText(1)">私聊</a></li>
					    <li><a href="javascript:void(0)" class="sendto" onclick="changeText(2)">群聊</a></li>
					  </ul>
				  </div>
                <button  class="am-btn am-btn-xs am-btn-danger" onclick="sendMessage()">发 送</button>
            </div>
            <!-- 输入区 -->
            <div class="am-form-group am-form">
                <textarea class="" id="message" name="message" rows="5"  placeholder="这里输入你想发送的信息..."></textarea>
            </div>
            
        </div>
        <!-- 列表区 -->
        <div class="am-panel am-panel-default" style="float:right;width: 20%;">
            <div class="am-panel-hd">
                <button class="am-btn am-btn-primary" data-am-collapse="{target: '#collapse-navonline'}">在线好友[<span id="onlinenum">  </span>] <i class="am-icon-bars"></i></button>
					<nav>
					<ul id="collapse-navonline" class="am-nav am-collapse">
					   <!--  <li class="am-active"><a href="">JS 介绍</a></li> -->
					  </ul>
					</nav>
            </div>
            <div class="am-panel-hd">
                <button class="am-btn am-btn-primary" data-am-collapse="{target: '#collapse-navallFriend'}">好友列表[<span id="allFriendNum">  </span>] <i class="am-icon-bars"></i></button>
					<nav>
					<ul id="collapse-navallFriend" class="am-nav am-collapse">
					   
					  </ul>
					</nav>
            </div>
            <div class="am-panel-hd">
                <button class="am-btn am-btn-primary" data-am-collapse="{target: '#collapse-navgroup'}">群    列    表[<span id="groupNum">  </span>]  <i class="am-icon-bars"></i></button>
					<nav>
					<ul id="collapse-navgroup" class="am-nav am-collapse">
					    
					  </ul>
					</nav>
            </div>
            
            <ul class="am-list am-list-static am-list-striped" >
                <li>图灵机器人 <button class="am-btn am-btn-xs am-btn-danger" id="tuling" data-am-button>未上线</button></li>
            </ul>
            <ul class="am-list am-list-static am-list-striped" id="list">
            </ul>
        </div>
    </div>
    <!-- content end -->
</div>
<a href="#" class="am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}">
    <span class="am-icon-btn am-icon-th-list"></span>
</a>
<jsp:include page="include/footer.jsp"/>
  <script src="${ctx}/js/Myjs/myChat.js"></script>
  <script src="${ctx}/js/Myjs/websocket-client.js"></script>
<script>

    $(function () {
        context.init({preventDoubleContext: false});
        context.settings({compress: true});
        context.attach('#chat-view', [
            {header: '操作菜单',},
            {text: '清理', action: clearConsole},
            {divider: true},
            {
                text: '选项', subMenu: [
                {header: '连接选项'},
                {text: '检查', action: checkConnection},
                {text: '连接', action: getConnection},
                {text: '断开', action: closeConnection},
                {text: '发送', action: sendMessage}
            ]
            },
            {
                text: '销毁菜单', action: function (e) {
                e.preventDefault();
                context.destroy('#chat-view');
            }
            }
        ]);
        
    });
    /**
     * 清空聊天区
     */
    function clearConsole(){
        $("#chat").html("");
    }
    if("${message}"){
        layer.msg('${message}', {
            offset: 0
        });
    }
    if("${error}"){
        layer.msg('${error}', {
            offset: 0,
            shift: 6
        });
    }
    function appendZero(s){return ("00"+ s).substr((s+"").length);}  //补0函数

    function getDateFull(){
        var date = new Date();
        var currentdate = date.getFullYear() + "-" + appendZero(date.getMonth() + 1) + "-" + appendZero(date.getDate()) + " " + appendZero(date.getHours()) + ":" + appendZero(date.getMinutes()) + ":" + appendZero(date.getSeconds());
        return currentdate;
    }
</script>
  
</body>
</html>
