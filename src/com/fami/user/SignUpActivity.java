package com.fami.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fami.BaseActivity;
import com.fami.MainActivity;
import com.fami.R;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.helper.StringifyArrayList;
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
}
