package com.aadya.mylibrary.support;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DataSource {

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private Context context;

	public DataSource(Context context)
	{
		this.context = context;
		dbHelper = new MySQLiteHelper(context);
	}


	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	/*********************************IncidentSubject START**********************************************/

	public int deleteAllVideoPath()
	{
		int insertId=0;
		//int cnt=0;

		insertId = database.delete(MySQLiteHelper.TABLE_NAME_1, MySQLiteHelper.COLUMN_ID + " > 0" , null);

		return insertId;
	}

	public int deleteVideoPath(String serverPath)
	{
		int insertId=0;
		//int cnt=0;

		insertId = database.delete(MySQLiteHelper.TABLE_NAME_1, MySQLiteHelper.SERVER_PATH + " = "+serverPath , null);

		return insertId;
	}


	/*public int addUpdateIncidentSubjectList(List<IncidentSubject> dataList)
	{
		int returnVal = 0, count = 0;

		String sql = "INSERT OR REPLACE INTO "+ MySQLiteHelper.TABLE_NAME_1 +" ( "+MySQLiteHelper.ID+", "+MySQLiteHelper.NAME+")  VALUES (?,?);";

		SQLiteStatement statement = database.compileStatement(sql);
		database.beginTransaction();

		try {
			for (int i = 0; i < dataList.size(); i++) {

				try {

					IncidentSubject obj = dataList.get(i);

					statement.bindLong(1, obj.getID());
					statement.bindString(2, obj.getName());

					statement.execute();

					count++;
				}catch (Exception e)
				{
					Log.e("loop Exception","For Loop : "+count);
				}

			}

			database.setTransactionSuccessful();

		}catch (Exception e)
		{
			e.printStackTrace();
			count = 0;
		}
		finally {
			//Log.e("finally","addUpdateServerCopy");
			database.endTransaction();
		}

		return count;
	}*/

	public int addUpdateVideoPath(String serverPath, String localPath)
	{
		int returnVal = 0, count = 0;

		String sql = "INSERT OR REPLACE INTO "+ MySQLiteHelper.TABLE_NAME_1 +" ( "+MySQLiteHelper.SERVER_PATH+", "+MySQLiteHelper.LOCAL_PATH+")  VALUES (?,?);";

		SQLiteStatement statement = database.compileStatement(sql);
		database.beginTransaction();

		try {

			//statement.bindLong(1, obj.getID());
			statement.bindString(1, serverPath.trim());
			statement.bindString(2, localPath.trim());

			statement.execute();
			database.setTransactionSuccessful();

			count++;
		}catch (Exception e)
		{
			//Log.e("loop Exception","For Loop : "+count);
			count = 0;
		}
		finally {
			//Log.e("finally","addUpdateServerCopy");
			database.endTransaction();
		}

		return count;
	}


	/*public List<IncidentSubject> getAllIncidentSubject()
	{
		List<IncidentSubject> dataList = new ArrayList<IncidentSubject>();

		String selectQuery = "SELECT  * FROM " + MySQLiteHelper.TABLE_NAME_1+" ORDER BY "+MySQLiteHelper.ID+" DESC";

		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				IncidentSubject obj=new IncidentSubject();

				obj.setID(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.ID)));
				obj.setName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.NAME)));

				dataList.add(obj);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}*/


	public String getLocalVideoPath(String serverPath)
	{
		String localPath = null;

		try {
			String selectQuery = "SELECT  * FROM "+MySQLiteHelper.TABLE_NAME_1+" WHERE "+MySQLiteHelper.SERVER_PATH+" = ?";

			Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(serverPath.trim())});
			if(cursor.getCount()>0) {
				if (cursor.moveToFirst()) {
					do {
						//obj.setID(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.ID)));
						//obj.setName(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.NAME)));

						localPath = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.LOCAL_PATH));

					} while (cursor.moveToNext());
				}
			}
			else
			{
				//obj.setId(0);
				//obj.setTitle("");
				//obj.setStatus(0);
			}
			cursor.close();

		}catch (Exception e)
		{

		}
		return localPath;
	}

	/*********************************IncidentSubject END**********************************************/





}