package com.fami.user.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.quickblox.chat.QBChatService;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.fami.BaseActivity;
import com.fami.MainActivity;
import com.fami.R;
import com.fami.family.CreateFami;
import com.fami.photo.helper.PhotoDataHolder;
import com.fami.photo.utils.Constants;
import com.fami.user.Family;
import com.fami.user.Member;
import com.fami.user.helper.ApplicationSingleton;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jivesoftware.smack.SmackException;

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
                        
                        PhotoDataHolder.getDataHolder().setSignInUserId(DataHolder.getDataHolder().getSignInUserId());
                        getFileList();
                        
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
	    	ArrayList<Integer> usersIDs = (ArrayList<Integer>) fields.get("member_id");
	    	QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
	        requestBuilder.setPage(1);
	        requestBuilder.setPerPage(usersIDs.size());
			QBUsers.getUsersByIDs(usersIDs , requestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>(){
				@Override
	            public void onSuccess(ArrayList<QBUser> users, Bundle params) {
					HashMap<Integer, Member> family = new HashMap<Integer, Member>();
			        for(QBUser user : users){
			            Member member = new Member();
			        	member.setEmail(user.getEmail());
			        	member.setId(user.getId());
			        	member.setFullName(user.getFullName());
			        	member.setPhone(user.getPhone());
			        	family.put(user.getId(), member);
			        }
			    	DataHolder.getDataHolder().setMenmber(family);
	            }

	            @Override
	            public void onError(List<String> errors) {

	            }
			});	
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
	
	public void onBackPressed(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	private void getFileList() {

        // Gey all user's files
        //
        QBPagedRequestBuilder builder = new QBPagedRequestBuilder();
        builder.setPerPage(Constants.QB_PER_PAGE);
        builder.setPage(Constants.QB_PAGE);
        QBContent.getFiles(builder, new QBEntityCallbackImpl<ArrayList<QBFile>>() {
            @Override
            public void onSuccess(ArrayList<QBFile> qbFiles, Bundle bundle) {
                PhotoDataHolder.getDataHolder().setQbFileList(qbFiles);
                // show activity_gallery
                //startGalleryActivity();
            }

            @Override
            public void onError(List<String> strings) {
            }
        });
    }
}
