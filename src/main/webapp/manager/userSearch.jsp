<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
</head>
<body>
	<h2>查询用户</h2>
	<form action="/news/servlet/UserServlet?type1=search&page=1&pageSize=5"
		method="post">
		<table width="500" border="0" align="center">
			<tbody>
				<tr height="40">
					<td></td>
					<td>查询条件</td>
				</tr>
				<tr>
					<td align="right">用户类型：</td>
					<td><select name="type">
							<option value="all">全部</option>
							<option value="user">用户</option>
							<option value="newsAuthor">新闻发布员</option>
							<option value="manager">管理员</option>
					</select></td>
				</tr>
				<tr height="40">
					<td width="100" align="right">用户名：</td>
					<td align="left"><input type="text" name="name" id="name"></td>
				</tr>
				<tr height="40">
					<td align="right">帐号可用性：</td>
					<td align="left"><select name="enable" id="select">
							<option value="all">全部</option>
							<option value="use">可用</option>
							<option value="stop">停用</option>
					</select></td>
				</tr>
				<tr height="40">
					<td align="right">注册日期：</td>
					<td align="left">介于<input type="date" name="lowDate">与<input
						type="date" name="upDate">之间
					</td>
				</tr>
				<tr height="40">
					<td></td>
					<td><input type="submit" value="  开 始 查 询   " /></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>
