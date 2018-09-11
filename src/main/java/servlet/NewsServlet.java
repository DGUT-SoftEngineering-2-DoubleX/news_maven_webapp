package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.News;
import bean.News;
import bean.News;
import bean.NewsType;
import bean.User;
import dao.DatabaseDao;
import service.NewsService;
import tools.Message;
import tools.PageInformation;
import tools.ServletTool;
import tools.Tool;
import tools.WebProperties;

public class NewsServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type1");
		NewsService newsService = new NewsService();
		Message message = new Message();
		if (type.equals("add")) {
			News news = ServletTool.news(request);

			User user = (User) request.getSession().getAttribute("user");
			news.setPublisherId(user.getUserId());

			int result = newsService.add(news);
			message.setResult(result);
			if (result == 1) {
				message.setMessage("添加新闻成功！请添加新的新闻！");
				message.setRedirectUrl("/news/news/manage/addNews.jsp");
			} else if (result == 0) {
				message.setMessage("添加新闻失败！请联系管理员！");
				message.setRedirectUrl("/news/index.jsp");
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);

		} else if (type.equals("showNews")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			List<News> newss = newsService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newss);
			getServletContext().getRequestDispatcher("/news/newsShow.jsp").forward(request, response);
		} else if (type.equals("showANews")) {
			Integer newsId = Integer.parseInt(request.getParameter("newsId"));
			News news = newsService.getNewsById(newsId);
			request.setAttribute("news", news);
			getServletContext().getRequestDispatcher("/news/aNewsShow.jsp").forward(request, response);
		} else if (type.equals("deleteANews") || type.equals("manageNews")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			List<News> newses = null;

			if (type.equals("manageNews")) {
				User user = (User) request.getSession().getAttribute("user");
				if ("manager".equals(user.getType()))
					newses = newsService.getOnePage(pageInformation);
				if ("newsAuthor".equals(user.getType()))
					newses = newsService.getOnePageByPublisherId(user.getUserId(), pageInformation);
			} else if ("deleteANews".equals(type))
				newses = newsService.deletes(pageInformation);

			/*
			 * if (type.equals("manageNews")) newses =
			 * newsService.getOnePage(pageInformation); else if
			 * (type.equals("deleteANews")) newses =
			 * newsService.deletes(pageInformation);
			 */

			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newses);
			getServletContext().getRequestDispatcher("/news/manage/manageNews.jsp").forward(request, response);
		} else if (type.equals("editANews")) {// 显示编辑页面
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			Integer newsId = Integer.parseInt(pageInformation.getIds());
			News news = newsService.getNewsById(newsId);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("news", news);
			getServletContext().getRequestDispatcher("/news/manage/editANews.jsp").forward(request, response);
		} else if (type.equals("edit")) {// 修改新闻
			News news = ServletTool.news(request);
			User user = (User) request.getSession().getAttribute("user");
			news.setPublisherId(user.getUserId());
			int result = newsService.update(news);
			message.setResult(result);
			if (result == 1) {
				message.setMessage("修改新闻成功！请添加新的新闻！");
			} else if (result == 0) {
				message.setMessage("修改新闻失败！请联系管理员！");
			}
			message.setRedirectTime(1000);
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if (type.equals("homepageTypes1")) {// 主页多个分类新闻区
			String newsTypesString = new String(WebProperties.config.getString("newsTypes").getBytes("ISO-8859-1"),
					"UTF-8");
			String[] newsTypes = newsTypesString.split(",");
			Integer homePageNewsN = Integer.parseInt(WebProperties.config.getString("homePageNewsN"));
			List<List<News>> newsesList = newsService.getByTypesTopN(newsTypes, homePageNewsN);
			request.setAttribute("newsTypes", newsTypes);
			request.setAttribute("newsesList", newsesList);
			request.setAttribute("homePageNewsCaptionMaxLength",
					Integer.parseInt(WebProperties.config.getString("homePageNewsCaptionMaxLength")));
			getServletContext().getRequestDispatcher("/index2.jsp").include(request, response);
			return;
		} else if (type.equals("homepageTypes")) {// 主页多个分类新闻区
			String newsTypesString = new String(WebProperties.config.getString("newsTypes").getBytes("ISO-8859-1"),
					"UTF-8");
			String[] newsTypes = newsTypesString.split(",");
			Integer homePageNewsN = Integer.parseInt(WebProperties.config.getString("homePageNewsN"));
			List<List<String>> newsCaptionsList = new ArrayList<List<String>>();
			List<List<News>> newsesList = newsService.getByTypesTopN1(newsTypes, homePageNewsN, newsCaptionsList);
			int newsTypesNumber = newsTypes.length;
			request.setAttribute("newsTypes", newsTypes);
			request.setAttribute("newsTypesNumber", newsTypesNumber);
			request.setAttribute("newsesList", newsesList);
			request.setAttribute("newsCaptionsList", newsCaptionsList);
			getServletContext().getRequestDispatcher("/index2.jsp").include(request, response);
			return;
		} else if (type.equals("showNewsByNewsType")) {// 主页多个分类新闻区
			List<NewsType> newsTypes = (List<NewsType>) this.getServletContext().getAttribute("newsTypes");
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("news", request, pageInformation);
			String newsType = request.getParameter("newsType");

			if (!("all").equals(newsType))
				pageInformation.setSearchSql(" newsType='" + newsType + "' ");

			List<News> newss = newsService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("newses", newss);
			request.setAttribute("newsTypes", newsTypes);
			request.setAttribute("newsType", newsType);
			getServletContext().getRequestDispatcher("/news/newsShowByType.jsp").forward(request, response);
			return;
		} else if (type.equals("passCheck")) {
			Integer newsId = Integer.parseInt(request.getParameter("newsId"));
			News news = newsService.getNewsById(newsId);
			int result = newsService.passCheck(news);
			message.setResult(result);
			if (result == 1) {
				message.setMessage("新闻审核已通过！");
			} else if (result == 0) {
				message.setMessage("新闻审核失败！请检查后再操作！");
			}
			message.setRedirectTime(1000);
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

}
