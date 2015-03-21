package com.fami.events;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EventsDBHelper extends SQLiteOpenHelper {

	public EventsDBHelper(Context context) {
		super(context, EventContract.DB_NAME, null, EventContract.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqlDB) {
		String sqlQuery =
				String.format("CREATE TABLE %s (" +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"%s TEXT)", EventContract.TABLE,
								    EventContract.Columns.EVENT);

		Log.d("EventsDBHelper","Query to form table: "+sqlQuery);
		sqlDB.execSQL(sqlQuery);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
		sqlDB.execSQL("DROP TABLE IF EXISTS "+EventContract.TABLE);
		onCreate(sqlDB);
	}
}
