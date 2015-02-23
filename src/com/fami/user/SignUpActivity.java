package com.fami.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

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
                qbUser.setPassword(signupPassword.getText().toString());
                QBUsers.signUpSignInTask(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                        progressDialog.hide();

                        DataHolder.getDataHolder().addQbUserToList(qbUser);
                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
                        DataHolder.getDataHolder().setSignInUserPassword(signupPassword.getText().toString());

                        startApp();
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

	private void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
