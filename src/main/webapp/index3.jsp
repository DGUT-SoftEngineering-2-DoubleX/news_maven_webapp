<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/myTagLib" prefix="myTag"%>

<!doctype html>
<html>
<head></head>
<body>
	<div class="news">
		<div class="newsleft">
			<table border="0" width="%100">
				<tbody>
					<c:forEach items="${requestScope.newsesList[0]}" var="news">
						<tr>
							<td class="mainPageUl"><a
								href="/news/servlet/NewsServlet?type1=showANews&newsId=${news.newsId}&page=1&pageSize=5"
								title="${news.caption}"> <c:choose>
										<c:when
											test="${ fn:length(news.caption)>homePageNewsCaptionMaxLength}">  
								        ${ fn:substring(news.caption,0,homePageNewsCaptionMaxLength-1)}...
								    </c:when>
										<c:when
											test="${ fn:length(news.caption)<=homePageNewsCaptionMaxLength}"> 
										${ news.caption}
								    </c:when>
									</c:choose>
							</a></td>
							<td align="right" width="115"><myTag:LocalDateTimeShow
									dateTime="${news.newsTime}" type="YMD" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="newsRight">
			<table border="0" width="%100">
				<tbody>
					<c:forEach items="${requestScope.newsesList[1]}" var="news">
						<tr>
							<td class="mainPageUl"><a
								href="/news/servlet/NewsServlet?type1=showANews&newsId=${news.newsId}&page=1&pageSize=5"
								title="${news.caption}"> <c:choose>
										<c:when
											test="${ fn:length(news.caption)>homePageNewsCaptionMaxLength}">  
								        ${ fn:substring(news.caption,0,homePageNewsCaptionMaxLength-1)}...
								    </c:when>
										<c:when
											test="${ fn:length(news.caption)<=homePageNewsCaptionMaxLength}"> 
										${ news.caption}
								    </c:when>
									</c:choose>
							</a></td>
							<td align="right" width="115"><myTag:LocalDateTimeShow
									dateTime="${news.newsTime}" type="YMD" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="newsleft">
			<table border="0" width="%100">
				<tbody>
					<c:forEach items="${requestScope.newsesList[2]}" var="news">
						<tr>
							<td class="mainPageUl"><a
								href="/news/servlet/NewsServlet?type1=showANews&newsId=${news.newsId}&page=1&pageSize=5"
								title="${news.caption}"> <c:choose>
										<c:when
											test="${ fn:length(news.caption)>homePageNewsCaptionMaxLength}">  
								        ${ fn:substring(news.caption,0,homePageNewsCaptionMaxLength-1)}...
								    </c:when>
										<c:when
											test="${ fn:length(news.caption)<=homePageNewsCaptionMaxLength}"> 
										${ news.caption}
								    </c:when>
									</c:choose>
							</a></td>
							<td align="right" width="115"><myTag:LocalDateTimeShow
									dateTime="${news.newsTime}" type="YMD" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="newsRight">
			<table border="0" width="%100">
				<tbody>
					<c:forEach items="${requestScope.newsesList[3]}" var="news">
						<tr>
							<td class="mainPageUl"><a
								href="/news/servlet/NewsServlet?type1=showANews&newsId=${news.newsId}&page=1&pageSize=5"
								title="${news.caption}"> <c:choose>
										<c:when
											test="${ fn:length(news.caption)>homePageNewsCaptionMaxLength}">  
								        ${ fn:substring(news.caption,0,homePageNewsCaptionMaxLength-1)}...
								    </c:when>
										<c:when
											test="${ fn:length(news.caption)<=homePageNewsCaptionMaxLength}"> 
										${ news.caption}
								    </c:when>
									</c:choose>
							</a></td>
							<td align="right" width="115"><myTag:LocalDateTimeShow
									dateTime="${news.newsTime}" type="YMD" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
