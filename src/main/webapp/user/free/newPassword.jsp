<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>输入新密码</title>
<meta name="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
<style type="text/css">
.span1 {
	color: #F00;
}

.hand {
	cursor: pointer;
	//
	鼠标变成手的形状
}
</style>
<script type="text/javascript">
	/*
	function valPassword() {
		var str = document.getElementById("password").value;
		var pattern = /^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$/;

		if (document.getElementById("password").value == null || document.getElementById("password").value == "") {
			document.getElementById("passwordspan").innerHTML = "*不能为空";
			return false;
		} else if (str.match(pattern) == null) {
			document.getElementById("passwordspan").innerHTML = "*密码至少需要8个字符，必须以字母开头，以字母或数字结尾，可以有-和_";
			return false;
		} else {
			document.getElementById("passwordspan").innerHTML = "ok";
			return true;
		}
	}

	function passwordSame() {
		var str = document.getElementById("password").value;
		if (document.getElementById("password2").value == null || document.getElementById("password2").value == "") {
			document.getElementById("passwordspan2").innerHTML = "*不能为空";
			return false;
		} else if (document.getElementById("password").value == document.getElementById("password2").value) {
			document.getElementById("passwordspan2").innerHTML = "ok";
			return true;
		} else {
			document.getElementById("passwordspan2").innerHTML = "*两次密码不一样";
			return false;
		}
	}
	*/
	function valPassword() {
		var str = $("#password").val();
		var pattern = /^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$/;

		if ($("#password").val() == null || $("#password").val() == "") {
			$("#passwordspan").html("*不能为空");
			return false;
		} else if (str.match(pattern) == null) {
			$("#passwordspan").html("*密码至少需要8个字符，必须以字母开头，以字母或数字结尾，可以有-和_");
			return false;
		} else {
			$("#passwordspan").html("");
			return true;
		}
	}

	function passwordSame() {
		var str = $("#password").val();
		if ($("#password2").val() == null || $("#password2").val() == "") {
			$("#passwordspan2").html("*不能为空");
			return false;
		} else if ($("#password").val() == $("#password2").val()) {
			$("#passwordspan2").html("");
			return true;
		} else {
			$("#passwordspan2").html("*两次密码不一样");
			return false;
		}
	}
	function check() {
		result1 = valPassword();
		result1 = passwordSame() && result1;
		if (!result1)
			return false; //阻止提交

		//符合条件往下执行修改密码操作：
		$.ajax({
			type : "POST",
			dataType : "json",
			url : '/news/servlet/UserServlet?type1=newPassword',
			data : $('#form1').serialize(),
			beforeSend : function() {
				// 禁用按钮防止重复提交
				$("#submit").attr({
					disabled : "disabled"
				});
			},
			success : function(data) {
				if (data != null) {
					alert(data.message);
					window.location.href = data.redirectUrl; //跳转网页
				}
			},
			error : function() {
				alert("设置新密码失败！未能连接到服务器！");
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
		<div class="center" style="width:450px;margin-top:40px">
			<table border="0" align="center">
				<tbody>
					<tr height="30">
						<td width="150"></td>
						<td width="300">输入新密码</td>
					</tr>
					<tr height="30">
						<td align="right">新密码：</td>
						<td align="left"><input type="password" name="password"
							id="password" onBlur="valPassword()"> <br> <span
							id="passwordspan" style="color: #E7060A;"></span></td>
					</tr>
					<tr height="30">
						<td align="right">重复一遍新密码：</td>
						<td align="left"><input type="password" name="password2"
							id="password2" onBlur="passwordSame()"> <br> <span
							id="passwordspan2" style="color: #E7060A;"></span></td>
					</tr>
					<tr height="30">
						<td></td>
						<td><button id="submit" type="button" onclick="check()">&nbsp;&nbsp;修&nbsp;改&nbsp;密
								&nbsp;码&nbsp;&nbsp;</button> <!--  <input type="submit" value=" 修 改 密 码 " /> --></td>
					</tr>
				</tbody>
			</table>
		</div>
		<input type="hidden" name="rand" id="rand" value="${param.rand}">
	</form>
</body>
</html>
