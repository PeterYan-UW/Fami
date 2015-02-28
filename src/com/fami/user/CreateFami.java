package com.fami.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fami.BaseActivity;
import com.fami.R;
import com.fami.chat.FamiChatActivity;

public class CreateFami extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_fami);
	}

	public void onClick(View view) {
        switch (view.getId()) {
	        case R.id.create_fami:
	        	Intent create_fami = new Intent(this, FamiChatActivity.class);
	            startActivity(create_fami);
	            finish();
        }
    }
	public void onBackPressed(){
		Intent logout = new Intent(this, LogInActivity.class);
		startActivity(logout);
		finish();
	}

}
