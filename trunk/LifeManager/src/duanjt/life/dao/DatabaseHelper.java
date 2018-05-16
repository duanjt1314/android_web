package duanjt.life.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "Life.db"; // ���ݿ�����
	private static final int version = 1; // ���ݿ�汾
	private static DatabaseHelper instance = null;

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {		
		super(context, name, factory, version);
	}

	private DatabaseHelper(Context context) { 
		super(context, DB_NAME, null, version);
	}

	/**
	 * Ϊ��ʵ�ֵ�һ���ݿ����ģʽ
	 * 
	 * @param context
	 * @return
	 */
	public static DatabaseHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseHelper(context);
		}
		return instance;
	}

	/**
	 * ��ʼ����ʱ����Ҫִ�е�sql���
	 */
	public void onCreate(SQLiteDatabase db) {
		//�������û����ֵ�����ѱ�ϵͳ���ñ����п���¼����������Ϣ��
		db.execSQL("create table Users(Id,Login_Id,Login_Pwd,Name,Phone,Mail,Address,Age,Notes)");
		db.execSQL("create table Diction(Id,Name,Note,Parent_Id,Order_Id)");
		db.execSQL("create table Lifing_Cost(Id,Time,Reason,Price,Cost_Type_Id,Notes,Img_Url,IsMark,FamilyPay,Create_By,Create_Time,Update_By,Update_Time,CusGroup,is_upload)");
		db.execSQL("create table SysConfig(Id,SysKey,SysValue)");
		db.execSQL("create table bank_card(id,time,price,save_type,balance,bank_type,note,create_by,create_time,update_by,update_time,imgurl,is_upload)");
		db.execSQL("create table income(id,time,price,note,create_by,create_time,update_by,update_time,family_income,is_mark,cus_group,is_upload)");
		
		// ������������
		CreateInitData(db);
	}

	/**
	 * ������������
	 */
	private void CreateInitData(SQLiteDatabase db) {
		// �����ļ�
		db.execSQL("insert into SysConfig(Id,SysKey,SysValue) values('1','ServerUrl','http://192.168.0.107:81');");
		db.execSQL("insert into SysConfig(Id,SysKey,SysValue) values('2','LastUpdateTime','1970-01-01 00:00:00')");
		//db.execSQL("insert into Users(Id,Login_Id,Login_Pwd,Name) values('123','1','202CB962AC59075B964B07152D234B','����')");
	}
	
	/**
	 * �汾�ŷ�������ʱ��Ҫִ�е�sql���
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
