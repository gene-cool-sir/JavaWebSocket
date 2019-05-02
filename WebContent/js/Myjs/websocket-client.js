    var ws = null;
    //wsServer = "ws://" + location.host+path + "/chatServer";
  //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {	 
    	 ws = new WebSocket(wsServer); //创建WebSocket对象
    }
    else {
        alert('当前浏览器 Not support websocket');
    };
   // ws = new WebSocket(wsServer); //创建WebSocket对象
    ws.onopen = function (evt) {
    	// 初始化好友信息
    	initPersion();
        layer.msg("已经建立连接", { offset: 0});
    };
    ws.onmessage = function (evt) {
        analysisMessage(evt.data);  //解析后台传回的消息,并予以展示
    };
    ws.onerror = function (evt) {
        layer.msg("产生异常", { offset: 0});
    };
    ws.onclose = function (evt) {
        layer.msg("已经关闭连接", { offset: 0});
    };
    
  //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
    	// 修改用户下线状态
    	var currentUserId = $("#currentUserId").val();
    	changeUserIsOnline(currentUserId);
    	closeConnection();
    };
    
    function changeUserIsOnline(userId) {
    	$.ajax({
    		async : false, //设置同步
    		type : 'POST',
    		url : path+'/changeUserIsOnline',
    		data : {"userId": userId},
    		dataType : 'json',
    		success : function(resoult) {
    			// 通知其他好友已经下线
    			if (resoult.message == "success") {
    				/* var content = JSON.stringify({
    		               content : message,
    		               from : $("#currentUserId").val(),
    		               to : "",      // 群的GroupId , 或者私聊的某个人ID
    		               type: 3,
    		               time : getDateFull()
    		    	   });
    		    	   ws.send(content);*/
    			}
    		}
    	})
    }
    /**
     * 连接
     */
    function getConnection(){
        if(ws == null){
            ws = new WebSocket(wsServer); //创建WebSocket对象
            ws.onopen = function (evt) {
                layer.msg("成功建立连接!", { offset: 0});
            };
            ws.onmessage = function (evt) {
                analysisMessage(evt.data);  //解析后台传回的消息,并予以展示
            };
            ws.onerror = function (evt) {
                layer.msg("产生异常", { offset: 0});
            };
            ws.onclose = function (evt) {
                layer.msg("已经关闭连接", { offset: 0});
            };
        }else{
            layer.msg("连接已存在!", { offset: 0, shift: 6 });
        }
    }

    /**
     * 关闭连接
     */
    function closeConnection(){
        if(ws != null){
        	//ws.onclose();
            ws.close();
            ws = null;
            $("#list").html("");    //清空在线列表
            layer.msg("已经关闭连接", { offset: 0});
        }else{
            layer.msg("未开启连接", { offset: 0, shift: 6 });
        }
    }

    /**
     * 检查连接
     */
    function checkConnection(){
        if(ws != null){
            layer.msg(ws.readyState == 0? "连接异常":"连接正常", { offset: 0});
        }else{
            layer.msg("连接未开启!", { offset: 0, shift: 6 });
        }
    }
    
    /**
     * 发送信息给后台
     */
    function sendMessage(fromId,toId){
    	
        if(ws == null){
            layer.msg("连接未开启!", { offset: 0, shift: 6 });
            return;
        }
        var flag  = tulingChat();
        if (flag) {// 和机器人聊天
        	return;
        }
        var toId =  $("#toIds").val();
        if (toId == '') {
        	layer.msg("请选择右侧发送的对象!", { offset: 0, shift: 6 });
            return;
        }
        var type=$("#chatType").val();
        var message = $("#message").val();
        if (fromId== undefined||fromId =="" ) {
        	fromId = $("#currentUserId").val();
        }
        if (toId== undefined||toId =="" ) {
        	toId = $("#toIds").val();
        }
        if(message == null || message == ""){
            layer.msg("请不要惜字如金!", { offset: 0, shift: 6 });
            return;
        }
        var content = JSON.stringify({
                content : message,
                from : fromId,
                to : toId,      // 群的GroupId , 或者私聊的某个人ID
                type: type,
                time : getDateFull()
            
        });
        ws.send(content);
    }
    
    /**
     * 解析服务器返回的消息
     * @param message
     * @returns
     */
    function analysisMessage(message){
        message = JSON.parse(message);
        var type=$("#chatType").val(message.type)
        if(message.type == "0"){      //机器人聊天
            showRoobootChat(message);
        }
        if(message.type == "1"){      //单独
            showSingleChat(message);
        }
        if(message.type == "2"){      //群聊
            showChat(message);
        }
        if(message.type == "3"){       //提示消息
            showNotice(message.content);
        }
        if (message.type == "4" ) {
        	// 不处理
        	$("#chatType").val("1");
        }
        if (message.type == "5" ) {
        	// 不处理
        	$("#chatType").val("2");
        }
        if (message.type != "0") {
        	// 刷新好友列表信息
        	initPersion();
        }
        
        
        
    }
    
    /**
     * 展示提示信息
     */
    function showNotice(notice){
        $("#chat").append("<div><p class=\"am-text-success\" style=\"text-align:center\"><span class=\"am-icon-bell\"></span> "+notice+"</p></div>");
        var chat = $("#chat-view");
        chat.scrollTop(chat[0].scrollHeight);   //让聊天区始终滚动到最下面
    }
    
	function showRoobootChat(message) {
		var currentUserName =$("#currentUserName").val();
		var html = "<li class=\"am-comment am-comment-primary\"><a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\""+path+"/head\"></a><div class=\"am-comment-main\">\n" +
        "<header class=\"am-comment-hd\"><div class=\"am-comment-meta\">   <a class=\"am-comment-author\" href=\"#link-to-user\">"+currentUserName+"</a> 发表于<time> "+message.time+"</time> </div></header><div class=\"am-comment-bd\"> <p>"+message.content+"</p></div></div></li>";
		$("#chat").append(html);
		$("#message").val("");  //清空输入区
		var chat = $("#chat-view");
		chat.scrollTop(chat[0].scrollHeight);   //让聊天区始终滚动到最下面

	}
	
	/**
     * 单独
     */
    function showSingleChat(message){
        var isSef = $("#currentUserId").val() != message.from ? "am-comment-flip" : "";   //如果是自己则显示在右边,他人信息显示在左边
        var html = "<li class=\"am-comment "+isSef+" am-comment-primary\"><a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\""+path+"/head\"></a><div class=\"am-comment-main\">\n" +
                "<header class=\"am-comment-hd\"><div class=\"am-comment-meta\">   <a class=\"am-comment-author\" href=\"#link-to-user\">"+message.fromUserName+"</a> 发表于<time> "+message.time+"</time> 发送给: "+message.toUserName+" </div></header><div class=\"am-comment-bd\"> <p>"+message.content+"</p></div></div></li>";
        $("#chat").append(html);
        $("#message").val("");  //清空输入区
        var chat = $("#chat-view");
        chat.scrollTop(chat[0].scrollHeight);   //让聊天区始终滚动到最下面
    }
	
    /**
     * 展示会话信息
     */
    function showChat(message){
        var isSef = $("#currentUserId").val() != message.from ? "am-comment-flip" : "";   //如果是自己则显示在右边,他人信息显示在左边
        var html = "<li class=\"am-comment "+isSef+" am-comment-primary\"><a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\""+path+"/head\"></a><div class=\"am-comment-main\">\n" +
                "<header class=\"am-comment-hd\"><div class=\"am-comment-meta\">   <a class=\"am-comment-author\" href=\"#link-to-user\">"+message.fromUserName+"</a> 发表于<time> "+message.time+"</time> </header><div class=\"am-comment-bd\"> <p>"+message.content+"</p></div></div></li>";
        $("#chat").append(html);
        $("#message").val("");  //清空输入区
        var chat = $("#chat-view");
        chat.scrollTop(chat[0].scrollHeight);   //让聊天区始终滚动到最下面
    }

    
    /**
     * 判断是否开启机器人聊天
     */
    $("#tuling").click(function(){
        $("#toIds").val("");
        if($(this).text() == "未上线"){
            $(this).text("已上线").removeClass("am-btn-danger").addClass("am-btn-success");
            // 是否清空面板
     	   eachText2($(".chattypeinfo"),"正在私聊");
     	   eachText2($(".chattypeinfomany"),"正在群聊");
     	   // 恢复其他按钮
     	   eachText($(".chattypeinfo"),"点击私聊");
     	   eachText($(".chattypeinfomany"),"点击群聊");
            showNotice("图灵机器人上线");
        }
        else{
            $(this).text("未上线").removeClass("am-btn-success").addClass("am-btn-danger");
            showNotice("图灵机器人下线");
        }
    });
    /**
     * 和机器人聊天
     */
    function tulingChat(currentId) {
   	 if(ws == null){
            layer.msg("连接未开启!", { offset: 0, shift: 6 });
            return;
        }
   	 	// 和机器人聊天
       if ($("#tuling").text() == "已上线") {
    	   var message = $("#message").val();
    	   if(message == null || message == ""){
               layer.msg("请不要惜字如金!,输入您想说的话!", { offset: 0, shift: 6 });
               return;
           }
    	   
    	   $("#toIds").val(""); // 清空聊天对象
    	   var content = JSON.stringify({
               content : message,
               from : $("#currentUserId").val(),
               to : "",      // 群的GroupId , 或者私聊的某个人ID
               type: 0,
               time : getDateFull()
    	   });
    	   ws.send(content);
    	   tuling(message);  //检测是否加入图灵机器人
    	   return true;
       } else {
    	   return false;
       }
   		
   }

/**
 * 图灵机器人
 * @param message
 */
function tuling(message){
    var html;
    var currentUserName =$("#currentUserName").val();
    $.getJSON("http://www.tuling123.com/openapi/api?key=6ad8b4d96861f17d68270216c880d5e3&info=" + message,function(data){
        if(data.code == 100000){
            html = "<li class=\"am-comment am-comment-flip am-comment-primary\"><a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\""+path+"/static/source/img/robot.jpg\"></a><div class=\"am-comment-main\">\n" +
                    "<header class=\"am-comment-hd\"><div class=\"am-comment-meta\">   <a class=\"am-comment-author\" href=\"#link-to-user\">Robot</a> 发表于<time> "+getDateFull()+"</time> 发送给: "+currentUserName+"</div></header><div class=\"am-comment-bd\"> <p>"+data.text+"</p></div></div></li>";
        }
        if(data.code == 200000){
            html = "<li class=\"am-comment am-comment-flip am-comment-primary\"><a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\""+path+"/static/source/img/robot.jpg\"></a><div class=\"am-comment-main\">\n" +
                    "<header class=\"am-comment-hd\"><div class=\"am-comment-meta\">   <a class=\"am-comment-author\" href=\"#link-to-user\">Robot</a> 发表于<time> "+getDateFull()+"</time> 发送给:  "+currentUserName+"</div></header><div class=\"am-comment-bd\"> <p>"+data.text+"</p><a href=\""+data.url+"\" target=\"_blank\">"+data.url+"</a></div></div></li>";
        }
        $("#chat").append(html);
        var chat = $("#chat-view");
        chat.scrollTop(chat[0].scrollHeight);
        $("#message").val("");  //清空输入区
    });
    
    /**
     * 清空聊天区
     */
    function clearConsole(){
        $("#chat").html("");
    }
    
}
 
   
