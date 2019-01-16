<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function search() {
		document.getElementById('searchForm').submit();
	}
</script>
</head>
<body>
	<form id="searchForm" action="/news/servlet/NewsServlet?type1=search">
		<div class="div-out">
			<div class="logleft">
				<img src="/news/images/log.png">
			</div>
			<div class="logMiddle">
				<div class="logMiddleInner">
					<input type="text" id="search" placeholder="请在此输入搜索内容"> <a
						href='javascript:void(0);' onclick="search();"> <img
						src="/news/images/search.jpg" height="25" width="24" /></a>
				</div>
			</div>
			<div class="logRight">
				<div class="logRightInner">
					<c:if test="${!(empty sessionScope.user) }">
						<a href="/news/user/manageUIMain/manageMain.jsp">管理</a>&nbsp;
					</c:if>

					<a href="/news/index.jsp">首页</a>&nbsp;
					<c:choose>
						<c:when test="${empty sessionScope.user}">
							<a href="/news/user/free/login.jsp">登录</a>
							&nbsp;<a href="/news/user/free/register.jsp">注册</a>
						</c:when>
						<c:otherwise>
					    	${sessionScope.user.name}&nbsp;
					    	<a href="/news/servlet/UserServlet?type1=exit">注销</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</form>
</body>
</html>
