package com.fami.user.helper;

import com.fami.Family;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {

    private static DataHolder dataHolder;
    private QBUser signInQbUser;
    private Family family;

    public static synchronized DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }

    public QBUser getSignInQbUser() {
        return signInQbUser;
    }

    public void setSignInQbUser(QBUser singInQbUser) {
        this.signInQbUser = singInQbUser;
    }

    public String getSignInUserOldPassword() {
        return signInQbUser.getOldPassword();
    }

    public int getSignInUserId() {
        return signInQbUser.getId();
    }

    public void setSignInUserPassword(String singInUserPassword) {
        signInQbUser.setOldPassword(singInUserPassword);
    }

    public String getSignInUserLogin() {
        return signInQbUser.getLogin();
    }

    public String getSignInUserEmail() {
        return signInQbUser.getEmail();
    }

    public String getSignInUserFullName() {
        return signInQbUser.getFullName();
    }

    public String getSignInUserPhone() {
        return signInQbUser.getPhone();
    }

    public String getSignInUserWebSite() {
        return signInQbUser.getWebsite();
    }

    public String getSignInUserTags() {
        if (signInQbUser.getTags() != null) {
            return signInQbUser.getTags().getItemsAsString();
        } else {
            return "";
        }
    }
    
	public void setChatRoom(String chatRoom){
		family.setChatRoom(chatRoom);
	}
	
	public void setMenmber(ArrayList<Integer> memberId){
		family.setMemberId(memberId);
	}
	public String getChatRoom(){
		return family.getChatRoom();
	}
	
	public ArrayList<Integer> getMenmber(){
		return family.getMemberId();
	}

	public void setFamily(Family family) {
        this.family = family;
	}
	
	public Family getFamily() {
        return this.family;
	}
}

