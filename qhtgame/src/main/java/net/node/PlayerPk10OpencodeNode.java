package net.node;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.sysconst.Code;
import com.util.DateUtil;

public class PlayerPk10OpencodeNode {

	private String period;
	private String date;
	private int page;
	private int count;
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
		if (	period==null || date==null)
			return null;
		long lbegintime = 0;
		long lendtime = 0;
		if (date.length()>0){
			if (date.length()!=10)
				return null;
			String begintime = date+" 00:00:00";
			String endtime=date+" 23:59:59";
		
			Timestamp _begin = DateUtil.StringToTimestamp(begintime);
			Timestamp _end = DateUtil.StringToTimestamp(endtime);
			if (_begin==null || _end==null)
				return null;
			lbegintime = _begin.getTime();
			lendtime = _end.getTime();
		}
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		return parameter;
	}
}

