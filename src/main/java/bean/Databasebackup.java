package bean;

import java.sql.Timestamp;

public class Databasebackup {
	private Integer databasebackupId;
	private String name;
	private Timestamp time;
	private String directory;

	public Integer getDatabasebackupId() {
		return databasebackupId;
	}

	public void setDatabasebackupId(Integer databasebackupId) {
		this.databasebackupId = databasebackupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
