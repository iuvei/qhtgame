/**
 * 翻页信息类
 * @author kinphy
 * @Time 2017-02-13 16:54:21
 */

package com.node;

public class KinPageInfo {

	private long total;
	private int pages;
	private int pageNum;
	private int pageSize;
	private int size;
	private Object obj;
	private Object abj;
	
	public Object getAbj() {
		return abj;
	}
	public void setAbj(Object abj) {
		this.abj = abj;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}	
}
