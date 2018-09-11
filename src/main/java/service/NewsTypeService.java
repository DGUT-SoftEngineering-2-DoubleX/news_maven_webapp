package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.NewsTypeDao;
import bean.NewsType;

public class NewsTypeService {
	public List<NewsType> getAll() {
		List<NewsType> NewsTypes = new ArrayList<NewsType>();
		try {
			NewsTypeDao newsTypeDao = new NewsTypeDao();
			NewsTypes = newsTypeDao.getAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NewsTypes;
	}
}
