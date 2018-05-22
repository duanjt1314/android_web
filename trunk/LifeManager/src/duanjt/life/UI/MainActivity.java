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
		// 允许执行JavaScript
		webView.getSettings().setJavaScriptEnabled(true);
		// 设置默认页面
		webView.loadUrl("file:///android_asset/login.html");
		// 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
		webView.addJavascriptInterface(new Contact(), "contact");
		// 限制在WebView中打开网页，而不用默认浏览器
		webView.setWebViewClient(new WebViewClient());
		// 设置后将可以alert提示框
		webView.setWebChromeClient(new WebChromeClient());
		// 设置后将能后访问本地数据库(localStorage)
		webView.getSettings().setDomStorageEnabled(true);
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		SysConfig config = new SysConfigDao(getApplicationContext()).GetByKey("ServerUrl");
		Log.i("初始化数据", Common.ToJson(config));
		if (!config.equals(null)) {
			Common.systemUrl = config.getSysValue();
		}
	}

	/**
	 * 键盘单击事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 双击返回键退出程序
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new Contact().close();
		}
		return false;
	}

	private final class Contact {
		// region 全局数据访问变量
		LifingCostDao lifingCostDao = new LifingCostDao(getApplicationContext());
		IncomeDao incomeDao = new IncomeDao(getApplicationContext());
		DictionDao dictionDao = new DictionDao(getApplicationContext());
		BankCardDao bankCardDao = new BankCardDao(getApplicationContext());

		// endregion

		// region 系统提示
		/**
		 * 系统消息提示，中下部分弹出黑色提示框，2秒后自动消失
		 * 
		 * @param msg
		 */
		@android.webkit.JavascriptInterface
		public void showMsg(String msg) {
			CustomToast.showToast(getApplicationContext(), msg, 2000);
		}

		/**
		 * 弹出提示对话框，只有一个确定按钮
		 * 
		 * @param msg
		 */
		@android.webkit.JavascriptInterface
		public void alertMsg(String msg) {
			new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage(msg)
			//
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

						}
					}).show();
		}

		/**
		 * 弹出提示框，有是/否两个按钮，并指定不同选择的回调函数
		 * 
		 * @param content
		 *            -提示内容
		 * @param title
		 *            -提示标题
		 * @param okFunction
		 *            -是对应的回调函数
		 * @param cancleFunction
		 *            -否对应的回调函数
		 */
		@android.webkit.JavascriptInterface
		public void confirm(String content, String title, final String okFunction, final String cancleFunction) {
			new AlertDialog.Builder(MainActivity.this).setTitle(title).setMessage(content)//
					.setPositiveButton("是", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							webView.loadUrl("javascript:" + okFunction + "()");
						}
					}).setNegativeButton("否", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							webView.loadUrl("javascript:" + cancleFunction + "()");
						}
					}).show();
		}

		/**
		 * 公共的访问后台的方法，请求的数据和返回的数据都是JSON字符串
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
				Log.i("接收数据", content);
				HashMap map = null;
				try {
					map = Common.fromJson(content);
					// map=new Gson().fromJson(content, HashMap.class);
				} catch (Exception e) {
					return Common.ToJson(new Response(false, "接收的数据转换为字典失败,接收数据:" + content, null));
				}

				if (!map.containsKey("methodName")) {
					return Common.ToJson(new Response(false, "接收数据不包含键methodName,无法查找方法,接收数据:" + content, null));
				}

				String methodName = map.get("methodName").toString();
				Method m = Contact.class.getMethod(methodName, HashMap.class);
				if (m == null) {
					return Common.ToJson(new Response(false, "未找到方法:" + methodName + ",接收数据:" + content, null));
				}
				map.remove("methodName");
				Object result = m.invoke(this, map);// 统一返回Response对象
				if (result.getClass() != Response.class) {
					Log.e("异常", "方法:" + methodName + " 返回的不是Response对象");
				}
				Log.i("返回数据", Common.ToJson(result));
				return Common.ToJson(result);
			} catch (Exception ex) {
				Log.e("请求异常", ex.getMessage());
				return Common.ToJson(new Response(false, "出现了无法识别的异常," + ex.getMessage(), null));
			}
		}

		/**
		 * 公共后台访问方法，异步请求，将通过回调函数的方法响应
		 * 方法名是methodName，回调名称为callback。返回的数据将是Response序列化后的字符串
		 * 暂时还没有验证通过
		 * 
		 * @param content
		 */
		@android.webkit.JavascriptInterface
		public void requestAsyn(String content) {
			Thread thread = new MyThread(content);
			thread.start();
		}

		// endregion

		// region 登录和退出
		/**
		 * 登录
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
					return new Response(true, "成功", user);
				}
				return new Response(false, "用户名或密码错误", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 调用JS的Close方法，返回
		 */
		// Html调用此方法传递数据
		@android.webkit.JavascriptInterface
		public void close() {// 调用JS中的方法
			webView.loadUrl("javascript:close()");
		}

		/**
		 * 提供给Js调用，退出系统
		 */
		@android.webkit.JavascriptInterface
		public void exit() {
			finish();
			// System.exit(0);
		}

		// endregion

		// region 数据下载 and 上传
		/**
		 * 下载用户信息
		 * 
		 * @throws Exception
		 */
		public Response DownLoadUsers(HashMap map) {
			// 下载用户数据
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
			return new Response(true, "成功", null);
		}

		/**
		 * 下载字典信息
		 * 
		 * @throws Exception
		 */
		public Response DownLoadDiction(HashMap map) {
			// 下载用户数据
			int total = 0;
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			String result = Common.getActionResult("/Service/GetDictions", nvps);
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.getBoolean("success")) {
					new DictionDao(getApplicationContext()).DeleteAll();// 删除现有数据

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
			return new Response(true, "成功", null);
		}

		/**
		 * 生活费信息同步
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

					String jarr = jsonObject.getJSONObject("data").getString("rows");// 数据
					total = jsonObject.getJSONObject("data").getInt("total");// 总条数
					// 新增
					TypeToken<List<LifingCost>> listType = new TypeToken<List<LifingCost>>() {
					};
					List<LifingCost> lifes = new Gson().fromJson(jarr, listType.getType());
					new LifingCostDao(getApplicationContext()).Save(lifes);
				} else {
					// 失败
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				Log.e("同步数据异常", e.getMessage() + "\r\n" + result);
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "成功", total);
		}

		/**
		 * 纯收入同步
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

					String jarr = jsonObject.getJSONObject("data").getString("rows");// 数据
					total = jsonObject.getJSONObject("data").getInt("total");// 总条数
					// 新增
					TypeToken<List<Income>> listType = new TypeToken<List<Income>>() {
					};
					List<Income> incomes = new Gson().fromJson(jarr, listType.getType());
					new IncomeDao(getApplicationContext()).Save(incomes);
				} else {
					// 失败
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "成功", total);
		}

		/**
		 * 银行卡信息同步
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

					String jarr = jsonObject.getJSONObject("data").getString("rows");// 数据
					total = jsonObject.getJSONObject("data").getInt("total");// 总条数
					// 新增
					TypeToken<List<BankCard>> listType = new TypeToken<List<BankCard>>() {
					};
					List<BankCard> banks = new Gson().fromJson(jarr, listType.getType());
					new BankCardDao(getApplicationContext()).Save(banks);
				} else {
					// 失败
					return new Response(false, jsonObject.getString("msg"), null);
				}
			} catch (JSONException e) {
				return new Response(false, e.getMessage(), null);
			}
			return new Response(true, "成功", total);
		}

		/**
		 * 获取需要上传的数据的总数
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
				Log.e("请求异常", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 上传生活费信息
		 * 
		 * @return
		 */
		public Response UploadLifing(HashMap map) {
			try {
				Log.i("提示", "准备上传数据到" + Common.systemUrl);
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
					Log.i("上传数据内容", Common.ToJson(array));
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					nvps.add(new BasicNameValuePair("lifings", new Gson().toJson(array)));
					String result = Common.getActionResult("/Service/InsertLifingCost", nvps);
					Response res = (Response) Common.ToObject(result, Response.class);
					if (res.getSuccess()) {
						Log.i("日志", "请求的数据返回成功,将状态修改为已上传");
						lifingCostDao.ModifyIsUpload(list);
					}
					Log.i("请求返回", result);
					return new Gson().fromJson(result, Response.class);
				} else {
					Log.i("生活费", "没有需要上传的数据");
					return new Response(true, "没有需要上传的数据", null);
				}
			} catch (Exception ex) {
				Log.e("请求异常", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 上传银行卡信息
		 * 
		 * @return
		 */
		public Response UploadBankCard(HashMap map) {
			try {
				Log.i("提示", "准备上传数据到" + Common.systemUrl);
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
					Log.i("上传数据内容", Common.ToJson(array));
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					nvps.add(new BasicNameValuePair("bank", new Gson().toJson(array)));
					String result = Common.getActionResult("/Service/InsertBankCard", nvps);
					Response res = (Response) Common.ToObject(result, Response.class);
					if (res.getSuccess()) {
						Log.i("日志", "请求的数据返回成功,将状态修改为已上传");
						bankCardDao.ModifyIsUpload(list);
					}
					Log.i("请求返回", result);
					return new Gson().fromJson(result, Response.class);
				} else {
					Log.i("生活费", "没有需要上传的数据");
					return new Response(true, "没有需要上传的数据", null);
				}
			} catch (Exception ex) {
				Log.e("请求异常", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 上传纯收入信息
		 * 
		 * @return
		 */
		public Response UploadIncome(HashMap map) {
			try {
				Log.i("提示", "准备上传数据到" + Common.systemUrl);
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
					Log.i("上传数据内容", Common.ToJson(array));
					List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
					nvps.add(new BasicNameValuePair("income", new Gson().toJson(array)));
					String result = Common.getActionResult("/Service/InsertIncome", nvps);
					Response res = (Response) Common.ToObject(result, Response.class);
					if (res.getSuccess()) {
						Log.i("日志", "请求的数据返回成功,将状态修改为已上传");
						incomeDao.ModifyIsUpload(list);
					}
					return new Gson().fromJson(result, Response.class);
				} else {
					Log.i("生活费", "没有需要上传的数据");
					return new Response(true, "没有需要上传的数据", null);
				}
			} catch (Exception ex) {
				Log.e("请求异常", ex.getMessage());
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

		// region 系统配置
		/**
		 * 根据键获取配置项的值
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
		 * 修改系统配置
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
			Log.i("保存系统配置", Common.ToJson(config));
			new SysConfigDao(getApplicationContext()).Modify(config);
			if (key.equals("ServerUrl")) {
				Common.systemUrl = value;
			}
			return "true";
		}

		// endregion

		// region 字典信息
		/**
		 * 根据父级编号查询字典信息
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

		// region 用户信息

		/**
		 * 获取当前登录用户信息
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

		// region 生活费信息
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
				return new Response(true, "请求成功", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// 根据编号查询生活费信息
		public Response lifing_getbyid(HashMap map) {
			try {
				String id = map.get("id").toString();
				LifingCost index = new LifingCostDao(getApplicationContext()).getById(id);
				return new Response(true, "请求成功", index);
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
					return new Response(true, "保存成功", null);
				} else {
					return new Response(false, "保存失败", null);
				}
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response lifing_delete(HashMap map) {
			try {
				String id = map.get("id").toString();
				new LifingCostDao(getApplicationContext()).Delete(id);
				return new Response(true, "删除成功", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response lifing_clear(HashMap map) {
			try {
				new LifingCostDao(getApplicationContext()).DeleteAll();
				return new Response(true, "删除成功", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 按日统计消费信息
		 * 
		 * @return
		 */
		public Response lifing_GetCollectByDay(HashMap map) {
			try {
				List<HashMap<String, String>> list = lifingCostDao.GetCollectByDay();
				return new Response(true, "成功", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 按月统计消费信息
		 * 
		 * @return
		 */
		public Response lifing_GetCollectByMonth(HashMap map) {
			try {
				List<HashMap<String, String>> list = lifingCostDao.GetCollectByMonth();
				return new Response(true, "成功", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 统计最近一年的收支数据，按月份汇总
		 * 
		 * @param map
		 * @return
		 */
		public Response lifing_GetLifeIncomeYear(HashMap map) {
			try {
				DataTable table = lifingCostDao.GetLifeIncomeYear();
				return new Response(true, "成功", table);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

		// region 银行卡信息

		// 分页查询银行卡信息
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
				return new Response(true, "请求成功", list);
			} catch (Exception e) {
				return new Response(false, "失败," + e.getMessage(), null);
			}
		}

		// 根据编号查询银行卡信息
		public Response bank_getbyid(HashMap map) {
			String id = map.get("id").toString();
			BankCard bank = new BankCardDao(getApplicationContext()).getById(id);
			return new Response(true, "成功", bank);
		}

		// 保存银行卡信息
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
					return new Response(true, "保存成功", null);
				} else {
					return new Response(false, "保存失败", null);
				}
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response bank_delete(HashMap map) {
			try {
				String id = map.get("id").toString();
				bankCardDao.Delete(id);
				return new Response(true, "删除成功", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		/**
		 * 清除本地所有数据
		 * 
		 * @return
		 */
		public Response bank_clear(HashMap map) {
			try {
				new BankCardDao(getApplicationContext()).DeleteAll();
				return new Response(true, "数据清除成功", null);
			} catch (Exception e) {
				return new Response(false, "数据清除失败", null);
			}
		}

		// endregion

		// region 纯收入信息
		public Response income_getpage(HashMap map) {
			try {
				int pageIndex = Integer.parseInt(map.get("pageIndex").toString());
				int pageSize = Integer.parseInt(map.get("pageSize").toString());

				String sqlWhere = " and create_by='" + Common.currUsers.getId() + "'";
				List<Income> list = incomeDao.GetByPage(pageIndex, pageSize, sqlWhere);
				return new Response(true, "请求成功", list);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_delete(HashMap map) {
			try {
				String id = map.get("id").toString();
				incomeDao.Delete(id);
				return new Response(true, "删除成功", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_clear(HashMap map) {
			try {
				incomeDao.DeleteAll();
				return new Response(true, "删除成功", null);
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		public Response income_getbyid(HashMap map) {
			try {
				String id = map.get("id").toString();
				Income index = incomeDao.getById(id);
				return new Response(true, "请求成功", index);
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
					return new Response(true, "保存成功", null);
				} else {
					return new Response(false, "保存失败", null);
				}
			} catch (Exception ex) {
				return new Response(false, ex.getMessage(), null);
			}
		}

		// endregion

	}

	/**
	 * 异步执行的线程
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
				Log.i("异步接收数据", content);
				HashMap map = null;
				try {
					map = Common.fromJson(content);
					// map=new Gson().fromJson(content, HashMap.class);
				} catch (Exception e) {
					res = new Response(false, "接收的数据转换为字典失败,接收数据:" + content, null);
				}

				if (map != null) {
					if (!map.containsKey("methodName") || !map.containsKey("callback")) {
						res = new Response(false, "接收数据必须包含键methodName和callback,无法查找方法,接收数据:" + content, null);
					} else {
						String methodName = map.get("methodName").toString();
						String callback = map.get("callback").toString();
						Method m = Contact.class.getMethod(methodName, HashMap.class);
						if (m == null) {
							res = new Response(false, "未找到方法:" + methodName + ",接收数据:" + content, null);
						} else {
							map.remove("methodName");
							map.remove("callback");
							Object result = m.invoke(this, map);// 统一返回Response对象
							if (result.getClass() != Response.class) {
								Log.e("异常", "方法:" + methodName + " 返回的不是Response对象");
							}

							res = (Response) result;
							Log.i("调用回调函数", callback);
							Log.i("调用回调函数", Common.ToJson(result));
							webView.loadUrl(String.format("javascript:%s(%s)", callback,Common.ToJson(result)));
						}
					}
				}
			} catch (Exception ex) {
				Log.e("请求异常", ex.getMessage());
				res = new Response(false, "出现了无法识别的异常," + ex.getMessage(), null);
			}finally{				
				Log.i("finally打印", Common.ToJson(res));
			}

	    }
	}
}
