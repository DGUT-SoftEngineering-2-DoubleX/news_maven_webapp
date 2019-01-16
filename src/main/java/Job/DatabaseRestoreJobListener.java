package Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class DatabaseRestoreJobListener implements JobListener {
	// getName() 方法返回一个字符串用以说明 JobListener 的名称。对于注册为全局的监听器，getName()
	// 主要用于记录日志，对于由特定 Job 引用的 JobListener，注册在 JobDetail 上的监听器名称必须匹配从监听器上
	// getName() 方法的返回值。
	public String getName() {
		return "DatabaseRestoreJobListener";
	}

	// Scheduler 在 JobDetail 将要被执行时调用这个方法。
	public void jobToBeExecuted(JobExecutionContext context) {
		System.out.println("DatabaseRestoreJob：即将开始！");
	}

	// Scheduler 在 JobDetail 即将被执行，但又被 TriggerListener 否决了时调用这个方法。
	public void jobExecutionVetoed(JobExecutionContext context) {
		System.out.println("DatabaseRestoreJob被 TriggerListener 否决！");
	}

	// Scheduler 在 JobDetail 被执行之后调用这个方法。
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		System.out.println("DatabaseRestoreJob：执行完毕！");
	}

}
