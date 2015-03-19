package com.fami.todolist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.fami.Family;
import com.fami.MainActivity;
import com.fami.R;
import com.fami.user.helper.DataHolder;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class TodolistActivity extends FragmentActivity {
	private TodoAdapter todoAdapter;
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

						QBCustomObject todo_item = new QBCustomObject();
						todo_item.putString("to_do", task);
						todo_item.putInteger("owner", DataHolder.getDataHolder().getSignInUserId());
						todo_item.putBoolean("done", false);
						todo_item.setClassName("Todo");
						QBCustomObjects.createObject(todo_item, new QBEntityCallbackImpl<QBCustomObject>() {
				    	    @Override
				    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
				    	    	updateUI();
				    	    }
				    	 
				    	    @Override
				    	    public void onError(List<String> errors) {
				    	 
				    	    }
				    	});
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
    	QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
    	requestBuilder.eq("owner", DataHolder.getDataHolder().getSignInUserId());
		QBCustomObjects.getObjects("Todo", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
		    	setTodoAdapter(customObjects);
				todo_list.setAdapter(todoAdapter);
		    }

			@Override
		    public void onError(List<String> errors) {
		    	Log.v("test", errors.get(0));
		    }
		});
	}

	protected void setTodoAdapter(ArrayList<QBCustomObject> customObjects) {
		this.todoAdapter=new TodoAdapter(customObjects, getApplicationContext());
		
	}

	public void onDoneButtonClick(View view) {
		View v = (View) view.getParent();
		TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
		String task = taskTextView.getText().toString();

		updateUI();
	}
	
	public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
}
