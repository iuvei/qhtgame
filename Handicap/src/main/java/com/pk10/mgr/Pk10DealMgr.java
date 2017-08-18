package com.pk10.mgr;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mgr.DaoMgr;
import com.node.ErrorCode;
import com.node.HandicapLitNode;
import com.node.HandicapMidNode;
import com.node.Pk10CurNode;
import com.node.Pk10Info;
import com.pk10.logic.HongGuangOpr;
import com.pk10.logic.Pk10WebInter;
import com.pk10.logic.YinHeOpr;
import com.sysconst.Code;
import com.util.CoinUtil;
import com.util.ComUtil;
import com.util.DateUtil;

import qht.game.dao.Pk10HandicapDao;
import qht.game.dao.Pk10WebinfoDao;
import qht.game.node.Pk10HandicapBetNode;
import qht.game.node.Pk10WebinfoNode;

public class Pk10DealMgr {
	private static final BigDecimal MIX_SINGLE_BET = CoinUtil.createNew("20000");	//单注限额
	private static final BigDecimal MIX_SINGLE_PERIOD = CoinUtil.createNew("200000"); //单期限额
	
	private static Map<Integer,Pk10WebinfoNode> mdata = new HashMap<Integer,Pk10WebinfoNode>();
	private static Map<Integer,Pk10WebInter> mopr = new HashMap<Integer,Pk10WebInter>();
	private static Pk10WebInter opr;
	
	public static synchronized boolean init() {
		//获取站点信息
		Pk10WebinfoDao dao = DaoMgr.getPk10Webinfo();
		if (!dao.updateInit(Code.PK10_STATUS_STOP))
			return false;
		List<Pk10WebinfoNode> listData =dao.selectList();
		if (listData==null)
			return false;
		for (Pk10WebinfoNode node : listData) {
			mdata.put(node.getId(), node);
			int type = node.getType();
			if (type==Code.PK10_TYPE_HONGGUANG)
				mopr.put(node.getId(), new HongGuangOpr(node.getId()));
			else if (type == Code.PK10_TYPE_YINHE)
				mopr.put(node.getId(), new YinHeOpr(node.getId()));
			else if (type==Code.PK10_TYPE_YONGLI)
				;
			else
				return false;
		}
		
		opr = null;
		return true;
	}
	
	public static Pk10WebinfoNode getWebinfo() {
		if (opr==null)
			return null;
		return mdata.get(opr.getId());
	}
	
	public static Pk10WebinfoNode getWebinfo(int id) {
		return mdata.get(id);
	}
	
	/*
	 * 打开网站登入，输入用户名与密码
	 */
	public static synchronized boolean open1(int id, String url,String username,String password) {
		if (opr!=null) {
			opr.close();
			opr = null;
		}
		opr = mopr.get(id);
		if (opr==null)
			return false;
		if (!opr.open1(url, username, password))
			return false;
		
		return true;
	}
	
	/*
	 * 获取验证码
	 */
	public static synchronized boolean open2(String path) {
		if (opr==null)
			return false;
		if (!opr.open2(path))
			return false;
		return true;
	}
	
	/*
	 * 输入验证码并登入网站
	 */
	public static synchronized boolean open3(String check) {
		if (opr==null)
			return false;
		if (!opr.open3(check))
			return false;
		opr.flushInfo();
		return true;
	}

	/*
	 * 关闭网站
	 */
	public static synchronized void close() {
		if (opr!=null) {
			opr.close();
			opr = null;
		}
	}
	
	/*
	 * 获得实时信息
	 */
	public static Pk10CurNode getInfo() {
		if (opr==null)
			return null;
		if (!opr.isOk())
			return null;
		return opr.getInfo();
	}
	
	/*
	 * 刷新网站信息
	 */
	public static synchronized void flush() {
		if (opr==null)
			return;
		if (opr.isOk()) {
			if (!opr.flushInfo()) {
				if (!opr.isOk()) {
					opr.close();
					Pk10WebinfoDao dao = DaoMgr.getPk10Webinfo();
					long updatetime = DateUtil.getCurTimestamp().getTime();
					if (!dao.setStatus(opr.getId(),Code.PK10_STATUS_STOP,updatetime))
						return;
					Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(opr.getId());
					if (___webinfo!=null)
						___webinfo.setStatus(Code.PK10_STATUS_STOP);
				}
			}
		}
	}

	/*
	 * 投注处理
	 */
	public static synchronized void deal() {
		if (opr==null)
			return;
		if (!opr.isOk()) {
			return;
		}
		Pk10CurNode info = opr.getInfo();
		if (info==null)
			return;
		String period = info.getPeriod();
		if (period==null || period.length()==0)
			return;
		int sealTime = info.getSealtime();
		if (sealTime<8)
			return;
		
		Pk10HandicapDao dao = DaoMgr.getPk10Handicap();
		List<Pk10HandicapBetNode> arrBet = dao.statusSelectlist(period, Code.PK10_HANDICAP_BET);	//从数据库中提取
		if (arrBet==null || arrBet.size()==0)
			return;
		HandicapMidNode betInfo = mycacl(arrBet,info.getBalance(),info.getAmount());	//选出压注额最大的一个接口
		if (betInfo==null)
			return;
		
		//更新实时信息
		if (!opr.flushInfo()) {
			if (!opr.isOk()) {
				opr.close();
				Pk10WebinfoDao _dao = DaoMgr.getPk10Webinfo();
				long updatetime = DateUtil.getCurTimestamp().getTime();
				if (!_dao.setStatus(opr.getId(),Code.PK10_STATUS_STOP,updatetime))
					return;
				Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(opr.getId());
				if (___webinfo!=null)
					___webinfo.setStatus(Code.PK10_STATUS_STOP);
			}
			System.out.println(DateUtil.TimestampToString(DateUtil.getCurTimestamp(), "YYYY-MM-DD HH:mm:ss") + "  opr.flushInfo error");
			return;
		}
		
		//投注
		ErrorCode errorCode = opr.bet(betInfo.getCtypep(), period, betInfo.getArrData());
		if (errorCode==null) {
			if (!opr.isOk()) {
				opr.close();
				Pk10WebinfoDao _dao = DaoMgr.getPk10Webinfo();
				long updatetime = DateUtil.getCurTimestamp().getTime();
				if (!_dao.setStatus(opr.getId(),Code.PK10_STATUS_STOP,updatetime))
					return;
				Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(opr.getId());
				if (___webinfo!=null)
					___webinfo.setStatus(Code.PK10_STATUS_STOP);
			}
			System.out.println(DateUtil.TimestampToString(DateUtil.getCurTimestamp(), "YYYY-MM-DD HH:mm:ss") + "  opr.bet error");
			return;
		}
		int code = errorCode.getCode();
		if (code==Code.BET_FAIL) {
			if (!opr.isOk()) {
				opr.close();
				Pk10WebinfoDao _dao = DaoMgr.getPk10Webinfo();
				long updatetime = DateUtil.getCurTimestamp().getTime();
				if (!_dao.setStatus(opr.getId(),Code.PK10_STATUS_STOP,updatetime))
					return;
				Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(opr.getId());
				if (___webinfo!=null)
					___webinfo.setStatus(Code.PK10_STATUS_STOP);
			}
		}
		for (Pk10Info node : betInfo.getArrData()) {	//处理结果
			List<String> arrOdd = node.getArrOdd();
			for (String odd : arrOdd) {
				int status = 0;
				String eventuate = "";
				if (code==Code.BET_SUCC || code==Code.BET_SUCC_BUT_RETURN_ERROR) {
					status = Code.PK10_HANDICAP_SETTLE;
					eventuate = ""+code+":succ";
					dao.updateStatus(odd,status,eventuate);
				} else if (code==Code.BET_FAIL) {
					;	//不作处理，回头接着投
				} else {	
					status = Code.PK10_HANDICAP_DEALFAIL;
					eventuate = ""+code+":fail";
					dao.updateStatus(odd,status,eventuate);
				}
			}
		}
	}

	/*
	 * 根据网站投注限额，汇总下注信息，筛选出最大下注额的接口数据并返回
	 */
	private static HandicapMidNode mycacl(List<Pk10HandicapBetNode> arrBet, BigDecimal balance, BigDecimal periodAmount) {
		Map<String,HandicapLitNode> result = new HashMap<String,HandicapLitNode>();
		
		BigDecimal amount = CoinUtil.zero;
		String primary = null;
		int runway = 0;
		int bettype = 0;
		String odd = null;
		for (Pk10HandicapBetNode node : arrBet) {
			runway = node.getRunway();
			amount = node.getBetamount();
			bettype = node.getBettype();
			odd = node.getOdd();
			
			if (runway==1 || runway==2 || runway==11) {
				primary = Pk10WebInter.CTYPEP2;
			} else if (runway==3 || runway==4 || runway==5 || runway==6) {
				primary = Pk10WebInter.CTYPEP3;
			} else if (runway==7 || runway==8 || runway==9 || runway==10) {
				primary = Pk10WebInter.CTYPEP4;
			} else {
				return null;
			}
			
			HandicapLitNode litList = result.get(primary);
			if (litList==null) {
				litList = new HandicapLitNode();
				result.put(primary,litList);
			}
			
			BigDecimal afterAllBet = CoinUtil.add(litList.getAllBet(), amount);	//余额限制
			BigDecimal afterPeriodAmount = CoinUtil.add(periodAmount, amount);	//单期限制
			if (	CoinUtil.compare(afterAllBet, balance)>0 ||
					CoinUtil.compare(afterPeriodAmount, MIX_SINGLE_PERIOD)>0 )
				continue;
			
				Pk10Info info = null;
				for (Pk10Info nnode : litList.getArrList()) {
					if (nnode.getRunway()==runway) {
						info = nnode;
						break;
					}
				}
				if (info==null) {
					info = new Pk10Info(runway);
					litList.addOneList(info);
				}
				
				BigDecimal aamount = CoinUtil.zero;
				switch (bettype) {
				case Code.PK10_BIG: {
						aamount = info.getBig();
					}
					break;
				case Code.PK10_SMALL: {
						aamount = info.getSmall();
					}
					break;
				case Code.PK10_SINGLE: {
						aamount = info.getSingle();
					}
					break;
				case Code.PK10_DOU: {
						aamount = info.getDou();
					}
					break;
				case Code.PK10_DRAGON: {
						aamount = info.getDragon();
					}
					break;
				case Code.PK10_TIGER: {
						aamount = info.getTiger();
					}
					break;
				default: {
						aamount = info.getNum(bettype);
					}
					break;
				}
				aamount = CoinUtil.add(aamount, amount);
				if (	CoinUtil.compare(aamount, MIX_SINGLE_BET)>0 )	//单注限制
					continue;
				
				
				switch (bettype) {
				case Code.PK10_BIG: {
						info.addBig(amount);
					}
					break;
				case Code.PK10_SMALL: {
						info.addSmall(amount);
					}
					break;
				case Code.PK10_SINGLE: {
						info.addSingle(amount);
					}
					break;
				case Code.PK10_DOU: {
						info.addDou(amount);
					}
					break;
				case Code.PK10_DRAGON: {
						info.addDragon(amount);
					}
					break;
				case Code.PK10_TIGER: {
						info.addTiger(amount);
					}
					break;
				default: {
						info.addArrNum(bettype, amount);
					}
					break;
				}
				info.addArrOdd(odd);
				litList.setAllBet(afterAllBet);
			}
		
		//选出最大压注额的投注接口
		String _crtype = null;
		List<Pk10Info> arr = null;
		BigDecimal _bet = CoinUtil.zero;
		for (Entry<String,HandicapLitNode> entry : result.entrySet()) {
			String crtype = entry.getKey();
			HandicapLitNode litNode = entry.getValue();
			
			if (CoinUtil.compare(litNode.getAllBet(), _bet)>0) {
				_crtype = crtype;
				arr = litNode.getArrList();
				_bet = litNode.getAllBet();
			}
		}
		
		return new HandicapMidNode(_crtype,arr);
	}
	
	/*
	 * 更新记录到数据库
	 */
	public static void insert(String period, int runway, int bettype, BigDecimal amount) {
		Pk10HandicapBetNode betInfo = new Pk10HandicapBetNode();
		betInfo.setOdd(ComUtil.get32UUID());
		betInfo.setPeriod(period);
		betInfo.setRunway(runway);
		betInfo.setBettype(bettype);
		betInfo.setBetamount(amount);
		betInfo.setStatus(Code.PK10_HANDICAP_BET);
		betInfo.setEventuate(" ");
		betInfo.setUpdatetime(DateUtil.getCurTimestamp().getTime());
		betInfo.setCreatetime(DateUtil.getCurTimestamp().getTime());
		
		Pk10HandicapDao dao = DaoMgr.getPk10Handicap();
		dao.insertOne(betInfo);
	}
}
