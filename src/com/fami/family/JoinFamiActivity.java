//package com.fami.family;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//
//import com.fami.BaseActivity;
//import com.fami.R;
//import com.fami.chat.ChatActivity;
//import com.fami.family.CreateFamiActivity.SectionsPagerAdapter;
//import com.fami.user.UsersFragment;
//import com.fami.user.activities.LogInActivity;
//import com.fami.user.helper.ApplicationSingleton;
//import com.quickblox.chat.QBChatService;
//import com.quickblox.chat.model.QBDialog;
//import com.quickblox.chat.model.QBDialogType;
//import com.quickblox.core.QBEntityCallbackImpl;
//import com.quickblox.core.request.QBPagedRequestBuilder;
//import com.quickblox.core.request.QBRequestGetBuilder;
//import com.quickblox.customobjects.QBCustomObjects;
//import com.quickblox.customobjects.model.QBCustomObject;
//import com.quickblox.users.QBUsers;
//import com.quickblox.users.model.QBUser;
//
//public class JoinFamiActivity  extends Activity {
//
//    private ListView famiListView;
//    private ProgressBar progressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.join_family_activity);
//
//        famiListView = (ListView) findViewById(R.id.familiesList);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//
//        // get dialogs
//        //
//        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
//        customObjectRequestBuilder.setPagesLimit(100);
//        QBCustomObjects.getObjects("Fami", new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
//            @Override
//            public void onSuccess(final ArrayList<QBCustomObject> famis, Bundle args) {
//            	buildListView(famis);
//            }
//
//            @Override
//            public void onError(List<String> errors) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(JoinFamiActivity.this);
//                dialog.setMessage("get Fami errors: " + errors).create().show();
//            }
//        });
//    }
//
//
//    void buildListView(List<QBCustomObject> Famis){
//        final FamilyAdapter adapter = new FamilyAdapter(Famis, JoinFamiActivity.this);
//        famiListView.setAdapter(adapter);
//
//        progressBar.setVisibility(View.GONE);
//
//        // choose dialog
//        //
//        dialogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                QBDialog selectedDialog = (QBDialog)adapter.getItem(position);
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ChatActivity.EXTRA_DIALOG, (QBDialog)adapter.getItem(position));
//
//                // group
//                bundle.putSerializable("mode", "Group");
//                // Open chat activity
//                //
//                ChatActivity.start(JoinFamiActivity.this, bundle);
//            }
//        });
//    }
//}
