package com.fami.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.fami.user.R;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;

import java.util.List;

public class LogInActivity extends BaseActivity{

	private EditText loginUser;
    private EditText loginPassword;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        loginUser = (EditText) findViewById(R.id.login_user);
        loginPassword = (EditText) findViewById(R.id.login_password);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                progressDialog.show();

                // Sign in application with user
                //
                String email = loginUser.getText().toString();
                QBUser qbUser = new QBUser(email, loginPassword.getText().toString(), email);
                QBUsers.signIn(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        progressDialog.hide();

                        setResult(RESULT_OK);

                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
                        // password does not come, so if you want use it somewhere else, try something like this:
                        DataHolder.getDataHolder().setSignInUserPassword(loginPassword.getText().toString());
                        DialogUtils.showLong(context, getResources().getString(R.string.user_successfully_sign_in));
                        
                        startApp();
                    }

                    @Override
                    public void onError(List<String> errors) {
                        progressDialog.hide();
                        DialogUtils.showLong(context, errors.get(0));
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
