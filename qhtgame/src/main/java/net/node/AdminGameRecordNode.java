package net.node;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.sysconst.Code;
import com.util.DateUtil;

public class AdminGameRecordNode {

	private String username;
	private int lottery_id;
	private String period;
	private String begintime;
	private String endtime;
	private int page;
	private int count;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getLottery_id() {
		return lottery_id;
	}
	public void setLottery_id(int lottery_id) {
		this.lottery_id = lottery_id;
	}
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

	public Map<String, Object> getParameter() {
		if (	username==null ||
				lottery_id<0 ||
				period==null ||
				begintime==null || begintime.length()==0 ||
				endtime==null || endtime.length()==0)
			return null;
		Timestamp _begin = DateUtil.StringToTimestamp(begintime);
		Timestamp _end = DateUtil.StringToTimestamp(endtime);
		if (_begin==null || _end==null)
			return null;
		long lbegintime = _begin.getTime();
		long lendtime = _end.getTime();
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("lorreyid", lottery_id);
		parameter.put("period", period);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		return parameter;
	}
}
