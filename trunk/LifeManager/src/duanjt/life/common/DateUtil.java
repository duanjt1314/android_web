package duanjt.life.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * 时间帮助类，统一使用java.util.date
 * 
 * @author Administrator
 *
 */
public class DateUtil {
	/**
	 * 将日期按指定时间格式格式化
	 * 
	 * @param date
	 *            待格式化日期
	 * @param format
	 *            格式化样式
	 * @return 格式化后的字符串
	 */
	public static String Format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 将日期按指定时间格式格式化,默认格式化样式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            待格式化日期
	 * @return 格式化后的字符串
	 */
	public static String Format(Date date) {
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 给指定日期增加指定天数，可以是负数
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date AddDay(Date date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	
	/**
	 * 给指定日期增加指定月数，可以是负数
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date AddMonth(Date date, int month) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MONTH, month);
		date = calendar.getTime(); 
		return date;
	}
	
	
}

