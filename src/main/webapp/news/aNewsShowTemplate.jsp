<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="tools.WebProperties" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="/myTagLib" prefix="myTag"%>
<!doctype html>
<html>
  <head>
  	<link href="/news2/css/1.css" rel="stylesheet" type="text/css">	
  	<script type="text/javascript" src="/news2/plugin/jquery/jquery-3.2.1.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){	
			$("#addCommentForm").on("submit", function(ev) {
				var url="/news2/servlet/CommentServlet?type=addComment"+
						"&newsId="+$("#newsId").val()+"&pageSize="+$("#pageSize").val()
						+"&page=1";
			
				//jQuery.post(url, [data], [callback], [type])
				//callback:发送成功时回调函数。
				//type:返回内容格式，xml, html, script, json, text, _default。
				$.post(url,	$("#addCommentForm").serialize(),function(data,textStatus){
						if (textStatus=="success"){	
							$("#showComment").html(data);
							$("#myTextarea11").val("");//清空内容
						}else
					      	alert("添加失败！");
 				}, "html");	
 				ev.preventDefault();	
			});	
		});	
		
       function getOnePage(type){	
    	  	var pageValue=parseInt( $("#page1").val() );
   	  	
    	  	if(type=="pre"){
    	  		pageValue-=1;
    	  	}else if(type=="next"){
    	  		pageValue+=1;   	  		
    	  	}
    	  	
    	  	var url="/news2/servlet/CommentServlet?type=showComment"
    	  			+"&newsId="+$("#newsId").val()+"&pageSize="+$("#pageSize").val()
					+"&page="+pageValue;
					
			$("#showComment").load(url);
      	}
      	
      	function praise(element, commentId,newsId){
			$.ajax({
 		        type: "post",
		        dataType: "json",
		        url: "/news2/servlet/CommentServlet?type=praise&"+"&commentId="+commentId+"&newsId="+newsId,
		        data: $("#showCommnentForm").serialize(),
		        success: function (data) {
		            if (data != null) {
		            	if(data.result>0){
		            		$(element).next().html(data.result); 
		            	}else//登录失败		                
							alert("点赞失败！");
		            }
		        },
		        error: function() { 
			        alert("未能连接到服务器!");   
		        }               
            }); 
      	}
      	
      	function model(commentId){
      		$("#myModel").show();
      		$("#replyCommentForm").on("submit", function(ev) {
      			var url="/news2/servlet/CommentServlet?type=addComment"+
      					"&newsId="+$("#newsId").val()+"&pageSize="+$("#pageSize").val()
						+"&page=1"+"&commentId="+commentId;			
      			
				$("#showComment").load(url,$("#replyCommentForm").serialize());	
 				ev.preventDefault();	
			});	
      	}		
	</script>  	
  </head>
  <body>
  	<!--新闻内容 -->
  	<div class="center" style="width:800px;margin-top:30px;">
		<table width="800" border="0">
			<tbody>
				<tr><td class="newsCaption">${requestScope.news.caption}</td>
				<tr><td align="center" height="50">作者：${requestScope.news.author}&nbsp;	&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
						<myTag:LocalDateTimeTag dateTime="${requestScope.news.newsTime}" type="YMDHM"/></td>
				</tr><tr><td height="30"><hr></td></tr>
				<tr>
					<td>${requestScope.news.content}</td>
				</tr>
				<tr><td height="30"><hr></td></tr>
			</tbody>
		</table>
		<input type="hidden" name="newsId" id="newsId" value="${param.newsId}">
		<input type="hidden" name="pageSize" id="pageSize" value="${param.pageSize}">
	</div>
	
	<!--回复评论 -->
	<div id="addComment">
	    <div class="center" style="width:800px;margin-top:30px;">
		  	<form id="addCommentForm">
		  		<div class="center" style="width:500px">
					<textarea name="content" cols="72" rows="8" id="myTextarea11" required></textarea>
				</div><p>
				<div class="center" style="width:50px;height:80px;">
					<br><input type="submit" value="  提 交 评 论  ">
				</div>		
				<input type="hidden" name="newsId" id="newsId" value="${param.newsId}">
			 	<input type="hidden" name="page" id="page" value="${param.page}">
				<input type="hidden" name="pageSize" id="pageSize" value="${param.pageSize}">
			</form> 
		 </div>
	</div>		 
	 
	 <!--对回复的回复 -->
	<div id="showComment"> 
		<form id="showCommnentForm">
			<div class="center" style="width:600px">
			  <a name="commentsStart"></a>
			  <div class="commentsHead">最新评论</div>
			  <c:forEach items="${requestScope.commentUserViews}"  var="commentUserView">	
				<div style="margin-bottom: 10px;;">
					<div>
						<div class="commentIcon">
							<img width="35" src="${commentUserView.headIconUrl}">
						</div>
						<div class="comment1">
							<div class="commentAuthor">${commentUserView.userName}</div>
							<div class="commentTime">
								<myTag:TimestampTag dateTime="${commentUserView.time}" type="latest"/>
							</div>
						</div>
						<div class="comment2">
							<div class="comment3">
								<a class="commentReplay" href="javascript:void(0);" onclick="model(${commentUserView.commentId});">
									回复
								</a>
							</div>			
							<div class="comment4">
								<span class="commentPraiseText">第${commentUserView.stair}楼</span>
								<a class="commentPraiseA" href="javascript:void(0);" onclick="praise(this,${commentUserView.commentId},${param.newsId});">							
								</a><span class="commentPraiseText">${commentUserView.praise}</span>
							</div>			
						</div>
					</div>	
					<div class="clear">
						<div class="comment5 ">
							<div class="commentContent">
								${commentUserView.content}
							</div>
						</div>
					</div>
				</div>
			  </c:forEach>
			  <div class="commentsHead" style="text-align:center;">
				<c:if test="${requestScope.pageInformation.page > 1}">
					<td><a href="javascript:void(0);" onclick="getOnePage('pre');">上一页</a></td>  
				</c:if>
				<c:if test="${requestScope.pageInformation.page < requestScope.pageInformation.totalPageCount}">
					<td><a href="javascript:void(0);" onclick="getOnePage('next');">下一页</a></td>
				</c:if> 		  
			  </div>		  
			</div>
		 	<input type="hidden" class="page" name="page" id="page1" value="${requestScope.pageInformation.page}">
		</form>
		
	    <div id="myModel" class="model">
			<form id="replyCommentForm"> 
				<div class='modelContent'>  
					<table><tbody><tr><td colspan='2'> 
							<textarea name='content' cols='60' rows='8' id='textarea' required></textarea></td>
						</tr><tr>
							<td align='center'><input type='submit'  value='提交'></td>
							<td align='center'><input type='button' onclick='$("#myModel").hide();' value='取消'></td>
						</tr></tbody></table>
				</div>
			</form>   
	    </div>
  </body>
</html>
