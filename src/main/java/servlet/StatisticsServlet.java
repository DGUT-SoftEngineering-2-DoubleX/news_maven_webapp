package servlet;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Statistics;
import service.StatisticsService;

public class StatisticsServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StatisticsService statisticsService = new StatisticsService();
		String condition = request.getParameter("condition");
		if ("lastYearEachMonth".equals(condition)) {
			Queue<Statistics> sumNewsStatistics = statisticsService.getSumNewsStatistics();
			Queue<Statistics> sumCommentStatistics = statisticsService.getSumCommentStatistics();
			request.setAttribute("sumNewsStatistics", sumNewsStatistics);
			request.setAttribute("sumCommentStatistics", sumCommentStatistics);
		} else {
			Queue<Statistics> newsAuthorResult = statisticsService.getNewsCountForNewsAuthor(condition);
			Queue<Statistics> userResult = statisticsService.getCommentCountForUser(condition);
			request.setAttribute("newsAuthorResult", newsAuthorResult);
			request.setAttribute("userResult", userResult);
		}

		request.setAttribute("condition", condition);
		getServletContext().getRequestDispatcher("/statisticsResult.jsp").forward(request, response);
	}

}
