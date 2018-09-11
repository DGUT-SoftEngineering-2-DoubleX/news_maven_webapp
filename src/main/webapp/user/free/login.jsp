<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function valName() {
		var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", "i"); //创建模式对象
		var str1 = document.getElementById("name").value; //获取文本框的内容

		if (document.getElementById("name").value == null || document.getElementById("name").value == "") {
			document.getElementById("namespan").innerHTML = "*不能为空";
			return false;
		} else {
			document.getElementById("namespan").innerHTML = "";
			return true;
		}
	}

	function valPassword() {
		var str = document.getElementById("password").value;
		if (document.getElementById("password").value == null || document.getElementById("password").value == "") {
			document.getElementById("passwordspan").innerHTML = "*不能为空";
			return false;
		} else {
			document.getElementById("passwordspan").innerHTML = "";
			return true;
		}
	}

	function submit1() {
		result1 = valName() && valPassword();
		if (result1)
			return true; //提交
		else
			return false; //阻止提交
	}
</script>
</head>

<body>

	<form action="/news/servlet/UserServlet?type1=login" method="post"
		onsubmit="return submit1()">
		<div class="center" style="width:320px;margin-top:40px">
			<table height="121" border="0" align="center">
				<tbody>
					<tr height="30">
						<td width="80"></td>
						<td>登录</td>
					</tr>
					<tr height="30">
						<td align="right">用户名：</td>
						<td align="left"><input type="text" name="name" id="name"
							onBlur="valName()"><span id="namespan"></span></td>
					</tr>
					<tr height="30">
						<td align="right">密码：</td>
						<td align="left"><input type="password" name="password"
							id="password" onBlur="valPassword()"><span
							id="passwordspan"></span></td>
					</tr>
					<tr height="30">
						<td></td>
						<td><input type="submit" value="    登  录    " /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>

</body>
</html>
