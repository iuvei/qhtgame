package com.node;

import qht.game.node.PlayerNode;

public class UserInfo {

	private String sessionID;
	private PlayerNode player;
	
	public UserInfo(String sessionID, PlayerNode player) {
		this.sessionID = sessionID;
		this.player = player;
	}

	public String getSessionID() {
		return sessionID;
	}

	public PlayerNode getPlayer() {
		return player;
	}

	public String getUsername() {
		return player.getUsername();
	}
	
	
}
