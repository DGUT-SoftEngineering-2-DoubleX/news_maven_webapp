package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tool {
	static public Map<String, String> pathMap = new Hashtable<String, String>();
	static public boolean isMaintain = false; // 网站是否处于维护状态

	// 从客户端获取分页、排序、删除等的参数
	public static void getPageInformation(String tableName, HttpServletRequest request,
			PageInformation pageInformation) {
		pageInformation.setTableName(tableName);

		String param = request.getParameter("pageSize");
		if (param == null || param.isEmpty())
			pageInformation.setPageSize(null);
		else
			pageInformation.setPageSize(Integer.parseInt(param));

		param = request.getParameter("totalPageCount");
		if (param == null || param.isEmpty())
			pageInformation.setTotalPageCount(null);
		else
			pageInformation.setTotalPageCount(Integer.parseInt(param));

		param = request.getParameter("allRecordCount");
		if (param == null || param.isEmpty())
			pageInformation.setAllRecordCount(null);
		else
			pageInformation.setAllRecordCount(Integer.parseInt(param));

		param = request.getParameter("page");
		if (param == null || param.isEmpty())
			pageInformation.setPage(null);
		else
			pageInformation.setPage(Integer.parseInt(param));

		pageInformation.setOrderField(request.getParameter("orderField"));
		pageInformation.setOrder(request.getParameter("order"));
		pageInformation.setIds(request.getParameter("ids"));
		pageInformation.setSearchSql(request.getParameter("searchSql"));
	}

	// 生成表的查询语句
	public static String getSql(PageInformation pageInformation, String type) {
		String sql = "";

		// 删除
		if (pageInformation.getIds() != null && !pageInformation.getIds().isEmpty()) {
			sql += "delete * from" + pageInformation.getTableName().toLowerCase() + " where "
					+ pageInformation.getTableName().toLowerCase() + "Id in (" + " " + pageInformation.getIds() + ") ";
		} else if ("count".equals(type)) {// 查询：只查符合条件的记录数目
			sql += "" + " select count(*) as count1 from " + pageInformation.getTableName().toLowerCase() + " ";
			// 查询条件
			if (pageInformation.getSearchSql() != null && !pageInformation.getSearchSql().isEmpty()) {
				sql += " where " + pageInformation.getSearchSql() + " ";
			}
		} else if ("select".equals(type)) {// 一般查询
			sql += "" + " select * from " + pageInformation.getTableName().toLowerCase() + " ";
			// 查询条件
			if (pageInformation.getSearchSql() != null && !pageInformation.getSearchSql().isEmpty()) {
				sql += " where " + pageInformation.getSearchSql() + " ";
			}
			// 排序,默认按主键的降序排列
			if (pageInformation.getOrderField() == null || pageInformation.getOrderField().isEmpty()) {
				sql += " ORDER BY " + pageInformation.getTableName() + "Id " + " desc ";
			} else {
				sql += " ORDER BY " + pageInformation.getOrderField() + " " + pageInformation.getOrder() + " ";
			}
			// 分页
			if (pageInformation.getPage() != null && pageInformation.getPage() > -100) {
				Integer start = (pageInformation.getPage() - 1) * pageInformation.getPageSize();

				sql += " limit " + start.toString() + "," + pageInformation.getPageSize();
			}
		}

		return sql;
	}

	//// 更新pageInformation的总页数等
	public static void setPageInformation(Integer allRecordCount, PageInformation pageInformation) {
		pageInformation.setAllRecordCount(allRecordCount);
		Integer totalPageCount = (int) Math.ceil(1.0 * allRecordCount / pageInformation.getPageSize());// 总页数
		pageInformation.setTotalPageCount(totalPageCount);

		// 防止页码越界 删除时有可能页码越界
		if (pageInformation.getPage() < 1)
			pageInformation.setPage(1);
		if (totalPageCount > 0 && pageInformation.getPage() > totalPageCount)
			pageInformation.setPage(totalPageCount);
	}

	// 从字符串中得到不超过maxLength的字符数量，其中汉字和全角字符占1个长度，英文和其他半角字符占0.5
	public static String getStringByMaxLength(String text, Integer maxLength) {
		int number = 0;
		maxLength *= 2;
		String resultString = "";
		for (int i = 0; i < text.length(); i++) {
			if (number < maxLength) {
				String b = text.substring(i, i + 1);
				if (b.getBytes().length == b.length() * 2) {// 中文，全角
					if (number == maxLength - 1) {// 只差一个字符,在最后加一个空格，保证长度为maxLength
						resultString += " ";
						break;
					}
					number += 2;
				} else// 半角，英文
					number++;

				resultString += text.substring(i, i + 1);
			} else
				break;
		}

		if (resultString.length() < text.length()) {// 要加..
			String b = resultString.substring(resultString.length() - 1);
			if (b.getBytes().length == b.length() * 2) {// 最后一个字符为中文，全角
				resultString = resultString.substring(0, resultString.length() - 1) + "..";
			} else {// 最后一个字符为半角字符
				b = resultString.substring(resultString.length() - 2, resultString.length() - 1);
				if (b.getBytes().length == b.length() * 2) {// 倒数第二个字符为中文，全角
					resultString = resultString.substring(0, resultString.length() - 2) + "...";
				} else {// 倒数第二个字符为半角字符
					resultString = resultString.substring(0, resultString.length() - 2) + "..";
				}
			}
		}
		return resultString;
	}

	static public Integer getRandomInRangeInteger(Integer min, Integer max) {
		int rand = min + (int) (Math.random() * ((max - min) + 1));
		return rand;
	}

	static public Long getSecondFromNow(Date old) {// 当前时间领先old多少秒
		Date now = new Date();
		long between = (now.getTime() - old.getTime()) / 1000;// 得到间隔秒数
		return between;
	}

	// 给ajax请求返回json格式的数据
	static public void returnJsonString(HttpServletResponse response, String jsonString)
			throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(jsonString);
		out.flush();
	}

	// 生成符合条件的随机字符串
	static public String getRandomString(Integer length, String firstChar, String remainString) {
		String result = "";
		int a = getRandomInRangeInteger(0, firstChar.length() - 1);
		result += firstChar.substring(a, a + 1);

		length--;
		for (int i = 0; i < length; i++) {
			a = getRandomInRangeInteger(0, remainString.length() - 1);
			result += remainString.substring(a, a + 1);
		}

		return result;
	}

	// 至少需要8个字符，以字母开头，以字母或数字结尾，可以有-和_
	static public String getRandomPassword() {
		Integer length = 8;
		String firstChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String remainString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

		return getRandomString(length, firstChar, remainString);
	}

	static public List<Integer> getListWithLengthInitIntValue(Integer length, Integer value) {
		List<Integer> myList = new ArrayList<Integer>();
		for (int i = 0; i < length; i++)
			myList.add(value);

		return myList;
	}

	// 保留两位小数，四舍五入的一个老土的方法
	public static Double formatDouble(Double d) {
		return new Double(Math.round(d * 100) * 1.0 / 100);
	}
}
