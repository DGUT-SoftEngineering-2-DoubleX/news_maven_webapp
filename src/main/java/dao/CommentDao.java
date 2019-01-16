package dao;

import static org.hamcrest.CoreMatchers.nullValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tools.PageInformation;
import tools.Tool;
import bean.ArticleNumberByMonthInAYear;
import bean.Comment;
import bean.CommentNumberTopTenInAYear;
import bean.CommentUserView;
import bean.Statistics;

public class CommentDao {

	public List<CommentUserView> getOnePage(PageInformation pageInformation, DatabaseDao databaseDao)
			throws SQLException {
		List<CommentUserView> commentUserViews = new ArrayList<CommentUserView>();
		String sqlCount = Tool.getSql(pageInformation, "count");
		Integer allRecordCount = databaseDao.getCount(sqlCount);// 符合条件的总记录数
		Tool.setPageInformation(allRecordCount, pageInformation);// 更新pageInformation的总页数等

		String sqlSelect = Tool.getSql(pageInformation, "select");
		databaseDao.query(sqlSelect);
		while (databaseDao.next()) {
			CommentUserView commentUserView = new CommentUserView();
			commentUserView.setCommentId(databaseDao.getInt("commentId"));
			commentUserView.setContent(databaseDao.getString("content"));
			commentUserView.setNewsId(databaseDao.getInt("newsId"));
			commentUserView.setPraise(databaseDao.getInt("praise"));
			commentUserView.setStair(databaseDao.getInt("stair"));
			commentUserView.setTime(databaseDao.getTimestamp("time"));
			commentUserView.setUserName(databaseDao.getString("name"));
			commentUserView.setHeadIconUrl(databaseDao.getString("headIconUrl"));
			commentUserView.setUserId(databaseDao.getInt("userId"));
			commentUserViews.add(commentUserView);
		}
		return commentUserViews;
	}

	public Integer paise(Integer commentId) throws SQLException, Exception {
		DatabaseDao databaseDao = new DatabaseDao();
		String sql = "update comment set praise=praise+1 where commentId=" + commentId;
		return databaseDao.update(sql);
	}

	public Integer getStairByNewsId(Integer newsId, DatabaseDao databaseDao) throws SQLException {
		String sql = "select count(*) as count1 from comment where newsId=" + newsId;
		Integer stair = 0;
		databaseDao.query(sql);
		while (databaseDao.next()) {
			stair = databaseDao.getInt("count1");
		}
		return stair;
	}

	public Integer addComment(Comment comment, DatabaseDao databaseDao) throws SQLException, Exception {
		String sql = "insert into comment(newsId,userId,content,stair) values(" + comment.getNewsId() + ","
				+ comment.getUserId() + ",'" + comment.getContent() + "', " + comment.getStair() + ")";
		return databaseDao.update(sql);
	}

	public Comment getById(Integer commentId, DatabaseDao databaseDao) throws SQLException {
		databaseDao.getById("comment", commentId);
		while (databaseDao.next()) {
			Comment comment = new Comment();
			comment.setCommentId(databaseDao.getInt("commentId"));
			comment.setContent(databaseDao.getString("content"));
			comment.setNewsId(databaseDao.getInt("newsId"));
			comment.setPraise(databaseDao.getInt("praise"));
			comment.setStair(databaseDao.getInt("stair"));
			comment.setTime(databaseDao.getTimestamp("time"));
			comment.setUserId(databaseDao.getInt("userIde"));
			return comment;
		}
		return null;
	}

	public CommentUserView getByIdFromView(Integer commentId, DatabaseDao databaseDao) throws SQLException {
		String sql = "select * from commentUserView  where commentId=" + commentId.toString();
		databaseDao.query(sql);
		while (databaseDao.next()) {
			CommentUserView commentUserView = new CommentUserView();
			commentUserView.setCommentId(databaseDao.getInt("commentId"));
			commentUserView.setContent(databaseDao.getString("content"));
			commentUserView.setNewsId(databaseDao.getInt("newsId"));
			commentUserView.setPraise(databaseDao.getInt("praise"));
			commentUserView.setStair(databaseDao.getInt("stair"));
			commentUserView.setTime(databaseDao.getTimestamp("time"));
			commentUserView.setUserName(databaseDao.getString("name"));
			commentUserView.setHeadIconUrl(databaseDao.getString("headIconUrl"));
			commentUserView.setUserId(databaseDao.getInt("userId"));
			return commentUserView;
		}
		return null;
	}

	public Integer updateComment(Comment comment, DatabaseDao databaseDao) throws SQLException, Exception {
		String sql = "update comment set content='" + comment.getContent() + "' where commentId="
				+ comment.getCommentId();
		return databaseDao.update(sql);
	}

	public Integer deleteComment(Comment comment, DatabaseDao databaseDao) throws SQLException, Exception {
		String sql = "delete from comment where commentId=" + comment.getCommentId();
		String sqlFollow = "update comment set stair=stair-1 where newsId=" + comment.getNewsId() + " and stair>"
				+ comment.getStair();
		Integer result = databaseDao.update(sql);
		if (result == 1)
			databaseDao.update(sqlFollow);
		return result;
	}

	public CommentNumberTopTenInAYear commentNumberByMonthInAYear(String year) throws SQLException, Exception {
		CommentNumberTopTenInAYear commentNumberTopTenInAYear = new CommentNumberTopTenInAYear();
		commentNumberTopTenInAYear.setYear(Integer.parseInt(year));
		String sql = "SELECT name as userName, count(*) as commentNumber from user,comment where user.userId=comment.userId and YEAR(time)='"
				+ year + "' GROUP BY user.userId ORDER BY count(*) LIMIT 10";
		DatabaseDao databaseDao = new DatabaseDao();
		databaseDao.query(sql);
		while (databaseDao.next()) {
			Statistics statistics = new Statistics();
			statistics.setName(databaseDao.getString("userName"));
			statistics.setCount(databaseDao.getInt("commentNumber"));
			commentNumberTopTenInAYear.getCommentList().add(statistics);
		}

		return commentNumberTopTenInAYear;
	}

	public List<CommentNumberTopTenInAYear> commentNumberTopTenInAYearEveryYear() throws SQLException, Exception {
		List<CommentNumberTopTenInAYear> commentNumberTopTenInAYears = new ArrayList<CommentNumberTopTenInAYear>();
		String sql = "SELECT YEAR(time) as year, name as userName, count(*) as commentNumber FROM user,comment where user.userId=comment.userId and YEAR(time) in (SELECT YEAR(time) FROM comment GROUP BY YEAR(time)) GROUP BY YEAR(time),user.userId ORDER BY YEAR(time),count(*) DESC LIMIT 10";
		DatabaseDao databaseDao = new DatabaseDao();
		databaseDao.query(sql);
		Integer nowYear = 0;
		CommentNumberTopTenInAYear commentNumberTopTenInAYear = null;
		while (databaseDao.next()) {
			Integer nowDatabaseYear = databaseDao.getInt("year");

			if (!nowYear.equals(nowDatabaseYear)) {// 新的一年
													// //注意：Integer相等必须用equals方法进行比较,不能直接用==比较是否相等
				nowYear = nowDatabaseYear;
				if (commentNumberTopTenInAYear != null)
					commentNumberTopTenInAYears.add(commentNumberTopTenInAYear);// 将旧的一年的数据加入数组
				// 新的一年的数据
				commentNumberTopTenInAYear = new CommentNumberTopTenInAYear();
				commentNumberTopTenInAYear.setYear(nowYear);
			}

			// 加入该月的数据
			// commentNumberTopTenInAYear.getCommentList().set(new
			// Statistics().getInt("month1") - 1,
			// databaseDao.getInt("articleNumber"));
			// articleNumberByMonthInAYear.setTotalNewsNumber(
			// articleNumberByMonthInAYear.getTotalNewsNumber() +
			// databaseDao.getInt("articleNumber"));

			Statistics statistics = new Statistics();
			statistics.setName(databaseDao.getString("userName"));
			statistics.setCount(databaseDao.getInt("commentNumber"));
			commentNumberTopTenInAYear.getCommentList().add(statistics);

		}
		if (commentNumberTopTenInAYear != null)
			commentNumberTopTenInAYears.add(commentNumberTopTenInAYear);// 将最后一年的数据加入数组

		return commentNumberTopTenInAYears;
	}

}
