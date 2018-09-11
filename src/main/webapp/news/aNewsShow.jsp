<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="tools.WebProperties"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!doctype html>
<html>
<head>
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="center" style="width:800px;margin-top:30px;">
		<table width="800" border="0">
			<tbody>
				<tr>
					<td class="newsCaption">${requestScope.news.caption}<c:if
							test="${sessionScope.user.type=='manager' && requestScope.news.check==0 }">
							<br>
							<a
								href="/news/servlet/NewsServlet?type1=passCheck&newsId=${news.newsId}">（点击此处通过审核）</a>
						</c:if>
					</td>
				<tr>
					<td align="center" height="50">作者：${requestScope.news.author}&nbsp;
						&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; <myTag:LocalDateTimeTag
							dateTime="${requestScope.news.newsTime}" type="YMDHM" />
					</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
				<tr>
					<td>${requestScope.news.content}</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
			</tbody>
		</table>
	</div>
	<jsp:include page='/comment/addComment.jsp' flush="true">
		<jsp:param name="newsId" value="${requestScope.news.newsId}" />
		<jsp:param name="page" value="1" />
		<jsp:param name="pageSize" value="${param.pageSize}" />
		<jsp:param name="totalPageCount" value="${param.totalPageCount}" />
	</jsp:include>
	<jsp:include page='<%="/servlet/CommentServlet?type=showCommnet"%>'
		flush="true">
		<jsp:param name="newsId" value="${requestScope.news.newsId}" />
		<jsp:param name="page" value="${param.page}" />
		<jsp:param name="pageSize" value="${param.pageSize}" />
		<jsp:param name="totalPageCount" value="${param.totalPageCount}" />
	</jsp:include>
</body>
</html>
<script language="JavaScript">
	if ('${param.addCommnet}' == 'addCommnet')
		location.hash = "#commentsStart";
</script>