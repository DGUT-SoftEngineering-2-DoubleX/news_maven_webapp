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
import dao.DatabaseDao;
import dao.NewsDao;
import dao.NewsDao;
import bean.ArticleNumberByMonthInAYear;
import bean.News;
import bean.News;

public class NewsService {
	public Integer add(News news) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			return newsDao.add(news, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public List<News> getOnePage(PageInformation pageInformation) {
		List<News> newses = new ArrayList<News>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			newses = newsDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newses;
	}

	// 自定义函数，获取某位新闻发布员发布的新闻内容
	public List<News> getOnePageByPublisherId(Integer publisherId, PageInformation pageInformation) {
		List<News> newses = new ArrayList<News>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			newses = newsDao.getOnePageByPublisherId(publisherId, pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newses;
	}

	public News getNewsById(Integer newsId) {
		NewsDao newsDao = new NewsDao();
		try {
			return newsDao.getById(newsId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 删除多条记录
	public List<News> deletes(PageInformation pageInformation) {
		List<News> newses = null;
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			newsDao.deletes(pageInformation.getTableName(), pageInformation.getIds(), databaseDao);
			pageInformation.setIds(null);
			newses = newsDao.getOnePage(pageInformation, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newses;
	}

	public List<List<News>> getByTypesTopN(String[] newsTypes, Integer n) {
		List<List<News>> newsesList = new ArrayList<List<News>>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			for (String type : newsTypes) {
				List<News> newses = newsDao.getByTypesTopN(type, n, databaseDao);
				newsesList.add(newses);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newsesList;
	}

	public List<List<News>> getByTypesTopN1(String[] newsTypes, Integer n, List<List<String>> newsCaptionsList) {
		List<List<News>> newsesList = new ArrayList<List<News>>();
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			for (String type : newsTypes) {
				List<News> newses = newsDao.getByTypesTopN(type, n, databaseDao);
				List<String> newsCaptions = new ArrayList<String>();
				for (News news : newses) {
					String newsCaption = Tool.getStringByMaxLength(news.getCaption(),
							Integer.parseInt(WebProperties.config.getString("homePageNewsCaptionMaxLength")));
					newsCaptions.add(newsCaption);
				}
				newsesList.add(newses);
				newsCaptionsList.add(newsCaptions);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newsesList;
	}

	public Integer update(News news) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			return newsDao.update(news, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public Integer passCheck(News news) {
		try {
			DatabaseDao databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			return newsDao.passCheck(news, databaseDao);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public String articleNumberByMonthInAYear(String year, HttpServletRequest request) {
		NewsDao newsDao = new NewsDao();
		List<Integer> articleNumberByMonthList = null;
		try {
			articleNumberByMonthList = newsDao.articleNumberByMonthInAYear(year);
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}

		Workbook wb = null;
		FileInputStream fis = null;
		String excelFileFullPath = "";
		try {
			String fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("excelTemplate"));// 获取相对路径的绝对路径
			excelFileFullPath = fullPath + "\\articleNumberByMonthInAYear.xlsm";
			// excel文件读写
			fis = new FileInputStream(excelFileFullPath);
			wb = new XSSFWorkbook(fis);
			fis.close();// 输入流使用后，及时关闭

			Sheet sheet = wb.getSheetAt(0);
			Row row;
			// 修改年度
			row = sheet.getRow(1);
			row.getCell(1).setCellValue(year + "年各月份发表的文章数");
			// 遍历excel行,插入数据库
			for (int i = 2; i <= 13; i++) {// 12行
				row = sheet.getRow(i);
				row.getCell(1).setCellValue(articleNumberByMonthList.get(i - 2));
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
			tool.callMacro("articleNumberByMonthInAYear");
			// 关闭并保存，释放对象
			tool.CloseExcel(true, true);

			String excelFile = "\\" + WebProperties.config.getString("projectName")
					+ WebProperties.config.getString("excelTemplate") + "\\articleNumberByMonthInAYear.xlsm";
			excelFile = excelFile.replace("\\", "/");
			return excelFile;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ComThread.Release();// 结束
		}
		return "-3";
	}

	public String articleNumberByMonthInAYearEveryYear(HttpServletRequest request) {
		NewsDao newsDao = new NewsDao();
		List<ArticleNumberByMonthInAYear> articleNumberByMonthInAYearEveryYearList = null;
		try {
			articleNumberByMonthInAYearEveryYearList = newsDao.articleNumberByMonthInAYearEveryYear();
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

			for (ArticleNumberByMonthInAYear ArticleNumberByMonthInAYear : articleNumberByMonthInAYearEveryYearList) {
				String fullPath = request.getServletContext()
						.getRealPath(WebProperties.config.getString("excelTemplate"));// 获取相对路径的绝对路径
				String excelFileFullPath = fullPath + "\\articleNumberByMonthInAYear1.xlsm";
				// 使用poi对excel文件读写 // 这里也可以使用jacob对excel文件进行读写，但poi的代码简单，可读性更强
				fis = new FileInputStream(excelFileFullPath);
				wb = new XSSFWorkbook(fis);
				fis.close();// 输入流使用后，及时关闭

				Sheet sheet = wb.getSheetAt(0);
				Row row;
				// 修改年度
				row = sheet.getRow(1);
				row.getCell(1).setCellValue(ArticleNumberByMonthInAYear.getYear() + "年各月份发表的文章数");
				// 遍历excel行
				for (int i = 2; i <= 13; i++) {// 12行
					row = sheet.getRow(i);
					row.getCell(1).setCellValue(ArticleNumberByMonthInAYear.getArticleNumberByMonthList().get(i - 2));
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
				jacobExcelTool.callMacro("articleNumberByMonthInAYear1");
				// 关闭并保存，释放对象
				jacobExcelTool.CloseExcel(true, true);

				jacobWordManager = new JacobWordManager(false);
				// 单个年份的word文件
				fullPath = request.getServletContext().getRealPath(WebProperties.config.getString("wordTemplate"));// 获取相对路径的绝对路径
				String wordTemplateFileFullPath = fullPath + "\\articleNumberByMonthInAYearEveryYear.docm";

				String tempWord = fullPath + "\\temp.docm";

				jacobWordManager.openDocument(tempWord);
				jacobWordManager.callMacro("deleteAll");
				jacobWordManager.callMacro("pasteChart");
				jacobWordManager.copyContentFromAnotherDocInsertBefore(wordTemplateFileFullPath);// 复制wordTemplateFileFullPath文件内容到本文件中
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#year", ArticleNumberByMonthInAYear.getYear().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#total", ArticleNumberByMonthInAYear.getTotalNewsNumber().toString());
				jacobWordManager.goToBegin();
				jacobWordManager.replaceText("#averageByMonth",
						"" + Tool.formatDouble(ArticleNumberByMonthInAYear.getTotalNewsNumber() * 1.0 / 12).toString());
				jacobWordManager.callMacro("println");
				jacobWordManager.closeDocumentWithSave();// 关闭文件

				// 多个年份合并的word文件
				String wordFileFullPath = fullPath + "\\articleNumberByMonthInAYearEveryYearAll.docm";
				jacobWordManager.openDocument(wordFileFullPath);

				if (first == 1) {// 输入第一年数据前，先将word文档清空（删除旧数据）
					jacobWordManager.callMacro("articleNumberByMonthInAYearEveryYearAll");
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
					+ WebProperties.config.getString("wordTemplate") + "\\articleNumberByMonthInAYearEveryYearAll.docm";
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
