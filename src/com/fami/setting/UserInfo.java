package com.fami.setting;


import java.util.ArrayList;
import java.util.List;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.user.helper.ApplicationSingleton;
import com.fami.user.helper.DataHolder;
import com.fami.user.utils.DialogUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fami.R.id;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;


public class UserInfo extends Fragment {
	private Button namebutton;
	private Button emailbutton;
	private Button phonebutton;
	private Button websitebutton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_user_info, container, false);
        
        final TextView username = (TextView) v.findViewById(R.id.Name_text); 
        final TextView useremail = (TextView) v.findViewById(R.id.Email_text); 
        final TextView userphone = (TextView) v.findViewById(R.id.Phone_text); 
        final TextView userwebsite = (TextView) v.findViewById(R.id.Website_text); 
        
        username.setText(DataHolder.getDataHolder().getSignInUserFullName());
        useremail.setText(DataHolder.getDataHolder().getSignInUserEmail());
        userphone.setText(DataHolder.getDataHolder().getSignInUserPhone());
        userwebsite.setText(DataHolder.getDataHolder().getSignInUserWebSite());
        
        namebutton = (Button) v.findViewById(R.id.Name_button);
        emailbutton = (Button) v.findViewById(R.id.Email_button);
        phonebutton = (Button) v.findViewById(R.id.Phone_button);
        websitebutton = (Button) v.findViewById(R.id.Website_button);
        
        
        
        namebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder namebuilder = new AlertDialog.Builder(getActivity());
            	namebuilder.setTitle("Edit Name");
            	namebuilder.setMessage("Please Enter Your Name");
        		final EditText inputField = new EditText(getActivity());
        		inputField.setText(DataHolder.getDataHolder().getSignInUserFullName());
        		inputField.setSelection(inputField.length());
        		namebuilder.setView(inputField);
        		namebuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				final String newname = inputField.getText().toString();
        				QBUser qbUser = new QBUser();
        				qbUser.setId(DataHolder.getDataHolder().getSignInUserId());
        				qbUser.setLogin(DataHolder.getDataHolder().getSignInUserLogin());
        				qbUser.setPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setOldPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setFullName(newname);
                        qbUser.setEmail(DataHolder.getDataHolder().getSignInUserEmail());
                        qbUser.setPhone(DataHolder.getDataHolder().getSignInUserPhone());
                        qbUser.setWebsite(DataHolder.getDataHolder().getSignInUserWebSite());
                        QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbUser, Bundle bundle) {
                                DataHolder.getDataHolder().setSignInQbUser(qbUser);
                                DialogUtils.showLong(getActivity(), getResources().getString(
                                        R.string.user_successfully_updated));
                                username.setText(newname);
                            }
                            @Override
                            public void onError(List<String> strings) {
                            }
                        });
        			}
        		});
        		namebuilder.setNegativeButton("Cancel",null);
        		namebuilder.create().show();
            }
        });
        
        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder emailbuilder = new AlertDialog.Builder(getActivity());
            	emailbuilder.setTitle("Edit Email");
            	emailbuilder.setMessage("Please Enter Your Email Address");
        		final EditText inputField = new EditText(getActivity());
        		inputField.setText(DataHolder.getDataHolder().getSignInUserEmail());
        		inputField.setSelection(inputField.length());
        		emailbuilder.setView(inputField);
        		emailbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				final String newemail = inputField.getText().toString();

        				QBUser qbUser = new QBUser();
        				qbUser.setId(DataHolder.getDataHolder().getSignInUserId());
        				qbUser.setLogin(DataHolder.getDataHolder().getSignInUserLogin());
        				qbUser.setPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setOldPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setFullName(DataHolder.getDataHolder().getSignInUserFullName());
                        qbUser.setEmail(newemail);
                        qbUser.setPhone(DataHolder.getDataHolder().getSignInUserPhone());
                        qbUser.setWebsite(DataHolder.getDataHolder().getSignInUserWebSite());
                        QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbUser, Bundle bundle) {
                                DataHolder.getDataHolder().setSignInQbUser(qbUser);
                                DialogUtils.showLong(getActivity(), getResources().getString(
                                        R.string.user_successfully_updated));
                                useremail.setText(newemail);
                            }
                            @Override
                            public void onError(List<String> strings) {
                            }
                        });	
        			}
        		});
        		emailbuilder.setNegativeButton("Cancel",null);
        		emailbuilder.create().show();
            }
        });
        
        phonebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder phonebuilder = new AlertDialog.Builder(getActivity());
            	phonebuilder.setTitle("Edit Phone");
            	phonebuilder.setMessage("Please Enter Your Phone Number");
        		final EditText inputField = new EditText(getActivity());
        		inputField.setText(DataHolder.getDataHolder().getSignInUserPhone());
        		inputField.setSelection(inputField.length());
        		phonebuilder.setView(inputField);
        		phonebuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				final String newphone = inputField.getText().toString();

        				QBUser qbUser = new QBUser();
        				qbUser.setId(DataHolder.getDataHolder().getSignInUserId());
        				qbUser.setLogin(DataHolder.getDataHolder().getSignInUserLogin());
        				qbUser.setPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setOldPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setFullName(DataHolder.getDataHolder().getSignInUserFullName());
                        qbUser.setEmail(DataHolder.getDataHolder().getSignInUserEmail());
                        qbUser.setPhone(newphone);
                        qbUser.setWebsite(DataHolder.getDataHolder().getSignInUserWebSite());
                        QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbUser, Bundle bundle) {
                                DataHolder.getDataHolder().setSignInQbUser(qbUser);
                                DialogUtils.showLong(getActivity(), getResources().getString(
                                        R.string.user_successfully_updated));
                                userphone.setText(newphone);
                            }
                            @Override
                            public void onError(List<String> strings) {
                            }
                        });	
        			}
        		});
        		phonebuilder.setNegativeButton("Cancel",null);
        		
        		phonebuilder.create().show();
            }
        });
        
        websitebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	AlertDialog.Builder websitebuilder = new AlertDialog.Builder(getActivity());
            	websitebuilder.setTitle("Edit Website");
            	websitebuilder.setMessage("Please Enter Your Website");
        		final EditText inputField = new EditText(getActivity());
        		inputField.setText(DataHolder.getDataHolder().getSignInUserWebSite());
        		inputField.setSelection(inputField.length());
        		websitebuilder.setView(inputField);
        		websitebuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface dialogInterface, int i) {
        				final String newwebsite = inputField.getText().toString();

        				QBUser qbUser = new QBUser();
        				qbUser.setId(DataHolder.getDataHolder().getSignInUserId());
        				qbUser.setLogin(DataHolder.getDataHolder().getSignInUserLogin());
        				qbUser.setPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setOldPassword(DataHolder.getDataHolder().getSignInUserOldPassword());
                        qbUser.setFullName(DataHolder.getDataHolder().getSignInUserFullName());
                        qbUser.setEmail(DataHolder.getDataHolder().getSignInUserEmail());
                        qbUser.setPhone(DataHolder.getDataHolder().getSignInUserPhone());
                        qbUser.setWebsite(newwebsite);
                        QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
                            @Override
                            public void onSuccess(QBUser qbUser, Bundle bundle) {
                                DataHolder.getDataHolder().setSignInQbUser(qbUser);
                                DialogUtils.showLong(getActivity(), getResources().getString(
                                        R.string.user_successfully_updated));
                                userwebsite.setText(newwebsite);
                            }
                            @Override
                            public void onError(List<String> strings) {
                            }
                        });	
        			}
        		});
        		websitebuilder.setNegativeButton("Cancel",null);
        		
        		websitebuilder.create().show();
            }
        });
        
        return v;
    }
    
}