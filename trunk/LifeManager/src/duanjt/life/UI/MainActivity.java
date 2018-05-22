package duanjt.life.UI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lifemanager.R;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import duanjt.life.common.*;
import duanjt.life.common.model.DataTable;
import duanjt.life.dao.*;
import duanjt.life.model.*;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.*;
import android.webkit.*;

public class MainActivity extends Activity {

	WebView webView = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webView = (WebView) findViewById(R.id.webView1);
		// ����ִ��JavaScript
		webView.getSettings().setJavaScriptEnabled(true);
		// ����Ĭ��ҳ��
		webView.loadUrl("file:///android_asset/login.html");
		// ���һ������, ��JS���Է��ʸö���ķ���, �ö����п��Ե���JS�еķ���
		webView.addJavascriptInterface(new Contact(), "contact");
		// ������WebView�д���ҳ��������Ĭ�������
		webView.setWebViewClient(new WebViewClient());
		// ���ú󽫿���alert��ʾ��
		webView.setWebChromeClient(new WebChromeClient());
		// ���ú��ܺ���ʱ������ݿ�(localStorage)
		webView.getSettings().setDomStorageEnabled(true);
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		SysConfig config = new SysConfigDao(getApplicationContext()).GetByKey("ServerUrl");
		Log.i("��ʼ������", Common.ToJson(config));
		if (!config.equals(null)) {
			Common.systemUrl = config.getSysValue();
		}
	}

	/**
	 * ���̵����¼�
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ˫�����ؼ��˳�����
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Contact().close();
		}
		return false;
	}

	private final class Contact {
		// region ȫ�����ݷ��ʱ���
		LifingCostDao lifingCostDao = new LifingCostDao(getApplicationContext());
		IncomeDao incomeDao = new IncomeDao(getApplicationContext());
		DictionDao dictionDao = new DictionDao(getApplicationContext());
		BankCardDao bankCardDao = new BankCardDao(getApplicationContext());

		// endregion

		// region ϵͳ��ʾ
		/**
		 * ϵͳ��Ϣ��ʾ�����²��ֵ�����ɫ��ʾ��2����Զ���ʧ
		 * 
		 * @param msg
		 */
		@android.webkit.JavascriptInterface
		public void showMsg(String msg) {
			CustomToast.showToast(getApplicationContext(), msg, 2000);
		}

		/**
		 * ������ʾ�Ի���ֻ��һ��ȷ����ť
		 * 
		 * @param msg
		 */
		@android.webkit.JavascriptInterface
		public void alertMsg(String msg) {
			new AlertDialog.Builder(MainActivity.this).setTitle("��ʾ").setMessage(msg)
			//
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

						}
					}).show();
		}

		/**
		 * ������ʾ������/��������ť����ָ����ͬѡ��Ļص�����
		 * 
		 * @param content
		 *            -��ʾ����
		 * @param title
		 *            -��ʾ����
		 * @param okFunction
		 *            -�Ƕ�Ӧ�Ļص�����
		 * @param cancleFunction
		 *            -���Ӧ�Ļص�����
		 */
		@android.webkit.JavascriptInterface
		public void confirm(String content, String title, final String okFunction, final String cancleFunction) {
			new AlertDialog.Builder(MainActivity.this).setTitle(title).setMessage(content)//
					.setPositiveButton("��", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							webView.loadUrl("javascript:" + okFunction + "()");
						}
					}).setNegativeButton("��", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							webView.loadUrl("javascript:" + cancleFunction + "()");
						}
					}).show();
		}

		/**
		 * �����ķ��ʺ�̨�ķ�������������ݺͷ��ص����ݶ���JSON�ַ���
		 * 
		 * @param content
		 * @throws NoSuchMethodException
		 * @throws InvocationTargetException
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 */
		@android.webkit.JavascriptInterface
		public String request(String content) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			try {
				Log.i("��������", content);
				HashMap map = null;
				try {
					map = Common.fromJson(content);
					// map=new Gson().fromJson(content, HashMap.class);
				} catch (Exception e) {
					return Common.ToJson(new Response(false, "���յ�����ת��Ϊ�ֵ�ʧ��,��������:" + content, null));
				}

				if (!map.containsKey("methodName")) {
					return Common.ToJson(new Response(false, "�������ݲ�������methodName,�޷����ҷ���,��������:" + content, null));
				}

				String methodName = map.get("methodName").toString();
				Method m = Contact.class.getMethod(methodName, HashMap.class);
				if (m == null) {
					return Common.ToJson(new Response(false, "δ�ҵ�����:" + methodName + ",��������:" + content, null));
				}
				map.remove("methodName");
				Object result = m.invoke(this, map);// ͳһ����Response����
				if (result.getClass() != Response.class) {
					Log.e("�쳣", "����:" + methodName + " ���صĲ���Response����");
				}
				Log.i("��������", Common.ToJson(result));
				return Common.ToJson(result);
			} catch (Exception ex) {
				Log.e("�����쳣", ex.getMessage());
				return Common.ToJson(new Response(false, "�������޷�ʶ����쳣," + ex.getMessage(), null));
			}
		}

		/**
		 * ������̨���ʷ������첽���󣬽�ͨ���ص������ķ�����Ӧ
		 * ��������methodName���ص�����Ϊcallback�����ص����ݽ���Response���л�����ַ���
		 * ��ʱ��û����֤ͨ��
		 * 
		 * @param content
		 */
		@android.webkit.JavascriptInterface
		public void requestAsyn(String content) {
			Thread thread = new MyThread(content);
			thread.start();
		}

		// endregion

		// region ��¼���˳�
		/**
		 * ��¼
		 * 
		 * @param hash
		 * @return
		 */
		public Response login(HashMap hash) {
			try {
				String loginId = hash.get("loginId").toString();
				String loginPwd = hash.get("loginPwd").toString();
				Users user = new UsersDao(getApplicationContext()).Login(loginId, MD5.GetMD5Code(loginPwd));
				if (user != null) {
					Common.currUsers = user;
					return new Response(true, "�ɹ�", user);
				}
				return new Response(false, "�û������������", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * ����JS��Close����������
		 */
		// Html���ô˷�����������
		@android.webkit.JavascriptInterface
		public void close() {// ����JS�еķ���
			webView.loadUrl("javascript:close()");
		}

		/**
		 * �ṩ��Js���ã��˳�ϵͳ
		 */
		@android.webkit.JavascriptInterface
		public void exit() {
			finish();
			// System.exit(0);
		}

		// endregion

		// region �������� and �ϴ�
		/**
		 * �����û���Ϣ
		 * 
		 * @throws Exception
		 */
		public Response DownLoadUsers(HashMap map) {
			// �����û�����
			int total = 0;
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			String result = Common.getActionResult("/Service/GetAllUsers", nvps);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getBoolean("success")) {
					new UsersDao(getApplicationContext()).DeleteAll();

					String jarr = jsonObject.getString("data");
					boolean con = new UsersDao(getApplicationContext()).Insert(jarr);
					if (con) {
						total = jsonObject.getJSONArray("data").length();
					}
				} else {
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "�ɹ�", null);
		}

		/**
		 * �����ֵ���Ϣ
		 * 
		 * @throws Exception
		 */
		public Response DownLoadDiction(HashMap map) {
			// �����û�����
			int total = 0;
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			String result = Common.getActionResult("/Service/GetDictions", nvps);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getBoolean("success")) {
					new DictionDao(getApplicationContext()).DeleteAll();// ɾ����������

					String jarr = jsonObject.getString("data");
					boolean con = new DictionDao(getApplicationContext()).Insert(jarr);
					if (con) {
						total = jsonObject.getJSONArray("data").length();
					}
				} else {
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "�ɹ�", null);
		}

		/**
		 * �������Ϣͬ��
		 * 
		 * @param pageSize
		 * @param start
		 * @return
		 */
		public Response DownLifing(HashMap map) {
			String pageSize = map.get("pageSize").toString();
			String start = map.get("start").toString();
			int total = 0;
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("pageSize", pageSize));
			nvps.add(new BasicNameValuePair("start", start));
			nvps.add(new BasicNameValuePair("createBy", Common.currUsers.getId()));
			String result = Common.getActionResult("/Service/GetLifingCost", nvps);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getBoolean("success")) {

					String jarr = jsonObject.getJSONObject("data").getString("rows");// ����
					total = jsonObject.getJSONObject("data").getInt("total");// ������
					// ����
					TypeToken<List<LifingCost>> listType = new TypeToken<List<LifingCost>>() {
					};
					List<LifingCost> lifes = new Gson().fromJson(jarr, listType.getType());
					new LifingCostDao(getApplicationContext()).Save(lifes);
				} else {
					// ʧ��
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				Log.e("ͬ�������쳣", e.getMessage() + "\r\n" + result);
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "�ɹ�", total);
		}

		/**
		 * ������ͬ��
		 * 
		 * @param pageSize
		 * @param start
		 * @return
		 */
		public Response DownIncome(HashMap map) {
			int total = 0;
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("pageSize", map.get("pageSize").toString()));
			nvps.add(new BasicNameValuePair("start", map.get("start").toString()));
			nvps.add(new BasicNameValuePair("createBy", Common.currUsers.getId()));
			String result = Common.getActionResult("/Service/GetIncome", nvps);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getBoolean("success")) {

					String jarr = jsonObject.getJSONObject("data").getString("rows");// ����
					total = jsonObject.getJSONObject("data").getInt("total");// ������
					// ����
					TypeToken<List<Income>> listType = new TypeToken<List<Income>>() {
					};
					List<Income> incomes = new Gson().fromJson(jarr, listType.getType());
					new IncomeDao(getApplicationContext()).Save(incomes);
				} else {
					// ʧ��
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "�ɹ�", total);
		}

		/**
		 * ���п���Ϣͬ��
		 * 
		 * @param pageSize
		 * @param start
		 * @return
		 */
		public Response DownBankCard(HashMap map) {
			int total = 0;
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("pageSize", map.get("pageSize").toString()));
			nvps.add(new BasicNameValuePair("start", map.get("start").toString()));
			// nvps.add(new BasicNameValuePair("createBy",
			// Common.currUsers.getId()));
			String result = Common.getActionResult("/Service/GetBankCard", nvps);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getBoolean("success")) {

					String jarr = jsonObject.getJSONObject("data").getString("rows");// ����
					total = jsonObject.getJSONObject("data").getInt("total");// ������
					// ����
					TypeToken<List<BankCard>> listType = new TypeToken<List<BankCard>>() {
					};
					List<BankCard> banks = new Gson().fromJson(jarr, listType.getType());
					new BankCardDao(getApplicationContext()).Save(banks);
				} else {
					// ʧ��
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "�ɹ�", total);
		}

		/**
		 * ��ȡ��Ҫ�ϴ������ݵ�����
		 * 
		 * @return
		 */
		public Response GetUploadCount(HashMap map) {
			try {
				List<LifingCost> list = new LifingCostDao(getApplicationContext()).Gets("and is_upload='0' and create_by='" + Common.currUsers.getId() + "'");
				int count = list.size();
				count += incomeDao.Gets("and is_upload='0' and create_by='" + Common.currUsers.getId() + "'").size();
				count += bankCardDao.Gets("and is_upload='0' and create_by='" + Common.currUsers.getId() + "'").size();
				return new Response(true, "", count);
			} catch (Exception ex) {
				Log.e("�����쳣", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * �ϴ��������Ϣ
		 * 
		 * @return
		 */
		public Response UploadLifing(HashMap map) {
			try {
				Log.i("��ʾ", "׼���ϴ����ݵ�" + Common.systemUrl);
				List<LifingCost> list = new LifingCostDao(getApplicationContext()).Gets("and is_upload='0' and create_by='" + Common.currUsers.getId() + "'");
				if (list.size() > 0) {
					JsonArray array = new JsonArray();
					for (int i = 0; i < list.size(); i++) {
						JsonObject obj = new JsonObject();
						obj.addProperty("Id", list.get(i).getId());
						obj.addProperty("Time", list.get(i).getTime());
						obj.addProperty("Reason", list.get(i).getReason());
						obj.addProperty("Price", list.get(i).getPrice());
						obj.addProperty("CostTypeId", list.get(i).getCostTypeId());
						obj.addProperty("Notes", list.get(i).getNotes());
						obj.addProperty("IsMark", list.get(i).getIsMark());
						obj.addProperty("FamilyPay", list.get(i).getFamilyPay());
						obj.addProperty("CreateBy", list.get(i).getCreateBy());
						obj.addProperty("CreateTime", list.get(i).getCreateTime());
						obj.addProperty("UpdateBy", list.get(i).getUpdateBy());
						obj.addProperty("UpdateTime", list.get(i).getUpdateTime());
						array.add(obj);
					}
					Log.i("�ϴ���������", Common.ToJson(array));
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					nvps.add(new BasicNameValuePair("lifings", new Gson().toJson(array)));
					String result = Common.getActionResult("/Service/InsertLifingCost", nvps);
					Response res = (Response) Common.ToObject(result, Response.class);
					if (res.getSuccess()) {
						Log.i("��־", "��������ݷ��سɹ�,��״̬�޸�Ϊ���ϴ�");
						lifingCostDao.ModifyIsUpload(list);
					}
					Log.i("���󷵻�", result);
					return new Gson().fromJson(result, Response.class);
				} else {
					Log.i("�����", "û����Ҫ�ϴ�������");
					return new Response(true, "û����Ҫ�ϴ�������", null);
				}
			} catch (Exception ex) {
				Log.e("�����쳣", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * �ϴ����п���Ϣ
		 * 
		 * @return
		 */
		public Response UploadBankCard(HashMap map) {
			try {
				Log.i("��ʾ", "׼���ϴ����ݵ�" + Common.systemUrl);
				List<BankCard> list = bankCardDao.Gets("and is_upload='0' and create_by='" + Common.currUsers.getId() + "'");
				if (list.size() > 0) {
					JsonArray array = new JsonArray();
					for (int i = 0; i < list.size(); i++) {
						JsonObject obj = new JsonObject();
						obj.addProperty("Id", list.get(i).getId());
						obj.addProperty("Time", list.get(i).getTime());
						obj.addProperty("Price", list.get(i).getPrice());
						obj.addProperty("Note", list.get(i).getNote());
						obj.addProperty("SaveType", list.get(i).getSaveType());
						obj.addProperty("BankType", list.get(i).getBankType());
						obj.addProperty("CreateBy", list.get(i).getCreateBy());
						obj.addProperty("CreateTime", list.get(i).getCreateTime());
						obj.addProperty("UpdateBy", list.get(i).getUpdateBy());
						obj.addProperty("UpdateTime", list.get(i).getUpdateTime());
						array.add(obj);
					}
					Log.i("�ϴ���������", Common.ToJson(array));
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					nvps.add(new BasicNameValuePair("bank", new Gson().toJson(array)));
					String result = Common.getActionResult("/Service/InsertBankCard", nvps);
					Response res = (Response) Common.ToObject(result, Response.class);
					if (res.getSuccess()) {
						Log.i("��־", "��������ݷ��سɹ�,��״̬�޸�Ϊ���ϴ�");
						bankCardDao.ModifyIsUpload(list);
					}
					Log.i("���󷵻�", result);
					return new Gson().fromJson(result, Response.class);
				} else {
					Log.i("�����", "û����Ҫ�ϴ�������");
					return new Response(true, "û����Ҫ�ϴ�������", null);
				}
			} catch (Exception ex) {
				Log.e("�����쳣", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * �ϴ���������Ϣ
		 * 
		 * @return
		 */
		public Response UploadIncome(HashMap map) {
			try {
				Log.i("��ʾ", "׼���ϴ����ݵ�" + Common.systemUrl);
				List<Income> list = incomeDao.Gets("and is_upload='0' and create_by='" + Common.currUsers.getId() + "'");
				if (list.size() > 0) {
					JsonArray array = new JsonArray();
					for (int i = 0; i < list.size(); i++) {
						JsonObject obj = new JsonObject();
						obj.addProperty("Id", list.get(i).getId());
						obj.addProperty("Time", list.get(i).getTime());
						obj.addProperty("Price", list.get(i).getPrice());
						obj.addProperty("Note", list.get(i).getNote());
						obj.addProperty("IsMark", list.get(i).getIsMark());
						obj.addProperty("FamilyIncome", list.get(i).getFamilyIncome());
						obj.addProperty("CreateBy", list.get(i).getCreateBy());
						obj.addProperty("CreateTime", list.get(i).getCreateTime());
						obj.addProperty("UpdateBy", list.get(i).getUpdateBy());
						obj.addProperty("UpdateTime", list.get(i).getUpdateTime());
						array.add(obj);
					}
					Log.i("�ϴ���������", Common.ToJson(array));
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					nvps.add(new BasicNameValuePair("income", new Gson().toJson(array)));
					String result = Common.getActionResult("/Service/InsertIncome", nvps);
					Response res = (Response) Common.ToObject(result, Response.class);
					if (res.getSuccess()) {
						Log.i("��־", "��������ݷ��سɹ�,��״̬�޸�Ϊ���ϴ�");
						incomeDao.ModifyIsUpload(list);
					}
					return new Gson().fromJson(result, Response.class);
				} else {
					Log.i("�����", "û����Ҫ�ϴ�������");
					return new Response(true, "û����Ҫ�ϴ�������", null);
				}
			} catch (Exception ex) {
				Log.e("�����쳣", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

		// region ϵͳ����
		/**
		 * ���ݼ���ȡ�������ֵ
		 * 
		 * @param key
		 * @return
		 */
		@android.webkit.JavascriptInterface
		public String sysConfig_getByKey(String key) {
			SysConfig config = new SysConfigDao(getApplicationContext()).GetByKey(key);
			return config.getSysValue();
		}

		/**
		 * �޸�ϵͳ����
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		@android.webkit.JavascriptInterface
		public String sysConfig_update(String key, String value) {
			SysConfig config = new SysConfigDao(getApplicationContext()).GetByKey(key);
			if (config != null) {
				config.setSysValue(value);
			}
			Log.i("����ϵͳ����", Common.ToJson(config));
			new SysConfigDao(getApplicationContext()).Modify(config);
			if (key.equals("ServerUrl")) {
				Common.systemUrl = value;
			}
			return "true";
		}

		// endregion

		// region �ֵ���Ϣ
		/**
		 * ���ݸ�����Ų�ѯ�ֵ���Ϣ
		 * 
		 * @return
		 */
		public Response diction_list(HashMap map) {
			try {
				int parentId = Integer.parseInt(map.get("parentId").toString());
				List<Diction> list = new DictionDao(getApplicationContext()).GetByParentId(parentId);
				return new Response(true, "", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

		// region �û���Ϣ

		/**
		 * ��ȡ��ǰ��¼�û���Ϣ
		 * 
		 * @return
		 */
		public Response GetCurrUser(HashMap map) {
			try {
				return new Response(true, "", Common.currUsers);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

		// region �������Ϣ
		public Response lifing_getpage(HashMap map) {
			try {
				int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
				int pageSize = Integer.parseInt(map.get("pageSize").toString());
				String keyWord = map.get("keyWord").toString();
				String sqlWhere = "";
				if (!keyWord.equals("")) {
					sqlWhere = " and (reason like '%@key%' or notes like '%@key%' or price='@key') and create_by='" + Common.currUsers.getId() + "'";
					sqlWhere = sqlWhere.replace("@key", keyWord);
				}
				List<LifingCost> list = new LifingCostDao(getApplicationContext()).GetByPage(pageIndex, pageSize, sqlWhere);
				for (int i = 0; i < list.size(); i++) {
					String typeName = new DictionDao(getApplicationContext()).GetNameById(list.get(i).getCostTypeId());
					list.get(i).setCostTypeName(typeName);
				}
				return new Response(true, "����ɹ�", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// ���ݱ�Ų�ѯ�������Ϣ
		public Response lifing_getbyid(HashMap map) {
			try {
				String id = map.get("id").toString();
				LifingCost index = new LifingCostDao(getApplicationContext()).getById(id);
				return new Response(true, "����ɹ�", index);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response lifing_save(HashMap map) {
			try {
				String objString = map.get("objString").toString();
				Boolean con = false;
				LifingCost index = (LifingCost) Common.ToObject(objString, LifingCost.class);
				index.setIsUpload("0");
				if (index.getId().equals("")) {
					index.setId(UUID.randomUUID().toString());
					index.setCreateTime(Common.getCurrDateTime());
					index.setUpdateTime(Common.getCurrDateTime());
					index.setCreateBy(Common.currUsers.getId());
					index.setUpdateBy(Common.currUsers.getId());
					con = new LifingCostDao(getApplicationContext()).Insert(index);
				} else {
					LifingCost l = lifingCostDao.getById(index.getId());
					index.setCreateBy(l.getCreateBy());
					index.setCreateTime(l.getCreateTime());
					index.setUpdateTime(Common.getCurrDateTime());
					index.setUpdateBy(Common.currUsers.getId());
					con = new LifingCostDao(getApplicationContext()).Modify(index);
				}
				if (con) {
					return new Response(true, "����ɹ�", null);
				} else {
					return new Response(false, "����ʧ��", null);
				}
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response lifing_delete(HashMap map) {
			try {
				String id = map.get("id").toString();
				new LifingCostDao(getApplicationContext()).Delete(id);
				return new Response(true, "ɾ���ɹ�", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response lifing_clear(HashMap map) {
			try {
				new LifingCostDao(getApplicationContext()).DeleteAll();
				return new Response(true, "ɾ���ɹ�", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * ����ͳ��������Ϣ
		 * 
		 * @return
		 */
		public Response lifing_GetCollectByDay(HashMap map) {
			try {
				List<HashMap<String, String>> list = lifingCostDao.GetCollectByDay();
				return new Response(true, "�ɹ�", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * ����ͳ��������Ϣ
		 * 
		 * @return
		 */
		public Response lifing_GetCollectByMonth(HashMap map) {
			try {
				List<HashMap<String, String>> list = lifingCostDao.GetCollectByMonth();
				return new Response(true, "�ɹ�", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * ͳ�����һ�����֧���ݣ����·ݻ���
		 * 
		 * @param map
		 * @return
		 */
		public Response lifing_GetLifeIncomeYear(HashMap map) {
			try {
				DataTable table = lifingCostDao.GetLifeIncomeYear();
				return new Response(true, "�ɹ�", table);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

		// region ���п���Ϣ

		// ��ҳ��ѯ���п���Ϣ
		public Response bank_getpage(HashMap map) {
			try {
				int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
				int pageSize = Integer.parseInt(map.get("pageSize").toString());
				String bankType = map.get("bankType").toString();
				String sqlWhere = "";
				if (!Values.isNull(bankType)) {
					sqlWhere += " and bank_type='" + bankType + "'";
				}
				List<BankCard> list = new BankCardDao(getApplicationContext()).GetByPage(pageIndex, pageSize, sqlWhere);
				for (int i = 0; i < list.size(); i++) {
					String bankName = new DictionDao(getApplicationContext()).GetNameById(list.get(i).getBankType());
					list.get(i).setBankTypeName(bankName);
				}
				return new Response(true, "����ɹ�", list);
			} catch (Exception e) {
				return new Response(false, "ʧ��," + e.getMessage(), null);
			}
		}

		// ���ݱ�Ų�ѯ���п���Ϣ
		public Response bank_getbyid(HashMap map) {
			String id = map.get("id").toString();
			BankCard bank = new BankCardDao(getApplicationContext()).getById(id);
			return new Response(true, "�ɹ�", bank);
		}

		// �������п���Ϣ
		public Response bank_save(HashMap map) {
			try {
				String objString = map.get("objString").toString();
				Boolean con = false;
				BankCard index = (BankCard) Common.ToObject(objString, BankCard.class);
				index.setIsUpload("0");
				if (Double.parseDouble(index.getPrice()) >= 0) {
					index.setSaveType("1000200001");
				} else {
					index.setSaveType("1000200002");
				}
				index.setPrice(Math.abs(Double.parseDouble(index.getPrice())) + "");
				if (index.getId().equals("")) {
					index.setId(UUID.randomUUID().toString());
					index.setCreateTime(Common.getCurrDateTime());
					index.setUpdateTime(Common.getCurrDateTime());
					index.setCreateBy(Common.currUsers.getId());
					index.setUpdateBy(Common.currUsers.getId());
					con = bankCardDao.Insert(index);
				} else {
					BankCard l = bankCardDao.getById(index.getId());
					index.setCreateBy(l.getCreateBy());
					index.setCreateTime(l.getCreateTime());
					index.setUpdateTime(Common.getCurrDateTime());
					index.setUpdateBy(Common.currUsers.getId());
					con = bankCardDao.Modify(index);
				}
				if (con) {
					return new Response(true, "����ɹ�", null);
				} else {
					return new Response(false, "����ʧ��", null);
				}
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response bank_delete(HashMap map) {
			try {
				String id = map.get("id").toString();
				bankCardDao.Delete(id);
				return new Response(true, "ɾ���ɹ�", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * ���������������
		 * 
		 * @return
		 */
		public Response bank_clear(HashMap map) {
			try {
				new BankCardDao(getApplicationContext()).DeleteAll();
				return new Response(true, "��������ɹ�", null);
			} catch (Exception e) {
				return new Response(false, "�������ʧ��", null);
			}
		}

		// endregion

		// region ��������Ϣ
		public Response income_getpage(HashMap map) {
			try {
				int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
				int pageSize = Integer.parseInt(map.get("pageSize").toString());

				String sqlWhere = " and create_by='" + Common.currUsers.getId() + "'";
				List<Income> list = incomeDao.GetByPage(pageIndex, pageSize, sqlWhere);
				return new Response(true, "����ɹ�", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_delete(HashMap map) {
			try {
				String id = map.get("id").toString();
				incomeDao.Delete(id);
				return new Response(true, "ɾ���ɹ�", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_clear(HashMap map) {
			try {
				incomeDao.DeleteAll();
				return new Response(true, "ɾ���ɹ�", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_getbyid(HashMap map) {
			try {
				String id = map.get("id").toString();
				Income index = incomeDao.getById(id);
				return new Response(true, "����ɹ�", index);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_save(HashMap map) {
			try {
				String objString = map.get("objString").toString();
				Boolean con = false;
				Income index = (Income) Common.ToObject(objString, Income.class);
				index.setIsUpload("0");
				if (index.getId().equals("")) {
					index.setId(UUID.randomUUID().toString());
					index.setCreateTime(Common.getCurrDateTime());
					index.setUpdateTime(Common.getCurrDateTime());
					index.setCreateBy(Common.currUsers.getId());
					index.setUpdateBy(Common.currUsers.getId());
					con = incomeDao.Insert(index);
				} else {
					Income i = incomeDao.getById(index.getId());
					index.setCreateBy(i.getCreateBy());
					index.setCreateTime(i.getCreateTime());
					index.setUpdateTime(Common.getCurrDateTime());
					index.setUpdateBy(Common.currUsers.getId());
					con = incomeDao.Modify(index);
				}
				if (con) {
					return new Response(true, "����ɹ�", null);
				} else {
					return new Response(false, "����ʧ��", null);
				}
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

	}

	/**
	 * �첽ִ�е��߳�
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyThread extends Thread {
		private String content;

		public MyThread(String content) {
			this.content = content;
		}
		
		public void run()
	    {
			Response res = null;
			try {
				Log.i("�첽��������", content);
				HashMap map = null;
				try {
					map = Common.fromJson(content);
					// map=new Gson().fromJson(content, HashMap.class);
				} catch (Exception e) {
					res = new Response(false, "���յ�����ת��Ϊ�ֵ�ʧ��,��������:" + content, null);
				}

				if (map != null) {
					if (!map.containsKey("methodName") || !map.containsKey("callback")) {
						res = new Response(false, "�������ݱ��������methodName��callback,�޷����ҷ���,��������:" + content, null);
					} else {
						String methodName = map.get("methodName").toString();
						String callback = map.get("callback").toString();
						Method m = Contact.class.getMethod(methodName, HashMap.class);
						if (m == null) {
							res = new Response(false, "δ�ҵ�����:" + methodName + ",��������:" + content, null);
						} else {
							map.remove("methodName");
							map.remove("callback");
							Object result = m.invoke(this, map);// ͳһ����Response����
							if (result.getClass() != Response.class) {
								Log.e("�쳣", "����:" + methodName + " ���صĲ���Response����");
							}

							res = (Response) result;
							Log.i("���ûص�����", callback);
							Log.i("���ûص�����", Common.ToJson(result));
							webView.loadUrl(String.format("javascript:%s(%s)", callback,Common.ToJson(result)));
						}
					}
				}
			} catch (Exception ex) {
				Log.e("�����쳣", ex.getMessage());
				res = new Response(false, "�������޷�ʶ����쳣," + ex.getMessage(), null);
			}finally{				
				Log.i("finally��ӡ", Common.ToJson(res));
			}

	    }
	}
}
