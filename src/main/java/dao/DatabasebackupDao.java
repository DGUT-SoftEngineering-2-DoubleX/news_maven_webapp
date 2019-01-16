package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import bean.Databasebackup;
import bean.News;

public class DatabasebackupDao {
	public List<Databasebackup> getAll() {
		List<Databasebackup> databasebackups=null;
		DatabaseDao databaseDao=null;
		try {
			databasebackups=new ArrayList<Databasebackup>(); 
			databaseDao=new DatabaseDao();
			databaseDao.query("select * from databasebackup");
			while (databaseDao.next()) {
				Databasebackup databasebackup=new Databasebackup();
				databasebackup.setDatabasebackupId(databaseDao.getInt("databasebackupId"));
				databasebackup.setName(databaseDao.getString("name"));
				databasebackup.setDirectory(databaseDao.getString("directory"));
				databasebackup.setTime(databaseDao.getTimestamp("time"));
				databasebackups.add(databasebackup);
			}
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			if(databaseDao.close()<0)
				return null;			
		}
		return databasebackups;
	}
	
	public Integer add(Databasebackup databasebackup) {
		Integer result;
		databasebackup.setDirectory(databasebackup.getDirectory().replace("\\", "\\\\"));
		String sql="insert into databasebackup(name,directory) values("
				+"'"+databasebackup.getName()+"','"
				+databasebackup.getDirectory()+"')";
		
		DatabaseDao databaseDao=null;
		try {
			databaseDao=new DatabaseDao();
			result=databaseDao.update(sql);
		}  catch (Exception e) {
			e.printStackTrace();
			result=-1;
		} finally{
			if(databaseDao.close()<0)
				result=-2;			
		}
		return result;
	}
	
	public Databasebackup getById(Integer databasebackupId){
		Databasebackup databasebackup=null;
		DatabaseDao databaseDao=null;
		try {
			databaseDao=new DatabaseDao();
			databasebackup = new Databasebackup();
			
			databaseDao.getById("databasebackup", databasebackupId);
			while (databaseDao.next()) {			
				databasebackup.setDatabasebackupId(databaseDao.getInt("databasebackupId"));
				databasebackup.setDirectory(databaseDao.getString("directory"));
				databasebackup.setName(databaseDao.getString("name"));
				databasebackup.setTime(databaseDao.getTimestamp("time"));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			if(databaseDao.close()<0)
				return null;			
		}
		
		return databasebackup;
	}
}
