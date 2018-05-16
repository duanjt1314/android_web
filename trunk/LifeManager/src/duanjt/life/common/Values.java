package duanjt.life.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.renderscript.Int3;

/**
 * 为了实现常用数据的快速转换而创建的类
 * 
 * @author 段江涛
 * 
 */
public class Values {	
	/*
	 * 转换为字符串
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
	
	//根据字符串格式2014-11-06获得Date格式
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
