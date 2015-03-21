package com.fami;

import java.util.ArrayList;
import java.util.List;

import com.fami.R;
import com.fami.chat.ChatActivity;
import com.fami.photo.activities.GalleryActivity;
import com.fami.setting.MainSetting;
import com.fami.todolist.TodolistActivity;
import com.fami.user.activities.UpdateActivity;
import com.fami.user.helper.DataHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;

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
                progressDialog.show();
                Intent intent = new Intent(this, TodolistActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sign_up:
            	break;
            case R.id.album:
            	progressDialog.show();
            	startGalleryActivity();
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
}
