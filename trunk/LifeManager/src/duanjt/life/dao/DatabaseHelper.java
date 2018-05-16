package duanjt.life.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "Life.db"; // 数据库名称
	private static final int version = 1; // 数据库版本
	private static DatabaseHelper instance = null;

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {		
		super(context, name, factory, version);
	}

	private DatabaseHelper(Context context) { 
		super(context, DB_NAME, null, version);
	}

	/**
	 * 为了实现单一数据库操作模式
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
	 * 初始化的时候需要执行的sql语句
	 */
	public void onCreate(SQLiteDatabase db) {
		//创建表，用户表、字典表、消费表、系统配置表、银行卡记录表、纯收入信息表
		db.execSQL("create table Users(Id,Login_Id,Login_Pwd,Name,Phone,Mail,Address,Age,Notes)");
		db.execSQL("create table Diction(Id,Name,Note,Parent_Id,Order_Id)");
		db.execSQL("create table Lifing_Cost(Id,Time,Reason,Price,Cost_Type_Id,Notes,Img_Url,IsMark,FamilyPay,Create_By,Create_Time,Update_By,Update_Time,CusGroup,is_upload)");
		db.execSQL("create table SysConfig(Id,SysKey,SysValue)");
		db.execSQL("create table bank_card(id,time,price,save_type,balance,bank_type,note,create_by,create_time,update_by,update_time,imgurl,is_upload)");
		db.execSQL("create table income(id,time,price,note,create_by,create_time,update_by,update_time,family_income,is_mark,cus_group,is_upload)");
		
		// 创建基础数据
		CreateInitData(db);
	}

	/**
	 * 创建基础数据
	 */
	private void CreateInitData(SQLiteDatabase db) {
		// 配置文件
		db.execSQL("insert into SysConfig(Id,SysKey,SysValue) values('1','ServerUrl','http://192.168.0.107:81');");
		db.execSQL("insert into SysConfig(Id,SysKey,SysValue) values('2','LastUpdateTime','1970-01-01 00:00:00')");
		//db.execSQL("insert into Users(Id,Login_Id,Login_Pwd,Name) values('123','1','202CB962AC59075B964B07152D234B','张三')");
	}
	
	/**
	 * 版本号发生更改时需要执行的sql语句
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
