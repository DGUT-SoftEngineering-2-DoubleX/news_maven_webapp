<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="tools.WebProperties"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!doctype html>
<html>
<head>
<script src='<%=WebProperties.config.getString("ueditConfigJs")%>'
	type="text/javascript"></script>
<script src='<%=WebProperties.config.getString("ueditJs")%>'
	type="text/javascript"></script>
<script src='<%=WebProperties.config.getString("ueditLang")%>'
	type="text/javascript"></script>
</head>

<body>
	<div class="div-out">
		<form
			action="/news/servlet/NewsServlet?type1=edit&newsId=${requestScope.news.newsId}"
			id="myform" method="post" onsubmit="return submit1()">
			<table width="800" align="center" border="1">
				<tbody>
					<tr>
						<td>标题：</td>
						<td><input type="text" size="100"
							value="${requestScope.news.caption}" name="caption" id="caption"></td>
					</tr>
					<tr>
						<td>类型：</td>
						<td><select name="newsType" id="newsType">
								<c:forEach items="${applicationScope.newsTypes}" var="newsType">
									<c:choose>
										<c:when test="${newsType.name == requestScope.news.newsType}">
											<option value="${newsType.name}" selected>${newsType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${newsType.name}">${newsType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td>作者：</td>
						<td><input type="text" value=${requestScope.news.author
							} name="author" id="author"></td>
					</tr>
					<tr>
						<td>日期：</td>
						<td><input type="datetime-local"
							value="${requestScope.news.newsTime}" name="newsTime"
							id="newsTime"></td>
					</tr>
					<tr>
						<td colspan="2">
							<div>
								<script id="content" type="text/plain"
									style="width:800px;height:500px;"></script>
							</div>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="修改新闻">&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" onclick="cancel()" value="取消修改"></td>
					</tr>
				</tbody>
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
	</div>
	<script type="text/javascript">	
	    //实例化编辑器
	    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	    var ue = UE.getEditor('content');
	    //设置编辑器的内容
	    ue.ready(function() {
    		ue.setContent('${requestScope.news.content}');
		});
		
		function cancel(){//取消修改 
			ids1=document.getElementById("ids"); 
		  	ids1.value="";
		  	//提交
		  	document.getElementById('myform').action="/news/servlet/NewsServlet?type1=newsManage";
			document.getElementById('myform').submit();			
		}
		
	</script>

</body>
</html>
