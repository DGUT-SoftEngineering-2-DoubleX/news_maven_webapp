package service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import bean.Databasebackup;
import dao.DatabasebackupDao;
import tools.Message;
import tools.WebProperties;

public class DatabaseService {

	public Message backup() {
		Message message = new Message();
		DateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		String backupFileName = df.format(new Date()) + ".sql";

		String backupFullPath = WebProperties.config.getString("databaseBackupDir") + "\\" + backupFileName;

		String mysqlCommand = "mysqldump -u" + WebProperties.config.getString("databaseUser") + " -p"
				+ WebProperties.config.getString("databasePassword") + " --opt "
				+ WebProperties.config.getString("databaseName") + " --ignore-table=news.databasebackup > "
				+ backupFullPath;

		StringBuffer dosCommand = new StringBuffer();
		dosCommand.append("cmd.exe /c \"").append(mysqlCommand).append("\"");
		System.out.println(dosCommand); // 打印执行的命令
		Process ls_proc; // java的进程类
		BufferedReader in = null;
		try {
			ls_proc = Runtime.getRuntime().exec(dosCommand.toString()); // 执行进程
			in = new BufferedReader(new InputStreamReader(ls_proc.getErrorStream()));
			StringBuffer sb = new StringBuffer("");// 记录dos命令在控制台的输出结果
			String aLine = "";
			while ((aLine = in.readLine()) != null) {
				sb.append(aLine).append("<br>");
			}
			in.close();
			message.setMessage(sb.toString());

			ls_proc.destroy();

			File backfile = new File(backupFullPath);
			// 生成的备份文件大小至少1k，防止生成0k大小的文件
			if (backfile.length() > 1024) {
				Databasebackup databasebackup = new Databasebackup();
				databasebackup.setName(backupFileName);
				databasebackup.setDirectory(backupFullPath);
				DatabasebackupDao databasebackupDao = new DatabasebackupDao();
				// 保存数据库备份信息到数据库
				if (databasebackupDao.add(databasebackup) > 0)
					message.setMessage(message.getMessage() + "<br>备份信息已添加至数据库!");
				else
					message.setMessage(message.getMessage() + "<br>备份信息添加至数据库失败！");
			} else {
				// - Assam's notes -
				// 如果此处生成的数据库备份文件一直为空，则将 mysql根目录 下的 bin文件目录 下的 mysqldump.exe
				// 文件复制到Windows系统的 %SystemRoot% 目录下
				// %SystemRoot% 的查看方法：在cmd界面输入“ echo %SystemRoot% ”进行查看
				message.setMessage(message.getMessage() + "<br>备份信息大小为空！<br>请检查系统PATH路径是否正常配置……<br>");
			}
		} catch (IOException e) {
			e.printStackTrace();
			message.setMessage(message.getMessage() + "<br>文件关闭失败！");
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
				message.setMessage(message.getMessage() + "<br>文件关闭失败！");
			}
		}
		return message;
	}

	public Message restore(HttpServletRequest request) {
		Message message = new Message();
		message.setResult(-1);
		Databasebackup databasebackup = new Databasebackup();
		String databaseRestoreIdString = request.getParameter("databaserestoreId");
		if (databaseRestoreIdString == null) {
			message.setMessage("暂无备份数据，请先备份数据库！");
			message.setResult(0);
			return message;
		}
		databasebackup.setDatabasebackupId(Integer.parseInt(databaseRestoreIdString));

		DatabasebackupDao databasebackupDao = new DatabasebackupDao();
		databasebackup = databasebackupDao.getById(databasebackup.getDatabasebackupId());

		if (databasebackup != null) {
			File backfile = new File(databasebackup.getDirectory());
			if (backfile.exists()) {
				String mysqlCommand = "mysql -u" + WebProperties.config.getString("databaseUser") + " -p"
						+ WebProperties.config.getString("databasePassword") + " "
						+ WebProperties.config.getString("databaseName") + " < " + databasebackup.getDirectory();

				StringBuffer dosCommand = new StringBuffer();
				dosCommand.append("cmd.exe /c \"").append(mysqlCommand).append("\"");
				System.out.println(dosCommand); // 打印执行的命令
				Process ls_proc; // java的进程类
				BufferedReader in = null;
				try {
					// - Assam's notes -
					// 如果还原数据库一直不成功或者返回的message有乱码（即还原不成功），则将 mysql根目录 下的
					// bin文件目录 下的 mysql.exe
					// 文件复制到Windows系统的 %SystemRoot% 目录下
					// %SystemRoot% 的查看方法：在cmd界面输入“ echo %SystemRoot% ”进行查看
					ls_proc = Runtime.getRuntime().exec(dosCommand.toString()); // 执行进程
					in = new BufferedReader(new InputStreamReader(ls_proc.getErrorStream()));
					StringBuffer sb = new StringBuffer("");// 记录dos命令在控制台的输出结果
					String aLine = "";
					while ((aLine = in.readLine()) != null) {
						sb.append(aLine).append("<br>");
					}
					in.close();
					message.setMessage(sb.toString());
					ls_proc.destroy();
					message.setMessage(message.getMessage() + "<br>备份信息已还原成功！");
				} catch (IOException e) {
					e.printStackTrace();
					message.setMessage(message.getMessage() + "<br>文件关闭失败！");
				} finally {
					try {
						if (in != null)
							in.close();
					} catch (IOException e) {
						message.setMessage(message.getMessage() + "<br>文件关闭失败！");
					}
				}
			} else
				message.setMessage("备份文件不存在！");
		}
		return message;
	}

	public List<Databasebackup> getAll() {
		DatabasebackupDao databasebackupDao = new DatabasebackupDao();
		return databasebackupDao.getAll();
	}
}
