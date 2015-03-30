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
import android.view.View;
import android.widget.*;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.user.Member;
import com.fami.user.activities.LogInActivity;
import com.fami.user.helper.DataHolder;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

public class TodolistActivity extends FragmentActivity {
    private int TodoUserId = DataHolder.getDataHolder().getSignInUserId();
    private String currentMode = "todo";
	private ListView todo_list;
	private TodoAdapter todoAdapter;
	private ListView done_list;
	private TodoAdapter doneAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_todo);
		todo_list = (ListView) findViewById(R.id.todolist_todo);
		done_list = (ListView) findViewById(R.id.todolist_done);
    	done_list.setVisibility(View.INVISIBLE);
		updateUI(currentMode);
	}

	private void updateUI(String Mode) {
		final TextView textViewToChange = (TextView) findViewById(R.id.userName);
		String username = DataHolder.getDataHolder().getSignInUserFullName();
		if (TodoUserId != DataHolder.getDataHolder().getSignInUserId()){
			username = DataHolder.getDataHolder().getMember(TodoUserId).getFullName();
		}
		textViewToChange.setText(""+username);
    	QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
    	requestBuilder.eq("owner", TodoUserId);
		QBCustomObjects.getObjects("Todo", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
		    	setTodoAdapter(customObjects);
		    }

			@Override
		    public void onError(List<String> errors) {
		    	Log.v("test", errors.get(0));
		    }
		});
	}
	
	protected void setTodoAdapter(ArrayList<QBCustomObject> customObjects) {
		ArrayList<QBCustomObject> todo_item = new ArrayList<QBCustomObject>();
		ArrayList<QBCustomObject> done_item = new ArrayList<QBCustomObject>();
		for (QBCustomObject item : customObjects){
			if (item.getFields().get("done").equals("true")){
				done_item.add(item);
			}
			else{
				todo_item.add(item);
			}
		}
		this.todoAdapter=new TodoAdapter(todo_item, TodoUserId, "todo", this);
		this.doneAdapter=new TodoAdapter(done_item, TodoUserId, "done", this);
		todo_list.setAdapter(todoAdapter);
		done_list.setAdapter(doneAdapter);
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
	
	public void onDeleteButtonClick(View view) {
		View v = (View) view.getParent();
		TextView taskTextView = (TextView) v.findViewById(R.id.taskID);
		String task_position = taskTextView.getText().toString();
		QBCustomObject task = (QBCustomObject) doneAdapter.getItem(Integer.parseInt(task_position));
		QBCustomObjects.deleteObject(task, new QBEntityCallbackImpl() {
    	    @Override
    	    public void onSuccess() {
    	    	updateUI(currentMode);
    	    }
    	});
	}
	public void onTakeButtonClick(View view) {
		View v = (View) view.getParent();
		TextView taskTextView = (TextView) v.findViewById(R.id.taskID);
		String task_position = taskTextView.getText().toString();
		QBCustomObject task = (QBCustomObject) todoAdapter.getItem(Integer.parseInt(task_position));
		HashMap<String, Object> fields = task.getFields();
		fields.remove("owner");
		fields.put("owner", DataHolder.getDataHolder().getSignInUserId());		
		task.setFields(fields);
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
	
	public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
	
	public void todoMenuClick(View view) {
        switch (view.getId()) {
        	case R.id.todo_fami:
        		AlertDialog.Builder chooseBuilder = new AlertDialog.Builder(this);
        		chooseBuilder.setTitle("Choose a fami member");
        		chooseBuilder.setIcon(android.R.drawable.ic_dialog_info);
        		HashMap<Integer, Member> allMember = DataHolder.getDataHolder().getAllMember();
        		final ArrayList<Integer> allMemberId = DataHolder.getDataHolder().getAllMemberId();
        		String[] members_name = new String[allMember.size()];
        		for (int i = 0; i<allMember.size(); i++){
        			if (allMemberId.get(i)==DataHolder.getDataHolder().getSignInUserId()){
        				members_name[i]="Self";
        			}
        			else{
        				members_name[i] = allMember.get(allMemberId.get(i)).getFullName();
        			}
        		}
        		final String[] all_members_name = members_name;
        		chooseBuilder.setSingleChoiceItems(all_members_name, 1, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						TodoUserId = Integer.valueOf(allMemberId.get(which));
						updateUI(currentMode);	
						dialog.dismiss();			
					}
				});
        		chooseBuilder.create().show();
        		break;
            case R.id.todolist:
            	currentMode = "todo";
            	todo_list.setVisibility(View.VISIBLE);
            	done_list.setVisibility(View.INVISIBLE);
                break;
            case R.id.donelist:
            	currentMode = "done";
            	done_list.setVisibility(View.VISIBLE);
            	todo_list.setVisibility(View.INVISIBLE);
                break;
            case R.id.add:
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
        				todo_item.putInteger("owner", TodoUserId);
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
                break;
        }
    	
    }
}
