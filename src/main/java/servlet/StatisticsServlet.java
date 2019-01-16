package servlet;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.Statistics;
import service.CommentService;
import service.NewsService;
import service.StatisticsService;
import tools.Message;
import tools.Tool;

public class StatisticsServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StatisticsService statisticsService = new StatisticsService();
		String condition = request.getParameter("condition");
		String type = request.getParameter("type");
		Message message = new Message();

		if (type != null && type != "") {
			if ("articleNumberByMonthInAYear".equals(type)) {
				NewsService newsService = new NewsService();
				String result = newsService.articleNumberByMonthInAYear(request.getParameter("year"), request);

				if (result.startsWith("-")) {
					message.setResult(-1);
					message.setMessage("操作失败！");
				} else {
					message.setResult(1);
					message.setMessage("操作成功！请下载以下链接的excel文件。");
					message.setRedirectUrl(result);
				}
				Gson gson = new Gson();
				String jsonString = gson.toJson(message);
				Tool.returnJsonString(response, jsonString);
			} else if ("articleNumberByMonthInAYearEveryYear".equals(type)) {
				NewsService newsService = new NewsService();
				String result = newsService.articleNumberByMonthInAYearEveryYear(request);

				if (result.startsWith("-")) {
					message.setResult(-1);
					message.setMessage("操作失败！");
				} else {
					message.setResult(1);
					message.setMessage("操作成功！请下载以下链接的excel文件。");
					message.setRedirectUrl(result);
				}
				Gson gson = new Gson();
				String jsonString = gson.toJson(message);
				Tool.returnJsonString(response, jsonString);
			} else if ("commentNumberTopTenInAYear".equals(type)) {
				CommentService commentService = new CommentService();
				String result = commentService.commentNumberByMonthInAYear(request.getParameter("year"), request);

				if (result.startsWith("-")) {
					message.setResult(-1);
					message.setMessage("操作失败！");
				} else {
					message.setResult(1);
					message.setMessage("操作成功！请下载以下链接的excel文件。");
					message.setRedirectUrl(result);
				}
				Gson gson = new Gson();
				String jsonString = gson.toJson(message);
				Tool.returnJsonString(response, jsonString);
			} else if ("commentNumberTopTenInAYearEveryYear".equals(type)) {
				CommentService commentService = new CommentService();
				String result = commentService.commentNumberTopTenInAYearEveryYear(request);

				if (result.startsWith("-")) {
					message.setResult(-1);
					message.setMessage("操作失败！");
				} else {
					message.setResult(1);
					message.setMessage("操作成功！请下载以下链接的excel文件。");
					message.setRedirectUrl(result);
				}
				Gson gson = new Gson();
				String jsonString = gson.toJson(message);
				Tool.returnJsonString(response, jsonString);
			}

			else if ("statistic".equals(type)) {
				if (condition != null && condition != "") {
					if ("lastYearEachMonth".equals(condition)) {
						Queue<Statistics> sumNewsStatistics = statisticsService.getSumNewsStatistics();
						Queue<Statistics> sumCommentStatistics = statisticsService.getSumCommentStatistics();
						request.setAttribute("sumNewsStatistics", sumNewsStatistics);
						request.setAttribute("sumCommentStatistics", sumCommentStatistics);
						request.setAttribute("condition", condition);
						getServletContext().getRequestDispatcher("/statistic/statisticsResult.jsp").forward(request,
								response);
					} else {
						Queue<Statistics> newsAuthorResult = statisticsService.getNewsCountForNewsAuthor(condition);
						Queue<Statistics> userResult = statisticsService.getCommentCountForUser(condition);
						request.setAttribute("newsAuthorResult", newsAuthorResult);
						request.setAttribute("userResult", userResult);
						request.setAttribute("condition", condition);
						getServletContext().getRequestDispatcher("/statistic/statisticsResult.jsp").forward(request,
								response);
					}

				}
			}
		}
	}

}
