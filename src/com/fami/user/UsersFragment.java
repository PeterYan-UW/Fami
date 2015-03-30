package com.fami.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fami.MainActivity;
import com.fami.R;
import com.fami.user.helper.ApplicationSingleton;
import com.fami.user.helper.DataHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    private ArrayList<Integer> Famied_users_id = new ArrayList<Integer>();

    public static UsersFragment getInstance() {
        return new UsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        usersList = (PullToRefreshListView) v.findViewById(R.id.usersList);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        createChatButton = (Button) v.findViewById(R.id.createChatButton);
    	getAllFamiedUser();
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
	    	if (!Famied_users_id.contains(newUsers.get(i).getId().toString())){
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

    private void getAllFamiedUser() {
    	QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
		QBCustomObjects.getObjects("Fami", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
		    	ArrayList<Integer> Famied_ids = new ArrayList<Integer>();
		    	for (QBCustomObject fami : customObjects){
		    		Famied_ids.addAll((Collection<? extends Integer>) fami.getFields().get("member_id"));
		    	}
		    	setFamiedUsers(Famied_ids);
		    }

			@Override
		    public void onError(List<String> errors) {
		    	Log.v("test", errors.get(0));
		    }
		});
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
        final HashMap<Integer, Member> members = getUsers(usersAdapter.getSelected());
        final ArrayList<Integer> memberIDs = getUserIds(usersAdapter.getSelected());
        memberIDs.add(qbUser.getId());
        final QBCustomObject family = new QBCustomObject();
    	family.putString("dialog_id", dialogId);
    	family.putArray("member_id", memberIDs);

    	

        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        QBCustomObjects.getObjects("Fami", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
       			@Override
       		    public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
       				family.putInteger("Fami_Tag", customObjects.size()+1);
       				family.setClassName("Fami");
       				QBCustomObjects.createObject(family, new QBEntityCallbackImpl<QBCustomObject>() {
       		    	    @Override
       		    	    public void onSuccess(QBCustomObject createdObject, Bundle bundle) {
       		    	    	Family family = new Family();
       		    	    	DataHolder.getDataHolder().setFamily(family);
       		    	    	DataHolder.getDataHolder().setChatRoom(dialogId);
       		    	    	DataHolder.getDataHolder().setMenmber(members);
       		    	    	QBUser qbUser = DataHolder.getDataHolder().getSignInQbUser();

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

       			@Override
       		    public void onError(List<String> errors) {
       				Log.v("wrong","wrong");
       		    }
       	});

        //family.setClassName("Fami");
    	
    	
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
    public static HashMap<Integer, Member> getUsers(List<QBUser> users){
        HashMap<Integer, Member> family = new HashMap<Integer, Member>();
        for(QBUser user : users){
            Member member = new Member();
        	member.setEmail(user.getEmail());
        	member.setId(user.getId());
        	member.setFullName(user.getFullName());
        	family.put(user.getId(), member);
        }
        return family;
    }

    private void setFamiedUsers(ArrayList<Integer> famied_ids) {
		this.Famied_users_id = famied_ids;	
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		
	}

}
