<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<script type="text/javascript">
	function valName() {
		var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", "i"); //创建模式对象
		var str1 = document.getElementById("name").value; //获取文本框的内容

		if (str1 == null || str1 == "") {
			document.getElementById("namespan").innerHTML = "*不能为空";
			return false;
		} else if (str1.length >= 8 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			document.getElementById("namespan").innerHTML = "ok";
			return true;
		} else {
			document.getElementById("namespan").innerHTML = "*至少需要8个字符，以字母开头，以字母或数字结尾，可以有-和_";
			return false;
		}
	}

	function valPassword() {
		var str = document.getElementById("password").value;
		var pattern = /^(\w){6,20}$/;

		if (document.getElementById("password").value == null || document.getElementById("password").value == "") {
			document.getElementById("passwordspan").innerHTML = "*不能为空";
			return false;
		} else if (str.match(pattern) == null) {
			document.getElementById("passwordspan").innerHTML = "*只能输入6-20个字母、数字、下划线";
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

	function submit1() {
		result1 = valName();
		result1 = valPassword() && result1;
		result1 = passwordSame() && result1;
		if (result1)
			return true; //提交
		else
			return false; //阻止提交
	}
</script>
</head>

<body>

	<form action="/news/servlet/UserServlet?type1=register" method="post"
		onsubmit="return submit1()">
		<div class="center" style="width:400px;margin-top:40px">
			<table border="0" align="center">
				<tbody>
					<tr height="30">
						<td width="120"></td>
						<td>注册</td>
					</tr>
					<tr height="30">
						<td align="right">用户类型：</td>
						<td><select name="type">
								<option value="user">普通用户</option>
								<option value="newsAuthor">新闻发布员</option>
								<option value="manager">管理员</option>
						</select></td>
					</tr>
					<tr height="30">
						<td align="right">用户名：</td>
						<td align="left"><input type="text" name="name" id="name"
							onBlur="valName()"> <br> <span id="namespan"
							style="color: #E7060A;"></span></td>
					</tr>
					<tr height="30">
						<td align="right">密码：</td>
						<td align="left"><input type="password" name="password"
							id="password" onBlur="valPassword()"> <br> <span
							id="passwordspan" style="color: #E7060A;"></span></td>
					</tr>
					<tr height="30">
						<td align="right">重新输入密码：</td>
						<td align="left"><input type="password" name="password2"
							id="password2" onBlur="passwordSame()"> <br> <span
							id="passwordspan2" style="color: #E7060A;"></span></td>
					</tr>
					<tr height="30">
						<td></td>
						<td><input type="submit" value="      注     册     " /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>
</html>
