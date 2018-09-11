package service;

import java.sql.SQLException;
import java.util.List;

import dao.AuthorityDao;
import bean.Authority;

public class AuthorityService {
	public List<Authority> getAll() {
		try {
			return (List<Authority>) (new AuthorityDao()).getAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
