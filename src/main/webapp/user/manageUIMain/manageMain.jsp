<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<meta charset="utf-8">
<script type="text/javascript">
	function getOnePage(type) {
		var url1;
		var page = document.getElementById("page");
		var pageSize = document.getElementById("pageSize");
		var totalPageCount = document.getElementById("totalPageCount");

		pageValue = parseInt(page.value);
		if (type == "pre") {
			pageValue -= 1;
			page.value = pageValue.toString();
		} else if (type == "next") {
			pageValue += 1;
			page.value = pageValue.toString();
		}
		//提交
		document.getElementById('myform').submit();
	}

	function setNoScrolling() {
		var frame = document.getElementById("frame");
		frame.setAttribute("scrolling", "no");
	}

	function setYesScrolling() {
		var frame = document.getElementById("frame");
		frame.setAttribute("scrolling", "yes");
	}
</script>
</head>
<div class="newsShowByType_frame center" id="frameDiv">
	<div class="newsShowByType_left" id="leftDiv">
		<ul style="list-style-type: none;">
			<li><a href="/news/servlet/UserServlet?type1=showPrivate"
				target='frame' onclick="setNoScrolling()">显示个人信息</a></li>
			<li><a href="/news/servlet/UserServlet?type1=changePrivate1"
				target='frame' onclick="setNoScrolling()">修改个人信息</a></li>
			<li><a href="/news/user/manage/changePassword.jsp"
				target='frame' onclick="setNoScrolling()">修改密码</a></li>
			<c:if test="${sessionScope.user.type=='manager'}">
				<br>
				<li><a
					href="/news/servlet/UserServlet?type1=showPage&page=1&pageSize=10"
					target='frame' onclick="setNoScrolling()">浏览用户</a></li>
				<li><a
					href="/news/servlet/UserServlet?type1=check&page=1&pageSize=10"
					target='frame' onclick="setNoScrolling()">审查信息</a></li>
				<li><a href="/news/manager/userSearch.jsp" target='frame'>查询用户</a></li>
				<li><a
					href="/news/servlet/UserServlet?type1=delete&page=1&pageSize=10"
					target='frame' onclick="setNoScrolling()">删除用户</a></li>
				<br>
				<li><a
					href="/news/servlet/NewsServlet?type1=manageNews&page=1&pageSize=5"
					target='frame' onclick="setNoScrolling()">管理新闻</a></li>
				<br>
				<li><a href="/news/statisticsResult.jsp" target='frame'
					onclick="setYesScrolling()">系统统计</a></li>

			</c:if>
			<c:if test="${sessionScope.user.type=='newsAuthor'}">
				<br>
				<li><a href="/news/news/manage/addNews.jsp" target='_blank'>添加新闻</a></li>
				<li><a
					href="/news/servlet/NewsServlet?type1=manageNews&page=1&pageSize=5"
					target='frame' onclick="setNoScrolling()">管理新闻</a></li>
			</c:if>
		</ul>
	</div>
	<div class="manageMain_right">
		<iframe name="frame" id="frame" scrolling="no" seamless
			frameborder="0" width="100%" height="100%"
			src="/news/servlet/UserServlet?type1=showPrivate"> </iframe>
	</div>
	<div class="clear"></div>
</div>
</body>

<script type="text/javascript">
	var father = document.getElementById('frameDiv');
	var son = document.getElementById('leftDiv');
	frameDiv.style.height = leftDiv.offsetHeight + 'px';
</script>

</html>
