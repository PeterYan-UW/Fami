package com.fami.user;

import java.util.List;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.R.id;
import com.fami.R.layout;
import com.fami.R.menu;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class UpdateActivity extends BaseActivity {
//	private EditText updatename;
//	private EditText updatepassword;
//	private EditText updateemail;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_update);
//	}
//	
//	
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.update, menu);
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
//	public void update_button_onClick(View view) {
//		switch (view.getId()) {
//			case R.id.update_button:
//				progressDialog.show();
//				updatename = (EditText) findViewById(R.id.nametext);
//				updatepassword = (EditText) findViewById(R.id.passwordtext);
//				updateemail = (EditText) findViewById(R.id.emailtext);
//
//				
//				QBUser qbUser = new QBUser();
//				qbUser.setLogin(updateemail.getText().toString());
//				qbUser.setFullName(updatename.getText().toString());
//				qbUser.setPassword(updatepassword.getText().toString());
//				qbUser.setEmail(updateemail.getText().toString());
//				
//				fillField(updateemail, DataHolder.getDataHolder().getSignInUserLogin());
//				fillField(updateemail, DataHolder.getDataHolder().getSignInUserEmail());
//				fillField(updatename, DataHolder.getDataHolder().getSignInUserFullName());
//				
//				
//				
//				QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
//                    @Override
//                    public void onSuccess(QBUser qbUser, Bundle bundle) {
//                    	progressDialog.hide();
//
//                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
//                        DataHolder.getDataHolder().setSignInUserPassword(updatepassword.getText().toString());
//                        startApp();
//                    }
//
//                    @Override
//                    public void onError(List<String> strings) {
//                    	progressDialog.hide();
//                    }
//                });
//				
//
//				break;
//		}
//	}
//		private void startApp() {
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//			finish();
//		}
//}
	private EditText loginEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText fullNameEditText;
    private EditText phoneEditText;
    private EditText webSiteEditText;
    private EditText tagsEditText;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_update);

        initUI();
        fillAllFields();
    }

    private void initUI() {
        
        loginEditText = (EditText) findViewById(R.id.emailtext);
        passwordEditText = (EditText) findViewById(R.id.passwordtext);
        emailEditText = (EditText) findViewById(R.id.emailtext);
        fullNameEditText = (EditText) findViewById(R.id.nametext);
        phoneEditText = (EditText) findViewById(R.id.phonetext);
        webSiteEditText = (EditText) findViewById(R.id.websitetext);
        tagsEditText = (EditText) findViewById(R.id.tagtext);
    }

    private void fillAllFields() {
        fillField(loginEditText, DataHolder.getDataHolder().getSignInUserLogin());
        fillField(emailEditText, DataHolder.getDataHolder().getSignInUserEmail());
        fillField(fullNameEditText, DataHolder.getDataHolder().getSignInUserFullName());
        fillField(phoneEditText, DataHolder.getDataHolder().getSignInUserPhone());
        fillField(webSiteEditText, DataHolder.getDataHolder().getSignInUserWebSite());
        fillField(tagsEditText, DataHolder.getDataHolder().getSignInUserTags());
    }

    public void update_button_onClick(View view) {
        switch (view.getId()) {
            case R.id.update_button:
                progressDialog.show();

                // Update user
                //
                // create QBUser object
                QBUser qbUser = new QBUser();
                if (DataHolder.getDataHolder().getSignInUserId() != -1) {
                    qbUser.setId(DataHolder.getDataHolder().getSignInUserId());
                }
                if (!DataHolder.getDataHolder().getSignInUserLogin().equals(loginEditText.getText().toString())) {
                    qbUser.setLogin(loginEditText.getText().toString());
                }
                if (!passwordEditText.getText().toString().equals("")) {
                    qbUser.setPassword(passwordEditText.getText().toString());
                    qbUser.setOldPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                }
                qbUser.setFullName(fullNameEditText.getText().toString());
                qbUser.setEmail(emailEditText.getText().toString());
                qbUser.setPhone(phoneEditText.getText().toString());
                qbUser.setWebsite(webSiteEditText.getText().toString());
                StringifyArrayList<String> tagList = new StringifyArrayList<String>();
                for (String tag : tagsEditText.getText().toString().toString().split(",")) {
                    tagList.add(tag);
                }
                qbUser.setTags(tagList);
                
                QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                    	progressDialog.hide();
                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
//                        if (!passwordEditText.equals("")) {
//                            DataHolder.getDataHolder().setSignInUserPassword(
//                                    passwordEditText.getText().toString());
//                        }
                        DialogUtils.showLong(context, getResources().getString(
                                R.string.user_successfully_updated));
                        startApp();
                    }

                    @Override
                    public void onError(List<String> strings) {

                    }
                });

                break;
        }
    }
    private void startApp() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
 
