<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
</head>
<body>

	<table width="500" border="0">
		<tbody>
			<tr>
				<td colspan="2" align="center"
					style="line-height: 30px;font-size: 20px;">个人信息：</td>
			</tr>
			<tr>
				<td align="right" width="150">用户类型：</td>
				<td>${ sessionScope.user.type}</td>
			</tr>
			<tr>
				<td align="right">用户名：</td>
				<td>${ sessionScope.user.name}</td>
			</tr>
			<tr>
				<td align="right">头像：</td>
				<td><img src="${ sessionScope.user.headIconUrl}" height="200" /></td>
			</tr>
			<tr>
				<td align="right">注册日期：</td>
				<td><myTag:TimestampTag
						dateTime="${ sessionScope.user.registerDate}" type="YMDHMS-" /></td>
			</tr>
			<c:if test="${sessionScope.user.type=='user'}">
				<tr>
					<td align="right">性别：</td>
					<td>${ requestScope.userinformation.sex}</td>
				</tr>
				<tr>
					<td align="right">爱好：</td>
					<td>${ requestScope.userinformation.hobby}</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</body>
</html>
