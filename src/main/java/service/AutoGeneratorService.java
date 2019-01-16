package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import bean.CommentUserView;
import bean.News;
import dao.CommentDao;
import dao.DatabaseDao;
import dao.NewsDao;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import tools.PageInformation;
import tools.Tool;
import tools.WebProperties;

public class AutoGeneratorService {
	public Integer generateNewsHtml(HttpServletRequest request){
		//创建一个合适的Configration对象
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);	
		configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
		configuration.setLocale(Locale.SIMPLIFIED_CHINESE);//设置地区，影响时间，数字等格式
		//设置模板所在的根文件夹
		configuration.setServletContextForTemplateLoading(request.getServletContext(),"/template/html"); 
		 // 建立数据模型 
	    Map rootMap = new HashMap(); 
	    List<News> newses=null;
	    DatabaseDao databaseDao=null;
	    try {
	    	databaseDao = new DatabaseDao();
			NewsDao newsDao = new NewsDao();
			
			newsDao.resetStaticHtml(databaseDao);//所有新闻均可以生成静态html
			newses = newsDao.getAll(databaseDao);
			CommentDao commentDao = new CommentDao();
			//评论分页信息
			PageInformation pageInformation = new PageInformation();
			Tool.getPageInformation("commentUserView", request, pageInformation);//新闻评论首页的分页信息
			pageInformation.setPage(1);//评论第一页
			pageInformation.setPageSize(2);//每页2条评论
			pageInformation.setOrder("desc");
			pageInformation.setOrderField("time");//排序字段
			//遍历每个新闻
			for (News news : newses) {
				rootMap.clear();//清空map中的数据

				pageInformation.setSearchSql(" (newsId=" + news.getNewsId() + ") ");
				List<CommentUserView> commentUserViews = commentDao.getOnePage(pageInformation, databaseDao);

				rootMap.put("news", news);
				rootMap.put("pageInformation", pageInformation);
				rootMap.put("commentUserViews", commentUserViews);
				rootMap.put("commentUserViewsSize", commentUserViews.size());

				String directory = request.getServletContext().getRealPath("/newsHtml");
				String yearString= String.valueOf(news.getNewsTime().getYear());
				String monthString=String.valueOf(news.getNewsTime().getMonth());
				
				directory += "\\" + yearString+ "\\"+ monthString;
				File nowDir = new File(directory);//文件夹				
				if (!nowDir.exists()) {//文件夹不存在
					nowDir.mkdirs();//创建文件夹
				}
				String filePath = directory + "\\" + news.getNewsId().toString() + ".html";//文件

				// 获取模板文件 
				Template template = configuration.getTemplate("aNewsShowTemplate.ftl");
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
				template.process(rootMap, out);//根据模板文件和数据，生成html文件	
				//网页地址
				if(news.getUrl().isEmpty()){
					String url=WebProperties.config.getString("newsHtmlRoot")+"/"+yearString + "/"
							+ monthString+ "/"+news.getNewsId().toString() + ".html";
					news.setUrl(url);
				}
			} 
			
			//开始事务处理
			databaseDao.setAutoCommit(false);
			newsDao.setStaticHtml(databaseDao);
			newsDao.batchUpdateUrl(newses,  databaseDao);			
			databaseDao.commit();
			databaseDao.setAutoCommit(true);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				databaseDao.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return -1;
	}
}
