<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#restore").click(function() {
			$.ajax({
				type : "post",
				dataType : "json",
				url : '/news/servlet/DatabaseServlet?type=restoreSecond',
				data : $("#restoreForm").serialize(),
				beforeSend : function() {
					// 禁用按钮防止重复提交
					$("#restore").attr({
						disabled : "disabled"
					});
					$("#myChart").html("<h3>正在操作,请稍后……</h3>");
				},
				success : function(message) {
					if (message != null && message.result != 0) {
						var nowDate = new Date();
						alert("还原结束！");
						$("#myChart").html("<h3>操作结束……</h3>" + message.message + "<br>结束时间：" + nowDate.toLocaleDateString() + " " + nowDate.toLocaleTimeString());
					} else if (message.result == 0) {
						alert("暂无备份数据，请先备份数据库！");
						$("#myChart").html("<h3>" + message.message + "</h3>");
					} else {
						alert("服务器异常！");
						$("#myChart").html("<h3>服务器异常！</h3>");
					}
				},
				complete : function() {
					// 完成后恢复按钮
					$('#restore').removeAttr("disabled");
				},
				error : function() {
					$("#myChart").html("服务器异常！");
				}
			});
		})
	});
</script>
</head>

<body>
	<h2>数据库还原</h2>
	<br>
	<form id="restoreForm">
		<select style="width:100%;" name="databaserestoreId" size="15"
			required>
			<c:if test="${requestScope.databasebackupsSize > 0}">
				<c:forEach items="${requestScope.databasebackups}"
					var="databasebackup">
					<option style="text-align:center;"
						value="${databasebackup.databasebackupId}">- ${databasebackup.name} -</option>
				</c:forEach>
			</c:if>
			<c:if test="${requestScope.databasebackupsSize == 0}">
				<option style="text-align:center;" disabled="disabled" value="null">- 暂无备份数据，请先备份数据库！ -</option>
			</c:if>
		</select><br> <br>
		<center>
			<input type="button" id="restore" value="  开 始 还 原   " />
		</center>
		<br>
		<div id="myChart"></div>
	</form>
</body>
</html>
