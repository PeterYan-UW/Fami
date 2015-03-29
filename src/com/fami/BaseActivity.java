package com.fami;

import com.fami.R;
import com.fami.user.utils.DialogUtils;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class BaseActivity extends FragmentActivity {

    protected Context context;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        progressDialog = DialogUtils.getProgressDialog(this);
    }

    protected void fillField(TextView textView, String value) {
        if (!TextUtils.isEmpty(value)) {
            textView.setText(value);
        }
    }
}
