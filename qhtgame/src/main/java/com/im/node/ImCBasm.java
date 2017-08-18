package com.im.node;

import java.util.List;

public class ImCBasm {
	private String CallbackCommand;
	private String From_Account;
	private String GroupId;
	private List<ImCBasmText> MsgBody;
	private String Type;
	public String getCallbackCommand() {
		return CallbackCommand;
	}
	public void setCallbackCommand(String callbackCommand) {
		CallbackCommand = callbackCommand;
	}
	public String getFrom_Account() {
		return From_Account;
	}
	public void setFrom_Account(String from_Account) {
		From_Account = from_Account;
	}
	public String getGroupId() {
		return GroupId;
	}
	public void setGroupId(String groupId) {
		GroupId = groupId;
	}
	public List<ImCBasmText> getMsgBody() {
		return MsgBody;
	}
	public void setMsgBody(List<ImCBasmText> msgBody) {
		MsgBody = msgBody;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
}
