package duanjt.life.dao;

import java.util.ArrayList;
import java.util.List;

import duanjt.life.model.BankCard;
import duanjt.life.model.Income;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BankCardDao {
	// 数据库
	SQLiteDatabase db;

	public BankCardDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}

	/**
	 * 新增
	 * 
	 * @param index
	 * @return
	 */
	public boolean Insert(BankCard index) {
		try {
			String sql = "insert into Bank_Card(Id,TIME,Price,Save_Type,Balance,Bank_Type,Note,Create_By," + "Create_Time,UpDate_By,UpDate_Time,ImgUrl,is_upload) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Object[] parm = new Object[] { index.getId(), index.getTime(), index.getPrice(), index.getSaveType(), index.getBalance(), index.getBankType(), index.getNote(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getImgUrl(), index.getIsUpload() };
			db.execSQL(sql, parm);
			return true;
		} catch (Exception e) {
			Log.e("保存纯收入错误", e.getMessage());
			return false;
		}
	}

	/**
	 * 通过集合增加模块信息
	 * 
	 * @param modules
	 *            模块实体集合
	 */
	public boolean Insert(List<BankCard> banks) {
		try {
			db.beginTransaction();

			for (int i = 0; i < banks.size(); i++) {
				BankCard index = banks.get(i);
				String sql = "insert into Bank_Card(Id,TIME,Price,Save_Type,Balance,Bank_Type,Note,Create_By," + "Create_Time,UpDate_By,UpDate_Time,ImgUrl,is_upload) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] parm = new Object[] { index.getId(), index.getTime(), index.getPrice(), index.getSaveType(), index.getBalance(), index.getBankType(), index.getNote(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getImgUrl(), index.getIsUpload() };
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
	 * 保存银行卡信息，如果存在则直接跳过
	 * 
	 * @param banks
	 * @return
	 */
	public boolean Save(List<BankCard> banks) {
		try {
			db.beginTransaction();

			for (int i = 0; i < banks.size(); i++) {
				// 判断是否存在
				if (getById(banks.get(i).getId()) == null) {
					BankCard index = banks.get(i);
					String sql = "insert into bank_card(id,time,price,save_type,balance,bank_type,note,create_by," + "create_time,update_by,update_time,imgurl,is_upload) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					Object[] parm = new Object[] { index.getId(), index.getTime().substring(0, 10), index.getPrice(), index.getSaveType(), index.getBalance(), index.getBankType(), index.getNote(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getImgUrl(), "1" };
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
	 * 修改
	 * 
	 * @param life
	 * @return
	 */
	public boolean Modify(BankCard index) {
		try {
			String sql = "update bank_card set time=?,price=?,save_type=?," + "balance=?,bank_type=?,note=?,create_by=?,create_time=?," + "update_by=?,update_time=?,imgurl=?,is_upload=? where id=?";
			Object[] parm = new Object[] { index.getTime(), index.getPrice(), index.getSaveType(), index.getBalance(), index.getBankType(), index.getNote(), index.getCreateBy(), index.getCreateTime(), index.getUpdateBy(), index.getUpdateTime(), index.getImgUrl(), index.getIsUpload(), index.getId() };
			db.execSQL(sql, parm);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 按条件查询所有数据
	 */
	public List<BankCard> Gets(String sqlWhere) {
		sqlWhere = " where 1=1 " + sqlWhere;
		List<BankCard> list = new ArrayList<BankCard>();
		Cursor c = db.rawQuery("select id,time,price,save_type,balance,bank_type,note,create_by," + "create_time,update_by,update_time,imgurl,is_upload from bank_card " + sqlWhere + " order by time desc,create_time desc", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				BankCard bank = new BankCard();

				bank.setId(c.getString(c.getColumnIndex("id")));
				bank.setTime(c.getString(c.getColumnIndex("time")));
				bank.setPrice(c.getString(c.getColumnIndex("price")));
				bank.setSaveType(c.getString(c.getColumnIndex("save_type")));
				bank.setBalance(c.getString(c.getColumnIndex("balance")));
				bank.setBankType(c.getString(c.getColumnIndex("bank_type")));
				bank.setNote(c.getString(c.getColumnIndex("note")));
				bank.setCreateBy(c.getString(c.getColumnIndex("create_by")));
				bank.setCreateTime(c.getString(c.getColumnIndex("create_time")));
				bank.setUpdateBy(c.getString(c.getColumnIndex("update_by")));
				bank.setUpdateTime(c.getString(c.getColumnIndex("update_time")));
				bank.setImgUrl(c.getString(c.getColumnIndex("imgurl")));
				bank.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(bank);
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
	public BankCard getById(String id) {
		BankCard bank = null;
		Cursor c = db.rawQuery("select id,time,price,save_type,balance,bank_type,note," + "create_by,create_time,update_by,update_time,imgurl,is_upload from bank_card where id=? ", new String[] { id });

		if (c.moveToFirst()) {
			c.moveToPosition(0);// 移动到指定记录
			bank = new BankCard();
			bank.setId(c.getString(c.getColumnIndex("id")));
			bank.setTime(c.getString(c.getColumnIndex("time")));
			bank.setPrice(c.getString(c.getColumnIndex("price")));
			bank.setSaveType(c.getString(c.getColumnIndex("save_type")));
			bank.setBalance(c.getString(c.getColumnIndex("balance")));
			bank.setBankType(c.getString(c.getColumnIndex("bank_type")));
			bank.setNote(c.getString(c.getColumnIndex("note")));
			bank.setCreateBy(c.getString(c.getColumnIndex("create_by")));
			bank.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			bank.setUpdateBy(c.getString(c.getColumnIndex("update_by")));
			bank.setUpdateTime(c.getString(c.getColumnIndex("update_time")));
			bank.setImgUrl(c.getString(c.getColumnIndex("imgurl")));
			bank.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
		}
		c.close();
		return bank;
	}

	/**
	 * 分页查询收入信息
	 */
	public List<BankCard> GetByPage(int pageIndex, int pageSize, String sqlWhere) {
		String sql = "select *, ( select round(SUM(case when Save_Type='1000200001' then Price else -Price end),2) from Bank_Card where Bank_Type=a.Bank_Type and (TIME<a.TIME or (TIME=a.TIME and Create_Time<=a.Create_Time))  ) bal2 from Bank_Card a ";
		sql += String.format(" where 1=1 %s  order by is_upload asc,time desc,create_time desc limit %d offset %d", //
				sqlWhere, pageSize, pageSize * (pageIndex - 1));
		List<BankCard> list = new ArrayList<BankCard>();
		Cursor c = db.rawQuery(sql, null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// 移动到指定记录
				BankCard bank = new BankCard();

				bank.setId(c.getString(c.getColumnIndex("id")));
				bank.setTime(c.getString(c.getColumnIndex("time")));
				bank.setPrice(c.getString(c.getColumnIndex("price")));
				bank.setSaveType(c.getString(c.getColumnIndex("save_type")));
				bank.setBalance(c.getString(c.getColumnIndex("bal2")));
				bank.setBankType(c.getString(c.getColumnIndex("bank_type")));
				bank.setNote(c.getString(c.getColumnIndex("note")));
				bank.setCreateBy(c.getString(c.getColumnIndex("create_by")));
				bank.setCreateTime(c.getString(c.getColumnIndex("create_time")));
				bank.setUpdateBy(c.getString(c.getColumnIndex("update_by")));
				bank.setUpdateTime(c.getString(c.getColumnIndex("update_time")));
				bank.setImgUrl(c.getString(c.getColumnIndex("imgurl")));
				bank.setIsUpload(c.getString(c.getColumnIndex("is_upload")));
				list.add(bank);
			}
		}
		c.close();
		return list;
	}

	/**
	 * 删除所有模块信息
	 */
	public void DeleteAll() {
		String sql = "delete from bank_card";
		db.execSQL(sql);
	}

	/**
	 * 根据编号删除
	 * 
	 * @param id
	 */
	public void Delete(String id) {
		String sql = "delete from bank_card where id=?";
		db.execSQL(sql, new Object[] { id });
	}

	/**
	 * 修改状态为已上传
	 * 
	 * @param list
	 */
	public boolean ModifyIsUpload(List<BankCard> lifes) {
		try {
			db.beginTransaction();

			for (int i = 0; i < lifes.size(); i++) {
				String sql = "update bank_card set is_upload='1' where id=?";
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

	/**
	 * 全部统一计算余额
	 */
	public void CalcBal() {
		String sql = "update Bank_Card set Balance=( select round(SUM(case when Save_Type='1000200001' then Price else -Price end),2)" + "from Bank_Card where Bank_Type=a.Bank_Type " + "and (TIME<a.TIME or (TIME=a.TIME and Create_Time<=a.Create_Time))  ) from Bank_Card a";
		db.execSQL(sql);
	}
	
}
