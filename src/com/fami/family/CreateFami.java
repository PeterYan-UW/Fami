package com.fami.family;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fami.BaseActivity;
import com.fami.R;
import com.fami.user.activities.LogInActivity;

public class CreateFami extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_fami);
	}

	public void onClick(View view) {
        switch (view.getId()) {
	        case R.id.create_fami:
	        	Intent create_fami = new Intent(this, FamiActivity.class);
	            startActivity(create_fami);
	            finish();
	            break;
	        case R.id.join_fami:
	        	Intent join_fami = new Intent(this, JoinFamiActivity.class);
	            startActivity(join_fami);
	            finish();
	            break;
	        	
        }
    }
	public void onBackPressed(){
		Intent logout = new Intent(this, LogInActivity.class);
		startActivity(logout);
		finish();
	}

}
