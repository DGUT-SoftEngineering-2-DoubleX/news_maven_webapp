package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import bean.NewsType;

public class NewsTypeDao {
	public List<NewsType> getAll() throws SQLException, Exception {
		List<NewsType> newsTypes = new ArrayList<NewsType>();
		String sql = "select * from newstype";
		DatabaseDao databaseDao = new DatabaseDao();
		databaseDao.query(sql);
		while (databaseDao.next()) {
			NewsType newsType = new NewsType();
			newsType.setName(databaseDao.getString("name"));
			newsType.setNewsTypeId(databaseDao.getInt("newsTypeId"));
			newsTypes.add(newsType);
		}
		return newsTypes;
	}
}
