/*
 * 报表生成
 */

package com.logic.mgr;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sysconst.Code;
import com.util.CoinUtil;
import com.util.DateUtil;

import qht.game.dao.FinanceRecordDao;
import qht.game.dao.GameRecordDao;
import qht.game.dao.ReportDao;
import qht.game.dao.ReturnwaterDao;
import qht.game.node.FinanceReportNode;
import qht.game.node.GameReportNode;
import qht.game.node.ReportNode;
import qht.game.node.ReturnwaterNode;

public class ReportGenMgr {
	
	private static long lgenTime = 1490354950160L;
	
	public static boolean init() {
		String str = DaoMgr.getSystem().selectOne(Code.SYSTEM_REPORT);
		lgenTime = Long.valueOf(str);
		if (lgenTime<=0)
			return false;
		return true;
	}
	
	public static void timer() {
		Timestamp genTime = new Timestamp(lgenTime);
		Timestamp curTime = new Timestamp(new Date().getTime());
		while (DateUtil.comparamDate(genTime,curTime)<0) {
			int date = DateUtil.getDay(genTime);
			long beginTime = DateUtil.getStartTimestamp(date).getTime();
			long endTime = DateUtil.getEndTimestamp(date).getTime();
			genReport(date,beginTime,endTime);
			lgenTime+= 86400000;
			genTime = new Timestamp(lgenTime);
			DaoMgr.getSystem().update(Code.SYSTEM_REPORT,String.valueOf(lgenTime));
		}
	}
	
	private static void genReport(int date, long beginTime, long endTime) {
		FinanceRecordDao financeDao = DaoMgr.getFinanceRecord();
		GameRecordDao gameDao = DaoMgr.getGameRecord();
		ReportDao reportDao = DaoMgr.getReport();
		ReturnwaterDao returnwaterDao = DaoMgr.getReturnwater();
		if (	financeDao==null ||
				gameDao==null ||
				reportDao==null ||
				returnwaterDao==null)
			return;
		
		List<FinanceReportNode> financeList = financeDao.sumSelectList(beginTime,endTime);
		List<GameReportNode> gameList = gameDao.sumSelectList(beginTime,endTime);
		Map<String,ReportNode> mapReport = new HashMap<String,ReportNode>();
				
		for (FinanceReportNode node : financeList) {
			String primary = node.getAppcode()+"#"+node.getUsername()+"#"+node.getTypeid();
			ReportNode reportNode = mapReport.get(primary);
			if (reportNode==null) {
				reportNode = new ReportNode();
				reportNode.setDate(date);
				reportNode.setAppcode(node.getAppcode());
				reportNode.setUsername(node.getUsername());
				reportNode.setTypeid(node.getTypeid());
				reportNode.setRecharge_count(0);
				reportNode.setRecharge_amount(CoinUtil.zero);
				reportNode.setWithdrawals_count(0);
				reportNode.setWithdrawals_amount(CoinUtil.zero);
				reportNode.setGame_count(0);
				reportNode.setBet_amount(CoinUtil.zero);
				reportNode.setPaid_amount(CoinUtil.zero);
				reportNode.setUpdatetime(DateUtil.getCurTimestamp().getTime());
				
				mapReport.put(primary, reportNode);
			}
			
			int type = node.getType();
			if (type==Code.AMOUNT_RECHANGE) {
				reportNode.setRecharge_count(reportNode.getRecharge_count()+node.getCount());
				reportNode.setRecharge_amount(CoinUtil.add(reportNode.getRecharge_amount(),node.getSumAmount()));
			} else if (type==Code.AMOUNT_WITHDRAWALS) {
				reportNode.setWithdrawals_count(reportNode.getWithdrawals_count()+node.getCount());
				reportNode.setWithdrawals_amount(CoinUtil.add(reportNode.getWithdrawals_amount(),node.getSumAmount()));
			}
		}
		
		for (GameReportNode node : gameList) {
			String primary = node.getAppcode()+"#"+node.getUsername()+"#"+node.getTypeid();
			ReportNode reportNode = mapReport.get(primary);
			if (reportNode==null) {
				reportNode = new ReportNode();
				reportNode.setDate(date);
				reportNode.setAppcode(node.getAppcode());
				reportNode.setUsername(node.getUsername());
				reportNode.setTypeid(node.getTypeid());
				reportNode.setRecharge_count(0);
				reportNode.setRecharge_amount(CoinUtil.zero);
				reportNode.setWithdrawals_count(0);
				reportNode.setWithdrawals_amount(CoinUtil.zero);
				reportNode.setGame_count(0);
				reportNode.setBet_amount(CoinUtil.zero);
				reportNode.setPaid_amount(CoinUtil.zero);
				reportNode.setUpdatetime(DateUtil.getCurTimestamp().getTime());
				
				mapReport.put(primary, reportNode);
			}
			
			reportNode.setGame_count(reportNode.getGame_count()+node.getCount());
			reportNode.setBet_amount(CoinUtil.add(reportNode.getBet_amount(),node.getSumBet()));
			reportNode.setPaid_amount(CoinUtil.add(reportNode.getPaid_amount(),node.getSumPaid()));
		}
		
		for (Entry<String,ReportNode> entry : mapReport.entrySet()) {
			reportDao.insert(entry.getValue());
		}
		
		for (Entry<String,ReportNode> entry : mapReport.entrySet()) {
			ReportNode reportNode = entry.getValue();
			ReturnwaterNode node = new ReturnwaterNode();
			node.setDate(reportNode.getDate());
			node.setAppcode(reportNode.getAppcode());
			node.setUsername(reportNode.getUsername());
			node.setTypeid(reportNode.getTypeid());
			node.setWater_amount(reportNode.getBet_amount());
			BigDecimal profit_amount = CoinUtil.sub(reportNode.getPaid_amount(), reportNode.getBet_amount());
			node.setProfit_amount(profit_amount);
			node.setUp_amount(reportNode.getRecharge_amount());
			node.setDown_amount(reportNode.getWithdrawals_amount());
			node.setStatus(Code.RETURN_NO);
			node.setReturn_amount(CoinUtil.zero);
			long tmpTime = DateUtil.getCurTimestamp().getTime();
			node.setUpdatetime(tmpTime);
			node.setCreatetime(tmpTime);

			returnwaterDao.insert(node);
		}
		
	}

}
