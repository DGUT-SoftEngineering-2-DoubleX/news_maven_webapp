package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Comment;
import bean.CommentUserView;
import bean.User;
import service.CommentService;
import service.NewsService;
import tools.Message;
import tools.PageInformation;
import tools.Tool;

public class CommentServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String newsId = request.getParameter("newsId");
		CommentService commentService = new CommentService();
		if (type.equals("showCommnet")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("commentUserView", request, pageInformation);
			pageInformation.setSearchSql(" (newsId=" + newsId + ") ");
			pageInformation.setOrder("desc");
			pageInformation.setOrderField("time");
			List<CommentUserView> commentUserViews = commentService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("commentUserViews", commentUserViews);
			getServletContext().getRequestDispatcher("/comment/showComment.jsp").include(request, response);
			return;
		} else if (type.equals("praise")) {// 点赞
			String commentId = request.getParameter("commentId");

			commentService.paise(Integer.parseInt(commentId));
			String url = "/servlet/NewsServlet?type1=showANews&newsId=" + newsId + "&page"
					+ request.getParameter("page") + "&pageSize" + request.getParameter("pageSize") + "&totalPageCount"
					+ request.getParameter("totalPageCount");
			getServletContext().getRequestDispatcher(url).forward(request, response);
		} else if (type.equals("addCommnet")) {// 添加评论
			Comment comment = new Comment();
			comment.setContent(request.getParameter("content"));
			comment.setNewsId(Integer.parseInt(newsId));
			User user = (User) request.getSession().getAttribute("user");
			comment.setUserId(user.getUserId());
			String commentId = request.getParameter("commentId");

			String url;
			if (commentId == null || commentId.isEmpty()) {
				commentService.addComment(comment);// 对新闻的回复
				url = "/servlet/NewsServlet?type1=showANews&newsId=" + newsId + "&page=1&addCommnet=addCommnet";
			} else {
				comment.setCommentId(Integer.parseInt(commentId));
				commentService.addCommentToComment(comment);// 对回复的回复
				url = "/servlet/NewsServlet?type1=showANews&newsId=" + newsId + "&page=1";
			}

			getServletContext().getRequestDispatcher(url).forward(request, response);
		} else if (type.equals("reviseCommnet")) {// 修改评论（自定义部分）
			Comment comment = new Comment();
			comment.setContent(request.getParameter("content"));
			String commentId = request.getParameter("commentId");
			comment.setCommentId(Integer.parseInt(commentId));
			String url;
			String page = request.getParameter("page");
			commentService.updateComment(comment);
			url = "/servlet/NewsServlet?type1=showANews&newsId=" + newsId + "&page=" + page;
			getServletContext().getRequestDispatcher(url).forward(request, response);
		} else if (type.equals("deleteCommnet")) {// 删除评论（自定义部分）
			Comment comment = new Comment();
			String commentId = request.getParameter("commentNo");
			String stair = request.getParameter("stair");
			comment.setCommentId(Integer.parseInt(commentId));
			comment.setStair(Integer.parseInt(stair));
			comment.setNewsId(Integer.parseInt(newsId));
			String url;
			// String page = request.getParameter("page");
			commentService.deleteComment(comment);

			url = "/servlet/NewsServlet?type1=showANews&newsId=" + newsId + "&page" + request.getParameter("page")
					+ "&pageSize" + request.getParameter("pageSize");
			getServletContext().getRequestDispatcher(url).forward(request, response);
		}
	}

}
