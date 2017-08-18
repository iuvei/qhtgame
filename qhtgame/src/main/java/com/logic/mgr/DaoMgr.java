/*
 * 数据库接口集
 */
package com.logic.mgr;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.util.Dao;

import qht.game.dao.AdminDao;
import qht.game.dao.AmountRecordDao;
import qht.game.dao.AppinfoDao;
import qht.game.dao.CustomerDao;
import qht.game.dao.FinanceOrderDao;
import qht.game.dao.FinanceRecordDao;
import qht.game.dao.GameRecordDao;
import qht.game.dao.LotteryDao;
import qht.game.dao.PK10PaidDao;
import qht.game.dao.PeriodRecordDao;
import qht.game.dao.Pk10InfoDao;
import qht.game.dao.Pk10PeriodDataDao;
import qht.game.dao.Pk10PlayerDao;
import qht.game.dao.Pk10Pointout;
import qht.game.dao.Pk10SymbolDao;
import qht.game.dao.PlayerDao;
import qht.game.dao.RebotRecordDao;
import qht.game.dao.RebotSchemeDao;
import qht.game.dao.ReportDao;
import qht.game.dao.ReturnwaterDao;
import qht.game.dao.SystemDao;

public class DaoMgr {
	private static final int DAO_ADMIN = 1;
	private static final int DAO_PLAYER = 2;
	private static final int DAO_PK10PERIODDATA = 3;
	private static final int DAO_GAMERECORD = 4; 
	private static final int DAO_AMOUNTRECORD = 5;
	private static final int DAO_PERIODRECORD = 6;
	private static final int DAO_FINANCEORDER = 10;
	private static final int DAO_FINANCERECORD = 11;
	private static final int DAO_REPORT = 12;
	private static final int DAO_SYSTEM = 13;
	private static final int DAO_LOTTERY = 14;
	private static final int DAO_APPINFO = 15;
	private static final int DAO_PK10SYMBOL = 16;
	private static final int DAO_PK10PLAYER = 17;
	private static final int DAO_PK10PAIDDAO = 18;
	private static final int DAO_PK10POINTOUT = 19;
	private static final int DAO_PK10INFO = 20;
	private static final int DAO_CUSTOMER = 22;
	private static final int DAO_RETURNWATER = 23;
	private static final int DAO_REBOTSCHEME = 24;
	private static final int DAO_REBOTRECORD = 25;
	
	
	
	private static Map<Integer,Object> mdata = new HashMap<Integer,Object>();
	
	public static void init(SqlSessionTemplate sqlSessionTemplate) {
		Dao.init(sqlSessionTemplate);
		mdata.put(DAO_ADMIN, new AdminDao());
		mdata.put(DAO_PLAYER, new PlayerDao());
		mdata.put(DAO_PK10PERIODDATA, new Pk10PeriodDataDao());
		mdata.put(DAO_GAMERECORD, new GameRecordDao());
		mdata.put(DAO_AMOUNTRECORD, new AmountRecordDao());
		mdata.put(DAO_PERIODRECORD, new PeriodRecordDao());
		mdata.put(DAO_FINANCEORDER, new FinanceOrderDao());
		mdata.put(DAO_FINANCERECORD, new FinanceRecordDao());
		mdata.put(DAO_REPORT, new ReportDao());
		mdata.put(DAO_SYSTEM, new SystemDao());
		mdata.put(DAO_LOTTERY, new LotteryDao());
		mdata.put(DAO_APPINFO, new AppinfoDao());
		mdata.put(DAO_CUSTOMER, new CustomerDao());
		mdata.put(DAO_PK10SYMBOL, new Pk10SymbolDao());
		mdata.put(DAO_PK10PLAYER, new Pk10PlayerDao());
		mdata.put(DAO_PK10PAIDDAO, new PK10PaidDao());
		mdata.put(DAO_PK10POINTOUT, new Pk10Pointout());
		mdata.put(DAO_PK10INFO, new Pk10InfoDao());
		mdata.put(DAO_RETURNWATER, new ReturnwaterDao());
		mdata.put(DAO_REBOTSCHEME, new RebotSchemeDao());
		mdata.put(DAO_REBOTRECORD, new RebotRecordDao());
	}

	public static AdminDao getAdmin() {
		return (AdminDao) mdata.get(DAO_ADMIN);
	}
	public static PlayerDao getPlayer() {
		return (PlayerDao) mdata.get(DAO_PLAYER);
	}
	public static Pk10PeriodDataDao getPk10PeriodData() {
		return (Pk10PeriodDataDao) mdata.get(DAO_PK10PERIODDATA);
	}
	public static GameRecordDao getGameRecord() {
		return (GameRecordDao) mdata.get(DAO_GAMERECORD);
	}
	public static AmountRecordDao getAmountRecord() {
		return (AmountRecordDao) mdata.get(DAO_AMOUNTRECORD);
	}
	public static PeriodRecordDao getPeriodRecord() {
		return (PeriodRecordDao) mdata.get(DAO_PERIODRECORD);
	}
	
	public static FinanceOrderDao getFinanceOrder() {
		return (FinanceOrderDao) mdata.get(DAO_FINANCEORDER);
	}

	public static FinanceRecordDao getFinanceRecord() {
		return (FinanceRecordDao) mdata.get(DAO_FINANCERECORD);
	}

	public static ReportDao getReport() {
		return (ReportDao) mdata.get(DAO_REPORT);
	}
	
	public static ReturnwaterDao getReturnwater() {
		return (ReturnwaterDao) mdata.get(DAO_RETURNWATER);
	}
	

	public static SystemDao getSystem() {
		return (SystemDao) mdata.get(DAO_SYSTEM);
	}

	public static LotteryDao getLottery() {
		return (LotteryDao) mdata.get(DAO_LOTTERY);
	}

	public static AppinfoDao getAppinfo() {
		return (AppinfoDao) mdata.get(DAO_APPINFO);
	}
	
	public static CustomerDao getCustomer() {
		return (CustomerDao) mdata.get(DAO_CUSTOMER);
	}

	public static Pk10SymbolDao getPk10Symbol() {
		return (Pk10SymbolDao) mdata.get(DAO_PK10SYMBOL);
	}

	public static Pk10PlayerDao getPk10Player() {
		return (Pk10PlayerDao) mdata.get(DAO_PK10PLAYER);
	}
	public static PK10PaidDao getPk10Paid() {
		return (PK10PaidDao) mdata.get(DAO_PK10PAIDDAO);
	}

	public static Pk10Pointout getPk10Pointout() {
		return (Pk10Pointout) mdata.get(DAO_PK10POINTOUT);
	}
	
	public static Pk10InfoDao getPk10InfoDao() {
		return (Pk10InfoDao) mdata.get(DAO_PK10INFO);
	}

	public static RebotSchemeDao getRebotScheme() {
		return (RebotSchemeDao) mdata.get(DAO_REBOTSCHEME);
	}

	public static RebotRecordDao getPk10RebotRecord() {
		return (RebotRecordDao) mdata.get(DAO_REBOTRECORD);
	}
	
}
