package com.fami.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fami.BaseActivity;
import com.fami.MainActivity;
import com.fami.R;
import com.fami.photo.helper.PhotoDataHolder;
import com.fami.photo.utils.Constants;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;
public class SignUpActivity extends BaseActivity {

    private EditText signupEmail;
    private EditText signupPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
    }

    private void initUI() {
        signupEmail = (EditText) findViewById(R.id.signup_email);
        signupPassword = (EditText) findViewById(R.id.signup_password);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up_button:
                progressDialog.show();

                // Sign Up user
                //
                QBUser qbUser = new QBUser();
                qbUser.setLogin(signupEmail.getText().toString());
                qbUser.setEmail(signupEmail.getText().toString());
                qbUser.setPassword(signupPassword.getText().toString());
//              StringifyArrayList<String> tags = new StringifyArrayList<String>();
//              tags.add("UnFami");
//				qbUser.setTags(tags);
                QBUsers.signUpSignInTask(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        progressDialog.hide();

                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
                        DataHolder.getDataHolder().setSignInUserPassword(signupPassword.getText().toString());
                        
                        PhotoDataHolder.getDataHolder().setSignInUserId(DataHolder.getDataHolder().getSignInUserId());
                        getFileList();
                        
                        createFami();
                    }

                    @Override
                    public void onError(List<String> strings) {
                        progressDialog.hide();
                        DialogUtils.showLong(context, strings.get(0));
                    }
                });

                break;
        }
    }

	private void createFami() {
        Intent intent = new Intent(this, CreateFami.class);
        startActivity(intent);
        finish();
    }
	private void getFileList() {

        // Gey all user's files
        //
        QBPagedRequestBuilder builder = new QBPagedRequestBuilder();
        builder.setPerPage(Constants.QB_PER_PAGE);
        builder.setPage(Constants.QB_PAGE);
        Log.v("here1","here");
        QBContent.getFiles(builder, new QBEntityCallbackImpl<ArrayList<QBFile>>() {
            @Override
            public void onSuccess(ArrayList<QBFile> qbFiles, Bundle bundle) {
            	Log.v("here2","here");
                PhotoDataHolder.getDataHolder().setQbFileList(qbFiles);
                // show activity_gallery
                //startGalleryActivity();
            }

            @Override
            public void onError(List<String> strings) {
            	Log.v("here3","here");
            }
        });
    }
}
