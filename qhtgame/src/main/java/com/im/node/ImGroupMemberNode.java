package com.im.node;

public class ImGroupMemberNode {

	private String Member_Account;
	private String Role;
	public String getMember_Account() {
		return Member_Account;
	}
	public void setMember_Account(String member_Account) {
		Member_Account = member_Account;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	/*@Override
	public String toString() {
		String result = "{\"Member_Account\":\"" + Member_Account + "\"";
		if (Role!=null) {
			result += ",\"Role\":\""+Role+"\"";
		}
		result += "}";
		return result;
	}*/
	
	
}
