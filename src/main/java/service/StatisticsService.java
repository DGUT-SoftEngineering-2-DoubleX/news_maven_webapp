package service;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import bean.Statistics;
import dao.DatabaseDao;
import dao.StatisticsDao;

public class StatisticsService {
	public Queue<Statistics> getNewsCountForNewsAuthor(String condition) {
		Queue<Statistics> result = new LinkedList<Statistics>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			StatisticsDao statisticsDao = new StatisticsDao();
			return statisticsDao.getNewsCountForNewsAuthor(condition, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Queue<Statistics> getCommentCountForUser(String condition) {
		Queue<Statistics> result = new LinkedList<Statistics>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			StatisticsDao statisticsDao = new StatisticsDao();
			return statisticsDao.getCommentCountForUser(condition, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Queue<Statistics> getSumNewsStatistics() {
		Queue<Statistics> result = new LinkedList<Statistics>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			StatisticsDao statisticsDao = new StatisticsDao();
			return statisticsDao.getSumNewsStatistics(databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Queue<Statistics> getSumCommentStatistics() {
		Queue<Statistics> result = new LinkedList<Statistics>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			StatisticsDao statisticsDao = new StatisticsDao();
			return statisticsDao.getSumNewsStatistics(databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
