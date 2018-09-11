package test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bean.User;
import dao.DatabaseDao;
import dao.UserDao;
import tools.PageInformation;

public class UsersDaoTest {

	static protected UserDao userDao;
	static protected DatabaseDao databaseDao;
	static protected PageInformation pageInformation;
	static protected User user;

	// 首先执行（在所有@test方法之前执行），并且只执行一次，多个@Test只执行一次
	@BeforeClass
	static public void beforeClass() throws Exception {
		userDao = new UserDao();
		databaseDao = new DatabaseDao();
		user = new User();
		pageInformation = new PageInformation();
		pageInformation.setPage(1);
		pageInformation.setPageSize(10);
		pageInformation.setTableName("user");
		pageInformation.setType("count");
		user.setName("admin");
		user.setPassword("123abc");
		user.setType("manager");
		user.setHeadIconUrl("\\news\\upload\\images\\headIcon\\15.jpg");
	}

	@Test
	public void testHasUser() throws SQLException {
		boolean testResult = userDao.hasUser(user, databaseDao);
		assertTrue(testResult);
	}

	// 不执行以下新用户注册测试，以免修改数据库内容影响其他测试函数执行
	// @Test
	// public void testRegister() throws SQLException {
	// int testResult = userDao.register(user, databaseDao);
	// assertEquals(testResult, -1);
	// }

	@Test
	public void testLogin() throws SQLException, Exception {
		int testResult = userDao.login(user);
		assertEquals(testResult, 1);
	}

	@Test
	public void testGetOnePage() throws SQLException, Exception {
		List<User> testResult = userDao.getOnePage(pageInformation, databaseDao);
		assertEquals(testResult.size(), 8);
	}

	@Test
	public void testGetByUserName() throws SQLException, Exception {
		User testResult = userDao.getByUserName(user.getName());
		assertNotNull(testResult);
	}

	// 不执行以下删除用户测试，以免修改数据库内容影响其他测试函数执行
	// @Test
	// public void testDeletes() throws SQLException, Exception {
	// int testResult = userDao.deletes(user.getUserId().toString(),
	// databaseDao);
	// assertEquals(testResult, 1);
	// }

	// 不执行以下修改用户头像测试，以免修改数据库内容影响其他测试函数执行
	// @Test
	// public void testUpdateHeadIcon() throws SQLException, Exception {
	// int testResult = userDao.updateHeadIcon(user, databaseDao);
	// assertEquals(testResult, 1);
	// }

	// 不执行以下修改用户可用性测试，以免修改数据库内容影响其他测试函数执行
	// 当用户可用性为stop时，此测试函数无法成功执行
	// @Test
	// public void testChangeEnable() throws SQLException, Exception {
	// int testResult = userDao.changeEnable(user.getUserId().toString(),
	// databaseDao);
	// assertEquals(testResult, 1);
	// }
}
