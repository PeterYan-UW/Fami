package com.fami.setting;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fami.R;
import com.fami.todolist.TodoAdapter;
import com.fami.user.Member;
import com.fami.user.helper.DataHolder;
import com.quickblox.customobjects.model.QBCustomObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FamiInfo extends Fragment implements OnItemClickListener{
	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ;  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    		View v = inflater.inflate(R.layout.activity_fami_info, container, false);
    		mainListView = (ListView) v.findViewById( R.id.fami_info_list );
    		mainListView.setAdapter(new famiinfoAdapter(getActivity()));
    		mainListView.setOnItemClickListener(this);
    		return v;
    }
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.v("we", Integer.toString(arg2));
		ArrayList<String> tels = new ArrayList<String>();
		HashMap<Integer, Member> members = DataHolder.getDataHolder().getAllMember();
		Iterator<Integer> IDs = members.keySet().iterator();
		int position = arg2;
		while (IDs.hasNext()) {
			int id = IDs.next();
			if (id!=DataHolder.getDataHolder().getSignInUserId()) {
				tels.add(members.get(id).getPhone());
			}
		}
		Iterator<String> t = tels.iterator();
		String p = t.next();
		while (position>0) {
			p = t.next();
			Log.v("phone",p);
			position--;
		}
		
		
		Intent myintent = new Intent(Intent.ACTION_VIEW);
		myintent.setData(Uri.parse("tel:"+p));
		startActivity(myintent);
	}
}
class single_member {
	String fullname;
	int idnumber;
	String phone;
	single_member(String fullname, int idnumber, String phone) {
		this.fullname = fullname;
		this.idnumber = idnumber;
		this.phone = phone;
	}
}
class famiinfoAdapter extends BaseAdapter {
	ArrayList<single_member> list;
	Context context;
	famiinfoAdapter(Context c) {
		context = c;
		list = new ArrayList<single_member>();
		
		HashMap<Integer, Member> members = DataHolder.getDataHolder().getAllMember();
		
		Iterator<Integer> IDs = members.keySet().iterator();
		while (IDs.hasNext()) {
			int id = IDs.next();
			if (id!=DataHolder.getDataHolder().getSignInUserId()) {
				list.add(new single_member(members.get(id).getFullName(),id, members.get(id).getPhone()));
			}
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row=inflater.inflate(R.layout.item_fami_info, viewGroup, false);
		TextView fullname = (TextView) row.findViewById(R.id.famiinfo_fullname);
		TextView email = (TextView) row.findViewById(R.id.famiinfo_email);
		TextView phone = (TextView) row.findViewById(R.id.famiinfo_phone);
		single_member sm=list.get(i);
		fullname.setText(sm.fullname);
		email.setText(Integer.toString(sm.idnumber));
		phone.setText(sm.phone);
		
		return row;
	}	
}












