<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="http://connect.qq.com/qc_jssdk.js" data-appid="101504585"
	data-redirecturi="http://220a27a0.nat123.cc:19537/news/user/free/qqLoginCallback.jsp"></script>
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
	*/
	function valName() {
		var pattern = new RegExp("^[a-z]([a-z0-9])*[-_]?([a-z0-9]+)$", "i"); //创建模式对象
		var str1 = $("#name").val(); //获取文本框的内容

		if ($("#name").val() == null || $("#name").val() == "") {
			$("#namespan").html("*不能为空");
			return false;
		} else {
			$("#namespan").html("");
			return true;
		}
	}
	function valPassword() {
		var str = $("#password").val();
		if ($("#password").val() == null || $("#password").val() == "") {
			$("#passwordspan").html("*不能为空");
			return false;
		} else {
			$("#passwordspan").html("");
			return true;
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

	function check() {
		result1 = valName() & valPassword();
		result1 = codeCheck() & result1;
		if (!result1)
			return false; //如果为假，表示不符合要求，提交阻止

		//符合条件往下执行修改密码操作：
		$.ajax({
			type : "POST",
			dataType : "json",
			url : '/news/servlet/UserServlet?type1=login',
			data : $('#myform').serialize(),
			success : function(data) {
				if (data != null) {
					if (data.result == 1) {
						window.location.href = data.redirectUrl; //跳转网页
					} else {
						alert(data.message);
						location.reload();
					}
				}
			},
			error : function() {
				alert("登录失败！未能连接到服务器！");
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

	$(document).ready(function() { //资源加载后才执行的 代码，就放到这个函数中，jquery能保证网页所有资源（html代码，图片，js文件，css文件等）都加载后，才执行此函数
		$("#checkImg").click(function() { //为id是checkImg的标签绑定  鼠标单击事件  的处理函数
			//$(selector).attr(attribute,value)  设置被选元素的属性值
			//网址后加如一个随机值rand，表示了不同的网址，防止缓存导致的图片内容不变
			$("#checkImg").attr("src", "/news/servlet/ImageCheckCodeServlet?rand=" + Math.random());
		});
	});
</script>
</head>

<body onkeydown="_key()">

	<form action="" method="post" id="myform" onsubmit="return false;">
		<div class="center" style="width:390px;margin-top:40px">
			<table height="121" border="0" align="center">
				<tbody>
					<tr height="30">
						<td width="130"></td>
						<td width="280">登录</td>
					</tr>
					<tr height="30">
						<td align="right">用户名：</td>
						<td align="left"><input type="text" name="name" id="name"
							onBlur="valName()"><span class="span1" id="namespan"></span></td>
					</tr>
					<tr height="30">
						<td align="right">密码：</td>
						<td align="left"><input type="password" name="password"
							id="password" onBlur="valPassword()"><span class="span1"
							id="passwordspan"></span></td>
					</tr>
					<tr>
						<td align="right">图形验证码：</td>
						<td valign="middle"><input type="text" name="checkCode"
							id="checkCode" onBlur="codeCheck()"><br> <span
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
						<td><button id="submit" type="button" onclick="check()">&nbsp;&nbsp;登&nbsp;&nbsp;
								录&nbsp;&nbsp;</button> <a href="/news/user/free/findPassword.jsp">&nbsp;&nbsp;<input
								type="button" value=" 找 回 密 码 "></a></td>
					</tr>
					<tr height="30">
						<td>其它账号登录：</td>
						<td><span id="qqLoginBtn"></span> <script
								type="text/javascript">
							QC.Login({
								btnId : "qqLoginBtn", //插入按钮的节点id
								showModal : true
							});
						</script></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>

</body>
</html>
