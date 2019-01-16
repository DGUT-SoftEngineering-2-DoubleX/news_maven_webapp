<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#inputYear").attr('value', new Date().getFullYear());
		$("#myform").on("submit", function(ev) {
			$.ajax({
				type : "post",
				dataType : "json",
				url : '/news/servlet/StatisticsServlet?type=commentNumberTopTenInAYear',
				data : $("#myform").serialize(),
				beforeSend : function() {
					// 禁用按钮防止重复提交
					$("#submit").attr({
						disabled : "disabled"
					});
				},
				success : function(data) {
					if (data != null) {
						alert(data.message);
						if (data.result == 1)
							$("#myChart").html("<br><br><a href='" + data.redirectUrl + "'>点击此处下载excel文件</a>");
					} else
						alert("异常！");
				},
				complete : function() {
					// 完成后恢复按钮
					$('#submit').removeAttr("disabled");
				},
				error : function() {
					alert("异常！");
				}
			});
			ev.preventDefault();
		});
	});
</script>

</head>
<body>
	<div>
		<h2>年度评论发布排行前十数据柱状图</h2>
		<br>
		<form id="myform">
			请输入年份： <input id="inputYear" type="number" step="1" min="2017"
				max="2030" name="year" title="年份应在2017-2030之间" /> <input
				id="submit" type="submit" value="    提交    " />
		</form>
	</div>

	<div id="myChart"></div>

</body>
</html>
