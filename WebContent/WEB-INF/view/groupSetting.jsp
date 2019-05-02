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

    <!-- content start -->
    <div class="admin-content">
        <div class="am-cf am-padding">
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">群组信息</strong> / <small>group</small></div>
        </div>
                <div class="am-cf am-padding">
            <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">群组设置</strong> / <small>group</small></div>
        </div>
         <div data-am-widget="list_news" class="am-list-news am-list-news-default" >
		<button type="button" class="am-btn am-btn-success" >搜索群</button>
     	<button type="button" class="am-btn am-btn-success" >添加群组[先搜索]</button>
		<button type="button" class="am-btn am-btn-warning" >删除群]</button>
		<button type="button" class="am-btn am-btn-warning" >刷新[]</button>
		<hr>
  	<!--列表标题-->
    <div class="am-list-news-hd am-cf">
        <a href="##" class="">
          <h2>群列表[点击群名称查看所有群成员]</h2>
        </a>
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
