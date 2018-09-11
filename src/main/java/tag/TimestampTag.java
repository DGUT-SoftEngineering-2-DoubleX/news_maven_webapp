package tag;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TimestampTag extends SimpleTagSupport {
	private Timestamp dateTime;
	private String type;

	public void doTag() throws JspException, IOException {
		SimpleDateFormat df;
		if (type != null) {
			String time = "";
			if (type.equals("YMDHMS-")) {
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				time = df.format(dateTime);
			} else if (type.equals("YMD-")) {
				df = new SimpleDateFormat("yyyy-MM-dd");
				time = df.format(dateTime);
			} else if (type.equals("YMD-short")) {
				df = new SimpleDateFormat("yy-MM-dd");
				time = df.format(dateTime);
			} else if (type.equals("latest")) {
				Calendar now = Calendar.getInstance();
				Calendar dateTimeC = Calendar.getInstance();
				dateTimeC.setTime(dateTime);
				if (now.get(Calendar.YEAR) > dateTimeC.get(Calendar.YEAR))
					time = String.valueOf(now.get(Calendar.YEAR) - dateTimeC.get(Calendar.YEAR)) + "年前";
				else if (now.get(Calendar.MONTH) > dateTimeC.get(Calendar.MONTH))
					time = String.valueOf(now.get(Calendar.MONTH) - dateTimeC.get(Calendar.MONTH)) + "月前";
				else if (now.get(Calendar.DAY_OF_MONTH) > dateTimeC.get(Calendar.DAY_OF_MONTH))
					time = String.valueOf(now.get(Calendar.DAY_OF_MONTH) - dateTimeC.get(Calendar.DAY_OF_MONTH)) + "天前";
				else if (now.get(Calendar.HOUR_OF_DAY) > dateTimeC.get(Calendar.HOUR_OF_DAY))
					time = String.valueOf(now.get(Calendar.HOUR_OF_DAY) - dateTimeC.get(Calendar.HOUR_OF_DAY)) + "小时前";
				else if (now.get(Calendar.MINUTE) > dateTimeC.get(Calendar.MINUTE))
					time = String.valueOf(now.get(Calendar.MINUTE) - dateTimeC.get(Calendar.MINUTE)) + "分钟前";
				else if (now.get(Calendar.SECOND) > dateTimeC.get(Calendar.SECOND))
					time = String.valueOf(now.get(Calendar.SECOND) - dateTimeC.get(Calendar.SECOND)) + "秒前";
				else
					time = "刚刚";
			}
			this.getJspContext().getOut().write(time);
		}
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
