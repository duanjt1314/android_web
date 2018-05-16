package duanjt.life.common;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import duanjt.life.model.Users;

import android.util.Log;

public class Common {
	/**
	 * 服务器IP地址
	 */
	public static String systemUrl = "http://192.168.0.107:81";

	/**
	 * 当前登录用户
	 */
	public static Users currUsers = null;

	/**
	 * 通过方法和参数调用Post返回字符串
	 * 
	 * @param action
	 *            方法名称，如:/LifeMan/Manage/Login
	 * @param parms
	 *            参数集合
	 * @return 调用Post返回的字符串
	 */
	public static String getActionResult(String action, List<BasicNameValuePair> parms) {
		String result = "";

		// 创建HttpClient实例
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3 * 1000); // 连接超时设置
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000); // 响应超时设置
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		// 创建Get方法实例
		HttpPost httpPost = new HttpPost(systemUrl + action);
		// HttpGet httpGet=new HttpGet("");//Get方式访问，参数直接放在地址后面
		// 下面是Post方式访问的传参方式
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (int i = 0; i < parms.size(); i++) {
			nvps.add(parms.get(i));
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				result = convertStreamToString(instreams);// 获得结果

				httpPost.abort();
			}

		} catch (ClientProtocolException e) {
			System.out.println("错误:" + e.getMessage());
			Log.e("错误信息", Log.getStackTraceString(e));
			result = "{success:false,msg:'网络访问错误'}";
			result = Common.ToJson(new Response(false, "网络访问异常,请检查连接", null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("错误:" + e.getMessage());
			Log.e("错误信息", Log.getStackTraceString(e));
			result = Common.ToJson(new Response(false, "网络访问异常,请检查连接", null));
		}
		// 返回结果
		return result;
	}

	/**
	 * 日期格式转换
	 * 
	 * @param str
	 *            格式：/Date(1337184000000)/
	 * @return
	 */
	public static String parseDate(String str, String format) {
		if (str.equals("") || str.equals(null) || str.equals("null")) {
			return "";
		}
		String intTime = str.substring(str.indexOf('(') + 1, str.lastIndexOf(')'));
		Date d = new Date(Long.valueOf(intTime));
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateString = formatter.format(d);
		return dateString;
	}

	public static String parseDate(String str) {
		if (str.equals("") || str.equals(null) || str.equals("null")) {
			return "";
		}
		String intTime = str.substring(str.indexOf('(') + 1, str.lastIndexOf(')'));
		Date d = new Date(Long.valueOf(intTime));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateString = formatter.format(d);
		return dateString;
	}

	/**
	 * 获得当前时间字符串
	 * 
	 * @return
	 */
	public static String getCurrDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new java.util.Date());
		return date;
	}

	/**
	 * 获得当前日期字符串
	 * 
	 * @return
	 */
	public static String getCurrDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new java.util.Date());
		return date;
	}

	/**
	 * 指定日期按指定格式 格式化
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式化，如：yyyy-MM-dd
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String r = sdf.format(date);
		return r;
	}

	/**
	 * 根据输入流获得字符串
	 * 
	 * @param is
	 *            输入流
	 * @return 字符串
	 */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 根据传入的数字在前面增加‘0’到指定位数
	 * 
	 * @param 需要处理的数字
	 * @param 处理后总长度
	 * @return
	 */
	public static String GetString(int value, int length) {
		String str = String.valueOf(value);
		while (str.length() < length) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 将对象转换为Json字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String ToJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	/**
	 * 将字符串转换为HashMap，主要是解决int被转换为double的问题
	 * 
	 * @param str
	 * @return
	 * @throws JSONException
	 */
	public static HashMap fromJson(String str) throws JSONException {
		// Gson gson = new GsonBuilder().registerTypeAdapter(new
		// TypeToken<HashMap>() {
		// }.getType(), new JsonDeserializer<HashMap>() {
		//
		// @Override
		// public HashMap deserialize(JsonElement json, Type typeOfT,
		// JsonDeserializationContext context) throws JsonParseException {
		// HashMap map = new HashMap();
		// JsonObject jsonObject = json.getAsJsonObject();
		// Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
		// for (Map.Entry<String, JsonElement> entry : entrySet) {
		// map.put(entry.getKey(), entry.getValue());
		// }
		// return map;
		// }
		//
		// }).create();
		//
		// HashMap map = gson.fromJson(str, new TypeToken<HashMap>() {
		// }.getType());
		// return map;

		HashMap map = new HashMap();
		JSONObject jObject = new JSONObject(str);
		Iterator iterator = jObject.keys();
		while (iterator.hasNext()) {
			String key = iterator.next() + "";
			map.put(key, jObject.get(key));
		}
		return map;

	}

	/**
	 * 将字符串转为实体对象
	 * 
	 * @param str
	 * @param type
	 * @return
	 */
	public static Object ToObject(String str, Type type) {
		Gson gson = new Gson();
		return gson.fromJson(str, type);
	}

}
