package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Authority;

public class AuthorityDao {
	public List<Authority> getAll() throws SQLException, Exception {
		List<Authority> authorities = new ArrayList<Authority>();
		DatabaseDao databaseDao = new DatabaseDao();
		databaseDao.query("select * from authority");
		while (databaseDao.next()) {
			Authority authority = new Authority();
			authority.setAuthorityId(databaseDao.getInt("authorityId"));
			authority.setUserType(databaseDao.getString("userType"));
			authority.setUrl(databaseDao.getString("url"));
			authority.setRedirectUrl(databaseDao.getString("redirectUrl"));
			authority.setParam(databaseDao.getString("param"));
			authorities.add(authority);
		}
		return authorities;
	}
}
