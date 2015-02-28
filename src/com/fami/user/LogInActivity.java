package com.fami.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.core.server.BaseService;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.fami.BaseActivity;
import com.fami.Family;
import com.fami.MainActivity;
import com.fami.R;
import com.fami.chat.ChatActivity;
import com.fami.chat.FamiChatActivity;
import com.fami.user.helper.ApplicationSingleton;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public class LogInActivity extends BaseActivity{

	private EditText loginUser;
    private EditText loginPassword;
	private QBChatService chatService;   
	static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 30;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_login);
        initUI();                        
        initChat();
    }

    private void initUI() {
        loginUser = (EditText) findViewById(R.id.login_user);
        loginPassword = (EditText) findViewById(R.id.login_password);
    }
    private void initChat() {
	    QBChatService.setDebugEnabled(true);
	    if (!QBChatService.isInitialized()) {
	        QBChatService.init(this);
	    }
	    chatService = QBChatService.getInstance();
    }
    
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                progressDialog.show();

                // Sign in application with user

                String email = loginUser.getText().toString();
                final String password = loginPassword.getText().toString();
                QBUser qbUser = new QBUser(email, password, email);
                QBUsers.signIn(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        setResult(RESULT_OK);                        
                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
                        ((ApplicationSingleton)getApplication()).setCurrentUser(qbUser, password);
                        // password does not come, so if you want use it somewhere else, try something like this:
                        DataHolder.getDataHolder().setSignInUserPassword(loginPassword.getText().toString());
                        DialogUtils.showLong(context, getResources().getString(R.string.user_successfully_sign_in));
                        QBUser user = ((ApplicationSingleton)getApplication()).getCurrentUser();
                        loginToChat(user);
                        progressDialog.hide();
                        checkFami();
                    }

                    @Override
                    public void onError(List<String> errors) {
                        progressDialog.hide();
                        DialogUtils.showLong(context, errors.get(0));
                    }
                });

                break;
            case R.id.sign_up:
            	signUp();
            	break;
        }
    }
    
    private void signUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
	}

	private void checkFami() {
		int UserId = DataHolder.getDataHolder().getSignInUserId();
		QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
		requestBuilder.in("member_id", UserId);
		 
		QBCustomObjects.getObjects("Fami", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
		    @Override
		    public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
		    	startApp(customObjects, params);
		    }
		 
		    @Override
		    public void onError(List<String> errors) {
		    	Log.v("test", errors.get(0));
		    }
		});
    }
	
    protected void startApp(ArrayList<QBCustomObject> customObjects, Bundle params) {
		if (customObjects.size()==0){
	        Intent intent = new Intent(this, CreateFami.class);
	        startActivity(intent);
	        finish();				
		}
		else{
	    	Family family = new Family();
	    	DataHolder.getDataHolder().setFamily(family);
			HashMap<String, Object> fields = customObjects.get(0).getFields();
	    	DataHolder.getDataHolder().setChatRoom((String) fields.get("dialog_id"));
	    	DataHolder.getDataHolder().setMenmber((ArrayList<Integer>) fields.get("member_id"));
	        Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
	        finish();			
		}
		
	}

	private void loginToChat(QBUser user){
        chatService.login(user, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {

                // Start sending presences
                //
                try {
                    chatService.startAutoSendPresence(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LogInActivity.this);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });
    }
}
