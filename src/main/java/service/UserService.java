package service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tools.FileTool;
import tools.PageInformation;
import tools.WebProperties;
import bean.User;
import bean.Userinformation;
import dao.DatabaseDao;
import dao.UserDao;
import dao.UserinformationDao;

public class UserService {
	public Integer register(User user) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao UserDao = new UserDao();
			if (UserDao.hasUser(user, databaseDao)) {
				return 0;// 失败，用户已存在
			} else {// 没有同名用户，可以注册
				if (UserDao.register(user, databaseDao) > 0)
					return 1; // 成功
				else
					return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;// 数据库操作失败
		} catch (Exception e) {
			e.printStackTrace();
			return -2;// 其他异常
		}
	}

	public Integer login(User user) {
		int result = -2; // 数据库操作失败
		try {
			UserDao UserDao = new UserDao();
			return UserDao.login(user);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<User> getOnePage(PageInformation pageInformation) {
		List<User> users = new ArrayList<User>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			users = userDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	// 自定义函数，获取某一用户除密码外的信息
	public User getOneUser(String userName) {
		User user = new User();
		try {
			UserDao userDao = new UserDao();
			user = userDao.getByUserName(userName);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public List<User> check(PageInformation pageInformation, String id) {
		List<User> users = null;
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();

			if (id != null && !id.isEmpty())
				userDao.changeEnable(id, databaseDao);

			users = userDao.getOnePage(pageInformation, databaseDao);

		} catch (SQLException e) {
			users = null;
			e.printStackTrace();
		} catch (Exception e) {
			users = null;
			e.printStackTrace();
		}
		return users;
	}

	// 删除多条记录
	public List<User> deletes(PageInformation pageInformation) {
		List<User> users = null;
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			userDao.deletes(pageInformation.getIds(), databaseDao);
			pageInformation.setIds(null);
			users = userDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			users = null;
			e.printStackTrace();
		} catch (Exception e) {
			users = null;
			e.printStackTrace();
		}
		return users;
	}

	// 修改密码
	public Integer changePassword(User user, String newPassword) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			if (userDao.hasUser(user, databaseDao)) {
				if (databaseDao.updateAStringFieldByIdAndOldPassword("user", user.getUserId(), "password", newPassword,
						"password", user.getPassword()) > 0)
					return 1;// 修改成功
				else
					return 0;// 用户存在，但修改失败，可能是密码问题
			} else
				return -1;// 用户不存在
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;// 数据库问题
		} catch (Exception e) {
			e.printStackTrace();
			return -3;// 其它异常
		}
	}

	public Userinformation getByUserId(Integer userId) {
		Userinformation userinformation = null;
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			UserinformationDao userinformationDao = new UserinformationDao();
			userinformation = userinformationDao.getByUserId(userId, databaseDao);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userinformation;
	}

	public Integer updatePrivate(User user, HttpServletRequest request) {
		Integer result;

		// 设置标记值，判断用户是否选择修改头像
		boolean fullIsNull = false;

		try {
			Userinformation userinformation = new Userinformation();
			if ("user".equals(user.getType())) {
				userinformation.setUserId(user.getUserId());
			}
			String oldHeadIconUrl = user.getHeadIconUrl();
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// Configure a repository (to ensure a secure temp location is used)
			String fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("tempDir"));// 获取相对路径的绝对路径
			File repository = new File(fullPath);
			factory.setRepository(repository);// 设置临时文件存放的文件夹
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解析request，将其中各表单元素和上传文件提取出来

			List<FileItem> items = upload.parseRequest(request);// items存放各表单元素

			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {// 遍历表单元素
				FileItem item = iter.next();

				if (item.isFormField()) {// 非上传文件表单元素
					if ("sex".equals(item.getFieldName()))// 获取表单元素的name属性
						userinformation.setSex(item.getString("UTF-8"));// 获取表单元素的值（一般是value属性）
					if ("hobby".equals(item.getFieldName()))// 获取表单元素的name属性
						userinformation.setHobby(item.getString("UTF-8"));// 获取表单元素的值（一般是value属性）
				} else {// 上传文件
					File uploadedFile = new File("");
					String randomFileName;
					do {
						randomFileName = FileTool.getRandomFileNameByCurrentTime(item.getName());
						String full = request.getServletContext().getRealPath(
								WebProperties.config.getString("headIconDirDefault") + "\\" + randomFileName);
						if (full == null) {
							fullIsNull = true;
							break;
						} else {
							uploadedFile = new File(full);
						}
					} while (uploadedFile.exists());// 确保文件未存在

					if (fullIsNull) {
						continue;
					} else {
						item.write(uploadedFile);// 将临时文件转存为新文件保存
						result = 1;// 表示上传文件成功
						item.delete();// 删除临时文件
						result = 2;// 表示上传文件成功，且临时文件删除
						user.setHeadIconUrl("\\" + WebProperties.config.getString("projectName")
								+ WebProperties.config.getString("headIconDirDefault") + "\\" + randomFileName);
					}
				}
			}
			DatabaseDao databaseDao = new DatabaseDao();
			UserDao userDao = new UserDao();
			UserinformationDao userinformationDao = new UserinformationDao();

			// 开始事务处理
			databaseDao.setAutoCommit(false);
			userDao.updateHeadIcon(user, databaseDao);// 更新用户表的头像
			if ("user".equals(user.getType())) {
				userinformation.setUserId(user.getUserId());
				// 普通用户有userinformation信息
				if (userinformationDao.hasUserId(user.getUserId(), databaseDao))
					userinformationDao.update(userinformation, databaseDao);// 更新用户详细信息
				else
					userinformationDao.insert(userinformation, databaseDao);// 插入新的用户详细信息
			}
			databaseDao.commit();
			databaseDao.setAutoCommit(true);
			result = 3;// 表示上传文件成功，临时文件删除，且路径保存到数据库成功

			if (fullIsNull) {
				result = 5;
			} else {
				if (oldHeadIconUrl
						.contains(FileTool.getFileName(WebProperties.config.getString("headIconFileDefault"))))
					result = 5;//// 表示上传文件成功，临时文件删除，且路径保存到数据库成功，老的图片使用系统默认图片，不需要删除
				else// 老的图片没有使用系统默认图片，需要删除
				if (FileTool.deleteFile(
						new File(FileTool.root.replace("\\" + WebProperties.config.getString("projectName"), "")
								+ oldHeadIconUrl)))
					result = 5;//// 表示上传文件成功，临时文件删除，且路径保存到数据库成功，老的图片被删除
				else
					result = 4;//// 表示上传文件成功，临时文件删除，且路径保存到数据库成功，老的图片无法被删除
			}

		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -3;
		}
		return result;
	}
}
