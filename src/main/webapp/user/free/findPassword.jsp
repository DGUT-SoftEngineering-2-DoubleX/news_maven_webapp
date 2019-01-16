<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>找回密码</title>
<meta name="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
</td>

<style type="text/css">
.span1 {
	color: #F00;
}
</style>

<script type="text/javascript">
	function emailCheck() {
		var pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$", "i"); //创建模式对象
		var str1 = $("#email").val(); //获取文本框的内容

		if (str1.length >= 8 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#emailSpan").html("");
			return true;
		} else {
			$("#emailSpan").html("电子邮箱格式错误!");
			canSubmit = false;
			return false;
		}
	}

	function check() {
		result1 = emailCheck();
		if (!result1)
			return false; //阻止提交

		//符合条件往下执行修改密码操作：
		$.ajax({
			type : "POST",
			dataType : "json",
			url : '/news/servlet/UserServlet?type1=findPassword',
			data : $('#form1').serialize(),
			beforeSend : function() {
				// 禁用按钮防止重复提交
				$("#submit").attr({
					disabled : "disabled"
				});
			},
			success : function(data) {
				if (data != null) {
					if (data.result == 1 || data.result == -2) {
						alert(data.message);
						window.location.href = data.redirectUrl; //跳转网页
					} else {
						alert(data.message);
						location.reload();
					}
				}
			},
			error : function() {
				alert("找回密码失败！未能连接到服务器！");
				location.reload();
			}
		});

		return false; //阻止提交

	}

	function _key() {
		if (event.keyCode == 13) {
			$("#submit").click();
		}
	}
</script>
</head>
<body onkeydown="_key()">
	<form id="form1" action="" method="post" onsubmit="return false;">
		<div class="center" style="width:400px;margin-top:40px">
			<table border="0" align="center">
				<tbody>
					<tr height="30">
						<td width="120"></td>
						<td>找回密码</td>
					</tr>
					<tr height="30">
						<td align="right">找回密码方式：</td>
						<td><select name="type">
								<option value="email">电子邮件</option>
								<option value="phone">手机号</option>
						</select></td>
					</tr>
					<tr>
						<td align="right"><label for="email">电子邮箱：</label></td>
						<td><input name="email" type="text" id="email" accesskey="p"
							size="25" maxlength="30" onBlur="emailCheck()" /> <br> <span
							class="span1" id="emailSpan"></span></td>
					</tr>
					<tr height="30">
						<td></td>
						<td><button id="submit" type="button" onclick="check()">&nbsp;&nbsp;找&nbsp;回&nbsp;密
								&nbsp;码&nbsp;&nbsp;</button> <!-- <input id="button" type="submit"
							value="      找 回 密 码     " /> --></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>
</html>
