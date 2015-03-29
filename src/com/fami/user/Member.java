package com.fami.user;

public class Member {

	private String fullName = "";
	private String email = "";
	private int id;
	private String phone = "";
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone= phone;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
