package com.fami.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Family {
	private String chatRoom = "";
	private HashMap<Integer, Member> member = null;
	public String getChatRoom() {
		return chatRoom;
	}
	public void setChatRoom(String chatRoom) {
		this.chatRoom = chatRoom;
	}
	public void setMember(HashMap<Integer, Member> member) {
		this.member = member;
	}
	public Member getMember(int id) {
		return member.get(id);
	}	
	public HashMap<Integer, Member> getAllMember() {
		return member;
	}
	public ArrayList<Integer> getAllMemberId() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.addAll(member.keySet());
		return ids;
	}
}
