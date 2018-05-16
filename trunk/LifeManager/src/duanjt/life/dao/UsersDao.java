package duanjt.life.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import duanjt.life.model.Users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * �û����ݷ���
 * 
 * @author duanjt
 * 
 */
public class UsersDao {
	// ���ݿ�
	SQLiteDatabase db;

	public UsersDao(Context context) {
		db = DatabaseHelper.getInstance(context).getWritableDatabase();
	}

	/**
	 * �����û�������Ϣ
	 * 
	 * @param viewUsers
	 *            �û�Json����
	 */
	public boolean Insert(String viewUsers) {
		try {
			List<Users> list = new ArrayList<Users>();
			JSONArray jarr = new JSONArray(viewUsers);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject index = jarr.getJSONObject(i);
				Users user = new Users();
				user.setId(index.getString("Id"));
				user.setLoginId(index.getString("LoginId"));
				user.setLoginPwd(index.getString("LoginPwd"));
				user.setName(index.getString("Name"));
				user.setPhone(index.getString("Phone"));
				user.setMail(index.getString("Mail"));
				user.setAddress(index.getString("Address"));
				user.setAge(index.getInt("Age"));
				user.setNotes(index.getString("Notes"));				
				list.add(user);
			}
			return Insert(list);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ͨ�����������û���Ϣ
	 * 
	 * @param users
	 *            �û�ʵ�弯��
	 */
	public boolean Insert(List<Users> users) {
		try {
			db.beginTransaction();

			for (int i = 0; i < users.size(); i++) {
				String sql = "insert into Users(Id,Login_Id,Login_Pwd,Name,Phone,Mail,Address,Age,Notes) "
						+ "values(?,?,?,?,?,?,?,?,?)";
				Object[] parm = new Object[] { users.get(i).getId(),
						users.get(i).getLoginId(), users.get(i).getLoginPwd(),
						users.get(i).getName(), users.get(i).getPhone(),
						users.get(i).getMail(), users.get(i).getAddress(),
						users.get(i).getAge(), users.get(i).getNotes()
						};
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

	public List<Users> GetAll() {
		List<Users> list = new ArrayList<Users>();
		Cursor c = db.rawQuery("select * from Users", null);

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);// �ƶ���ָ����¼
				Users user = new Users();
				user.setId(c.getString(c.getColumnIndex("Id")));
				user.setLoginId(c.getString(c.getColumnIndex("Login_Id")));
				user.setLoginPwd(c.getString(c.getColumnIndex("Login_Pwd")));
				user.setName(c.getString(c.getColumnIndex("Name")));
				user.setPhone(c.getString(c.getColumnIndex("Phone")));
				user.setMail(c.getString(c.getColumnIndex("Mail")));
				user.setAddress(c.getString(c.getColumnIndex("Address")));
				user.setAge(c.getInt(c.getColumnIndex("Age")));
				user.setNotes(c.getString(c.getColumnIndex("Notes")));				
				list.add(user);
			}
		}
		c.close();
		return list;
	}

	/**
	 * �û���¼���ʺ������¼
	 * 
	 * @param loginId
	 *            ��¼��
	 * @param loginPwd
	 *            ����
	 * @return �û��������Ϊnull��ʾ��¼ʧ��
	 */
	public Users Login(String loginId, String loginPwd) {
		String sql = "select * from Users where Login_Id=? and Login_Pwd=?";
		String[] parm = new String[] { loginId, loginPwd };
		Cursor c = db.rawQuery(sql, parm);
		if (c.moveToFirst()) {
			Users user = new Users();
			c.moveToPosition(0);// �ƶ���ָ����¼
			user.setId(c.getString(c.getColumnIndex("Id")));
			user.setLoginId(c.getString(c.getColumnIndex("Login_Id")));
			user.setLoginPwd(c.getString(c.getColumnIndex("Login_Pwd")));
			user.setName(c.getString(c.getColumnIndex("Name")));
			user.setPhone(c.getString(c.getColumnIndex("Phone")));
			user.setMail(c.getString(c.getColumnIndex("Mail")));
			user.setAddress(c.getString(c.getColumnIndex("Address")));
			user.setAge(c.getInt(c.getColumnIndex("Age")));
			user.setNotes(c.getString(c.getColumnIndex("Notes")));			
			return user;
		} else {
			// δ��ѯ���û�
			return null;
		}
	}

	/**
	 * ɾ�������û���Ϣ
	 */
	public void DeleteAll() {
		String sql = "delete from Users";
		db.execSQL(sql);
	}
}
