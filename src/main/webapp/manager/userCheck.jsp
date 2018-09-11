<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function checkAUser(userId) {
		var ids = document.getElementById("ids");
		ids.value = userId;
		//提交
		document.getElementById('myform').submit();
	}

	function getOnePage(type, orderFieldName) {
		var url1;
		var page = document.getElementById("page");
		var pageSize = document.getElementById("pageSize");
		var totalPageCount = document.getElementById("totalPageCount");

		var order = document.getElementById("order");
		var orderField = document.getElementById("orderField");

		if (orderFieldName != "") { //切换排序
			orderField.value = orderFieldName; //设置排序字段名
			if (order.value == "asc") //切换排序
				order.value = "desc";
			else
				order.value = "asc";

			page.value = 1; //排序后从第一页开始显示					
		}

		pageValue = parseInt(page.value);
		if (type == "first")
			page.value = "1";
		else if (type == "pre") {
			pageValue -= 1;
			page.value = pageValue.toString();
		} else if (type == "next") {
			pageValue += 1;
			page.value = pageValue.toString();
		} else if (type == "last") {
			page.value = totalPageCount.value;
		}
		//提交
		document.getElementById('myform').submit();
	}
</script>
</head>

<body>
	<h2>审查用户</h2>
	<br>
	<form action="/news/servlet/UserServlet?type1=check" id="myform"
		method="post">
		<table align="center" class="tableDefault" style="width:520px;">
			<tr style="background-color:#FFACAC;">
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','userId');">Id</a></td>
				<td>用户类型</td>
				<td>用户名</td>
				<td>注册日期</td>
				<td><a href='javascript:void(0);'
					onclick="getOnePage('','enable');">可用性</a></td>
				<td>切换可用性</td>
			</tr>
			<c:forEach items="${requestScope.users}" var="user">
				<c:if
					test="${(user.name!=sessionScope.user.name) && !(user.type=='manager' && user.enable=='use') && (user.name!='anonymous')}">
					<tr class="trDefault">
						<td>${user.userId}</td>
						<td>${user.type}</td>
						<td>${user.name}</td>
						<td><myTag:TimestampTag dateTime="${user.registerDate}"
								type="YMDHMS-" /></td>
						<td align="center"><c:choose>
								<c:when test="${user.enable == 'use'}">可用</c:when>
								<c:when test="${user.enable == 'stop'}">停用</c:when>
							</c:choose></td>
						<td align="center"><c:choose>
								<c:when test="${user.enable == 'use'}">
									<a href="javascript:void(0);"
										onclick="checkAUser('${user.userId}');">停用</a>
								</c:when>
								<c:when test="${user.enable == 'stop'}">
									<a href="javascript:void(0);"
										onclick="checkAUser('${user.userId}');">启用</a>
								</c:when>
							</c:choose></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
		<table align="center" class="tableDefault">
			<tr>
				<c:if test="${requestScope.pageInformation.page > 1}">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('first','');">首页</a></td>
				</c:if>

				<c:if test="${requestScope.pageInformation.page > 1}">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('pre','');">上一页</a></td>
				</c:if>

				<c:if
					test="${requestScope.pageInformation.page < requestScope.pageInformation.totalPageCount}">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('next','');">下一页</a></td>
				</c:if>
				<c:if
					test="${requestScope.pageInformation.page < requestScope.pageInformation.totalPageCount}">
					<td><a href="javascript:void(0);"
						onclick="getOnePage('last','');">尾页</a></td>
				</c:if>
				<td>共${requestScope.pageInformation.totalPageCount}页</td>
			</tr>
		</table>
		<input type="hidden" name="page" id="page"
			value="${requestScope.pageInformation.page}"> <input
			type="hidden" name="pageSize" id="pageSize"
			value="${requestScope.pageInformation.pageSize}"> <input
			type="hidden" name="totalPageCount" id="totalPageCount"
			value="${requestScope.pageInformation.totalPageCount}"> <input
			type="hidden" name="allRecordCount" id="allRecordCount"
			value="${requestScope.pageInformation.allRecordCount}"> <input
			type="hidden" name="orderField" id="orderField"
			value="${requestScope.pageInformation.orderField}"> <input
			type="hidden" name="order" id="order"
			value="${requestScope.pageInformation.order}"> <input
			type="hidden" name="ids" id="ids"
			value="${requestScope.pageInformation.ids}"> <input
			type="hidden" name="searchSql" id="searchSql"
			value="${requestScope.pageInformation.searchSql}"> <input
			type="hidden" name="result" id="result"
			value="${requestScope.pageInformation.result}">
	</form>
</body>
</html>
