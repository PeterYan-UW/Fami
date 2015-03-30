package com.fami.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
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

public class EventListActivity<SettingActivity> extends FragmentActivity {
	private ListView event_list;
	private EventAdapter eventAdapter;
	private static String calanderURL = "content://com.android.calendar/calendars";
	private static String calanderEventURL = "content://com.android.calendar/events";
	private static String calanderRemiderURL = "content://com.android.calendar/reminders";
	
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
	public void onAddtoCalender(View view) {
		View v = (View) view.getParent();
		TextView EventName = (TextView) v.findViewById(R.id.event_name);
		final String event_name = EventName.getText().toString();
		TextView EventDate = (TextView) v.findViewById(R.id.event_date);
		String event_date = EventDate.getText().toString();
		TextView EventRepeat = (TextView) v.findViewById(R.id.event_repeat);
		final String event_repeat = EventRepeat.getText().toString();
		String[] num = {"","",""};
		
		int j = 0;
		int n = 0;
		for (j = 0; j < event_date.length(); j++){
			if (event_date.charAt(j) != '/'){
				num[0] = num[0] + event_date.charAt(j);
			}else{
				break;
			}	
		}
		n = j + 1;
		for (j = n; j < event_date.length(); j++){
			if (event_date.charAt(j) != '/'){
				num[1] = num[1] + event_date.charAt(j);
			}else{
				break;
			}	
		}
		n = j + 1;
		for (j = n; j < event_date.length(); j++){
			if (event_date.charAt(j) != '/'){
				num[2] = num[2] + event_date.charAt(j);
			}else{
				break;
			}	
		}
		
		final int Task_year = Integer.parseInt(num[0]);
		final int Task_month = Integer.parseInt(num[1]);
		final int Task_day = Integer.parseInt(num[2]);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Confirm");
	    builder.setMessage("Do you want to add to google calendar?");
	    
	    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialogInterface, int i) {
	        	
                String calId = "";
                Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null,
                        null, null, null);
                if(userCursor.getCount() > 0){
                    userCursor.moveToFirst();
                    calId = userCursor.getString(userCursor.getColumnIndex("_id"));

                }
                ContentValues event = new ContentValues();
                event.put("title", event_name);
                event.put("description", event_name);
                event.put("calendar_id",calId);
                if (event_repeat.equals("356")){
                    event.put("rrule", "FREQ=YEARLY");
                }
                else if (event_repeat.equals("31")){
                    event.put("rrule", "FREQ=MONTHLY");
                }
                else if (event_repeat.equals("7")){
                    event.put("rrule", "FREQ=WEEKLY");
                }
                else if (event_repeat.equals("1")){
                    event.put("rrule", "FREQ=DAILY");
                }
                else if (event_repeat.equals("0")){
                }



                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(Task_year,Task_month - 1,Task_day);
                mCalendar.set(Calendar.HOUR_OF_DAY,12);
                long start = mCalendar.getTime().getTime();
                mCalendar.set(Calendar.HOUR_OF_DAY,23);
                long end = mCalendar.getTime().getTime();

                event.put("dtstart", start);
                event.put("dtend", end);
                event.put("hasAlarm",1);
                event.put("eventTimezone", Time.getCurrentTimezone());

                Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);
                long id = Long.parseLong( newEvent.getLastPathSegment() );
                ContentValues values1 = new ContentValues();
                values1.put( "event_id", id );
                values1.put( "method", 1 );
                values1.put( "minutes", 10 );
                getContentResolver().insert(Uri.parse(calanderRemiderURL), values1);
                Toast.makeText(EventListActivity.this, "Successful", Toast.LENGTH_LONG).show();
				
	        }
	    });
	 
	    builder.setNegativeButton("No",null);
		
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
