package com.fami.todolist;

import java.util.HashMap;
import java.util.List;

import com.fami.R;
import com.quickblox.customobjects.model.QBCustomObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TodoAdapter extends BaseAdapter {

    private List<QBCustomObject> dataSource;
    private LayoutInflater inflater;
    
    public TodoAdapter(List<QBCustomObject> dataSource, Context ctx) {
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
            convertView = inflater.inflate(R.layout.list_item_todo, null);
            holder = new ViewHolder();
            holder.task = (TextView) convertView.findViewById(R.id.todoTask);
            holder.done = (Button) convertView.findViewById(R.id.button1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QBCustomObject task = dataSource.get(position);
        if (task != null) {
            holder.task.setText(task.getFields().get("to_do").toString());
            holder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	DoneTask(task);
                }
            });
        }
        return convertView;
    }
	
    protected void DoneTask(QBCustomObject task) {
		HashMap<String, Object> fields = new HashMap<String, Object>();
	}

	private static class ViewHolder {
        TextView task;
        Button done;
    }
}
