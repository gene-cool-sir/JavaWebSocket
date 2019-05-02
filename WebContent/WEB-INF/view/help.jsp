<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>哎 聊 | 帮助</title>
    <jsp:include page="include/commonfile.jsp"/>
    <style>
        html, body{
            height:80%;;
        }
    </style>
</head>
<body>
<jsp:include page="include/header.jsp"/>
<div class="am-cf admin-main">
    <jsp:include page="include/sidebar.jsp"/>

    <!-- content start -->
    <div class="admin-content">
        <div class="am-cf am-padding">
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">帮助</strong> / <small>help</small></div>
        </div>
        <div class="am-tabs am-margin" data-am-tabs>
            <ul class="am-tabs-nav am-nav am-nav-tabs">
                <li class="am-active"><a href="#tab1">帮助</a></li>
            </ul>
            <div class="am-tabs-bd">
                <div class="am-tab-panel am-fade am-in am-active" id="tab1">
                    本系统使用说明: <br>
                    此为开发的v1.0版本; 左侧为用户个人信息设置,好友设置,群组设置<br>
                    点击聊天tab页的时候,进入聊天系统:<br>
                    右侧展示在线好友 /  所有好友  / 所在的群组<br>
                    点击私聊: 可以给指定的好友发送信息<br>
        点击群聊: 发送的信息,所有的群成员都可以接手到该信息<br>
        点击图灵机器人聊天: 调用了接口, 可以和图灵机器人对话聊天(比较有意思哦!!!!!!)<br>
        若有问题请联系我: www.houyinbing@sina.cn<br>
                </div>
            </div>
        </div>
        <!-- content end -->
    </div>
    <a href="#" class="am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}">
        <span class="am-icon-btn am-icon-th-list"></span>
    </a>
    <jsp:include page="include/footer.jsp"/>
</body>
</html>
