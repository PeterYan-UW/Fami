package com.fami;

import com.fami.R;
import com.fami.chat.ChatActivity;
import com.fami.chat.DialogsActivity;
import com.fami.user.BaseActivity;
import com.fami.user.UpdateActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_page);
	}
	
	public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat:
                progressDialog.show();
                Intent intent = new Intent(this, DialogsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.setting:
            	Intent i = new Intent(MainActivity.this,UpdateActivity.class);
        		startActivity(i);
        		finish();
        }
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_page, menu);
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
}
