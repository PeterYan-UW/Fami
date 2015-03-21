package com.fami.events;

import com.fami.MainActivity;
import com.fami.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.fami.events.EventContract;
import com.fami.events.EventsDBHelper;
import com.fami.todolist.TaskContract;
import com.fami.todolist.TaskDBHelper;
import com.fami.todolist.TodolistActivity;

public class Events extends ActionBarActivity {
	private ListAdapter listAdapter;
	private EventsDBHelper helper;
	private ListView events_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		events_list = (ListView) findViewById(R.id.todolist_list);
		updateUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void updateUI() {
		helper = new EventsDBHelper(Events.this);
		SQLiteDatabase sqlDB = helper.getReadableDatabase();
		Cursor cursor = sqlDB.query(TaskContract.TABLE,
				new String[]{EventContract.Columns._ID, EventContract.Columns.EVENT},
				null, null, null, null, null);

		listAdapter = new SimpleCursorAdapter(
				this,
				R.layout.event_view,
				cursor,
				new String[]{TaskContract.Columns.TASK},
				new int[]{R.id.eventTextView},
				0
		);
		events_list.setAdapter(listAdapter);
	}
	
	
	public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
}
