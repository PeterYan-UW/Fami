package com.fami.todolist;

import java.util.List;

import com.fami.R;
import com.fami.user.helper.DataHolder;
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
    private int currentUser;
    private String currentMode;
    
    public TodoAdapter(List<QBCustomObject> dataSource, int currentUser, String currentMode, Context ctx) {
        this.dataSource = dataSource;
        this.inflater = LayoutInflater.from(ctx);
        this.currentUser = currentUser;
        this.currentMode = currentMode;
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
            holder.task_id = (TextView) convertView.findViewById(R.id.taskID);
            holder.done_btn = (Button) convertView.findViewById(R.id.done_task);
            holder.take_btn = (Button) convertView.findViewById(R.id.take_task);
            holder.delete_btn = (Button) convertView.findViewById(R.id.delete_task);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QBCustomObject task = dataSource.get(position);
        if (task != null) {
            holder.task.setText(task.getFields().get("to_do").toString());
            holder.task_id.setText(Integer.toString(position));
            if (currentMode.equals("done")){
                holder.done_btn.setVisibility(View.INVISIBLE);
                holder.take_btn.setVisibility(View.INVISIBLE);
                if (currentUser == DataHolder.getDataHolder().getSignInUserId()){
                    holder.delete_btn.setVisibility(View.VISIBLE);
                }
                else{
                    holder.delete_btn.setVisibility(View.INVISIBLE);
                }
            }
            else if (currentUser == DataHolder.getDataHolder().getSignInUserId()){
                holder.done_btn.setVisibility(View.VISIBLE);
                holder.take_btn.setVisibility(View.INVISIBLE);
                holder.delete_btn.setVisibility(View.INVISIBLE);
            }
            else{
                holder.done_btn.setVisibility(View.INVISIBLE);
                holder.take_btn.setVisibility(View.VISIBLE); 
                holder.delete_btn.setVisibility(View.INVISIBLE);          	
            }
        }
        return convertView;
    }
	
	private static class ViewHolder {
        TextView task;
        TextView task_id;
        Button done_btn;
        Button take_btn;
        Button delete_btn;
    }
}
