package com.fami.setting;
import com.fami.MainActivity;
import com.fami.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;

public class MainSetting extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_setting);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("User Info").setIndicator("User Info", null),
                UserInfo.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Fami Info").setIndicator("Fami Info", null),
                FamiInfo.class, null);
    }
    public void onBackPressed(){
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
		finish();
	}
    public void onClick(View view) {
        switch (view.getId()) {
	        case R.id.add_member:
	        	Log.v("add member","add member");
	            break;
	        case R.id.leave_fami:
	        	AlertDialog.Builder leavefami = new AlertDialog.Builder(this);
	        	leavefami.setTitle("LEAVE FAMI");
	        	leavefami.setMessage("Are you sure you want to leave?");
	        	leavefami.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				Log.v("leave fami","leave fami");
        			}
        		});
	        	leavefami.setNegativeButton("Cancel",null);
	        	leavefami.create().show();
	            break;   	
        }
    }
}