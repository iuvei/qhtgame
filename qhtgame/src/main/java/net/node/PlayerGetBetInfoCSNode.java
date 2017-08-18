package net.node;

public class PlayerGetBetInfoCSNode {
	 /* 		"period":""		查询当期请传"000000"
	 * 		"begintime":2017-04-06 17:47:21	(YYYY-MM-DD HH:mm:ss) 如果是查当前期，该字段值不用
	 * 		"endtime":2017-04-06 17:47:21	(YYYY-MM-DD HH:mm:ss) 如果是查当前期，该字段值不用
	 */
	private String period;
	private String begintime;
	private String endtime;
	private int page;
	private int count;
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
