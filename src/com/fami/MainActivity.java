package com.fami;

import java.util.ArrayList;
import java.util.List;

import com.fami.R;
import com.fami.chat.ChatActivity;
import com.fami.user.UpdateActivity;
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
            	Intent enter_setting = new Intent(this,UpdateActivity.class);
        		startActivity(enter_setting);
        		finish();
                break;
        }
    }

	public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_page, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
