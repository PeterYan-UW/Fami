package com.fami.setting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.family.AddMemberActivity;
import com.fami.user.Member;
import com.fami.user.activities.LogInActivity;
import com.fami.user.helper.ApplicationSingleton;
import com.fami.user.helper.DataHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.request.QBRequestUpdateBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

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
	    		Intent addMember = new Intent(this, AddMemberActivity.class);
	    		startActivity(addMember);
	    		finish();
	            break;
	        case R.id.leave_fami:
	        	AlertDialog.Builder leavefami = new AlertDialog.Builder(this);
	        	leavefami.setTitle("LEAVE FAMI");
	        	leavefami.setMessage("Are you sure you want to leave?");
	        	leavefami.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				LeaveFami();
        			}
        		});
	        	leavefami.setNegativeButton("Cancel",null);
	        	leavefami.create().show();
	            break;   	
        }
    }
	protected void LeaveFami() {
		// Get group dialog
        //
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.eq("_id", DataHolder.getDataHolder().getChatRoom());
     
		QBChatService.getChatDialogs(QBDialogType.GROUP, requestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>(){
			@Override
			public void onSuccess(java.util.ArrayList<QBDialog> result, Bundle params) {
				QBDialog dialog = result.get(0);
				ArrayList<Integer> users = dialog.getOccupants();
				for(int i=0; i<users.size(); i++){
					if (users.get(i)==DataHolder.getDataHolder().getSignInUserId()){
						users.remove(i);
					}
				}

                QBRequestUpdateBuilder request = new QBRequestUpdateBuilder();
                request.push("occupants_ids", users);
				QBChatService.getInstance().getGroupChatManager().updateDialog(dialog, request , new QBEntityCallbackImpl<QBDialog>() {
                    @Override
                    public void onSuccess(QBDialog dialog, Bundle args) {
                    	leaveUserFami();
                    }

                    @Override
                    public void onError(List<String> errors) {
                    }
                });
			};
		});
		
	}
	
    private void leaveUserFami(){
    	QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.ctn("dialog_id", DataHolder.getDataHolder().getChatRoom());  
    	QBCustomObjects.getObjects("Fami", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>(){
    		@Override
    	    public void onSuccess(ArrayList<QBCustomObject> result, Bundle bundle) {
    			QBCustomObject family = result.get(0);
    			QBRequestUpdateBuilder request = new QBRequestUpdateBuilder();
    			ArrayList<String> member_ids = (ArrayList<String>) family.getFields().get("member_id");

				for(int i=0; i<member_ids.size(); i++){
					if (Integer.valueOf(member_ids.get(i))==DataHolder.getDataHolder().getSignInUserId()){
						member_ids.remove(i);
					}
				}
    			Log.v("member id are: ", member_ids.toString());
    	        request.push("member_id", member_ids);
    	    	QBCustomObjects.updateObject(family, request, new QBEntityCallbackImpl<QBCustomObject>() {
    	    	    @Override
    	    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
    	    	            Intent i = new Intent(MainSetting.this,LogInActivity.class);
    	    	            startActivity(i);
    	    	            finish();
    	    	    }
    	    	 
    	    	    @Override
    	    	    public void onError(List<String> errors) {
    	    	 
    	    	    }
    	    	});
    		}
    	});
    	
	}
}