<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>注册</title>
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

	//id:需验证的标签id，messageId显示验证信息的标签的id，patternString：验证模式，rightMessage：验证通过需显示的信息，errorMessage：验证失败需显示的信息
	function check(id, patternString, messageId, rightMessage, errorMessage) {
		var pattern = new RegExp(patternString, "i"); //创建模式对象
		var str1 = $("#" + id).val(); //获取文本框的内容

		if (str1.length >= 3 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#" + messageId).html(rightMessage); //设置id为userNameSpan的标签的内部内容为 ok
			return true;
		} else {
			$("#" + messageId).html(errorMessage);
			canSubmit = false;
			return false;
		}
	}

	function userNameCheck() {
		var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", "i"); //创建模式对象
		var str1 = $("#name").val(); //获取文本框的内容

		if (str1.length >= 3 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#nameSpan").html(""); //设置id为userNameSpan的标签的内部内容为 ok
			return true;
		} else {
			$("#nameSpan").html("用户名至少需要3个字符，必须以字母开头，以字母或数字结尾，可以有-和_");
			return false;
		}
	}

	function passwordCheck() {
		var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", "i"); //创建模式对象
		var str1 = $("#password").val(); //获取文本框的内容

		if (str1.length >= 8 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#passwordSpan").html("");
			return true;
		} else {
			$("#passwordSpan").html("密码至少需要8个字符，必须以字母开头，以字母或数字结尾，可以有-和_");
			return false;
		}
	}

	function emailCheck() {
		var pattern = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$", "i"); //创建模式对象
		var str1 = $("#email").val(); //获取文本框的内容

		if (str1.length >= 8 && pattern.test(str1)) { //pattern.test() 模式如果匹配，会返回true，不匹配返回false
			$("#emailSpan").html("");
			return true;
		} else {
			$("#emailSpan").html("电子邮箱格式错误!");
			return false;
		}
	}

	function codeCheck() {
		if ($("#checkCode").val() == "") {
			$("#checkCodeSpan").html("请填写验证码！");
			return false;
		} else {
			$("#checkCodeSpan").html("");
			return true;
		}
	}

	function checkAll() {
		result1 = userNameCheck();
		result1 = passwordCheck() & result1;
		result1 = emailCheck() & result1;
		result1 = codeCheck() & result1;
		if (!result1)
			return false; //阻止提交

		//符合条件往下执行修改密码操作：
		$.ajax({
			type : "POST",
			dataType : "json",
			url : '/news/servlet/UserServlet?type1=register',
			data : $('#form1').serialize(),
			beforeSend : function() {
				// 禁用按钮防止重复提交
				$("#submit").attr({
					disabled : "disabled"
				});
			},
			success : function(data) {
				if (data != null) {
					if (data.result == 1 || data.result == -10 || data.result == -11) {
						alert(data.message);
						window.location.href = data.redirectUrl; //跳转网页
					} else {
						alert(data.message);
						location.reload();
					}
				}
			},
			error : function() {
				alert("注册失败！未能连接到服务器！");
			}
		});

		return false; //阻止提交
	}

	$(document).ready(function() { //资源加载后才执行的 代码，就放到这个函数中，jquery能保证网页所有资源（html代码，图片，js文件，css文件等）都加载后，才执行此函数

		$("#name").blur(function() { //为id是userName的标签绑定  失去焦点事件  的处理函数
			check(userName, "^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", userNameSpan,
				"", "用户名至少需要3个字符，必须以字母开头，以字母或数字结尾，可以有-和_");
		});

		$("#password").blur(function() { //为id是password的标签绑定  失去焦点事件  的处理函数
			check(password, "^(\w){6,20}$", passwordSpan,
				"", "密码只能输入6-20个字母、数字、下划线");
		});

		$("#email").blur(function() { //为id是userName的标签绑定  失去焦点事件  的处理函数
			check(email, "", emailSpan,
				"", "电子邮箱格式错误!");
		});

		$("#checkCode").blur(function() { //为id是userName的标签绑定  失去焦点事件  的处理函数
			check(checkCode, "", checkCodeSpan,
				"", "请填写验证码！");
		});

		$("#checkImg").click(function() { //为id是checkImg的标签绑定  鼠标单击事件  的处理函数
			//$(selector).attr(attribute,value)  设置被选元素的属性值
			//网址后加如一个随机值rand，表示了不同的网址，防止缓存导致的图片内容不变
			$("#checkImg").attr("src", "/news/servlet/ImageCheckCodeServlet?rand=" + Math.random());
		});
	});

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
							onBlur="userNameCheck()"> <br> <span id="nameSpan"
							style="color: #E7060A;"></span></td>
					</tr>
					<tr height="30">
						<td align="right">密码：</td>
						<td align="left"><input type="password" name="password"
							id="password" onBlur="passwordCheck()"> <br> <span
							id="passwordSpan" style="color: #E7060A;"></span></td>
					</tr>
					<tr>
						<td align="right"><label for="email">电子邮箱：</label></td>
						<td><input name="email" type="text" id="email" accesskey="p"
							size="25" maxlength="30" onBlur="emailCheck()" /> <br> <span
							class="span1" id="emailSpan"></span></td>
					</tr>
					<tr>
						<td align="right">图形验证码：</td>
						<td valign="middle"><input type="text" name="checkCode"
							id="checkCode" onBlur="codeCheck()"> <br> <span
							class="span1" id="checkCodeSpan"></span><br> <img
							id="checkImg" src="/news/servlet/ImageCheckCodeServlet?rand=-1"
							class="hand" /></td>
					</tr>
					<tr height="30">
						<td></td>
						<!-- 将submit更改为button标签，若采用submit标签，会导致ajax刷新当前界面，
						使得服务器端的验证码内容进行更新，用户输入的验证码与更新后的验证码无法对应，
						造成无法登录的情况。采用button标签后，需要在body标签中添加enter键响应函数
						“_key()”，以此来模拟submit标签的特性 -->
						<td><button id="submit" type="button" onclick="checkAll()">&nbsp;&nbsp;注&nbsp;&nbsp;
								册&nbsp;&nbsp;</button> <!-- <input id="button" type="submit"
							value="      注     册     " /> --></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>
</html>
