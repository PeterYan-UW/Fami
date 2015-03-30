package com.fami.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.todolist.TodolistActivity;
import com.fami.user.Member;
import com.fami.user.helper.DataHolder;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

public class EventListActivity extends FragmentActivity {
	private ListView event_list;
	private EventAdapter eventAdapter;
	private static String calanderURL = "content://com.android.calendar/calendars";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_event);
		event_list = (ListView) findViewById(R.id.event_list);
		updateUI();
	}

	private void updateUI() {
    	QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
    	requestBuilder.ctn("dialog_id", DataHolder.getDataHolder().getChatRoom());
		QBCustomObjects.getObjects("Event", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
			@Override
		    public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
		    	setEventAdapter(customObjects);
		    }

			@Override
		    public void onError(List<String> errors) {
		    	Log.v("test", errors.get(0));
		    }
		});
	}
	
	protected void setEventAdapter(ArrayList<QBCustomObject> customObjects) {
		ArrayList<QBCustomObject> event_item = new ArrayList<QBCustomObject>();
		for (QBCustomObject item : customObjects){
			event_item.add(item);
		}
		this.eventAdapter=new EventAdapter(event_item, this);
		event_list.setAdapter(eventAdapter);
	}
	
	public void AddEvents(View view) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        startActivity(intent);
        finish();
	}
	
	public void GetUser(View view){
		Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                null, null, null);
        if(userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            String userName = userCursor.getString(userCursor.getColumnIndex("name"));
            Toast.makeText(this, userName, Toast.LENGTH_LONG).show();
        }
	}
	
	@Override
	public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
	
}
