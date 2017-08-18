package com.pk10.logic;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.logic.mgr.DaoMgr;
import com.node.ErrorObj;
import com.pk10.node.Pk10HandicapNode;
import com.pk10.node.Pk10OpenInfo;
import com.sysconst.Code;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.DateUtil;
import com.util.HttpUtil;

import net.node.Pk10HandicapCSNode;
import qht.game.dao.PK10PaidDao;
import qht.game.node.LotteryNode;
import qht.game.node.PK10BetNode;
import qht.game.node.PK10PaidNode;
import qht.game.node.Pk10InfoNode;
import qht.game.node.Pk10PeriodDataInfo;

public class Pk10PrizeMgr {
	private static long timeOpen;
	private static boolean isOpen;
	private static long timeNotBet;
	private static boolean isNotBet;
	private static long timeSealplate;
	private static boolean isSealPlate;	//是否封盘
	private static PK10PaidNode mpaid;
	private static Map<Integer,PK10BetNode> mdata = new HashMap<Integer,PK10BetNode>();
	private static Pk10OpenInfo info;
	private static Pk10Player pk10Player = new Pk10Player();
	private static Pk10Robot pk10Robot = new Pk10Robot();
	private static String urlHandicap;
	
	public static synchronized boolean init() {
		//投注限制加载
		PK10PaidDao dao = DaoMgr.getPk10Paid();
		List<PK10BetNode> betall = dao.selectBetAll();
		if (betall==null)
			return false;
		for (PK10BetNode node : betall) {
			int index = node.getId();
			
			PK10BetNode _node = mdata.get(index);
			if (_node==null) {
				mdata.put(index, node);
			}
		}
		
		//加载飞盘URL
		urlHandicap = DaoMgr.getSystem().selectOne(Code.SYSTEM_HANDICAPURL);
		if (urlHandicap==null)
			return false;
		
		//同步近2天官网开奖数据
		Pk10GovernmentMgr.dailyPeriod(2);
		
		//获得PK10配表数据
		LotteryNode lotteryNode = DaoMgr.getLottery().selectOne(Code.LORREY_PK10);
		if (lotteryNode==null)
			return false;
		timeOpen = lotteryNode.getOpentime();
		isOpen = false;
		timeSealplate = lotteryNode.getSpacetime();
		isSealPlate = false;
		timeNotBet = lotteryNode.getNotbettime();
		isNotBet = false;
		
		//加载PK10赔付数据
		mpaid = DaoMgr.getPk10Paid().selectOne(Code.LORREY_PK10);
		if (mpaid==null)
			return false;
		
		//获取最近一期开奖数据
		for (int i=0; i<20; i++) {
			info = Pk10GovernmentMgr.getOpencode();
			if (info!=null) {
				break;
			}
		}
		if (info!=null) {
			Pk10GovernmentMgr.insertRecord(info.getPeriod(),info.getOpencode(),info.getOpentime());
		} else {
			return false;
		}
		
		//对未开奖的记录进行结算处理
		String nextPeriod = info.getNextPeriod();
		List<Pk10InfoNode> listBet = DaoMgr.getPk10InfoDao().getNotSettle();
		Map<String,Pk10Player> mapUser = new HashMap<String,Pk10Player>();
		for (Pk10InfoNode node : listBet) {
			if (nextPeriod.equals(node.getPeriod())) {
				pk10Player.addBet(node);
			} else {
				Pk10Player _player = mapUser.get(node.getPeriod());
				if (_player==null) {
					_player = new Pk10Player();
					mapUser.put(node.getPeriod(), _player);
				}
				_player.addBet(node);
			}
		}
		for (Entry<String,Pk10Player> entry : mapUser.entrySet()) {
			String period = entry.getKey();
			Pk10Player _player = entry.getValue();
			
			Pk10PeriodDataInfo periodData = DaoMgr.getPk10PeriodData().selectOne(period);
			if (periodData!=null) {
				_player.openCode(mpaid, period, periodData.getOpencode());
			}
		}
		
		return true;
	}
	
	public static PK10BetNode getMpaid(int lottery_id) {
		return mdata.get(lottery_id);
	}
	
	public static boolean isSealPlate() {
		return isSealPlate;
	}
	
	public static boolean isNotBet() {
		return isNotBet;
	}
	
	public static PK10PaidNode getMpaid() {
		return mpaid;
	}
	public static Pk10Player getPk10Player() {
		return pk10Player;
	}
	
	public static Pk10Robot getPk10Robot() {
		return pk10Robot;
	}
	
	//开盘
	public static void openPlate() {
		isOpen = true;
		isSealPlate = false;	//开盘
		isNotBet = false;		//可以撤单
		String text = "------现在开始下注------";
		ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, text, false);
	}
	
	//封盘
	public static void closePlate() {
		isSealPlate = true;
		String text = "------现在封盘------";
		ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, text, false);
	}
	
	//不可撤单
	public static void notBet() {
		isNotBet = true;
		String text = "------现在开始不可撤单------";
		ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, text, false);
	}
	
	//开奖处理
	public static void openCode(Pk10OpenInfo info) {
		if (mpaid==null) {
			System.out.println("配表错误");
			return;
		}
		try {
			pk10Player.openCode(mpaid,info.getPeriod(),info.getOpencode());
			pk10Robot.openCode(mpaid,info);
		} catch (Exception e) {
			System.out.println("处理开奖失败");
			System.out.println(e);
		} finally {
			pk10Player.clear();
			pk10Robot.clear();
			Pk10PointoutMgr.roundInit();
			Pk10RobotMgr.roundInit();
			isOpen = false;
		}
	}	
	
	public static void setOpentime(long _time) {
		timeOpen = _time;
	}
	public static void setNotBet(long _time) {
		timeNotBet = _time;
	}
	
	public static void setSealplate(long _time) {
		timeSealplate = _time;
	}
	
	public static long getSealplate() {
		return timeSealplate;
	}
	
	public static Pk10OpenInfo getPk10OpenInfo() {
		return info;
	}
	
	private static void setData(Pk10OpenInfo _info) {
		if (info==null) {
			info = _info;
			Pk10GovernmentMgr.insertRecord(info.getPeriod(),info.getOpencode(),info.getOpentime());
		} else if (info.getNextPeriod().equals(_info.getNextPeriod()) && info.getPeriod().equals(_info.getPeriod())) {	
			info.setLocaltime(_info.getLocaltime());
			info.setTimestamp(_info.getTimestamp());
			
			long remain = info.getNextOpentime()-info.getTimestamp();
			if (!isOpen) {	//开盘
				if (remain <= timeOpen) {
					openPlate();
				}
			}
			
			if (!isSealPlate){	//封盘
				if (remain <= timeSealplate) {
					closePlate();
				}
			}
			
			if (!isNotBet) {	//撤单
				if (remain <= timeNotBet) {
					notBet();
				}
			}
			
			int _opentime = (int)((_info.getNextOpentime()-_info.getTimestamp())/1000);
			if (_opentime>0)
				Pk10PointoutMgr.timer(_opentime);
		} else if (info.getNextPeriod().equals(_info.getPeriod())) {	//开奖
			info = _info;
			Pk10PrizeMgr.openCode(info);
			Pk10GovernmentMgr.insertRecord(info.getPeriod(),info.getOpencode(),info.getOpentime());
		} else {
			info = _info;
			Pk10GovernmentMgr.insertRecord(info.getPeriod(),info.getOpencode(),info.getOpentime());
		}
	}
	
	/*
	 * 定时开奖
	 */
	public static void timer() {
		try {
			Pk10OpenInfo _info = Pk10GovernmentMgr.getOpencode();
			if (_info!=null) {
				setData(_info);
			} else {
				Timestamp timestamp = DateUtil.getCurTimestamp();
				info.setLocaltime(timestamp.getTime());
				System.out.println(timestamp + "     syn pk10 fail");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/*
	 * 定时飞盘
	 */
	public static void handicapTimer() {
		if (!isNotBet)
			return;
		try {
			List<Pk10HandicapNode> arrBet = pk10Player.dealHandicap();		
			if (arrBet!=null && arrBet.size()>0) {
				Pk10HandicapCSNode cs = new Pk10HandicapCSNode();
				cs.setArr(arrBet);
				String parame = new Gson().toJson(cs);
				String result = HttpUtil.sendPost(urlHandicap,"str="+parame,2000);
				if (result!=null && result.length()>0) {
					ErrorObj obj = new Gson().fromJson(result, ErrorObj.class);
					if (obj.getCode()==Code.OK_CODE) {
						System.out.println(obj.getDesc());
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}	
}
