package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tools.PageInformation;
import dao.CommentDao;
import dao.DatabaseDao;
import bean.Comment;
import bean.CommentUserView;

public class CommentService {
	public List<CommentUserView> getOnePage(PageInformation pageInformation) {
		List<CommentUserView> commentUserViews = new ArrayList<CommentUserView>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			commentUserViews = commentDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentUserViews;
	}

	// 点赞
	public Integer paise(Integer commentId) {
		try {
			CommentDao commentDao = new CommentDao();
			if (commentDao.paise(commentId) > 0)
				return 1;//
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;//
		} catch (Exception e) {
			e.printStackTrace();
			return -3;//
		}
	}

	// 对新闻的回复，添加新评论
	public Integer addComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			Integer stair = commentDao.getStairByNewsId(comment.getNewsId(), databaseDao);
			comment.setStair(stair + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	// 对回复的回复
	public Integer addCommentToComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentUserView oldCommentUserView = commentDao.getByIdFromView(comment.getCommentId(), databaseDao);
			String content = oldCommentUserView.getContent();
			if (content.contains("<br><br>")) {// 消除之前的留言
				content = content.substring(content.indexOf("<br><br>") + 8);
			}
			String s = "回复第" + oldCommentUserView.getStair() + "楼层&nbsp;" + oldCommentUserView.getUserName()
					+ "&nbsp;的留言：" + content + "<br><br>";

			comment.setContent(s + comment.getContent());
			Integer stair = commentDao.getStairByNewsId(comment.getNewsId(), databaseDao);
			comment.setStair(stair + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	// 自定义函数，更新评论内容
	public Integer updateComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			return commentDao.updateComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	// 自定义函数，删除评论内容
	public Integer deleteComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			return commentDao.deleteComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}
}
