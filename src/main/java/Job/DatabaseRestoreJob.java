package Job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import service.DatabaseService;
import tools.Message;
import tools.Tool;

public class DatabaseRestoreJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		Tool.isMaintain = true;

		// 获取外部数据
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String jobName = dataMap.getString("jobName");
		System.out.println(jobName);

		// 备份数据库操作
		DatabaseService databaseService = new DatabaseService();
		Message message = databaseService.backup();

		Tool.isMaintain = false;
	}

}