package com.fami.todolist;

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

import com.fami.MainActivity;
import com.fami.R;
import com.fami.todolist.TaskContract;
import com.fami.todolist.TaskDBHelper;

public class TodolistActivity extends Activity {
	private ListAdapter listAdapter;
	private TaskDBHelper helper;
	private ListView todo_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todolist);
		todo_list = (ListView) findViewById(R.id.todolist_list);
		updateUI();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_task:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Add a task");
				builder.setMessage("What do you want to do?");
				final EditText inputField = new EditText(this);
				builder.setView(inputField);
				builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String task = inputField.getText().toString();

						helper = new TaskDBHelper(TodolistActivity.this);
						SQLiteDatabase db = helper.getWritableDatabase();
						ContentValues values = new ContentValues();

						values.clear();
						values.put(TaskContract.Columns.TASK,task);

						db.insertWithOnConflict(TaskContract.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);
						updateUI();
					}
				});

				builder.setNegativeButton("Cancel",null);

				builder.create().show();
				return true;

			default:
				return false;
		}
	}

	private void updateUI() {
		helper = new TaskDBHelper(TodolistActivity.this);
		SQLiteDatabase sqlDB = helper.getReadableDatabase();
		Cursor cursor = sqlDB.query(TaskContract.TABLE,
				new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
				null, null, null, null, null);

		listAdapter = new SimpleCursorAdapter(
				this,
				R.layout.task_view,
				cursor,
				new String[]{TaskContract.Columns.TASK},
				new int[]{R.id.taskTextView},
				0
		);
		todo_list.setAdapter(listAdapter);
	}

	public void onDoneButtonClick(View view) {
		View v = (View) view.getParent();
		TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
		String task = taskTextView.getText().toString();

		String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
						TaskContract.TABLE,
						TaskContract.Columns.TASK,
						task);


		helper = new TaskDBHelper(TodolistActivity.this);
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		sqlDB.execSQL(sql);
		updateUI();
	}
	
	public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
}
