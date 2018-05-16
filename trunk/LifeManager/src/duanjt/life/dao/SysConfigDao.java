package duanjt.life.dao;

import java.util.ArrayList;
import java.util.List;

import duanjt.life.model.SysConfig;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SysConfigDao {
	// ���ݿ�
	SQLiteDatabase db;

	public SysConfigDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}
	
	/**
	 * �������ϵͳ������Ϣ����
	 */
	public List<SysConfig> GetAll() {
		List<SysConfig> list = new ArrayList<SysConfig>();
		Cursor c = db.rawQuery("select * from SysConfig", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// �ƶ���ָ����¼
				SysConfig config = new SysConfig();
				config.setId(c.getString(c.getColumnIndex("Id")));
				config.setSysKey(c.getString(c.getColumnIndex("SysKey")));
				config.setSysValue(c.getString(c.getColumnIndex("SysValue")));
				list.add(config);
			}
		}
		c.close();
		return list;
	}
	
	/**
	 * ���ݼ���ѯϵͳ������Ϣ
	 * @param key
	 * @return
	 */
	public SysConfig GetByKey(String key){
		Cursor c = db.rawQuery("select * from SysConfig where SysKey=?", new String[]{key});
		if(c.moveToFirst()){
			c.moveToPosition(0);
			SysConfig config=new SysConfig();
			config.setId(c.getString(c.getColumnIndex("Id")));
			config.setSysKey(c.getString(c.getColumnIndex("SysKey")));
			config.setSysValue(c.getString(c.getColumnIndex("SysValue")));
			return config;
		}else{
			return null;
		}		
	}
	
	/**
	 * �޸�ϵͳ������Ϣ
	 * @param config
	 */
	public void Modify(SysConfig config){
		String sql="update SysConfig set SysKey=?,SysValue=? where Id=?";
		db.execSQL(sql,new Object[]{config.getSysKey(),config.getSysValue(),config.getId()});		
	}
}
