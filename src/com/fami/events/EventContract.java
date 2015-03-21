package com.fami.events;

import android.provider.BaseColumns;

public class EventContract {
	public static final String DB_NAME = "com.example.EventList.db.tasks";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "events";


	public class Columns {
		public static final String EVENT = "event";
		public static final String _ID = BaseColumns._ID;
	}
}
