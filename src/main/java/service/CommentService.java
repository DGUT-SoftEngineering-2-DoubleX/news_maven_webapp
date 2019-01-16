package service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jacob.com.ComThread;

import tools.JacobExcelTool;
import tools.JacobWordManager;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;
import dao.CommentDao;
import dao.DatabaseDao;
import dao.NewsDao;
import bean.Comment;
import bean.CommentNumberTopTenInAYear;
import bean.CommentUserView;

public class CommentService {
	public List<CommentUserView> getOnePage(PageInformation pageInformation) {
		List<CommentUserView> commentUserViews = new ArrayList<CommentUserView>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentDao commentDao = new CommentDao();
			commentUserViews = commentDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentUserViews;
	}

	// 点赞
	public Integer paise(Integer commentId) {
		try {
			CommentDao commentDao = new CommentDao();
			if (commentDao.paise(commentId) > 0)
				return 1;//
			else
				return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;//
		} catch (Exception e) {
			e.printStackTrace();
			return -3;//
		}
	}

	// 对新闻的回复，添加新评论
	public Integer addComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			Integer stair = commentDao.getStairByNewsId(comment.getNewsId(), databaseDao);
			comment.setStair(stair + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	// 对回复的回复
	public Integer addCommentToComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			CommentUserView oldCommentUserView = commentDao.getByIdFromView(comment.getCommentId(), databaseDao);
			String content = oldCommentUserView.getContent();
			if (content.contains("<br><br>")) {// 消除之前的留言
				content = content.substring(content.indexOf("<br><br>") + 8);
			}
			String s = "回复第" + oldCommentUserView.getStair() + "楼层&nbsp;" + oldCommentUserView.getUserName()
					+ "&nbsp;的留言：" + content + "<br><br>";

			comment.setContent(s + comment.getContent());
			Integer stair = commentDao.getStairByNewsId(comment.getNewsId(), databaseDao);
			comment.setStair(stair + 1);
			return commentDao.addComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	// 自定义函数，更新评论内容
	public Integer updateComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			return commentDao.updateComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	// 自定义函数，删除评论内容
	public Integer deleteComment(Comment comment) {
		CommentDao commentDao = new CommentDao();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			return commentDao.deleteComment(comment, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		} catch (Exception e) {
			e.printStackTrace();
			return -3;
		}
	}

	public String commentNumberByMonthInAYear(String year, HttpServletRequest request) {
		CommentDao commentDao = new CommentDao();
		CommentNumberTopTenInAYear commentNumberTopTenInAYear = new CommentNumberTopTenInAYear();
		try {
			commentNumberTopTenInAYear = commentDao.commentNumberByMonthInAYear(year);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

		Workbook wb = null;
		FileInputStream fis = null;
		String excelFileFullPath = "";
		try {
			String fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("excelTemplate"));// 获取相对路径的绝对路径
			excelFileFullPath = fullPath + "\\commentNumberTopTenInAYear.xlsm";
			// excel文件读写
			fis = new FileInputStream(excelFileFullPath);
			wb = new XSSFWorkbook(fis);
			fis.close();// 输入流使用后，及时关闭

			Sheet sheet = wb.getSheetAt(0);
			Row row;
			// 修改年度
			row = sheet.getRow(0);
			row.getCell(0).setCellValue(year + "评论发表数量前十排行");
			// 遍历excel行,插入数据库
			for (int i = 2; i <= 11; i++) {// 10行
				if (i <= commentNumberTopTenInAYear.getCommentList().size() + 1) {
					row = sheet.getRow(i);
					row.getCell(0).setCellValue(commentNumberTopTenInAYear.getCommentList().get(i - 2).getName());
					row.getCell(1).setCellValue(commentNumberTopTenInAYear.getCommentList().get(i - 2).getCount());
				} else {
					row = sheet.getRow(i);
					row.getCell(0).setCellValue("暂无用户");
					row.getCell(1).setCellValue(0);
				}

			}
			// 将最新的 Excel 内容写回到原始 Excel 文件中
			FileOutputStream excelFileOutPutStream = new FileOutputStream(excelFileFullPath);
			wb.write(excelFileOutPutStream);
			excelFileOutPutStream.flush();
			excelFileOutPutStream.close();

			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
			return "-2";
		}

		try {
			ComThread.InitSTA();// 仅允许同时运行一个线程，其他线程锁住
			JacobExcelTool tool = new JacobExcelTool();
			// 打开
			tool.OpenExcel(excelFileFullPath, false, false);
			// 调用Excel宏
			tool.callMacro("commentNumberTopTenInAYear");
			// 关闭并保存，释放对象
			tool.CloseExcel(true, true);

			String excelFile = "\\" + WebProperties.config.getString("projectName")
					+ WebProperties.config.getString("excelTemplate") + "\\commentNumberTopTenInAYear.xlsm";
			excelFile = excelFile.replace("\\", "/");
			return excelFile;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ComThread.Release();// 结束
		}
		return "-3";
	}

	public String commentNumberTopTenInAYearEveryYear(HttpServletRequest request) {
		CommentDao commentDao = new CommentDao();
		List<CommentNumberTopTenInAYear> CommentNumberTopTenInAYearList = null;
		try {
			CommentNumberTopTenInAYearList = commentDao.commentNumberTopTenInAYearEveryYear();
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		String wordFile = "-1";
		Workbook wb = null;
		FileInputStream fis = null;
		// 打开word
		ComThread.InitMTA(true);
		JacobWordManager jacobWordManager = null;
		try {
			int first = 1;

			for (CommentNumberTopTenInAYear commentNumberTopTenInAYear : CommentNumberTopTenInAYearList) {
				String fullPath = request.getServletContext()
						.getRealPath(WebProperties.config.getString("excelTemplate"));// 获取相对路径的绝对路径
				String excelFileFullPath = fullPath + "\\commentNumberTopTenInAYear1.xlsm";
				// 使用poi对excel文件读写 // 这里也可以使用jacob对excel文件进行读写，但poi的代码简单，可读性更强
				fis = new FileInputStream(excelFileFullPath);
				wb = new XSSFWorkbook(fis);
				fis.close();// 输入流使用后，及时关闭

				Sheet sheet = wb.getSheetAt(0);
				Row row;
				// 修改年度
				row = sheet.getRow(0);
				row.getCell(0).setCellValue(commentNumberTopTenInAYear.getYear() + "评论发表数量前十排行");
				// 遍历excel行,插入数据库
				for (int i = 2; i <= 11; i++) {// 10行
					if (i <= commentNumberTopTenInAYear.getCommentList().size() + 1) {
						row = sheet.getRow(i);
						row.getCell(0).setCellValue(commentNumberTopTenInAYear.getCommentList().get(i - 2).getName());
						row.getCell(1).setCellValue(commentNumberTopTenInAYear.getCommentList().get(i - 2).getCount());
					} else {
						row = sheet.getRow(i);
						row.getCell(0).setCellValue("暂无用户");
						row.getCell(1).setCellValue(0);
					}

				}

				// 将最新的 Excel 内容写回到原始 Excel 文件中
				FileOutputStream excelFileOutPutStream = new FileOutputStream(excelFileFullPath);
				wb.write(excelFileOutPutStream);
				excelFileOutPutStream.flush();
				excelFileOutPutStream.close();

				wb.close();

				// excel调用宏,poi无法调用宏
				JacobExcelTool jacobExcelTool = new JacobExcelTool();
				// 打开
				jacobExcelTool.OpenExcel(excelFileFullPath, false, false);
				// 调用Excel宏
				jacobExcelTool.callMacro("commentNumberTopTenInAYearEveryYear");
				// 关闭并保存，释放对象
				jacobExcelTool.CloseExcel(true, true);

				jacobWordManager = new JacobWordManager(false);
				// 单个年份的word文件
				fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("wordTemplate"));// 获取相对路径的绝对路径
				String wordTemplateFileFullPath = fullPath + "\\commentNumberTopTenInAYear.docm";

				String tempWord = fullPath + "\\temp.docm";

				jacobWordManager.openDocument(tempWord);
				jacobWordManager.callMacro("deleteAll");
				jacobWordManager.callMacro("pasteChart");
				jacobWordManager.copyContentFromAnotherDocInsertBefore(wordTemplateFileFullPath);// 复制wordTemplateFileFullPath文件内容到本文件中
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#year", commentNumberTopTenInAYear.getYear().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.callMacro("println");
				jacobWordManager.closeDocumentWithSave();// 关闭文件

				// 多个年份合并的word文件
				String wordFileFullPath = fullPath + "\\commentNumberTopTenInAYearAll.docm";
				jacobWordManager.openDocument(wordFileFullPath);

				if (first == 1) {// 输入第一年数据前，先将word文档清空（删除旧数据）
					jacobWordManager.callMacro("commentNumberTopTenInAYearAll");
					first++;
				}
				jacobWordManager.goToEnd();// 到文件尾
				jacobWordManager.copyContentFromAnotherDocInsertAfter(tempWord);// 复制wordTemplateFileFullPath文件内容到本文件中
				jacobWordManager.closeDocumentWithSave();
				jacobWordManager.close();// 关闭word程序
				// jacobWordManager.println();//换行
				// jacobWordManager.println();//换行

			}

			wordFile = "\\" + WebProperties.config.getString("projectName")
					+ WebProperties.config.getString("wordTemplate") + "\\commentNumberTopTenInAYearAll.docm";
			wordFile = wordFile.replace("\\", "/");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			wordFile = "-2";
		} finally {
			jacobWordManager.close();// 关闭word程序
			ComThread.Release();
		}
		return wordFile;
	}

}
