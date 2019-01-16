<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<script src="/news/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="//connect.qq.com/qc_jssdk.js"
	data-appid="101504585"
	data-redirecturi="http://220a27a0.nat123.cc:19537/news/user/free/qqLoginCallback.jsp"
	charset="utf-8"></script>
<script type="text/javascript">
	$(document).ready(function() {
		if (QC.Login.check()) {
			QC.api("get_user_info")
				.success(function(s) { //成功回调
					QC.Login.getMe(function(openId, accessToken) {
						var url = "/news/servlet/UserServlet?type1=qq";
						url += "&nickname=" + s.data.nickname;
						url += "&openId=" + openId;
						url += "&accessToken=" + accessToken;

						window.parent.window.location.href = url; //在当前窗口中打开窗口
					})
				})
				.error(function(f) { //失败回调
					alert("获取用户信息失败！登录失败！");
					location.href = "/news/user/free/login.jsp";
				})
				.complete(function(c) { //完成请求回调
					//	alert("获取用户信息完成！");
				});
		} else {
			alert("请登录！");
			location.href = "/news/user/free/login.jsp";
		}
	});
</script>
</head>
<body id="mainDiv">
	<div>
		<h3>数据传输中，请稍后...</h3>
	</div>
</body>
</html>



