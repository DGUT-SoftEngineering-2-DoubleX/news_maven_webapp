<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
</head>
<body style="overflow-x:hidden;">
	<h2>系统统计</h2>
	<form action="/news/servlet/StatisticsServlet" id="myform"
		method="post">
		<span> 查找 <select name="condition" id="condition">
				<option value="all"
					<c:if test="${requestScope.condition == 'all' }">selected="selected"</c:if>
				>全部</option>
				<option value="lastWeek"
					<c:if test="${requestScope.condition == 'lastWeek' }">selected="selected"</c:if>
				>近一周</option>
				<option value="lastMonth"
					<c:if test="${requestScope.condition == 'lastMonth' }">selected="selected"</c:if>
				>近一月</option>
				<option value="lastYear"
					<c:if test="${requestScope.condition == 'lastYear' }">selected="selected"</c:if>
				>近一年</option>
				<option value="lastYearEachMonth"
					<c:if test="${requestScope.condition == 'lastYearEachMonth' }">selected="selected"</c:if>
				>近一年每月新闻数、评论数</option>
		</select> 系统信息内容
		</span> <input type="submit" value="   查   找   " />
	</form>

	<c:if
		test="${requestScope.newsAuthorResult==null && requestScope.userResult==null && requestScope.sumNewsStatistics==null && requestScope.sumCommentStatistics==null}">
		<h3>请选择条件进行查找！</h3>
	</c:if>

	<c:if
		test="${requestScope.newsAuthorResult.isEmpty() || requestScope.userResult.isEmpty() || requestScope.sumNewsStatistics.isEmpty() || requestScope.sumCommentStatistics.isEmpty()}">
		<h3>查找结果为空！</h3>
	</c:if>

	<c:if
		test="${requestScope.newsAuthorResult!=null && !requestScope.newsAuthorResult.isEmpty()}">
		<br>
		<table width="500" border="1" cellspacing="0" cellpadding="0">
			<tr align="center" height="25">
				<td width="250">新闻发布员昵称</td>
				<td>发布新闻数量</td>
			</tr>
			<c:forEach items="${requestScope.newsAuthorResult}" var="authorMap">
				<tr align="center" height="25">
					<td width="250">${authorMap.name }</td>
					<td>${authorMap.count }</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<c:if
		test="${requestScope.userResult!=null && !requestScope.userResult.isEmpty()}">
		<br>
		<table width="500" border="1" cellspacing="0" cellpadding="0">
			<tr align="center" height="25">
				<td width="250">用户昵称</td>
				<td>发布评论数量</td>
			</tr>
			<c:forEach items="${requestScope.userResult}" var="userMap">
				<tr align="center" height="25">
					<td width="250">${userMap.name }</td>
					<td>${userMap.count }</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<c:if
		test="${requestScope.sumNewsStatistics!=null && !requestScope.sumNewsStatistics.isEmpty()}">
		<br>
		<table width="500" border="1" cellspacing="0" cellpadding="0">
			<tr align="center" height="25">
				<td width="250">时间</td>
				<td>发布新闻总数量</td>
			</tr>
			<c:forEach items="${requestScope.sumNewsStatistics}"
				var="newsStatistics">
				<tr align="center" height="25">
					<td width="250">${newsStatistics.name }</td>
					<td>${newsStatistics.count }</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<c:if
		test="${requestScope.sumCommentStatistics!=null && !requestScope.sumCommentStatistics.isEmpty()}">
		<br>
		<table width="500" border="1" cellspacing="0" cellpadding="0">
			<tr align="center" height="25">
				<td width="250">时间</td>
				<td>用户评论总数量</td>
			</tr>
			<c:forEach items="${requestScope.sumCommentStatistics}"
				var="commentStatistics">
				<tr align="center" height="25">
					<td width="250">${commentStatistics.name }</td>
					<td>${commentStatistics.count }</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

</body>
</html>