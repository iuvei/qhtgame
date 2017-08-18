package com.im.node;

import java.util.List;

public class ImClientInfo {
	private String sdkAppId;
	private String accountType;
	private String userSig;
	private String appGroupid;
	private List<ImClientNode> list;
	public String getSdkAppId() {
		return sdkAppId;
	}
	public void setSdkAppId(String sdkAppId) {
		this.sdkAppId = sdkAppId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getUserSig() {
		return userSig;
	}
	public void setUserSig(String userSig) {
		this.userSig = userSig;
	}
	public String getAppGroupid() {
		return appGroupid;
	}
	public void setAppGroupid(String appGroupid) {
		this.appGroupid = appGroupid;
	}
	public List<ImClientNode> getList() {
		return list;
	}
	public void setList(List<ImClientNode> list) {
		this.list = list;
	}
}
