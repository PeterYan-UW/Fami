package com.fami;

import com.fami.user.helper.DataHolder;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.model.QBUser;

public class ModifyUserTags {


	
	private static String findTag(String flag){
		QBUser qbUser = DataHolder.getDataHolder().getSignInQbUser();
        StringifyArrayList<String> tags = qbUser.getTags();
		String regex = flag+"-*";
        for (String string : tags) {
            if(string.matches(regex+"-*")){
            	String[] parts = string.split("-");
            	return parts[1];
            }
        }
		return null;
	}

	private static StringifyArrayList<String> addTag(StringifyArrayList<String> tags, String flag, String value) {
		String newTag = String.format("%s=%s", flag, value);
		String regex = flag+"-*";
        for (String string : tags) {
            if(string.matches(regex+"-*")){
            	tags.remove(string);
            }
        }
        tags.add(newTag);
		return tags;
	}
	
	public static String getRole(){
		String role=findTag("Role");
		return role;
	}	
	
	public static String getFamiId(){
		String role=findTag("FamiId");
		return role;
	}
	
//	public static StringifyArrayList<String> updateFami(StringifyArrayList<String> tags, String new_fami){
//		if (new_fami == "Fami"){
//			tags.remove("UnFami");
//		}
//		else{
//			tags.remove("Fami");
//		}
//		tags.add(new_fami);
//		return tags;
//	}	
	public static StringifyArrayList<String> addRole(StringifyArrayList<String> tags, String value){
		StringifyArrayList<String> newTags=addTag(tags, "Role", value);
		return newTags;
	}
}
