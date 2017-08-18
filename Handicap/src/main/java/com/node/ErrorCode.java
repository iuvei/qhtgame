package com.node;

public class ErrorCode {

	private int code;
	private Object desc;
	public ErrorCode(int code, Object desc) {
		super();
		this.code = code;
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public Object getDesc() {
		return desc;
	}
}
