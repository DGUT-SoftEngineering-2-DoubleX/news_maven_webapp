<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!doctype html>
<html>
<head>
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<meta charset="utf-8">
</head>
<body>
	<div class="center" style="width:800px;margin-top:30px;">
		<form action="/news/servlet/CommentServlet?type=addCommnet"
			method="post">
			<div class="center" style="width:500px">
				<textarea name="content" cols="72" rows="8" id="textarea"
					style="resize: none;" required></textarea>
			</div>
			<p>
			<div class="center" style="width:50px;height:80px;">
				<br>
				<c:if
					test="${requestScope.news.check==1 && (sessionScope.user.type!='manager' && sessionScope.user.type!='newsAuthor')}">
					<input type="submit" name="submit" id="submit" value="  提 交 评 论  ">
				</c:if>
				<c:if
					test="${requestScope.news.check==0 || (sessionScope.user.type=='manager' || sessionScope.user.type=='newsAuthor') }">
					<button type="button" disabled="disabled" style="width: 82px;">提
						交 评 论</button>
				</c:if>
			</div>
			<input type="hidden" name="newsId" id="newsId"
				value="${param.newsId}"> <input type="hidden" name="page"
				id="page" value="${param.page}"> <input type="hidden"
				name="pageSize" id="pageSize" value="${param.pageSize}">
		</form>
	</div>
</body>
</html>
