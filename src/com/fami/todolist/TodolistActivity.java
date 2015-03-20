package com.fami.todolist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.user.helper.DataHolder;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

public class TodolistActivity extends FragmentActivity {
    private String currentUser = "self";
    private String currentMode = "todo";
	private TodoAdapter todoAdapter;
	private ListView todo_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_todo);
		todo_list = (ListView) findViewById(R.id.todolist_todo);
		updateUI(currentMode);
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
				    	    	updateUI(currentMode);
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

	private void updateUI(String Mode) {
		Boolean status = false;
		if (Mode.equals("todo")){
			status = false;
		}
		else{
			status = true;
		}
    	QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
    	requestBuilder.eq("owner", DataHolder.getDataHolder().getSignInUserId());
    	requestBuilder.eq("done", status);
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
		this.todoAdapter=new TodoAdapter(customObjects, currentUser, this);
	}

	public void onDoneButtonClick(View view) {
		View v = (View) view.getParent();
		TextView taskTextView = (TextView) v.findViewById(R.id.taskID);
		String task_position = taskTextView.getText().toString();
		QBCustomObject task = (QBCustomObject) todoAdapter.getItem(Integer.parseInt(task_position));
		HashMap<String, Object> fields = task.getFields();
		fields.remove("done");
		fields.put("done", true);		
		task.setFields(fields);
    	Log.v("before update", "before update");
		QBCustomObjects.updateObject(task, new QBEntityCallbackImpl<QBCustomObject>() {
    	    @Override
    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
    	    	updateUI(currentMode);
    	    }
    	 
    	    @Override
    	    public void onError(List<String> errors) {
    	 
    	    }
    	});
	}
	
	public void onTakeButtonClick(View view) {
		updateUI(currentMode);
	}
	public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
	
	public void todoMenuClick(View view) {
        switch (view.getId()) {
            case R.id.todolist:
            	currentMode = "todo";
            	updateUI(currentMode);
                break;
            case R.id.donelist:
            	currentMode = "done";
            	updateUI(currentMode);
                break;
            case R.id.add:
                break;
        }
    	
    }
}
