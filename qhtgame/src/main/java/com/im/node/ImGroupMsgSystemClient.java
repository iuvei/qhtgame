package com.im.node;

import java.util.List;

public class ImGroupMsgSystemClient {
	private String GroupId;
	private List<String> ToMembers_Account;
	private String Content;
	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String groupId) {
		GroupId = groupId;
	}
	public List<String> getToMembers_Account() {
		return ToMembers_Account;
	}
	public void setToMembers_Account(List<String> toMembers_Account) {
		ToMembers_Account = toMembers_Account;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
}
