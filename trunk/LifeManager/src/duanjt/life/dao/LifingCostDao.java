package duanjt.life.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import duanjt.life.common.Common;
import duanjt.life.common.Values;
import duanjt.life.model.LifingCost;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LifingCostDao {
	// 数据库
	SQLiteDatabase db;

	public LifingCostDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}

	// region 基础功能

	/**
	 * 新增模块集合信息
	 * 
	 * @param modules
	 *            模块Json集合
	 */
	public boolean Insert(String lifes) {
		try {
			List<LifingCost> list = new ArrayList<LifingCost>();
			JSONArray jarr = new JSONArray(lifes);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject index = jarr.getJSONObject(i);
				LifingCost life = new LifingCost();
				life.setId(index.getString("Id"));
				life.setTime(index.getString("Time"));
				life.setReason(index.getString("Reason"));
				life.setPrice(index.getString("Price"));
				life.setCostTypeId(index.getString("CostTypeId"));
				life.setNotes(index.getString("Notes"));
				life.setImgUrl(index.getString("ImgUrl"));
				life.setIsMark(index.getString("IsMark"));
				life.setFamilyPay(index.getString("FamilyPay"));
				life.setCreateBy(index.getString("CreateBy"));
				life.setCreateTime(index.getString("CreateTime"));
				life.setUpdateBy(index.getString("UpdateBy"));
				life.setUpdateTime(index.getString("UpdateTime"));
				life.setIsUpload("0");
				list.add(life);
			}
			return Insert(list);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 新增
	 * 
	 * @param index
	 * @return
	 */
	public boolean Insert(LifingCost index) {
		try {
			String sql = "insert into Lifing_Cost(Id,TIME,Reason,Price,Cost_Type_Id,Notes,Img_Url,IsMark,FamilyPay,Create_By,Create_Time,UpDate_By,UpDate_Time,CusGroup,is_upload ) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Object[] parm = new Object[] { index.getId(), index.getTime(), index.getReason(), index.getPrice(), index.getCostTypeId(), index.getNotes(), index.getImgUrl(), index.getIsMark(), index.getFamilyPay(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getCusGroup(), index.getIsUpload() };
			db.execSQL(sql, parm);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 保存生活费信息，如果存在则直接跳过
	 * 
	 * @param modules
	 *            模块实体集合
	 */
	public boolean Save(List<LifingCost> lifes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < lifes.size(); i++) {
				// 判断是否存在
				if (getById(lifes.get(i).getId()) == null) {
					String sql = "insert into Lifing_Cost(Id,TIME,Reason,Price,Cost_Type_Id,Notes,Img_Url,IsMark,FamilyPay,Create_By,Create_Time,UpDate_By,UpDate_Time,CusGroup,is_upload ) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					Object[] parm = new Object[] { lifes.get(i).getId(), lifes.get(i).getTime().substring(0, 10), lifes.get(i).getReason(), lifes.get(i).getPrice(), lifes.get(i).getCostTypeId(), lifes.get(i).getNotes(), lifes.get(i).getImgUrl(), lifes.get(i).getIsMark(), lifes.get(i).getFamilyPay(), lifes.get(i).getCreateBy(), lifes.get(i).getCreateTime(), lifes.get(i).getUpdateBy(), lifes.get(i).getUpdateTime(), lifes.get(i).getCusGroup(), "1" };
					db.execSQL(sql, parm);
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction(); // 处理完成
			return true;
		} catch (Exception e) {
			db.endTransaction(); // 处理完成
			return false;
		}
	}

	/**
	 * 通过集合增加模块信息
	 * 
	 * @param modules
	 *            模块实体集合
	 */
	public boolean Insert(List<LifingCost> lifes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < lifes.size(); i++) {
				String sql = "insert into Lifing_Cost(Id,TIME,Reason,Price,Cost_Type_Id,Notes,Img_Url,IsMark,FamilyPay,Create_By,Create_Time,UpDate_By,UpDate_Time,CusGroup,is_upload ) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] parm = new Object[] { lifes.get(i).getId(), lifes.get(i).getTime(), lifes.get(i).getReason(), lifes.get(i).getPrice(), lifes.get(i).getCostTypeId(), lifes.get(i).getNotes(), lifes.get(i).getImgUrl(), lifes.get(i).getIsMark(), lifes.get(i).getFamilyPay(), lifes.get(i).getCreateBy(), lifes.get(i).getCreateTime(), lifes.get(i).getUpdateBy(), lifes.get(i).getUpdateTime(), lifes.get(i).getCusGroup(), lifes.get(i).getIsUpload() };
				db.execSQL(sql, parm);
			}
			db.setTransactionSuccessful();
			db.endTransaction(); // 处理完成
			return true;
		} catch (Exception e) {
			db.endTransaction(); // 处理完成
			return false;
		}
	}

	/**
	 * 修改
	 * 
	 * @param life
	 * @return
	 */
	public boolean Modify(LifingCost life) {
		try {
			String sql = "update Lifing_Cost set Time=?,Reason=?,Price=?,Cost_Type_Id=?,IsMark=?,FamilyPay=?,Update_By=?,Update_Time=?,Notes=?,is_upload=? where Id=?";
			Object[] parm = new Object[] { life.getTime(), life.getReason(), life.getPrice(), life.getCostTypeId(), life.getIsMark(), life.getFamilyPay(), life.getUpdateBy(), life.getUpdateTime(), life.getNotes(), life.getIsUpload(), life.getId() };
			db.execSQL(sql, parm);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 查询所有消费信息
	 * 
	 * @return
	 */
	public List<LifingCost> GetAll() {
		return GetAll("");
	}

	/**
	 * 查询所有消费信息(按条件)
	 */
	public List<LifingCost> GetAll(String sqlWhere) {
		sqlWhere = " where 1=1 " + sqlWhere;
		List<LifingCost> list = new ArrayList<LifingCost>();
		Cursor c = db.rawQuery("select b.Name as CostTypeName,a.* from Lifing_Cost a inner join Diction b on a.Cost_Type_Id=b.Id " + sqlWhere + " order by Time desc,Create_Time desc", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				LifingCost life = new LifingCost();

				life.setId(c.getString(c.getColumnIndex("Id")));
				life.setTime(c.getString(c.getColumnIndex("Time")));
				life.setReason(c.getString(c.getColumnIndex("Reason")));
				life.setPrice(c.getString(c.getColumnIndex("Price")));
				life.setCostTypeId(c.getString(c.getColumnIndex("Cost_Type_Id")));
				life.setNotes(c.getString(c.getColumnIndex("Notes")));
				life.setImgUrl(c.getString(c.getColumnIndex("Img_Url")));
				life.setIsMark(c.getString(c.getColumnIndex("IsMark")));
				life.setFamilyPay(c.getString(c.getColumnIndex("FamilyPay")));
				life.setCreateBy(c.getString(c.getColumnIndex("Create_By")));
				life.setCreateTime(c.getString(c.getColumnIndex("Create_Time")));
				life.setUpdateBy(c.getString(c.getColumnIndex("Update_By")));
				life.setUpdateTime(c.getString(c.getColumnIndex("Update_Time")));
				life.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(life);
			}
		}
		c.close();
		return list;
	}

	/**
	 * 按条件查询所有数据
	 */
	public List<LifingCost> Gets(String sqlWhere) {
		List<LifingCost> list = new ArrayList<LifingCost>();
		Cursor c = db.rawQuery("select * from Lifing_Cost where 1=1 " + sqlWhere, null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				LifingCost life = new LifingCost();

				life.setId(c.getString(c.getColumnIndex("Id")));
				life.setTime(c.getString(c.getColumnIndex("Time")));
				life.setReason(c.getString(c.getColumnIndex("Reason")));
				life.setPrice(c.getString(c.getColumnIndex("Price")));
				life.setCostTypeId(c.getString(c.getColumnIndex("Cost_Type_Id")));
				life.setNotes(c.getString(c.getColumnIndex("Notes")));
				life.setImgUrl(c.getString(c.getColumnIndex("Img_Url")));
				life.setIsMark(c.getString(c.getColumnIndex("IsMark")));
				life.setFamilyPay(c.getString(c.getColumnIndex("FamilyPay")));
				life.setCreateBy(c.getString(c.getColumnIndex("Create_By")));
				life.setCreateTime(c.getString(c.getColumnIndex("Create_Time")));
				life.setUpdateBy(c.getString(c.getColumnIndex("Update_By")));
				life.setUpdateTime(c.getString(c.getColumnIndex("Update_Time")));
				life.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(life);
			}
		}
		c.close();
		return list;
	}

	/**
	 * 根据编号查询
	 * 
	 * @param id
	 * @return
	 */
	public LifingCost getById(String id) {
		LifingCost life = null;
		Cursor c = db.rawQuery("select * from Lifing_Cost where Id=? ", new String[] { id });

		if (c.moveToFirst()) {
			c.moveToPosition(0);// 移动到指定记录
			life = new LifingCost();
			life.setId(c.getString(c.getColumnIndex("Id")));
			life.setTime(c.getString(c.getColumnIndex("Time")));
			life.setReason(c.getString(c.getColumnIndex("Reason")));
			life.setPrice(c.getString(c.getColumnIndex("Price")));
			life.setCostTypeId(c.getString(c.getColumnIndex("Cost_Type_Id")));
			life.setNotes(c.getString(c.getColumnIndex("Notes")));
			life.setImgUrl(c.getString(c.getColumnIndex("Img_Url")));
			life.setIsMark(c.getString(c.getColumnIndex("IsMark")));
			life.setFamilyPay(c.getString(c.getColumnIndex("FamilyPay")));
			life.setCreateBy(c.getString(c.getColumnIndex("Create_By")));
			life.setCreateTime(c.getString(c.getColumnIndex("Create_Time")));
			life.setUpdateBy(c.getString(c.getColumnIndex("Update_By")));
			life.setUpdateTime(c.getString(c.getColumnIndex("Update_Time")));
			life.setIsUpload(c.getString(c.getColumnIndex("is_upload")));

		}
		c.close();
		return life;
	}

	public List<LifingCost> GetByPage(int pageIndex, int pageSize, String sqlWhere) {
		String sql = String.format("select * from Lifing_Cost where 1=1 %s  order by is_upload asc,time desc,create_time desc limit %d offset %d", //
				sqlWhere, pageSize, pageSize * (pageIndex - 1));
		List<LifingCost> list = new ArrayList<LifingCost>();
		Cursor c = db.rawQuery(sql, null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				LifingCost life = new LifingCost();

				life.setId(c.getString(c.getColumnIndex("Id")));
				life.setTime(c.getString(c.getColumnIndex("Time")));
				life.setReason(c.getString(c.getColumnIndex("Reason")));
				life.setPrice(c.getString(c.getColumnIndex("Price")));
				life.setCostTypeId(c.getString(c.getColumnIndex("Cost_Type_Id")));
				life.setNotes(c.getString(c.getColumnIndex("Notes")));
				life.setImgUrl(c.getString(c.getColumnIndex("Img_Url")));
				life.setIsMark(c.getString(c.getColumnIndex("IsMark")));
				life.setFamilyPay(c.getString(c.getColumnIndex("FamilyPay")));
				life.setCreateBy(c.getString(c.getColumnIndex("Create_By")));
				life.setCreateTime(c.getString(c.getColumnIndex("Create_Time")));
				life.setUpdateBy(c.getString(c.getColumnIndex("Update_By")));
				life.setUpdateTime(c.getString(c.getColumnIndex("Update_Time")));
				life.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(life);
			}
		}
		c.close();
		return list;
	}

	/**
	 * 删除所有模块信息
	 */
	public void DeleteAll() {
		String sql = "delete from Lifing_Cost";
		db.execSQL(sql);
	}

	/**
	 * 根据编号删除
	 * 
	 * @param id
	 */
	public void Delete(String id) {
		String sql = "delete from Lifing_Cost where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	/**
	 * 修改状态为已上传
	 * 
	 * @param list
	 */
	public boolean ModifyIsUpload(List<LifingCost> lifes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < lifes.size(); i++) {
				String sql = "update Lifing_Cost set is_upload='1' where Id=?";
				Object[] parm = new Object[] { lifes.get(i).getId(), };
				db.execSQL(sql, parm);
			}
			db.setTransactionSuccessful();
			db.endTransaction(); // 处理完成
			return true;
		} catch (Exception e) {
			db.endTransaction(); // 处理完成
			return false;
		}
	}

	// endregion

	// region 统计

	/**
	 * 按日统计消费信息，统计最近两个月的数据
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> GetCollectByDay() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select Time Time,sum(price) Price from Lifing_Cost where Time>=? group by time order by time desc";
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -60);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

		Cursor c = db.rawQuery(sql, new String[] { Common.formatDate(date, "yyyy-MM-dd") });
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Time", c.getString(c.getColumnIndex("Time")));
				map.put("Price", c.getString(c.getColumnIndex("Price")));
				list.add(map);
			}
		}
		c.close();
		return list;
	}

	/**
	 * 按月统计消费信息,统计最近一年的数据
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> GetCollectByMonth() {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select substr(time,0,8) Time,sum(price) Price from Lifing_Cost where Time>? group by substr(time,0,8) order by time desc";
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -365);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

		Cursor c = db.rawQuery(sql, new String[] { Common.formatDate(date, "yyyy-MM-dd") });
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Time", c.getString(c.getColumnIndex("Time")));
				map.put("Price", c.getString(c.getColumnIndex("Price")));
				list.add(map);
			}
		}
		c.close();
		return list;
	}

	// endregion

}
