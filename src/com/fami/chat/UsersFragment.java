package com.fami.chat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fami.Family;
import com.fami.MainActivity;
import com.fami.ModifyUserTags;
import com.fami.R;
import com.fami.user.helper.ApplicationSingleton;
import com.fami.user.helper.DataHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRoster;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.model.QBPresence;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.quickblox.chat.listeners.QBRosterListener;
import com.quickblox.chat.listeners.QBSubscriptionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsersFragment extends Fragment implements QBEntityCallback<ArrayList<QBUser>> {

    private static final int PAGE_SIZE = 10;
    private PullToRefreshListView usersList;
    private Button createChatButton;
    private int listViewIndex;
    private int listViewTop;
    private ProgressBar progressBar;
    private UsersAdapter usersAdapter;

    private int currentPage = 0;
    private List<QBUser> users = new ArrayList<QBUser>();

    public static UsersFragment getInstance() {
        return new UsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        usersList = (PullToRefreshListView) v.findViewById(R.id.usersList);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        createChatButton = (Button) v.findViewById(R.id.createChatButton);
        createChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ApplicationSingleton)getActivity().getApplication()).addDialogsUsers(usersAdapter.getSelected());

                // Create new group dialog
                //
                QBDialog dialogToCreate = new QBDialog();
                dialogToCreate.setName(usersListToChatName());
                dialogToCreate.setType(QBDialogType.GROUP);
                ArrayList<Integer> occupantsUsers = getUserIds(usersAdapter.getSelected());
                occupantsUsers.add(DataHolder.getDataHolder().getSignInUserId());
                dialogToCreate.setOccupantsIds(occupantsUsers);

                QBChatService.getInstance().getGroupChatManager().createDialog(dialogToCreate, new QBEntityCallbackImpl<QBDialog>() {
                    @Override
                    public void onSuccess(QBDialog dialog, Bundle args) {
                    	updateUserFami(dialog.getDialogId());
                    }

                    @Override
                    public void onError(List<String> errors) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setMessage("dialog creation errors: " + errors).create().show();
                    }
                });
            }
        });

        usersList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                loadNextPage();
                listViewIndex = usersList.getRefreshableView().getFirstVisiblePosition();
                View v = usersList.getRefreshableView().getChildAt(0);
                listViewTop = (v == null) ? 0 : v.getTop();
            }
        });
        loadNextPage();
        return v;
    }


    public static QBPagedRequestBuilder getQBPagedRequestBuilder(int page) {
        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(page);
        pagedRequestBuilder.setPerPage(PAGE_SIZE);

        return pagedRequestBuilder;
    }


    @Override
    public void onSuccess(ArrayList<QBUser> newUsers, Bundle bundle){

        // save users
        //
    	for (int i = 0; i < newUsers.size(); i++){
    		if (newUsers.get(i).getTags().contains("UnFami")){
    			users.add(newUsers.get(i));
    		}
    	}
    	users.remove(DataHolder.getDataHolder().getSignInQbUser());

        // Prepare users list for simple adapter.
        //
        usersAdapter = new UsersAdapter(users, getActivity());
        usersList.setAdapter(usersAdapter);
        usersList.onRefreshComplete();
        usersList.getRefreshableView().setSelectionFromTop(listViewIndex, listViewTop);

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(){

    }

    @Override
    public void onError(List<String> errors){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("get users errors: " + errors).create().show();
    }


    private String usersListToChatName(){
        String chatName = "";
        for(QBUser user : usersAdapter.getSelected()){
            String prefix = chatName.equals("") ? "" : ", ";
            chatName = chatName + prefix + user.getLogin();
        }
        return chatName;
    }

    private void updateUserFami(final String dialogId){
        QBUser qbUser = DataHolder.getDataHolder().getSignInQbUser();
        final ArrayList<Integer> memberIDs = getUserIds(usersAdapter.getSelected());
        memberIDs.add(qbUser.getId());
    	QBCustomObject family = new QBCustomObject();
    	family.putString("dialog_id", dialogId);
    	family.putArray("member_id", memberIDs);
    	family.setClassName("Fami");
    	QBCustomObjects.createObject(family, new QBEntityCallbackImpl<QBCustomObject>() {
    	    @Override
    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
    	    	Family family = new Family();
    	    	DataHolder.getDataHolder().setFamily(family);
    	    	DataHolder.getDataHolder().setChatRoom(dialogId);
    	    	DataHolder.getDataHolder().setMenmber(memberIDs);
    	    	QBUser qbUser = DataHolder.getDataHolder().getSignInQbUser();
    	    	StringifyArrayList<String> tagList = ModifyUserTags.updateFami(qbUser.getTags(), "Fami");
    	    	
    	    	qbUser.setTags(tagList);

                QBUsers.updateUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {
                    	MainActivity.start(getActivity(), bundle);
                    }
                    @Override
                    public void onError(List<String> strings) {

                    }
                });
    	    }
    	 
    	    @Override
    	    public void onError(List<String> errors) {
    	 
    	    }
    	});
	}

	private void loadNextPage() {
        ++currentPage;

        QBUsers.getUsers(getQBPagedRequestBuilder(currentPage), UsersFragment.this);
    }

    public static ArrayList<Integer> getUserIds(List<QBUser> users){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for(QBUser user : users){
            ids.add(user.getId());
        }
        return ids;
    }


}
