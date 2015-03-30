package com.fami;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fami.R;
import com.fami.chat.ChatActivity;
import com.fami.event.EventListActivity;
import com.fami.family.AddMemberActivity;
import com.fami.photo.activities.GalleryActivity;
import com.fami.setting.MainSetting;
import com.fami.todolist.TodolistActivity;
import com.fami.user.activities.LogInActivity;
import com.fami.user.activities.UpdateActivity;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;
import com.fami.whereabouts.activities.MapActivity;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_page);
		

		QBUser qbUser = new QBUser();
		qbUser.setId(DataHolder.getDataHolder().getSignInUserId());
		qbUser.setLogin(DataHolder.getDataHolder().getSignInUserLogin());
		qbUser.setPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
        qbUser.setOldPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
        qbUser.setFullName(DataHolder.getDataHolder().getSignInUserFullName());
        qbUser.setEmail(DataHolder.getDataHolder().getSignInUserEmail());
        qbUser.setPhone(DataHolder.getDataHolder().getSignInUserPhone());
        qbUser.setWebsite(DataHolder.getDataHolder().getSignInUserWebSite());

        StringifyArrayList<String> tagList = new StringifyArrayList<String>();
        tagList.add("fami"+Integer.toString(DataHolder.getDataHolder().getFamiTag()));
        qbUser.setTags(tagList);
        
        QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                DataHolder.getDataHolder().setSignInQbUser(qbUser);
                Log.v("true","settag");
            }
            @Override
            public void onError(List<String> strings) {
            	Log.v("wrong","settag");
            }
        });
		
	}

	public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat:
                progressDialog.show();        
                QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
                customObjectRequestBuilder.ctn("ID", DataHolder.getDataHolder().getChatRoom());
                customObjectRequestBuilder.setPagesLimit(1);
                QBChatService.getChatDialogs(QBDialogType.GROUP, customObjectRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
                    @Override
                    public void onSuccess(final ArrayList<QBDialog> dialogs, Bundle bundle) {
                        bundle.putSerializable(ChatActivity.EXTRA_DIALOG, dialogs.get(0));
                        ChatActivity.start(MainActivity.this, bundle);
                        progressDialog.hide(); 
                    }

                    @Override
                    public void onError(List<String> errors) {
                		Log.v("unsucess error", "unsucess error");
                    }
                });
                break;
            case R.id.setting:
            	Intent enter_setting = new Intent(this,MainSetting.class);
        		startActivity(enter_setting);
        		finish();
                break;
            case R.id.todolist:
                Intent intent = new Intent(this, TodolistActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sign_up:
            	break;
            case R.id.album:
            	startGalleryActivity();
            	finish();
                break;
            case R.id.location:
            	Intent locIntent = new Intent(this, MapActivity.class);
            	startActivity(locIntent);
                finish();
                break;
            case R.id.event:
            	Intent eventIntent = new Intent(this, EventListActivity.class);
            	startActivity(eventIntent);
                finish();
                break;
            	
        }
    }
	
	public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
		
	}
	private void startGalleryActivity() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(MainActivity.this,LogInActivity.class);
        startActivity(i);
        finish();
    }
}
