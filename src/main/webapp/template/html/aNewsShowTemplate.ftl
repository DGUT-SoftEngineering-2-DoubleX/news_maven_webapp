<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  	<link href="/news/css/newsCSS.css" rel="stylesheet" type="text/css">	
  	<script type="text/javascript" src="/news/js/jquery/jquery-2.1.4.min.js"></script>
	<script type="text/javascript">
       function getOnePage(type) {
		var page = $("input[name='page']");
		var pageValue = parseInt(page.val());
		if (type == "pre") {
			pageValue -= 1;
			page.val(pageValue.toString());
		} else if (type == "next") {
			pageValue += 1;
			page.val(pageValue.toString());
		}
		//提交
		$("#myform").submit();
	}

	function praise(commentId, newsId) {
		$("#myform").attr("action", "/news/servlet/CommentServlet?type=praise&"
			+ "&commentId=" + commentId + "&newsId=" + newsId);
		$("#myform").submit();
	}

	function reviseComment(commentId) {
		$("#myModel").html("<form action='/news/servlet/CommentServlet?type=reviseCommnet&' method='post'>" +
			"<div class='modelContent'>" +
			"<h2>修改评论页面</h2>" +
			"<table><tbody><tr><td colspan='2'>" +
			"<textarea name='content' cols='60' rows='8' id='textarea' style='resize: none;' required></textarea></td>" +
			"</tr><tr>" +
			"<td align='center'><input type='submit' name='submit' id='submit' value='提交评论'></td>" +
			"<td align='center'><input type='submit' onclick='cancel();' value='取消'></td>" +
			"</tr></tbody></table>" +
			"</div>" +
			"<input type='hidden' name='newsId' id='newsId' value='${news.newsId}'>" +
			"<input type='hidden' name='page' id='page' value='${pageInformation.page}'>" +
			"<input type='hidden' name='pageSize' id='pageSize' value='${pageInformation.pageSize}'>" +
			"<input type='hidden' name='totalPageCount' id='totalPageCount' value='${pageInformation.totalPageCount}'>" +
			"<input type='hidden' name='commentId' id='commentId'>" +
			"</form>");
		$("#myModel").html("<form action='/news/servlet/CommentServlet?type=reviseCommnet&' method='post'>" +
			"<div class='modelContent'>" +
			"<h2>修改评论页面</h2>" +
			"<table><tbody><tr><td colspan='2'>" +
			"<textarea name='content' cols='60' rows='8' id='textarea' style='resize: none;' required></textarea></td>" +
			"</tr><tr>" +
			"<td align='center'><input type='submit' name='submit' id='submit' value='提交评论'></td>" +
			"<td align='center'><input type='submit' onclick='cancel();' value='取消'></td>" +
			"</tr></tbody></table>" +
			"</div>" +
			"<input type='hidden' name='newsId' id='newsId' value='${news.newsId}'>" +
			"<input type='hidden' name='page' id='page' value='${pageInformation.page}'>" +
			"<input type='hidden' name='pageSize' id='pageSize' value='${pageInformation.pageSize}'>" +
			"<input type='hidden' name='totalPageCount' id='totalPageCount' value='${pageInformation.totalPageCount}'>" +
			"<input type='hidden' name='commentId' id='commentId'>" +
			"</form>");
		$("#commentId").val(commentId);
		$("#myModel").css("display", "block");
	}

	function model(commentId) {
		$("#myModel").html("<form action='/news/servlet/CommentServlet?type=addCommnet' method='post'>" +
			"<div class='modelContent'>" +
			"<h2>评论页面</h2>" +
			"<table><tbody><tr><td colspan='2'>" +
			"<textarea name='content' cols='60' rows='8' id='textarea' required></textarea></td>" +
			"</tr><tr>" +
			"<td align='center'><input type='submit' name='submit' id='submit' value='提交评论'></td>" +
			"<td align='center'><input type='submit' onclick='cancel();' value='取消'></td>" +
			"</tr></tbody></table>" +
			"</div>" +
			"<input type='hidden' name='newsId' id='newsId' value='${news.newsId}'>" +
			"<input type='hidden' name='page' id='page' value='${pageInformation.page}'>" +
			"<input type='hidden' name='pageSize' id='pageSize' value='${pageInformation.pageSize}'>" +
			"<input type='hidden' name='totalPageCount' id='totalPageCount' value='${pageInformation.totalPageCount}'>" +
			"<input type='hidden' name='commentId' id='commentId'>" +
			"</form>");
		$("#commentId").val(commentId);
		$("#myModel").css("display", "block");
	}

	function cancel() {
		$("#myModel").html("");
		$("#myModel").css("display", "none");
	}		
	</script>  	
  </head>
  <body>
  	<!--新闻内容 -->
	<div class="center" style="width:800px;margin-top:30px;">
		<table width="800" border="0">
			<tbody>
				<tr>
					<td class="newsCaption">${news.caption}</td>
				<tr>
					<td align="center" height="50">作者：${news.author}&nbsp;
						&nbsp;&nbsp; &nbsp;&nbsp; &nbsp; ${news.newsTime}</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
				<tr>
					<td>${news.content}</td>
				</tr>
				<tr>
					<td height="30"><hr></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!--回复评论 -->
	 <div class="center" style="width:800px;margin-top:30px;">
		<form action="/news/servlet/CommentServlet?type=addCommnet"
			method="post">
			<div class="center" style="width:500px">
				<textarea name="content" cols="72" rows="8" id="textarea"
					style="resize: none;" required></textarea>
			</div>
			<p>
			<div class="center" style="width:50px;height:80px;">
				<br><input type="submit" name="submit" id="submit" value="  提 交 评 论  ">
			</div>
			<input type="hidden" name="newsId" id="newsId"
				value="${news.newsId}"> <input type="hidden" name="page"
				id="page" value="${pageInformation.page}"> <input type="hidden"
				name="pageSize" id="pageSize" value="${pageInformation.pageSize}">
		</form>
	</div>
	 
	 
	 <!--对回复的回复 -->
	<form
		action="/news/servlet/NewsServlet?type1=showANews&newsId=${news.newsId}"
		id="myform" method="post">
		<div class="center" style="width:600px">
			<a name="commentsStart"></a>
			<div class="commentsHead">最新评论</div>
			<#list commentUserViews as commentUserView>
				<div style="margin-bottom: 10px;;">
					<div>
						<div class="commentIcon">
							<img width="35" src="${commentUserView.headIconUrl}">
						</div>
						<div class="comment1">
							<div class="commentAuthor">${commentUserView.userName}</div>
								<div class="commentTime">
									<myTag:TimestampTag dateTime="${commentUserView.time}"
										type="latest" />
								</div>
							</div>
							<div class="comment2">
								<div class="comment3">
									<a class="commentReplay" href="javascript:void(0);"
										onclick="model(${commentUserView.commentId});"> 回复 </a>
								</div>
								<div class="comment4">
									<span class="commentPraiseText">第${commentUserView.stair}楼</span>
									<a class="commentPraiseA" href="javascript:void(0);"
										onclick="praise(${commentUserView.commentId},${news.newsId});">
									</a><span class="commentPraiseText">${commentUserView.praise}</span>
								</div>
							</div>
						</div>
						<div class="clear">
							<div class="comment5 ">
								<div class="commentContent">${commentUserView.content}</div>
							</div>
						</div>
					</div>
				 </#list>
				<div class="commentsHead" style="text-align:center;">
					<#if (pageInformation.page > 1) >
						<td><a href="javascript:void(0);"
							onclick="getOnePage('pre');">上一页</a></td>
					</#if>
					<#if (pageInformation.page > 1 && pageInformation.page < pageInformation.totalPageCount) >
						<span>&nbsp;&nbsp;||&nbsp;&nbsp;</span>
					</#if>
					<#if (pageInformation.page < pageInformation.totalPageCount) >
						<td><a href="javascript:void(0);"
							onclick="getOnePage('next');">下一页</a></td>
					</#if>
				</div>
			<#if (commentUserViewsSize == 0) >
				<br>
				<br>
				<h2 align="center">当前还没有用户评论哦，赶快来抢沙发吧！</h2>
				<br>
				<br>
			</#if>
		</div>

		<input type="hidden" class="page" name="page" id="page"
			value="${pageInformation.page}"> <input
			type="hidden" class="pageSize" name="pageSize" id="pageSize"
			value="${pageInformation.pageSize}">

	</form>

	<div id="myModel" class="model"></div>
  </body>
</html>
