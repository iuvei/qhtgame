package net.node;

public class HandicapSelectDataCSNode {
	private int type;		//1_全部 2_未同步 3_已同步
	private int page;
	private int count;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
