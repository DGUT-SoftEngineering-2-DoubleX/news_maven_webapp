package tools;

import javax.servlet.http.HttpServletRequest;

public class SearchTool {
	// 增加一个字符型与查询条件:模糊查询
	private static String addStringFuzzyAnd(String fieldName, String searchSql, HttpServletRequest request) {
		String param = request.getParameter(fieldName);
		if (param != null && !param.isEmpty() && !param.equals("all")) {
			if (searchSql.length() > 1) {// 已经有一个条件，需要用 and
				searchSql += " and " + fieldName + " like '%" + param + "%' ";
			} else {
				searchSql += " " + fieldName + " like '%" + param + "%' ";
			}
		}
		return searchSql;
	}

	// 增加一个字符型与查询条件:精确查询
	private static String addStringAnd(String fieldName, String searchSql, HttpServletRequest request) {
		String param = request.getParameter(fieldName);
		if (param != null && !param.isEmpty() && !param.equals("all")) {
			if (searchSql.length() > 1) {// 已经有一个条件，需要用 and
				searchSql += " and " + fieldName + "='" + param + "' ";
			} else {
				searchSql += " " + fieldName + "='" + param + "' ";
			}
		}
		return searchSql;
	}

	// 增加一个日期与查询条件:>=lowDate and <upDate
	private static String addDateAnd(String fieldName, String lowDateName, String upDateName, String searchSql,
			HttpServletRequest request) {
		String lowDate = request.getParameter(lowDateName);
		String upDate = request.getParameter(upDateName);

		if (lowDate != null && !lowDate.isEmpty()) {
			if (searchSql.length() > 1) {// 已经有一个条件，需要用 and
				searchSql += " and " + fieldName + ">='" + lowDate + "' ";
			} else {
				searchSql += " " + fieldName + ">='" + lowDate + "' ";
			}
		}

		if (upDate != null && !upDate.isEmpty()) {
			if (searchSql.length() > 1) {// 已经有一个条件，需要用 and
				searchSql += " and " + fieldName + "<'" + upDate + "' ";
			} else {
				searchSql += " " + fieldName + "<'" + upDate + "' ";
			}
		}
		return searchSql;
	}

	// 用户表的查询条件
	public static String user(HttpServletRequest request) {
		String searchSql = "";
		searchSql = addStringAnd("type", searchSql, request);
		searchSql = addStringFuzzyAnd("name", searchSql, request);
		searchSql = addStringAnd("enable", searchSql, request);
		searchSql = addDateAnd("registerDate", "lowDate", "upDate", searchSql, request);
		String a;
		return searchSql;
	}
}
