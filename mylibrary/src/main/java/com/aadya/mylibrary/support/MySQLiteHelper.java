package com.aadya.mylibrary.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "myLibrary.db";
	private static final int DATABASE_VERSION = 5;

	public static final String TABLE_NAME_1 = "videoPath_table";




	public static final String COLUMN_ID = "column_id";
	public static final String ID = "id";
	public static final String LOCAL_PATH = "localPath";
	public static final String SERVER_PATH = "serverPath";



	private static final String CREATE_TABLE_1 = "create table "
			+ TABLE_NAME_1 + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ SERVER_PATH + " text NOT NULL UNIQUE, "
			+ LOCAL_PATH + " text not null);";




	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		database.execSQL(CREATE_TABLE_1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);

		onCreate(db);

	}

}