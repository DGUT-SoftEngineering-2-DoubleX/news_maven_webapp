package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import tools.Tool;

public class DatabaseDao {
	public static String drv = "com.mysql.jdbc.Driver";// 数据库类型
	public static String url = "jdbc:mysql://localhost:3306/news";// 数据库网址
	public static String usr = "root";// 用户名
	public static String pwd = "123456";// 密码

	Connection connect = null;
	Statement stmt = null;
	ResultSet rs = null;

	boolean defaultCommit;

	public DatabaseDao() throws Exception {
		Class.forName(drv);
		connect = DriverManager.getConnection(url, usr, pwd);
		stmt = connect.createStatement();
	}

	public void query(String sql) throws SQLException {
		rs = stmt.executeQuery(sql);
	}

	public Integer update(String sql) throws SQLException {
		return stmt.executeUpdate(sql);
	}

	public boolean next() throws SQLException {// rs的下一条记录是否存在
		return rs.next();
	}

	public String getString(String field) throws SQLException {// 获取字符串类型字段的值，字段值为null型的，按照空字符串处理
		return rs.getString(field);
	}

	public Integer getInt(String field) throws SQLException {// 获取整数类型字段的值
		return rs.getInt(field);
	}

	public Timestamp getTimestamp(String field) throws SQLException {// 获取整数类型字段的值
		return rs.getTimestamp(field);
	}

	public LocalDateTime getLocalDateTime(String field) throws SQLException {// 获取日期时间类型字段的值
		return rs.getTimestamp(field).toLocalDateTime();
	}

	public Float getFloat(String field) throws SQLException {// 获取实数类型字段的值
		return rs.getFloat(field);
	}

	public ArrayList<String> FieldsList(String tableName) throws SQLException {// 获取表的字段名称，并保存到数组中
		ArrayList<String> fieldList = new ArrayList<String>();
		String sql = "select * from " + tableName + " limit 1";// limit
																// 1表示查询结果只包含一条记录
		query(sql);
		ResultSetMetaData fields = rs.getMetaData();// ResultSetMetaData记录了表的元数据，如字段名称，字段类型等

		for (int i = 1; i < fields.getColumnCount() + 1; i++) {// getColumnCount（）获取字段数据
			fieldList.add(fields.getColumnName(i));// getColumnName(i)获取字段名称
		}
		return fieldList;
	}

	public Integer getCount(String sql) throws SQLException {// 查询符合条件的记录的数目
		query(sql);
		while (next()) {
			return this.getRs().getInt("count1");
		}
		return 0;
	}

	// 根据表、字段，获取该字段上所有非重复值
	public ArrayList<String> getStringFieldValueByTableAndField(String tableName, String fieldName)
			throws SQLException {
		ArrayList<String> FieldValueList = new ArrayList<String>();
		query("select distinct " + fieldName + " from " + tableName);

		while (next()) {
			FieldValueList.add(getString(fieldName));
		}

		return FieldValueList;
	}

	// 修改某个表，某条记录（id）的某个字符型字段的值
	public Integer updateAStringFieldById(String tableName, Integer id, String fieldName, String fieldValue)
			throws SQLException {
		String sql = "update " + tableName + " set " + fieldName + "='" + fieldValue + "' where "
				+ tableName.toLowerCase() + "Id='" + id.toString() + "'";
		return update(sql);
	}

	// 修改表中某位用户的密码
	public Integer updateAStringFieldByIdAndOldPassword(String tableName, Integer id, String fieldName,
			String fieldValue, String oldFieldName, String oldPassword) throws SQLException {
		String sql = "update " + tableName + " set " + fieldName + "='" + fieldValue + "' where "
				+ tableName.toLowerCase() + "Id='" + id.toString() + "' and password='" + oldPassword + "'";
		return update(sql);
	}

	public void setAutoCommit(boolean f) throws SQLException {
		connect.setAutoCommit(f);
	}

	public void commit() throws SQLException {
		connect.commit();
	}

	public boolean hasId(String tableName, Integer id) throws SQLException {
		tableName = tableName.toLowerCase();
		String sql = "select * from " + tableName + " where " + tableName + "Id=" + id.toString();
		query(sql);
		while (next()) {
			return true;
		}
		return false;
	}

	public void getById(String tableName, Integer id) throws SQLException {
		tableName = tableName.toLowerCase();
		String sql = "select * from " + tableName + " where " + tableName + "Id=" + id.toString();
		query(sql);
	}

	// 删除多个用户
	public Integer deletes(String tableName, String ids, DatabaseDao databaseDao) throws SQLException {// 查询失败返回-1
		if (ids != null && ids.length() > 0) {
			String sql = "delete from " + tableName + " where " + tableName.toLowerCase() + "Id in (" + ids + ")";
			return databaseDao.update(sql);
		} else
			return -1;
	}

	public Connection getConnect() {
		return connect;
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

}
