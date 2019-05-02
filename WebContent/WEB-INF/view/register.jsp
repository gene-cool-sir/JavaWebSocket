<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>注册</title>
    <link href="${cp}/img/logo.ico" rel="icon"  type="image/x-ico" /> 
    <link href="${cp}/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${cp}/css/animate.css" rel="stylesheet"/>
    <link href="${cp}/css/style.css" rel="stylesheet"/>
    <link href="${cp}/css/login.css" rel="stylesheet"/>

	<script src="${cp}/js/jquery.js" type="text/javascript"></script>
    <script src="${cp}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${cp}/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${cp}/js/layer.js" type="text/javascript"></script>
    <!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="jump animated fadeInDown">
      <a href="${cp}/user/login" class="jumpLink">
        登录
      </a>
    </div>
    <div class="wrapper animated flipInY">
      <div class="container offsetUp">
        <img class="s-logo" src="${cp}/static/source/img/s-logo.png" alt="With Me">
        <div class="form">
          <input id="username" name="username" type="text" placeholder="用户名">
          <input id="nickname" name="nickname" type="text" placeholder="昵称">
          <input id=password  name="password" type="password" placeholder="密码">
          <button type="submit" onclick="checkRegist()">注册</button>
        </div>
      </div>
    </div>
        
        <script type="text/javascript">
    	function checkRegist(){
    		var currentUser = {}; 
    		currentUser.username = document.getElementById("username").value;
    		currentUser.nickname = document.getElementById("nickname").value;
    		currentUser.password = document.getElementById("password").value;
    		//document.getElementById("userName").value = '';
    		//document.getElementById("userNickName").value = '';
    		//document.getElementById("userPassword").value = '';
    		if(currentUser.username == ''){
    			layer.msg('用户名不能为空',{icon:2});
    			return;
    		}
    		else if(currentUser.username.length >= 12){
    			layer.msg('用户名长度不能超过12个字符',{icon:2});
    			return;
    		}
    		if(currentUser.nickname == ''){
    			layer.msg('昵称不能为空',{icon:2});
    			return;
    		}
    		else if(currentUser.nickname.length >= 15){
    			layer.msg('用户名长度不能超过15个字符',{icon:2});
    			return;
    		}
    		else if(currentUser.password == ''){
    			layer.msg('密码不能为空',{icon:2});
    			return;
    		}
    		else if(currentUser.password.length>= 20){
    			layer.msg('密码长度不能超过20个字符',{icon:2});
    			return;
    		}
    		var registResoult = null;
    		$.ajax({
				async : false, //设置同步
				type : 'POST',
				url : '${cp}/doRegister',
				data : currentUser,
				dataType : 'json',
				success : function(resoult) {
					registResoult = resoult.resoult;
				},
				error : function(resoult) {
					layer.alert('查询用户错误');
			}
			});
			if(registResoult == 'success'){
				layer.msg('注册成功', {
					icon : 2,
					zIndex : 20000001,
					time : 3000
				});
				window.location.href="${cp}/user/login";
			}
			else if(registResoult == 'exist'){
				layer.msg('这个用户名已经被占用啦！',{icon:2});
			}
			else if(registResoult == 'fail'){
				layer.msg('服务器异常',{icon:2});
			}
    	}
    </script>
  </body>
</html>