<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
</head>
<body>
	<form id="myform" action="/news/servlet/UserServlet">
		<table width="700px" border="1" align="center">
			<tbody>
				<tr>
					<td><input type="radio" name="qqType" value="bindOldUser">绑定已有用户</td>
					<td><input type="text" name="name">请输入绑定的用户名</td>
				</tr>
				<tr>
					<td><input type="radio" name="qqType" value="bindNewUser">直接登录（系统自动创建用户并绑定）</td>
					<td><input type="hidden" name="type1" value="qqBindUser"></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="绑定"></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>

