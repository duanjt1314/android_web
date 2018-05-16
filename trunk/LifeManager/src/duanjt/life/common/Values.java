package duanjt.life.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.renderscript.Int3;

/**
 * Ϊ��ʵ�ֳ������ݵĿ���ת������������
 * 
 * @author �ν���
 * 
 */
public class Values {	
	/*
	 * ת��Ϊ�ַ���
	 */
	public static String getString(Object value) {
		return String.valueOf(value);
	}
	
	public static int getInt(Object value) {
		return Integer.parseInt(value.toString());
	}
	
	public static double getDouble(Object value) {
		return Double.parseDouble(value.toString());
	}
	
	public static Boolean getBoolean(Object value){
		if(value.toString().equals("1"))
			return true;
		return Boolean.getBoolean(value.toString());
	}
	
	//�����ַ�����ʽ2014-11-06���Date��ʽ
	public static Date getDate(String value){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = df.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
	}
	
	public char getChar(Object value){
		return value.toString().charAt(0);
	}
	
	public static Boolean isNull(String value){
		if(value.equals(""))
			return true;
		if(value.equals(null))
			return true;		 
		
		return false;
	}
}
