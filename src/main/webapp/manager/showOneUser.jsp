<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
</head>
<body>

	<a href="/news/servlet/UserServlet?type1=showPage&page=1&pageSize=10"
		target='frame' style="text-decoration: none;">«点击此处返回上一页</a>
	<br>
	<table width="500" border="0">
		<tbody>
			<tr>
				<td colspan="2" align="center"
					style="line-height: 30px;font-size: 20px;">展示个人信息：</td>
			</tr>
			<tr>
				<td align="right" width="150">用户类型：</td>
				<td>${ sessionScope.oneUser.type}</td>
			</tr>
			<tr>
				<td align="right">用户名：</td>
				<td>${ sessionScope.oneUser.name}</td>
			</tr>
			<tr>
				<td align="right">头像：</td>
				<td><img src="${ sessionScope.oneUser.headIconUrl}"
					height="200" /></td>
			</tr>
			<tr>
				<td align="right">注册日期：</td>
				<td><myTag:TimestampTag
						dateTime="${ sessionScope.oneUser.registerDate}" type="YMDHMS-" /></td>
			</tr>
			<c:if test="${sessionScope.oneUser.type=='user'}">
				<tr>
					<td align="right">性别：</td>
					<td>${ requestScope.oneUserinformation.sex}</td>
				</tr>
				<tr>
					<td align="right">爱好：</td>
					<td>${ requestScope.oneUserinformation.hobby}</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</body>
</html>
