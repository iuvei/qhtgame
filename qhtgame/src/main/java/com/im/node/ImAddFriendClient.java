package com.im.node;

import java.util.List;

public class ImAddFriendClient {
	private String From_Account;
	private List<ImAddFriendClientNode> AddFriendItem;
	public String getFrom_Account() {
		return From_Account;
	}
	public void setFrom_Account(String from_Account) {
		From_Account = from_Account;
	}
	public List<ImAddFriendClientNode> getAddFriendItem() {
		return AddFriendItem;
	}
	public void setAddFriendItem(List<ImAddFriendClientNode> addFriendItem) {
		AddFriendItem = addFriendItem;
	}
}
