package com.fami.user.helper;

import com.fami.user.Family;
import com.fami.user.Member;
import com.quickblox.content.model.QBFile;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DataHolder {

    private static DataHolder dataHolder;
    private QBUser signInQbUser;
    private Family family;
    private int signInUserId;
    private List<QBFile> qbFileList;
    private int FamiTag;

    public static synchronized DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }
    public void setFamiTag(int FamiTag) {
    	this.FamiTag = FamiTag;
    }
    public int getFamiTag() {
    	return FamiTag;
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
	
	public void setMenmber(HashMap<Integer, Member> member){
		family.setMember(member);
	}
	public void addMenmber(HashMap<Integer, Member> member){
		family.addMember(member);
	}
	public String getChatRoom(){
		return family.getChatRoom();
	}
	
	public Member getMember(int id){
		return family.getMember(id);
	}
	public HashMap<Integer, Member> getAllMember(){
		return family.getAllMember();
	}	
	public ArrayList<Integer> getAllMemberId(){
		return family.getAllMemberId();
	}

	public void setFamily(Family family) {
        this.family = family;
	}
	
	public Family getFamily() {
        return this.family;
	}
	
	public int PhotogetSignInUserId() {
        return signInUserId;
    }

    public void setSignInUserId(int signInUserId) {
        this.signInUserId = signInUserId;
    }

    public void setQbFileList(List<QBFile> qbFileList) {
        this.qbFileList = qbFileList;
    }

    public int getQbFileListSize() {
        return qbFileList.size();
    }

    public String getPublicUrl(int position) {
        return qbFileList.get(position).getUid();
    }

    public void addQbFile(QBFile qbFile) {
        qbFileList.add(qbFile);
    }
}


