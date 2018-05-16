package duanjt.life.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import duanjt.life.model.LifeNames;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LifeNamesDao {
	// 数据库
	SQLiteDatabase db;

	public LifeNamesDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}

	/**
	 * 新增集合信息
	 * 
	 * @param diction
	 *            字典Json集合
	 */
	public boolean Insert(String diction) {
		try {
			List<LifeNames> list = new ArrayList<LifeNames>();
			JSONArray jarr = new JSONArray(diction);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject index = jarr.getJSONObject(i);
				LifeNames dic = new LifeNames();
				dic.setId(index.getString("Id"));
				dic.setName(index.getString("Name"));
				list.add(dic);
			}
			return Insert(list);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 通过集合增加信息
	 * 
	 * @param users
	 *            字典实体集合
	 */
	public boolean Insert(List<LifeNames> dics) {
		try {
			db.beginTransaction();

			for (int i = 0; i < dics.size(); i++) {
				String sql = "insert into LifeNames(Id,Name) values(?,?)";
				Object[] parm = new Object[] { dics.get(i).getId(),//
						dics.get(i).getName() };
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
	 * 获得所有信息集合
	 */
	public List<LifeNames> GetAll() {
		List<LifeNames> list = new ArrayList<LifeNames>();
		Cursor c = db.rawQuery("select * from LifeNames", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				LifeNames dic = new LifeNames();
				dic.setId(c.getString(c.getColumnIndex("Id")));
				dic.setName(c.getString(c.getColumnIndex("Name")));
				list.add(dic);
			}
		}
		c.close();
		return list;
	}
	
	/**
	 * 获得名称
	 * @param name
	 * @return
	 */
	public List<String> GetByName(String name){
		List<String> list=new ArrayList<String>();
		Cursor c=db.rawQuery("select * from LifeNames where Name like '%"+name+"%' order by Name asc", null);
		if(c.moveToNext()){
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				list.add(c.getString(c.getColumnIndex("Name")));				
			}
		}
		c.close();
		return list;
	}

	/**
	 * 删除所有信息
	 */
	public void DeleteAll() {
		String sql = "delete from LifeNames";
		db.execSQL(sql);
	}
}
