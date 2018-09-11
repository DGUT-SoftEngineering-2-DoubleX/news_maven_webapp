package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import bean.User;
import bean.Userinformation;
import dao.DatabaseDao;
import service.UserService;
import tools.PageInformation;

public class UsersServiceTest {
	static protected UserService userService;
	static protected DatabaseDao databaseDao;
	static protected PageInformation pageInformation;
	static protected User user;

	// 首先执行（在所有@test方法之前执行），并且只执行一次，多个@Test只执行一次
	@BeforeClass
	static public void beforeClass() throws Exception {
		userService = new UserService();
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

	// 注册测试
	@Test
	public void testRegister() {
		int testResult = userService.register(user);
		assertEquals(testResult, 0);
	}

	// 登录测试
	@Test
	public void testLogin() {
		int testResult = userService.login(user);
		assertEquals(testResult, 1);
	}

	// 获取一页用户测试
	@Test
	public void testGetOnePage() {
		List<User> testResult = userService.getOnePage(pageInformation);
		assertEquals(testResult.size(), 8);
	}

	// 根据用户名获取某个用户信息测试
	@Test
	public void testGetOneUser() {
		User testResult = userService.getOneUser(user.getName());
		assertNotNull(testResult);
	}

	// 不执行以下删除用户测试，以免修改数据库内容影响其他测试函数执行
	// 根据用户Id的字符串序列删除某些用户信息测试
	// @Test
	// public void testDeletes() {
	// pageInformation.setIds(user.getUserId().toString());
	// List<User> testResult = userService.deletes(pageInformation);
	// assertEquals(testResult.size(), 7);
	// }

	// 更改用户密码测试
	@Test
	public void testChangePassword() {
		int testResult = userService.changePassword(user, "123abc");
		assertEquals(testResult, 1);
	}

	// 根据用户Id获取用户信息测试
	@Test
	public void testGetByUserId() {
		Userinformation testResult = userService.getByUserId(user.getUserId());
		assertNull(testResult);
	}

	// 鉴于技术有限，用java语言未能实现对文件的模拟上传操作，因此本测试代码并未对userService.updatePrivate方法进行测试，需要开发人员自行进行测试，望谅解！
}
