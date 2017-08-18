package com.node;

public class ErrorCode {

	private int code;
	private String desc;
	public ErrorCode(int code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
}
