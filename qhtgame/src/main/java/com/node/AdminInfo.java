package com.node;

import qht.game.node.AdminNode;

public class AdminInfo {

	private String sessionID;
	private AdminNode admin;
	
	public AdminInfo(String sessionID, AdminNode admin) {
		this.sessionID = sessionID;
		this.admin = admin;
	}

	public String getSessionID() {
		return sessionID;
	}
	
	public AdminNode getAdmin() {
		return admin;
	}

	public String getUsername() {
		return admin.getUsername();
	}
	
	
}
