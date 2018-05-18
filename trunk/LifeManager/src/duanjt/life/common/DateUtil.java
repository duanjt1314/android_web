package duanjt.life.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * ʱ������࣬ͳһʹ��java.util.date
 * 
 * @author Administrator
 *
 */
public class DateUtil {
	/**
	 * �����ڰ�ָ��ʱ���ʽ��ʽ��
	 * 
	 * @param date
	 *            ����ʽ������
	 * @param format
	 *            ��ʽ����ʽ
	 * @return ��ʽ������ַ���
	 */
	public static String Format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * �����ڰ�ָ��ʱ���ʽ��ʽ��,Ĭ�ϸ�ʽ����ʽ:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            ����ʽ������
	 * @return ��ʽ������ַ���
	 */
	public static String Format(Date date) {
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * ��ָ����������ָ�������������Ǹ���
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date AddDay(Date date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);// ��������������һ��.����������,������ǰ�ƶ�
		date = calendar.getTime(); // ���ʱ���������������һ��Ľ��
		return date;
	}
	
	/**
	 * ��ָ����������ָ�������������Ǹ���
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

