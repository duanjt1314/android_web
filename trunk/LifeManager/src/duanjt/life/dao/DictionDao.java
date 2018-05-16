package duanjt.life.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import duanjt.life.common.Values;
import duanjt.life.model.Diction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DictionDao {
	// ���ݿ�
	SQLiteDatabase db;

	public DictionDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}

	/**
	 * �����ֵ伯����Ϣ
	 * 
	 * @param diction
	 *            �ֵ�Json����
	 */
	public boolean Insert(String diction) {
		try {
			List<Diction> list = new ArrayList<Diction>();
			JSONArray jarr = new JSONArray(diction);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject index = jarr.getJSONObject(i);
				Diction dic = new Diction();
				dic.setId(index.getString("Id"));
				dic.setName(index.getString("Name"));
				dic.setNote(index.getString("Note"));
				dic.setParentId(index.getInt("ParentId"));
				dic.setOrderId(index.getInt("OrderId"));
				list.add(dic);
			}
			return Insert(list);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ͨ�����������ֵ���Ϣ
	 * 
	 * @param users
	 *            �ֵ�ʵ�弯��
	 */
	public boolean Insert(List<Diction> dics) {
		try {
			db.beginTransaction();

			for (int i = 0; i < dics.size(); i++) {
				String sql = "insert into Diction(Id,Name,Note,Parent_Id,Order_Id) values(?,?,?,?,?)";
				Object[] parm = new Object[] { dics.get(i).getId(), dics.get(i).getName(), dics.get(i).getNote(), dics.get(i).getParentId(), dics.get(i).getOrderId() };
				db.execSQL(sql, parm);
			}
			db.setTransactionSuccessful();
			db.endTransaction(); // �������
			return true;
		} catch (Exception e) {
			db.endTransaction(); // �������
			return false;
		}
	}

	/**
	 * ��������ֵ���Ϣ����
	 */
	public List<Diction> GetAll() {
		List<Diction> list = new ArrayList<Diction>();
		Cursor c = db.rawQuery("select * from Diction", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// �ƶ���ָ����¼
				Diction dic = new Diction();
				dic.setId(c.getString(c.getColumnIndex("Id")));
				dic.setName(c.getString(c.getColumnIndex("Name")));
				dic.setNote(c.getString(c.getColumnIndex("Note")));
				dic.setOrderId(c.getInt(c.getColumnIndex("Order_Id")));
				dic.setParentId(c.getInt(c.getColumnIndex("Parent_Id")));
				list.add(dic);
			}
		}
		c.close();
		return list;
	}

	/**
	 * ���ݸ�����Ų�ѯ�ֵ伯����Ϣ
	 * 
	 * @return
	 */
	public List<Diction> GetByParentId(int parentId) {
		List<Diction> list = new ArrayList<Diction>();
		Cursor c = db.rawQuery("select * from Diction where Parent_Id=" + parentId, new String[] {});

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// �ƶ���ָ����¼
				Diction dic = new Diction();
				dic.setId(c.getString(c.getColumnIndex("Id")));
				dic.setName(c.getString(c.getColumnIndex("Name")));
				dic.setNote(c.getString(c.getColumnIndex("Note")));
				dic.setOrderId(c.getInt(c.getColumnIndex("Order_Id")));
				dic.setParentId(c.getInt(c.getColumnIndex("Parent_Id")));
				list.add(dic);
			}
		}
		c.close();
		return list;
	}

	/**
	 * �����ֵ��Ż�ȡ�ֵ���Ϣ
	 * 
	 * @param id
	 * @return
	 */
	public Diction GetById(String id) {

		Cursor c = db.rawQuery("select * from Diction where Id=?", new String[] { id });

		if (c.getCount() > 0 && c.moveToFirst()) {
			c.moveToPosition(0);// �ƶ���ָ����¼
			Diction dic = new Diction();
			dic.setId(c.getString(c.getColumnIndex("Id")));
			dic.setName(c.getString(c.getColumnIndex("Name")));
			dic.setNote(c.getString(c.getColumnIndex("Note")));
			dic.setOrderId(c.getInt(c.getColumnIndex("Order_Id")));
			dic.setParentId(c.getInt(c.getColumnIndex("Parent_Id")));
			return dic;
		} else {
			return null;
		}
	}

	/**
	 * �����ֵ�����ѯ�ֵ����ƣ������ھͷ��ؿ��ַ���
	 * 
	 * @param id
	 * @return
	 */
	public String GetNameById(String id) {
		Diction diction = GetById(id);
		if (diction != null) {
			return diction.getName();
		} else {
			return id;
		}
	}

	/**
	 * ɾ�������ֵ���Ϣ
	 */
	public void DeleteAll() {
		String sql = "delete from Diction";
		db.execSQL(sql);
	}
}
