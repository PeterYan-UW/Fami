package com.fami.event;

import com.fami.event.EventListActivity;
import com.fami.photo.activities.GalleryActivity;
import com.fami.user.helper.DataHolder;

import android.app.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import android.widget.DatePicker.OnDateChangedListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.ContentValues;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.fami.MainActivity;
import com.fami.R;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

public class EventDetailActivity extends Activity{
    private DatePicker datePicker;
    //private TimePicker timePicker;
    private EditText Event_name;
    private int Task_year;
    private int Task_month;
    private int Task_day;
    private String event_name;
    private int event_date;
    private int event_repeat;
    private String option;
    private Button btnDate;
    private boolean condition = false;
    //private TextView t;


    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        Event_name = (EditText) findViewById(R.id.enter_event);
    }

    public void eventOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnDatePickerDialog:
                int y,m,d;
                Calendar cal=Calendar.getInstance();
                y = cal.get(Calendar.YEAR);
                m = cal.get(Calendar.MONTH);
                d = cal.get(Calendar.DATE);
                DatePickerDialog datePicker=new DatePickerDialog(EventDetailActivity.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                        Toast.makeText(EventDetailActivity.this, year+"year "+ monthOfYear+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
                        Task_year = year;
                        Task_month = monthOfYear + 1;
                        Task_day = dayOfMonth;
                        condition = true;
                        if (condition){
                            showDate(Task_year,Task_month,Task_day);
                        }
                            }
                        }, y, m, d);
                datePicker.show();
                break;

            case R.id.add_event:
            	event_name = Event_name.getText().toString();
            	QBCustomObject event = new QBCustomObject();
            	event.put("event_name", event_name);
            	event.put("event_date", event_date);
            	event.put("event_repeat", event_repeat);
            	event.put("dialog_id", DataHolder.getDataHolder().getChatRoom());
            	event.setClassName("Event");
            	QBCustomObjects.createObject(event, new QBEntityCallbackImpl<QBCustomObject>() {
		    	    @Override
		    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
		    	    	backToEventList();
		    	    }
		    	 
		    	    @Override
		    	    public void onError(List<String> errors) {
		    	 
		    	    }
		    	});
                break;
            case R.id.cancel_add_event:
            	backToEventList();
                break;
            case R.id.event_choose:
                AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailActivity.this);
                builder.setTitle("Choose");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                final String[] r = {"Every Year", "Every Month", "Every Week", "None"};
                builder.setSingleChoiceItems(r, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        option = r[which];
                        ShowRepeat(option);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    	option = "None";
                        ShowRepeat(option);
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;

            default:
                break;
        }

    }

    private void ShowRepeat(String option) {

        TextView show=(TextView)findViewById(R.id.repeat);
        show.setText(option);
        if (option.equals("Every Year")){
        	event_repeat = 365;
        }
        else if (option.equals("Every Month")){
        	event_repeat = 31;
        }
        else if (option.equals("Every Week")){
        	event_repeat = 7;
        }
        else if (option.equals("None")){
        	event_repeat = 0;
        }

    }

    private void showDate(int year,int month,int day)
    {
        TextView show=(TextView)findViewById(R.id.show);
        show.setText(""+year+"."+ month +"."+day+".");
        event_date = year*10000+month*100+day;
    }
    
	private void backToEventList() {
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
        finish();
    }
	
	@Override
	public void onBackPressed(){
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
        finish();
	}
}