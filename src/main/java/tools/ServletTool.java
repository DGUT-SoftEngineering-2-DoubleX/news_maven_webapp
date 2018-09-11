package tools;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import bean.News;

public class ServletTool {
	static public News news(HttpServletRequest request) {

		News news = new News();
		String newsId = request.getParameter("newsId");
		if (newsId != null && !newsId.isEmpty())
			news.setNewsId(Integer.parseInt(request.getParameter("newsId")));

		news.setCaption(request.getParameter("caption"));
		news.setAuthor(request.getParameter("author"));
		news.setNewsType(request.getParameter("newsType"));
		// uedit上传的内容的数据名称是：editorValue
		news.setContent(request.getParameter("editorValue"));
		String a = request.getParameter("newsTime");
		// DateTimeFormatter用于将字符串解析成LocalDateTime类型的对象，或者反之
		LocalDateTime localDateTime = LocalDateTime.parse(a, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		news.setNewsTime(localDateTime);

		return news;
	}
}
