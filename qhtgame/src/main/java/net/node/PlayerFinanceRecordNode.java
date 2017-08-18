package net.node;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.sysconst.Code;
import com.util.DateUtil;

public class PlayerFinanceRecordNode {

	private String username;
	private int type;
	private String operator;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
		if (	username==null || username.length()==0 ||
				(type!=0 && type!=Code.AMOUNT_RECHANGE && type!=Code.AMOUNT_WITHDRAWALS) ||
				operator==null ||
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
		parameter.put("type", type);
		parameter.put("oprname", operator);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		return parameter;
	}
}
