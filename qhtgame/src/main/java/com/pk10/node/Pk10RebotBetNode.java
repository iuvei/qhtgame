package com.pk10.node;

public class Pk10RebotBetNode {
	private String username;
	private String nickname;
	private String text;
	private int time;
	public Pk10RebotBetNode(String username, String nickname, String text, int time) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.text = text;
		this.time = time;
	}
	public String getUsername() {
		return username;
	}
	public String getNickname() {
		return nickname;
	}
	public String getText() {
		return text;
	}
	public int getTime() {
		return time;
	}
}
