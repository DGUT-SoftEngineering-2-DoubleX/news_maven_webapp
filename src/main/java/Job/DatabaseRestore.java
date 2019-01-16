package Job;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

public class DatabaseRestore {
	static public Scheduler scheduler;
	static public JobDetail jobDetail;
	static public CronTrigger cronTrigger;

	static public void createScheduler() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
	}

	static public void scheduleJob() throws SchedulerException {

		/* Write on 2018-12-9 by Assam */
		// String selectString="select * ";
		/* Finish on 2018-12-9 by Assam */

		jobDetail = JobBuilder.newJob(DatabaseRestoreJob.class).withIdentity("databaseRestoreJob", "databaseJobGroup")
				.build();

		// 通过JobDataMap给job传递数据
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		jobDataMap.put("jobName", "备份数据库");

		// cronTrigger 创建
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 28 10 * * ? *");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("databaseRestoreTrigger", "databaseTriggerGroup").withSchedule(cronScheduleBuilder)
				.build();

		DatabaseRestoreJobListener databaseRestoreJobListener = new DatabaseRestoreJobListener();
		// 监听部分的job
		Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey("databaseRestoreJob", "databaseJobGroup"));
		scheduler.getListenerManager().addJobListener(databaseRestoreJobListener, matcher);

		// 全局注册
		// sched.getListenerManager().addJobListener(new MyJobListener());
		// 监听特定组的job
		// GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals("group1");
		// sched.getListenerManager().addJobListener(new MyJobListener(),
		// matcher);

		// 执行
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	static public void start() throws SchedulerException {
		scheduler.start();
	}

}
