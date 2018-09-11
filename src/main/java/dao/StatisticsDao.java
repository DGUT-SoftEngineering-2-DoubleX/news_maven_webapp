package dao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import bean.Statistics;

public class StatisticsDao {
	public Queue<Statistics> getNewsCountForNewsAuthor(String condition, DatabaseDao databaseDao) throws SQLException {
		Queue<Statistics> result = new LinkedList<Statistics>();
		String sqlStart = "select name,count(*) as count from news,user where publisherId=userId and checked=1";
		String sqlEnd = " group by publisherId order by count(*) desc";
		String sqlCondition = "";

		if ("lastWeek".equals(condition)) {
			sqlCondition = " and DATE_SUB(CURDATE(),INTERVAL 7 DAY) <= date(publishTime) ";
		} else if ("lastMonth".equals(condition)) {
			sqlCondition = " and DATE_SUB(CURDATE(),INTERVAL 30 DAY) <= date(publishTime) ";
		} else if ("lastYear".equals(condition)) {
			sqlCondition = " and DATE_SUB(CURDATE(),INTERVAL 365 DAY) <= date(publishTime) ";
		}

		String sql = sqlStart + sqlCondition + sqlEnd;

		databaseDao.query(sql);
		while (databaseDao.next()) {
			Statistics statistics = new Statistics();
			statistics.setName(databaseDao.getString("name"));
			statistics.setCount(databaseDao.getInt("count"));
			result.add(statistics);
		}

		return result;
	}

	public Queue<Statistics> getCommentCountForUser(String condition, DatabaseDao databaseDao) throws SQLException {
		Queue<Statistics> result = new LinkedList<Statistics>();
		String sqlStart = "select name,count(*) as count from comment,user where comment.userId=user.userId ";
		String sqlEnd = " group by comment.userId  order by count(*) desc";
		String sqlCondition = "";

		if ("lastWeek".equals(condition)) {
			sqlCondition = " and DATE_SUB(CURDATE(),INTERVAL 7 DAY) <= date(time) ";
		} else if ("lastMonth".equals(condition)) {
			sqlCondition = " and DATE_SUB(CURDATE(),INTERVAL 30 DAY) <= date(time) ";
		} else if ("lastYear".equals(condition)) {
			sqlCondition = " and DATE_SUB(CURDATE(),INTERVAL 365 DAY) <= date(time) ";
		}

		String sql = sqlStart + sqlCondition + sqlEnd;

		databaseDao.query(sql);
		while (databaseDao.next()) {
			Statistics statistics = new Statistics();
			statistics.setName(databaseDao.getString("name"));
			statistics.setCount(databaseDao.getInt("count"));
			result.add(statistics);
		}

		return result;
	}

	public Queue<Statistics> getSumNewsStatistics(DatabaseDao databaseDao) throws SQLException {
		Queue<Statistics> result = new LinkedList<Statistics>();
		String sqlStart = "select count(*) as count from news where checked=1 ";
		String sqlCondition = "";

		Integer integer = 0;
		while (integer < 12) {
			if (integer == 0) {
				sqlCondition = " and DATE_FORMAT( publishTime, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) ";
			} else {
				sqlCondition = " and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( publishTime, '%Y%m' ) ) = "
						+ integer.toString();
			}
			String sql = sqlStart + sqlCondition;

			databaseDao.query(sql);
			while (databaseDao.next()) {
				Statistics statistics = new Statistics();
				if (integer == 0) {
					statistics.setName("本月");
				} else {
					statistics.setName("最近第" + integer.toString() + "个月");
				}
				statistics.setCount(databaseDao.getInt("count"));
				result.add(statistics);
			}
			integer++;
		}

		return result;
	}

	public Queue<Statistics> getSumCommentStatistics(DatabaseDao databaseDao) throws SQLException {
		Queue<Statistics> result = new LinkedList<Statistics>();
		String sqlStart = "select count(*) as count from comment where ";
		String sqlCondition = "";

		Integer integer = 0;
		while (integer < 12) {
			if (integer == 0) {
				sqlCondition = " DATE_FORMAT( time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) ";
			} else {
				sqlCondition = " PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( time, '%Y%m' ) ) = "
						+ integer.toString();
			}
			String sql = sqlStart + sqlCondition;

			databaseDao.query(sql);
			while (databaseDao.next()) {
				Statistics statistics = new Statistics();
				if (integer == 0) {
					statistics.setName("本月");
				} else {
					statistics.setName("最近第" + integer.toString() + "个月");
				}
				statistics.setCount(databaseDao.getInt("count"));
				result.add(statistics);
			}
			integer++;
		}

		return result;
	}
}
