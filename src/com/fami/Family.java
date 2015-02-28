package com.fami;

import java.util.ArrayList;

public class Family {
	private String chatRoom = "";
	private ArrayList<Integer> memberId = null;
	public String getChatRoom() {
		return chatRoom;
	}
	public void setChatRoom(String chatRoom) {
		this.chatRoom = chatRoom;
	}
	public ArrayList<Integer> getMemberId() {
		return memberId;
	}
	public void setMemberId(ArrayList<Integer> memberId) {
		this.memberId = memberId;
	}
}
