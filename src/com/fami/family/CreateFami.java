package com.fami.family;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.fami.BaseActivity;
import com.fami.R;
import com.fami.user.activities.LogInActivity;
import com.fami.user.helper.DataHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;

public class CreateFami extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_fami);
	}

	public void onClick(View view) {
        switch (view.getId()) {
	        case R.id.create_fami:
	        	Intent create_fami = new Intent(this, CreateFamiActivity.class);
	            startActivity(create_fami);
	            finish();
	            break;
	        case R.id.join_fami:
        		AlertDialog.Builder searchBuilder = new AlertDialog.Builder(this);
        		searchBuilder.setTitle("Search for Fami");
        		searchBuilder.setMessage("Please type in your Fami ID?");
        		final EditText inputField = new EditText(this);
        		searchBuilder.setView(inputField);
        		searchBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				final String FamiID = inputField.getText().toString();
        				QBRequestGetBuilder famiRequestBuilder = new QBRequestGetBuilder();
        				famiRequestBuilder.eq("_id", FamiID);
        				QBCustomObjects.getObjects("Fami", famiRequestBuilder ,new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
        					@Override
        				    public void onSuccess(final ArrayList<QBCustomObject> famiObjects, Bundle params) {
        						Log.v("search success", "search success " + famiObjects.size());
        				    	if (famiObjects.size() == 1){
            		                AlertDialog.Builder fami_join = new AlertDialog.Builder(CreateFami.this);
            		                fami_join.setMessage("Found a Fami with FamiID: " + FamiID);
            		                fami_join.setPositiveButton("Join", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											JoinFami(famiObjects.get(0));									
										}

            		                	
            		                });
            		                fami_join.show(); 
        				    	}
        				    }

        					@Override
        				    public void onError(List<String> errors) {
        		                AlertDialog.Builder fami_error = new AlertDialog.Builder(CreateFami.this);
        		                fami_error.setMessage("get Fami errors: " + errors).create().show();
        		                fami_error.show();
        				    }
        				});
        			}
        		});

        		searchBuilder.setNegativeButton("Cancel",null);

        		searchBuilder.create().show();
	            break;
	        	
        }
    }
	private void JoinFami(QBCustomObject qbCustomObject) {
		HashMap<String, Object> fields = qbCustomObject.getFields();
		ArrayList<Integer> member_id = (ArrayList<Integer>) fields.get("member_id");
		member_id.add(DataHolder.getDataHolder().getSignInUserId());
		fields.remove("member_id");
		fields.put("member_id", member_id);
		qbCustomObject.setFields(fields);
//		QBCustomObjects.updateObject(qbCustomObject, new QBEntityCallbackImpl<QBCustomObject>() {
//    	    @Override
//    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
//    	    	
//    	    }
//    	 
//    	    @Override
//    	    public void onError(List<String> errors) {
//    	 
//    	    }
//    	});
		
	}
	public void onBackPressed(){
		Intent logout = new Intent(this, LogInActivity.class);
		startActivity(logout);
		finish();
	}

}
