package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import service.UserService;
import tools.EMailTool;
import tools.Message;
import tools.PageInformation;
import tools.SearchTool;
import tools.Tool;
import bean.User;
import bean.Userinformation;

public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type1");
		UserService userService = new UserService();
		Message message = new Message();
		if (type.equals("register")) {
			User user = new User();
			user.setType(request.getParameter("type"));
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));

			Integer result = 0;
			String checkCode = request.getParameter("checkCode");
			HttpSession session = request.getSession();
			String severCheckCode = (String) session.getAttribute("checkCode");// 获取session中的验证码

			if (severCheckCode == null) {// 服务器端验证图片验证码不存在
				result = -3;
			} else if (!severCheckCode.equals(checkCode)) {// 服务器端验证图片验证码验证失败
				result = -4;
			} else {// 验证码验证正确
				if (user.getType().equals("user"))
					user.setEnable("use");
				else
					user.setEnable("stop");
				user.setEmail(request.getParameter("email"));
				result = userService.registerChecker(user);// 注册用户
			}

			if (result == 0) {
				message.setMessage("数据库操作失败,请重新注册！");
			} else if (result == -1) {
				message.setMessage("同名用户已存在，请更改名称后再重新注册！");
			} else if (result == -10) {
				message.setMessage("该E-mail已被注册，请找回密码！");
				message.setRedirectUrl("/news/user/free/login.jsp");
			} else if (result == -11) {
				message.setMessage("同名用户已存在且该E-mail已被注册，请找回密码！");
				message.setRedirectUrl("/news/user/free/login.jsp");
			} else if (result == -3 || result == -4) {
				message.setMessage("验证码输入有误!");
			} else if (result == 1) {
				Integer rand = Tool.getRandomInRangeInteger(10, 100000);// 随机数作为验证修改密码用
				result = EMailTool.sendRegister(user.getEmail(), rand);// 发送邮件
				if (result == 1) {// 发送邮件成功
					session.setAttribute("registerUser", user);
					session.setAttribute("email", user.getEmail());
					session.setAttribute("rand", rand);
					session.setAttribute("time", new Date());
					message.setMessage("用户注册邮件已发送至" + user.getEmail() + ",请查收邮件并点击链接通过注册验证！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				} else {
					message.setMessage("用户注册邮件发送失败,,请重新注册！");
				}
			}
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if (type.equals("emailForRegister")) {

			String rand = (String) request.getParameter("rand");
			HttpSession session = request.getSession();
			Integer trueRand = (Integer) session.getAttribute("rand");
			if (!rand.equals(trueRand.toString())) {
				// rand值不对，无权限
				message.setMessage("注册失败，请重新注册！");
				message.setRedirectUrl("/news/user/free/register.jsp");
			} else {
				User user = new User();
				user = (User) session.getAttribute("registerUser");
				Integer rusult = userService.register(user);
				if (rusult == 1) {
					message.setMessage("用户注册成功！正在跳转登陆界面……");
					message.setRedirectUrl("/news/user/free/login.jsp");
				} else if (rusult == -1) {
					message.setMessage("数据库操作失败，请重新注册！");
					message.setRedirectUrl("/news/user/free/register.jsp");
				}
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if (type.equals("login")) {
			User user = new User();
			user.setName(request.getParameter("name"));
			user.setPassword(request.getParameter("password"));

			Integer result = 0;
			String checkCode = request.getParameter("checkCode");
			HttpSession session = request.getSession();
			String severCheckCode = (String) session.getAttribute("checkCode");// 获取session中的验证码

			if (severCheckCode == null) {// 服务器端验证图片验证码不存在
				result = -3;
			} else if (!severCheckCode.equals(checkCode)) {// 服务器端验证图片验证码验证失败
				result = -4;
			} else {
				result = userService.login(user);
			}
			message.setResult(result);
			if (result == 1) {
				user.setPassword(null);// 防止密码泄露
				request.getSession().setAttribute("user", user);
				String originalUrl = (String) request.getSession().getAttribute("originalUrl");
				if (originalUrl == null)
					message.setRedirectUrl("/news/index.jsp");
				else
					message.setRedirectUrl(originalUrl);
			} else if (result == 0) {
				message.setMessage("该用户存在，但已被停用，请联系管理员！");
			} else if (result == -1) {
				message.setMessage("密码有误，请重新登录！");
			} else if (result == -2) {
				message.setMessage("出现其它未知错误，请重新登录！");
			} else if (result == -3 || result == -4) {
				message.setMessage("验证码输入有误!");
			}
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if (type.equals("findPassword")) { // 找回密码
			User user = new User();
			user.setEmail(request.getParameter("email"));
			Integer rand = Tool.getRandomInRangeInteger(10, 100000);// 随机数作为验证修改密码用
			Integer result = userService.findPasswordByEmail(user, rand);
			if (result == 1) {// 发送邮件成功
				HttpSession session = request.getSession();
				session.setAttribute("email", user.getEmail());
				session.setAttribute("rand", rand);
				session.setAttribute("time", new Date());
				message.setMessage("找回密码邮件已发送至" + user.getEmail() + ",请查收邮件并点击链接通过验证！");
				message.setRedirectUrl("/news/user/free/login.jsp");
			} else if (result == 0 || result == -1) {
				message.setMessage("");
				message.setRedirectUrl("/news/user/free/findPassword.jsp");
			} else if (result == -2) {
				message.setMessage("该邮箱不存在，请先注册后再进行操作！");
				message.setRedirectUrl("/news/user/free/register.jsp");
			} else if (result == -3) {
				message.setMessage("出现其他错误，请重新找回密码！");
				message.setRedirectUrl("/news/user/free/findPassword.jsp");
			}
			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if (type.equals("newPassword")) {
			User user = new User();
			user.setPassword(request.getParameter("password"));
			String rand = (String) request.getParameter("rand");
			HttpSession session = request.getSession();
			Integer trueRand = (Integer) session.getAttribute("rand");
			user.setEmail((String) session.getAttribute("email"));
			Date old = (Date) session.getAttribute("time");
			Integer result = 0;

			if (!rand.equals(trueRand.toString())) {
				// rand值不对，无权限修改密码
				message.setMessage("修改密码失败，请重新找回密码！");
				message.setRedirectUrl("/news/user/free/findPassword.jsp");
			} else if (old == null || Tool.getSecondFromNow(old) > 300) {
				message.setMessage("修改密码超时，请重新找回密码！");
				message.setRedirectUrl("/news/user/free/findPassword.jsp");
			} else {
				result = userService.updatePassword(user);
				if (result == 0) {
					message.setMessage("修改密码失败，请重新找回密码！");
					message.setRedirectUrl("/news/user/free/findPassword.jsp");
				} else if (result == 1) {
					message.setMessage("修改密码成功，正在跳转登陆界面！");
					message.setRedirectUrl("/news/user/free/login.jsp");
				} else if (result == -1) {
					message.setMessage("数据库操作失败，请重新找回密码！");
					message.setRedirectUrl("/news/user/free/findPassword.jsp");
				} else {
					message.setMessage("出现其他错误，请重新找回密码！");
					message.setRedirectUrl("/news/user/free/findPassword.jsp");
				}
			}
			session.removeAttribute("email");// 删除session数据
			session.removeAttribute("rand");
			session.removeAttribute("time");

			message.setResult(result);
			Gson gson = new Gson();
			String jsonString = gson.toJson(message);
			Tool.returnJsonString(response, jsonString);
		} else if (type.equals("exit")) {
			request.getSession().removeAttribute("user");
			response.sendRedirect("/news/index.jsp");
		} else if (type.equals("showPage")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			List<User> users = userService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("users", users);
			getServletContext().getRequestDispatcher("/manager/userShow.jsp").forward(request, response);

		} else if (type.equals("showOne")) {
			String userName = request.getParameter("oneUserName");
			User user = userService.getOneUser(userName);
			if (user != null) {
				user.setPassword(null); // 防止密码泄露
				request.getSession().setAttribute("oneUser", user);
				if ("user".equals(user.getType())) {
					Userinformation oneUserinformation = userService.getByUserId(user.getUserId());
					request.setAttribute("oneUserinformation", oneUserinformation);
				}
			} else {
				message.setMessage("用户不存在，请检查后再进行操作！");
				message.setRedirectTime(1000);
				request.setAttribute("message", message);
				getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
			}

			getServletContext().getRequestDispatcher("/manager/showOneUser.jsp").forward(request, response);

		} else if (type.equals("search")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			pageInformation.setSearchSql(SearchTool.user(request));
			List<User> users = userService.getOnePage(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("users", users);
			getServletContext().getRequestDispatcher("/manager/userShow.jsp").forward(request, response);
		} else if (type.equals("check")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			String id = pageInformation.getIds();
			pageInformation.setIds(null);
			List<User> users = userService.check(pageInformation, id);
			if (users == null) {
				message.setMessage("切换可用性失败，请联系管理员！");
				message.setRedirectUrl("/news/servlet/UserServlet?type1=check&page=1&pageSize=10");
			} else {
				request.setAttribute("pageInformation", pageInformation);
				request.setAttribute("users", users);
				getServletContext().getRequestDispatcher("/manager/userCheck.jsp").forward(request, response);
			}
		} else if (type.equals("delete")) {
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("user", request, pageInformation);
			pageInformation.setSearchSql(" (type='user' or type='newsAuthor')");
			List<User> users = userService.deletes(pageInformation);
			request.setAttribute("pageInformation", pageInformation);
			request.setAttribute("users", users);
			getServletContext().getRequestDispatcher("/manager/userDelete.jsp").forward(request, response);
		} else if (type.equals("changePassword")) {
			String newPassword = request.getParameter("newPassword");
			User user = (User) request.getSession().getAttribute("user");
			user.setPassword(request.getParameter("oldPassword"));
			Integer result = userService.changePassword(user, newPassword);
			message.setResult(result);
			if (result == 1) {
				message.setMessage("修改密码成功！");
			} else if (result == 0) {
				message.setMessage("旧密码错误,修改密码失败！");
			} else if (result == -1) {
				message.setMessage("修改密码失败！请重新操作");
			} else {
				message.setMessage("数据库操作失败！请重新操作");
			}
			message.setRedirectTime(1000);
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if (type.equals("showPrivate")) {// 显示普通用户个人信息
			User user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				Userinformation userinformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userinformation", userinformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/showPrivate.jsp").forward(request, response);
		} else if (type.equals("changePrivate1")) {// 修改普通用户个人信息的第一步：显示可修改信息
			User user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				Userinformation userinformation = userService.getByUserId(user.getUserId());
				request.setAttribute("userinformation", userinformation);
			}
			getServletContext().getRequestDispatcher("/user/manage/changePrivate.jsp").forward(request, response);
		} else if (type.equals("changePrivate2")) {// 修改普通用户个人信息的第二步：修改信息
			User user = (User) request.getSession().getAttribute("user");
			if ("user".equals(user.getType())) {
				Userinformation userinformation = new Userinformation();
				userinformation.setUserId(user.getUserId());
				userinformation.setSex(request.getParameter("sex"));
				userinformation.setHobby(request.getParameter("hobby"));
			}
			Integer result = userService.updatePrivate(user, request);
			message.setResult(result);
			if (result == 5) {
				message.setMessage("修改个人信息成功！");
				message.setRedirectUrl("/news/servlet/UserServlet?type1=showPrivate");
			} else if (result == 0) {
				message.setMessage("修改个人信息失败，请联系管理员！");
				message.setRedirectUrl("/news/servlet/UserServlet?type1=showPrivate");
			}
			request.setAttribute("message", message);
			getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
		} else if (type.equals("qq")) {// qq登录
			User user = new User();
			user.setOpenId(request.getParameter("openId"));
			user.setAccessToken(request.getParameter("accessToken"));
			user.setName(request.getParameter("nickname"));

			if (userService.qqLogin(user) == 1) {
				user.setPassword(null);// 防止密码泄露
				request.getSession().setAttribute("user", user);
				getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
			} else {
				// 如果已有用户登录，可以强行将原有用户注销
				request.getSession().removeAttribute("user");
				// 保存qq用户信息
				request.getSession().setAttribute("qqUser", user);
				message.setResult(-1);
				// 绑定用户的页面
				getServletContext().getRequestDispatcher("/user/free/qqBindUser.jsp").forward(request, response);
			}

		} else if (type.equals("qqBindUser")) {// qq登录 回调后
			User user = (User) request.getSession().getAttribute("qqUser");
			Integer result;
			String qqType = request.getParameter("qqType");

			if ("bindNewUser".equals(qqType)) {
				result = userService.qqBindNewUser(user);
				if (result == 1) {// 登录成功
					request.getSession().setAttribute("user", user);
					response.sendRedirect("/news/user/manageUIMain/manageMain.jsp");
					return;
				} else {// 绑定失败，需要重新登录
					response.sendRedirect("/user/free/login.jsp");
					return;
				}
			} else if ("bindOldUser".equals(qqType)) {
				userService.qqBindOldUser(user);
				return;
			}
		}
	}

}
