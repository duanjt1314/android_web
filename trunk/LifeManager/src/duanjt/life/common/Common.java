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
	 * ������IP��ַ
	 */
	public static String systemUrl = "http://192.168.0.107:81";

	/**
	 * ��ǰ��¼�û�
	 */
	public static Users currUsers = null;

	/**
	 * ͨ�������Ͳ�������Post�����ַ���
	 * 
	 * @param action
	 *            �������ƣ���:/LifeMan/Manage/Login
	 * @param parms
	 *            ��������
	 * @return ����Post���ص��ַ���
	 */
	public static String getActionResult(String action, List<BasicNameValuePair> parms) {
		String result = "";

		// ����HttpClientʵ��
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3 * 1000); // ���ӳ�ʱ����
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000); // ��Ӧ��ʱ����
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		// ����Get����ʵ��
		HttpPost httpPost = new HttpPost(systemUrl + action);
		// HttpGet httpGet=new HttpGet("");//Get��ʽ���ʣ�����ֱ�ӷ��ڵ�ַ����
		// ������Post��ʽ���ʵĴ��η�ʽ
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
				result = convertStreamToString(instreams);// ��ý��

				httpPost.abort();
			}

		} catch (ClientProtocolException e) {
			System.out.println("����:" + e.getMessage());
			Log.e("������Ϣ", Log.getStackTraceString(e));
			result = "{success:false,msg:'������ʴ���'}";
			result = Common.ToJson(new Response(false, "��������쳣,��������", null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("����:" + e.getMessage());
			Log.e("������Ϣ", Log.getStackTraceString(e));
			result = Common.ToJson(new Response(false, "��������쳣,��������", null));
		}
		// ���ؽ��
		return result;
	}

	/**
	 * ���ڸ�ʽת��
	 * 
	 * @param str
	 *            ��ʽ��/Date(1337184000000)/
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
	 * ��õ�ǰʱ���ַ���
	 * 
	 * @return
	 */
	public static String getCurrDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new java.util.Date());
		return date;
	}

	/**
	 * ��õ�ǰ�����ַ���
	 * 
	 * @return
	 */
	public static String getCurrDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new java.util.Date());
		return date;
	}

	/**
	 * ָ�����ڰ�ָ����ʽ ��ʽ��
	 * 
	 * @param date
	 *            ����
	 * @param format
	 *            ��ʽ�����磺yyyy-MM-dd
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String r = sdf.format(date);
		return r;
	}

	/**
	 * ��������������ַ���
	 * 
	 * @param is
	 *            ������
	 * @return �ַ���
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
	 * ���ݴ����������ǰ�����ӡ�0����ָ��λ��
	 * 
	 * @param ��Ҫ���������
	 * @param ������ܳ���
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
	 * ������ת��ΪJson�ַ���
	 * 
	 * @param obj
	 * @return
	 */
	public static String ToJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	/**
	 * ���ַ���ת��ΪHashMap����Ҫ�ǽ��int��ת��Ϊdouble������
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
	 * ���ַ���תΪʵ�����
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
