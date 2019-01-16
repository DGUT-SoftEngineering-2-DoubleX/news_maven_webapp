<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#backup").click(function() {
			$.ajax({
				type : "post",
				dataType : "json",
				url : '/news/servlet/DatabaseServlet?type=backup',
				data : "",
				beforeSend : function() {
					// 禁用按钮防止重复提交
					$("#backup").attr({
						disabled : "disabled"
					});
					$("#myChart").html("<h3>正在操作,请稍后……</h3>");
				},
				success : function(message) {
					if (message != null) {
						var nowDate = new Date();
						alert("操作结束！");
						$("#myChart").html("<h3>操作结束……</h3>" + message.message + "<br>结束时间：" + nowDate.toLocaleDateString() + " " + nowDate.toLocaleTimeString());
					} else {
						alert("服务器异常！");
						$("#myChart").html("<h3>服务器异常！</h3>");
					}
				},
				complete : function() {
					// 完成后恢复按钮
					$('#backup').removeAttr("disabled");
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
	<h2>备份数据库</h2>
	<br>
	<form>
		<input type="button" id="backup" value="  开 始 备 份   " />
	</form>
	<br>
	<div id="myChart"></div>
</body>
</html>
