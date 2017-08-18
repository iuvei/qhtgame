package qht.game.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logic.mgr.AdminMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.AdminInfo;
import com.node.KinPageInfo;
import com.node.UserInfo;
import com.sysconst.Code;
import com.sysconst.Function;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.CoinUtil;
import com.util.ComUtil;
import com.util.DateUtil;

import net.node.AdminFinanceOrderNode;
import net.node.AdminFinanceRecordNode;
import net.node.BFinanceOprCSNode;
import net.node.FinanceDealCSNode;
import net.node.FinanceOprCSNode;
import qht.game.dao.AmountRecordDao;
import qht.game.dao.FinanceOrderDao;
import qht.game.dao.FinanceRecordDao;
import qht.game.dao.PlayerDao;
import qht.game.node.AdminNode;
import qht.game.node.AmountRecord;
import qht.game.node.CountOrderNode;
import qht.game.node.FinanceAMDOWNOrderNode;
import qht.game.node.FinanceAMUPOrderNode;
import qht.game.node.FinanceOrderNode;
import qht.game.node.FinanceRecord;
import qht.game.node.PlayerNode;


@Controller
@RequestMapping(value="/finance/")
public class FinanceCLR {
	
	/*
	 * http://---/finance/request.do
	 * 玩家申请充值提现
	 * request: {
	 * 		"type":1,
	 * 		"amount":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/request.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> request(FinanceOprCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		String username = userInfo.getUsername();
		
		int type = cs.getType();
		String strAmount = cs.getAmount();
		if ( 	(type!=Code.AMOUNT_RECHANGE && type!=Code.AMOUNT_WITHDRAWALS) ||
				strAmount==null || strAmount.length()==0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		//String username = player.getUsername();
		BigDecimal amount = CoinUtil.createNew(strAmount);
		if (amount==null || CoinUtil.compareZero(amount)<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		BigDecimal balance = player.getBalance();
		if (type==Code.AMOUNT_WITHDRAWALS) {
			if (CoinUtil.compare(balance, amount)<0)
				return Function.objToJson(Code.NOENGHOUMONEY_CODE,Code.NOENGHOUMONEY_DESC,null);
			balance = CoinUtil.sub(balance, amount);
		} else {
			balance = CoinUtil.add(balance, amount);
		}
		
		FinanceOrderNode record = new FinanceOrderNode();
		record.setOdd(ComUtil.get32UUID());
		record.setUsername(username);
		record.setType(type);
		record.setAmount(amount);
		record.setRequesttime(DateUtil.getCurTimestamp().getTime());
		record.setRequestname(username);
		record.setTag(Code.TAG_NODEAL);
		record.setUpdatetime(DateUtil.getCurTimestamp().getTime());
		if (!DaoMgr.getFinanceOrder().insert(record))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,record);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * http://---/finance/brequest.do
	 * 后台申请充值提现
	 * request: {
	 * 		"username":""
	 * 		"type":1,
	 * 		"amount":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/brequest.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> brequest(BFinanceOprCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		String username = cs.getUsername();
		
		PlayerNode player = null;
		UserInfo userInfo = PlayerMgr.getByName(username);
		if (userInfo==null) {
			player = DaoMgr.getPlayer().selectOneByName(username);
			if (player==null)
				return Function.objToJson(Code.PLAYERNOTEXIT_CODE,Code.PLAYERNOTEXIT_DESC,null);
		} else {
			player = userInfo.getPlayer();
		}
		
		int type = cs.getType();
		String strAmount = cs.getAmount();
		if ( 	(type!=Code.AMOUNT_RECHANGE && type!=Code.AMOUNT_WITHDRAWALS) ||
				strAmount==null || strAmount.length()==0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		BigDecimal amount = CoinUtil.createNew(strAmount);
		if (amount==null || CoinUtil.compareZero(amount)<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		BigDecimal balance = player.getBalance();
		if (type==Code.AMOUNT_WITHDRAWALS) {
			if (CoinUtil.compare(balance, amount)<0)
				return Function.objToJson(Code.NOENGHOUMONEY_CODE,Code.NOENGHOUMONEY_DESC,null);
			balance = CoinUtil.sub(balance, amount);
		} else {
			balance = CoinUtil.add(balance, amount);
		}
		
		FinanceOrderNode record = new FinanceOrderNode();
		record.setOdd(ComUtil.get32UUID());
		record.setUsername(username);
		record.setType(type);
		record.setAmount(amount);
		record.setRequesttime(DateUtil.getCurTimestamp().getTime());
		record.setRequestname(admin.getUsername());
		record.setTag(Code.TAG_NODEAL);
		record.setUpdatetime(DateUtil.getCurTimestamp().getTime());
		if (!DaoMgr.getFinanceOrder().insert(record))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,record);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 处理玩家申请的充值提现记录（后台）
	 * request: {
	 * 		"id":""		订单号
	 * 		"type":1	1_处理 2_忽略
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/deal.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deal(FinanceDealCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		long id = cs.getId();
		int type = cs.getType();
		if ( 	(type!=Code.TAG_DEAL && type!=Code.TAG_IGNORE) ||
				id<=0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		FinanceOrderDao dao = DaoMgr.getFinanceOrder();
		PlayerDao playerDao = DaoMgr.getPlayer();
		AmountRecordDao amountRecordDao = DaoMgr.getAmountRecord();
		FinanceRecordDao financeRecordDao = DaoMgr.getFinanceRecord();
		
		FinanceOrderNode record = dao.selectOne(id);
		if (record==null)
			return Function.objToJson(Code.OBJNOTEXIT_CODE,Code.OBJNOTEXIT_DESC,null);
		if (record.getTag()!=Code.TAG_NODEAL)	//只处理未处理的记录
			return Function.objToJson(Code.STATUS_CODE,Code.STATUS_DESC,null);
		
		PlayerNode player = null;
		UserInfo userInfo = PlayerMgr.getByName(record.getUsername());
		if (userInfo==null) {
			player = playerDao.selectOneByName(record.getUsername());
			if (player==null)
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		} else {
			player = userInfo.getPlayer();
		}
		
		String tmp = "上分";
		if (record.getType()==Code.AMOUNT_WITHDRAWALS)
			tmp = "下分";
		tmp += record.getAmount();
		String respone = "玩家【"+player.getNickname()+"】请求【"+tmp+"】";
		
		if (type==Code.TAG_IGNORE) {	//如果是忽略，直接更新状态
			if (!dao.setTag(record.getId(),Code.TAG_IGNORE)) {
				ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, respone+"处理状态【失败】", false);
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			}
			ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, respone+"处理状态【忽略】", false);
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		} else if (type==Code.TAG_DEAL) {
			//验证余额是否充足
			BigDecimal preBalance = player.getBalance();
			BigDecimal balance = preBalance;
			BigDecimal amount = record.getAmount();
			int financeType = record.getType();
			if (financeType==Code.AMOUNT_WITHDRAWALS) {
				if (CoinUtil.compare(preBalance, amount)<0)
					return Function.objToJson(Code.NOENGHOUMONEY_CODE,Code.NOENGHOUMONEY_DESC,null);
				balance = CoinUtil.sub(preBalance, amount);
			} else if (financeType==Code.AMOUNT_RECHANGE) {
				balance = CoinUtil.add(preBalance, amount);
			} else {
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			}
			
			//更新余额
			long curTimestamp = DateUtil.getCurTimestamp().getTime();
			player.setBalance(balance);
			player.setUpdatetime(curTimestamp);
			playerDao.updateBalance(player.getId(),balance,curTimestamp);
			
			//插入财务记录
			FinanceRecord financeRecord = new FinanceRecord();
			financeRecord.setAppcode(player.getAppcode());
			financeRecord.setUsername(player.getUsername());
			financeRecord.setType(financeType);
			financeRecord.setTypeid(player.getTypeid());
			financeRecord.setAmount(amount);
			financeRecord.setRequestname(record.getRequestname());
			financeRecord.setOprname(admin.getUsername());
			financeRecord.setUpdatetime(DateUtil.getCurTimestamp().getTime());
			financeRecordDao.insert(financeRecord);
			
			//插入资金明细
			AmountRecord amountRecord = new AmountRecord();
			amountRecord.setAppcode(player.getAppcode());
			amountRecord.setUsername(player.getUsername());
			amountRecord.setType(financeType);
			amountRecord.setAmount(amount);
			amountRecord.setBef_bal(preBalance);
			amountRecord.setAft_bal(balance);
			amountRecord.setUpdatetime(DateUtil.getCurTimestamp().getTime());
			amountRecordDao.insert(amountRecord);
			
			if (!dao.setTag(record.getId(),Code.TAG_DEAL)) {
				ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, respone+"处理状态【失败】", false);
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			}
			ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, respone+"处理状态【成功】", false);
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		}
		
		return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
	}
	
	/*
	 * 查询玩家申请的充值提现记录（后台）
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/bselect.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bselect(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		List<FinanceOrderNode> data = DaoMgr.getFinanceOrder().selectList();
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	/*http://119.23.125.241:8080/qhtgame/finance/countselect.do
	 * 查询玩家申请的充值提现记录（后台）  条数 zxb 20170508
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,"desc":"成功","info":{"num":4}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/countselect.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> countselect(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		CountOrderNode data = DaoMgr.getFinanceOrder().countselectList();
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	//按条件 财务记录： http://---/finance/frecordlist.do
	@CrossOrigin
	@RequestMapping(value="/frecordlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> financeRecordlist(AdminFinanceRecordNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		FinanceRecordDao dao = DaoMgr.getFinanceRecord();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<FinanceRecord> pageList = dao.selectList(parameter);
			PageInfo<FinanceRecord> pageInfo = new PageInfo<FinanceRecord>(pageList);
			
			data = new KinPageInfo();
			data.setTotal(pageInfo.getTotal());
			data.setPages(pageInfo.getPages());
			data.setPageNum(pageInfo.getPageNum());
			data.setPageSize(pageInfo.getPageSize());
			data.setSize(pageInfo.getSize());
			data.setObj(pageList);
		} catch (Exception e) {
			data = null;
			System.out.println(e);
		}
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	//按条件 查询订单  上分查询： http://---/finance/upOrderselectlist.do
		@CrossOrigin
		@RequestMapping(value="/upOrderselectlist.do",method=RequestMethod.POST)
		@ResponseBody
		public Map<String,Object> fOrderselectlist(AdminFinanceOrderNode cs, HttpServletRequest request) {
			String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
			if (sessionID==null)
				return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
			AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
			if (adminInfo==null)
				return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
			AdminNode admin = adminInfo.getAdmin();
			if (admin==null)
				return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
			
			Map<String,Object> parameter = cs.getParameter();
			if (parameter==null)
				return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
			FinanceOrderDao dao = DaoMgr.getFinanceOrder();
			
			String begintime = cs.getBegintime();
			String endtime = cs.getEndtime();
			Timestamp _begin = DateUtil.StringToTimestamp(begintime);
			Timestamp _end = DateUtil.StringToTimestamp(endtime);
			if (_begin==null || _end==null)
				return null;
			long lbegintime = _begin.getTime();
			long lendtime = _end.getTime();
			
			KinPageInfo data = null;
			try {
				PageHelper.startPage(cs.getPage(),cs.getCount());
				List<FinanceOrderNode> pageList = dao.selectListup(parameter);
				
				FinanceAMUPOrderNode AM = dao.countByAMUP(cs.getUsername(),cs.getTag(),lbegintime,lendtime);
				
				PageInfo<FinanceOrderNode> pageInfo = new PageInfo<FinanceOrderNode>(pageList);
				data = new KinPageInfo();
				data.setTotal(pageInfo.getTotal());
				data.setPages(pageInfo.getPages());
				data.setPageNum(pageInfo.getPageNum());
				data.setPageSize(pageInfo.getPageSize());
				data.setSize(pageInfo.getSize());
				data.setObj(pageList);
				data.setAbj(AM);
//				data.setObj(AM);
			} catch (Exception e) {
				data = null;
				System.out.println(e);
			}
			if (data==null)
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
		}
		
		//按条件 查询订单  下分查询： http://---/finance/downOrderselectlist.do
		@CrossOrigin
		@RequestMapping(value="/downOrderselectlist.do",method=RequestMethod.POST)
		@ResponseBody
		public Map<String,Object> downOrderselectlist(AdminFinanceOrderNode cs, HttpServletRequest request) {
			String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
			if (sessionID==null)
				return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
			AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
			if (adminInfo==null)
				return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
			AdminNode admin = adminInfo.getAdmin();
			if (admin==null)
				return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
			
			Map<String,Object> parameter = cs.getParameter();
			if (parameter==null)
				return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
			
			FinanceOrderDao dao = DaoMgr.getFinanceOrder();
			
			String begintime = cs.getBegintime();
			String endtime = cs.getEndtime();
			Timestamp _begin = DateUtil.StringToTimestamp(begintime);
			Timestamp _end = DateUtil.StringToTimestamp(endtime);
			if (_begin==null || _end==null)
				return null;
			long lbegintime = _begin.getTime();
			long lendtime = _end.getTime();
			
			KinPageInfo data = null;
			try {
				PageHelper.startPage(cs.getPage(),cs.getCount());
				List<FinanceOrderNode> pageList = dao.selectListdown(parameter);
				FinanceAMDOWNOrderNode AM = dao.countByAMDOWN(cs.getUsername(),cs.getTag(),lbegintime,lendtime);
				PageInfo<FinanceOrderNode> pageInfo = new PageInfo<FinanceOrderNode>(pageList);
				
				data = new KinPageInfo();
				data.setTotal(pageInfo.getTotal());
				data.setPages(pageInfo.getPages());
				data.setPageNum(pageInfo.getPageNum());
				data.setPageSize(pageInfo.getPageSize());
				data.setSize(pageInfo.getSize());
				data.setObj(pageList);
				data.setAbj(AM);
			} catch (Exception e) {
				data = null;
				System.out.println(e);
			}
			if (data==null)
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
		}
	
}
