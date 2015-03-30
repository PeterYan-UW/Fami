package com.fami.event;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.fami.R;
import com.quickblox.customobjects.model.QBCustomObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {

    private List<QBCustomObject> dataSource;
    private LayoutInflater inflater;
    
    public EventAdapter(List<QBCustomObject> dataSource, Context ctx) {
        this.dataSource = dataSource;
        this.inflater = LayoutInflater.from(ctx);
    }
    
	@Override
	public int getCount() {
        return dataSource.size();
	}

	@Override
	public Object getItem(int position) {
        return dataSource.get(position);
	}

	@Override
	public long getItemId(int position) {
        return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_event, null);
            holder = new ViewHolder();
            holder.event_name = (TextView) convertView.findViewById(R.id.event_name);
            holder.event_date = (TextView) convertView.findViewById(R.id.event_date);
            holder.count_down = (TextView) convertView.findViewById(R.id.count_down);
            holder.event_id = (TextView) convertView.findViewById(R.id.event_id);
            holder.event_repeat = (TextView) convertView.findViewById(R.id.event_repeat);            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QBCustomObject task = dataSource.get(position);
        if (task != null) {
            holder.event_name.setText(task.getFields().get("event_name").toString());
            int date = Integer.valueOf((String) task.getFields().get("event_date"));
            int repeat = Integer.valueOf((String) task.getFields().get("event_repeat"));
            int year = date/10000;
            int month = date%10000/100;
            int day = date%100;
            Calendar cl = GregorianCalendar.getInstance();
            cl.set(year, month-1, day);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            Log.v("today is ",today.toString());
            holder.event_date.setText(year+"/"+month+"/"+day);
            holder.count_down.setText(getCountDown(today, cl));
            holder.event_id.setText(Integer.toString(position));
            holder.event_repeat.setText(Integer.toString(repeat));
        }
        return convertView;
    }
	
	private String getCountDown(Calendar today, Calendar cl) {

		Integer[] elapsed = new Integer[3];
		Calendar clone = (Calendar) today.clone(); // Otherwise changes are been reflected.
		elapsed[0] = elapsed(clone, cl, Calendar.YEAR);
		clone.add(Calendar.YEAR, elapsed[0]);
		elapsed[1] = elapsed(clone, cl, Calendar.MONTH);
		clone.add(Calendar.MONTH, elapsed[1]);
		elapsed[2] = elapsed(clone, cl, Calendar.DATE);
		clone.add(Calendar.DATE, elapsed[2]);

		String count_down = String.format("%d years, %d months, %d days", elapsed);
		return count_down;
	}
	
	public static int elapsed(Calendar before, Calendar after, int field) {
	    Calendar clone = (Calendar) before.clone(); // Otherwise changes are been reflected.
	    int elapsed = -1;
	    while (!clone.after(after)) {
	        clone.add(field, 1);
	        elapsed++;
	    }
	    return elapsed;
	}
	private static class ViewHolder {
        TextView event_name;
        TextView event_date;
        TextView count_down;
        TextView event_id;
        TextView event_repeat;
    }
}
