package duanjt.life.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import duanjt.life.common.Values;
import duanjt.life.model.Income;
import duanjt.life.model.LifingCost;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class IncomeDao {
	// ���ݿ�
	SQLiteDatabase db;

	public IncomeDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}

	/**
	 * ����
	 * 
	 * @param index
	 * @return
	 */
	public boolean Insert(Income index) {
		try {
			String sql = "insert into income(id,time,price,note,family_income,is_mark,cus_group,create_by,create_time,update_by,update_time,is_upload) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			Object[] parm = new Object[] { index.getId(), index.getTime(), index.getPrice(), index.getNote(), index.getFamilyIncome(), index.getIsMark(), index.getCusGroup(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getIsUpload() };
			db.execSQL(sql, parm);
			return true;
		} catch (Exception e) {
			Log.e("���洿�������", e.getMessage());
			return false;
		}
	}

	/**
	 * ͨ����������ģ����Ϣ
	 * 
	 * @param modules
	 *            ģ��ʵ�弯��
	 */
	public boolean Insert(List<Income> incomes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < incomes.size(); i++) {
				Income index = incomes.get(i);
				String sql = "insert into income(id,time,price,note,family_income,is_mark,cus_group,create_by,create_time,update_by,update_time,is_upload) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] parm = new Object[] { index.getId(), index.getTime(), index.getPrice(), index.getNote(), index.getFamilyIncome(), index.getIsMark(), index.getCusGroup(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getIsUpload() };
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
	 * �����������Ϣ�����������ֱ������
	 * 
	 * @param modules
	 *            ģ��ʵ�弯��
	 */
	public boolean Save(List<Income> incomes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < incomes.size(); i++) {
				// �ж��Ƿ����
				if (getById(incomes.get(i).getId()) == null) {
					String sql = "insert into income(id,time,price,note,family_income,is_mark,cus_group,create_by,create_time,update_by,update_time,is_upload) values(?,?,?,?,?,?,?,?,?,?,?,?)";
					Object[] parm = new Object[] { incomes.get(i).getId(), incomes.get(i).getTime().substring(0, 10), 
							incomes.get(i).getPrice(), incomes.get(i).getNote(), incomes.get(i).getFamilyIncome(), 
							incomes.get(i).getIsMark(), incomes.get(i).getCusGroup(), 
							incomes.get(i).getCreateBy(), incomes.get(i).getCreateTime(), incomes.get(i).getUpdateBy(),
							incomes.get(i).getUpdateTime(), "1" };
					db.execSQL(sql, parm);
				}
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
	 * �޸�
	 * 
	 * @param life
	 * @return
	 */
	public boolean Modify(Income index) {
		try {
			String sql = "update income set time=?,price=?,note=?,family_income=?,is_mark=?,create_by=?," + //
					"create_time=?,update_by=?,update_time=?,cus_group=?,is_upload=? where id=?";
			Object[] parm = new Object[] { index.getTime(), index.getPrice(), index.getNote(), //
					index.getFamilyIncome(), index.getIsMark(), index.getCreateBy(), index.getCreateTime(), //
					index.getUpdateBy(), index.getUpdateTime(), index.getCusGroup(), index.getIsUpload(), //
					index.getId() };
			db.execSQL(sql, parm);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * ��������ѯ��������
	 */
	public List<Income> Gets(String sqlWhere) {
		sqlWhere = " where 1=1 " + sqlWhere;
		List<Income> list = new ArrayList<Income>();
		Cursor c = db.rawQuery("select id,time,price,note,family_income,is_mark,create_by,create_time,update_by,update_time,cus_group,is_upload from income " + sqlWhere + " order by time desc,create_time desc", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// �ƶ���ָ����¼
				Income income = new Income();

				income.setId(c.getString(c.getColumnIndex("id")));
				income.setTime(c.getString(c.getColumnIndex("time")));
				income.setPrice(c.getString(c.getColumnIndex("price")));
				income.setNote(c.getString(c.getColumnIndex("note")));
				income.setFamilyIncome(c.getString(c.getColumnIndex("family_income")));
				income.setIsMark(c.getString(c.getColumnIndex("is_mark")));
				income.setCreateBy(c.getString(c.getColumnIndex("create_by")));
				income.setCreateTime(c.getString(c.getColumnIndex("create_time")));
				income.setUpdateBy(c.getString(c.getColumnIndex("update_by")));
				income.setUpdateTime(c.getString(c.getColumnIndex("update_time")));
				income.setCusGroup(c.getString(c.getColumnIndex("cus_group")));
				income.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(income);
			}
		}
		c.close();
		return list;
	}

	/**
	 * ���ݱ�Ų�ѯ
	 * 
	 * @param id
	 * @return
	 */
	public Income getById(String id) {
		Income income = null;
		Cursor c = db.rawQuery("select id,time,price,note,family_income,is_mark,create_by,create_time,update_by,update_time,cus_group,is_upload from income where id=? ", new String[] { id });

		if (c.moveToFirst()) {
			c.moveToPosition(0);// �ƶ���ָ����¼
			income = new Income();
			income.setId(c.getString(c.getColumnIndex("id")));
			income.setTime(c.getString(c.getColumnIndex("time")));
			income.setPrice(c.getString(c.getColumnIndex("price")));
			income.setNote(c.getString(c.getColumnIndex("note")));
			income.setFamilyIncome(c.getString(c.getColumnIndex("family_income")));
			income.setIsMark(c.getString(c.getColumnIndex("is_mark")));
			income.setCreateBy(c.getString(c.getColumnIndex("create_by")));
			income.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			income.setUpdateBy(c.getString(c.getColumnIndex("update_by")));
			income.setUpdateTime(c.getString(c.getColumnIndex("update_time")));
			income.setCusGroup(c.getString(c.getColumnIndex("cus_group")));
			income.setIsUpload(c.getString(c.getColumnIndex("is_upload")));

		}
		c.close();
		return income;
	}

	/**
	 * ��ҳ��ѯ������Ϣ
	 */
	public List<Income> GetByPage(int pageIndex, int pageSize, String sqlWhere) {
		String sql = String.format("select * from income where 1=1 %s  order by time desc,create_time desc limit %d offset %d", //
				sqlWhere, pageSize, pageSize * (pageIndex - 1));
		List<Income> list = new ArrayList<Income>();
		Cursor c = db.rawQuery(sql, null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// �ƶ���ָ����¼
				Income income = new Income();

				income.setId(c.getString(c.getColumnIndex("id")));
				income.setTime(c.getString(c.getColumnIndex("time")));
				income.setPrice(c.getString(c.getColumnIndex("price")));
				income.setNote(c.getString(c.getColumnIndex("note")));
				income.setFamilyIncome(c.getString(c.getColumnIndex("family_income")));
				income.setIsMark(c.getString(c.getColumnIndex("is_mark")));
				income.setCreateBy(c.getString(c.getColumnIndex("create_by")));
				income.setCreateTime(c.getString(c.getColumnIndex("create_time")));
				income.setUpdateBy(c.getString(c.getColumnIndex("update_by")));
				income.setUpdateTime(c.getString(c.getColumnIndex("update_time")));
				income.setCusGroup(c.getString(c.getColumnIndex("cus_group")));
				income.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(income);
			}
		}
		c.close();
		return list;
	}

	/**
	 * ɾ������ģ����Ϣ
	 */
	public void DeleteAll() {
		String sql = "delete from income";
		db.execSQL(sql);
	}

	/**
	 * ���ݱ��ɾ��
	 * 
	 * @param id
	 */
	public void Delete(String id) {
		String sql = "delete from income where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	/**
	 * �޸�״̬Ϊ���ϴ�
	 * 
	 * @param list
	 */
	public boolean ModifyIsUpload(List<Income> lifes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < lifes.size(); i++) {
				String sql = "update income set is_upload='1' where id=?";
				Object[] parm = new Object[] { lifes.get(i).getId(), };
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

}
