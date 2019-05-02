<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cp" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>登录</title>
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
      <a href="${cp}/register" class="jumpLink">
        注册
      </a>
    </div>
    <div class="wrapper animated flipInY">
      <div class="container offsetUps">
        <img class="s-logo" src="${cp}/img/s-logo.png" alt="With Me">
        
        <div class="form">
          <input id="userName" type="text" placeholder="用户名">
          <input id="userPassword" type="password" placeholder="密码">
          <button onclick="checkLogin()">登录</button>
        </div>
      </div>
    </div>
    <script type="text/javascript">
    	function checkLogin(){
    		var currentUser = {}; 
    		currentUser.username = document.getElementById("userName").value;
    		currentUser.password = document.getElementById("userPassword").value;
    		//document.getElementById("userName").value = '';
    		//document.getElementById("userPassword").value = '';
    		if(currentUser.username == ''){
    			layer.msg('用户名不能为空',{icon:2});
    			return;
    		}
    		else if(currentUser.username.length >= 12){
    			layer.msg('用户名长度不能超过12个字符',{icon:2});
    			return;
    		}
    		else if(currentUser.password == ''){
    			layer.msg('密码不能为空',{icon:2});
    			return;
    		}
    		var loginResoult = null;
    		$.ajax({
				async : false, //设置同步
				type : 'POST',
				url : '${cp}/user/login',
				data : currentUser,
				dataType : 'json',
				success : function(resoult) {
					loginResoult = resoult.resoult;
				},
				error : function(resoult) {
					layer.alert('查询用户错误');
			}
			});
			if(loginResoult == 'success'){
				layer.msg('登录成功',{icon:1});
				window.location.href="${cp}/main";
			}
			else if(loginResoult == 'unexist'){
				layer.msg('用户不存在',{icon:2});
			}
			else if(loginResoult == 'wrong'){
				layer.msg('用户名或者密码不对哦，再想想~',{icon:2});
			}
			else if(loginResoult == 'fail'){
				layer.msg('用户已注销',{icon:2});
			}
    	}
    </script>
  </body>
</html>