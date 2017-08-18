/*
 * 北京赛车获取官网开奖数据类
 */

package com.pk10.logic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.logic.mgr.DaoMgr;
import com.pk10.node.Pk10OpenInfo;
import com.pk10.node.Pk10PeriodDataDayList;
import com.pk10.node.Pk10PeriodDataDayNode;
import com.pk10.node.Pk10Syn;
import com.pk10.node.Pk10SynNext;
import com.pk10.node.Pk10SynPeriod;
import com.util.DateUtil;
import com.util.HttpUtil;

import qht.game.dao.Pk10PeriodDataDao;
import qht.game.node.Pk10PeriodDataInfo;

public class Pk10GovernmentMgr {
	//101.37.98.234 a.apiplus.net
	private static final String NEWURL = "http://a.apiplus.net/newly.do?token=a4ff7b0b09f14ea0&code=bjpk10&format=json&extend=true";
	//http://api.kaijiangtong.com/lottery/?name=bjpks&format=json&uid=744336&token=31bb37bb3f266549ce0046cdb29ed9eb59067d89
	//http://a.apiplus.net/newly.do?token=a4ff7b0b09f14ea0&code=bjpk10&format=json&extend=true
		
	//private static final String URL = "http://a.apiplus.net/daily.do?token=a4ff7b0b09f14ea0&code=bjpk10&date=2017-04-22&format=json";
	
	/*
	 * 按天获取记录URL
	 */
	public static String getDayUrl(String date) {
		String result = "http://a.apiplus.net/daily.do?token=a4ff7b0b09f14ea0&code=bjpk10&date=";
		result += date;
		result += "&format=json";
		return result;
	}
	
	/*
	 * 获取开奖记录
	 * @return	Pk10OpenInfo
	 */
	public static Pk10OpenInfo getOpencode() {
		/*{
		 * "rows":5,
		 * "code":"bjpk10",
		 * "remain":"585hrs",
		 * "next":[{"expect":"609377","opentime":"2017-03-28 15:17:30"}],
		 * "open":[
		 * 			{"expect":"609376","opencode":"08,07,05,02,03,01,04,09,10,06","opentime":"2017-03-28 15:12:46"},
		 * 			{"expect":"609375","opencode":"08,10,09,04,01,07,02,06,03,05","opentime":"2017-03-28 15:07:48"},
		 * 			{"expect":"609374","opencode":"03,09,02,01,05,04,08,10,06,07","opentime":"2017-03-28 15:02:44"},
		 * 			{"expect":"609373","opencode":"03,01,07,08,10,04,02,05,09,06","opentime":"2017-03-28 14:58:04"},
		 * 			{"expect":"609372","opencode":"05,03,04,08,02,10,09,06,07,01","opentime":"2017-03-28 14:52:49"}
		 * 		],
		 * "time":"2017-03-28 15:14:29"}
		 * */
		try {
			//从官网获取数据
			String str = HttpUtil.sendGet(NEWURL,null,3000);
			if (str==null || str.length()==0 || !str.substring(0, 1).equals("{")) {
				return null;
			}
			
			//解析json
			Pk10Syn objJson = new Gson().fromJson(str, Pk10Syn.class);
			if (objJson==null)
				return null;
			
			//以下为数据转换
			List<Pk10SynNext> listNext = objJson.getNext();
			if (listNext==null || listNext.size()!=1)
				return null;
			Pk10SynNext next = listNext.get(0);
			
			List<Pk10SynPeriod> listPeriod = objJson.getOpen();
			if (listPeriod==null || listPeriod.size()==0)
				return null;
			Pk10SynPeriod open = listPeriod.get(0);
			
			long timestamp = DateUtil.StringToTimestamp(objJson.getTime()).getTime();
			String nextPeriod = next.getExpect();
			long nextOpentime = DateUtil.StringToTimestamp(next.getOpentime()).getTime();
			String period = open.getExpect();
			long opentime = DateUtil.StringToTimestamp(open.getOpentime()).getTime();
			
			List<Integer> opencode = new ArrayList<Integer>();
			String[] strOpencode = open.getOpencode().split(",");
			for (String node : strOpencode) {
				opencode.add(Integer.valueOf(node));
			}
			
			Pk10OpenInfo result = new Pk10OpenInfo();
			result.setLocaltime(DateUtil.getCurTimestamp().getTime());
			result.setTimestamp(timestamp);
			result.setNextPeriod(nextPeriod);
			result.setNextOpentime(nextOpentime);
			result.setPeriod(period);
			result.setOpentime(opentime);
			result.setOpencode(opencode);
			return result;
		} catch (Exception e) {
			System.out.println(DateUtil.getCurTimestamp() + "     syn pk10 error");
		}
		
		return null;
	}
	
	/*
	 * 插入至数据库
	 */
	public static void insertRecord(String period, List<Integer> opencode, long opentime) {
		Pk10PeriodDataDao dao = DaoMgr.getPk10PeriodData();
		Pk10PeriodDataInfo record = dao.selectOne(period);
		if (record==null) {
			record = new Pk10PeriodDataInfo();
			record.setPeriod(period);
			record.setOpencode(opencode);
			record.setOpentime(opentime);
			dao.insert(record);
		}
	}
	
	/*
	 * 获取最近day天的记录到数据库中
	 */
	public static void dailyPeriod(int day) {
		long myTime = DateUtil.getCurTimestamp().getTime();
		
		for (int i=0; i<day; i++) {
			String myDate = DateUtil.getDayString(new Timestamp(myTime));
			synOneDay(myDate);
			myTime -= 86400000;
		}
	}
	
	/*
	 * 按天更新开奖数据
	 */
	public static boolean synOneDay(String date) {
		String url = getDayUrl(date);
		
		String data = null;
		for (int i=0; i<10; i++) {
			data = HttpUtil.sendGet(url,null,5000);
			if (data==null || data.length()==0 || !data.substring(0, 1).equals("{")) {
				data = null;
				continue;
			}
			break;
		}
		if (data == null)
			return false;
		
		Pk10PeriodDataDayList objJson = new Gson().fromJson(data, Pk10PeriodDataDayList.class);
		if (objJson==null)
			return false;
		System.out.println(date);
		
		List<Pk10PeriodDataDayNode> listData = objJson.getData();
		for (Pk10PeriodDataDayNode node : listData) {
			String period = node.getExpect();
			long opentime = DateUtil.StringToTimestamp(node.getOpentime()).getTime();
			List<Integer> opencode = new ArrayList<Integer>();
			String[] strOpencode = node.getOpencode().split(",");
			for (String _node : strOpencode) {
				opencode.add(Integer.valueOf(_node));
			}
			
			insertRecord(period, opencode, opentime);
		}
		return true;
	}
}
