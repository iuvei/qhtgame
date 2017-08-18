package qht.game.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logic.mgr.AdminMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.AdminInfo;
import com.node.KinPageInfo;
import com.node.UserInfo;
import com.pk10.logic.Pk10PrizeMgr;
import com.pk10.node.Pk10OpenInfo;
import com.sysconst.Code;
import com.sysconst.Function;
import com.util.CoinUtil;
import com.util.DateUtil;

import net.node.AdminAmountRecordNode;
import net.node.AdminFinanceRecordNode;
import net.node.AdminGameRecordNode;
import net.node.AdminReportSelectListCSNode;
import net.node.PlayerFinanceRecordNode;
import net.node.PlayerGameRecordNode;
import net.node.PlayerPk10OpencodeNode;
import qht.game.dao.AmountRecordDao;
import qht.game.dao.FinanceRecordDao;
import qht.game.dao.GameRecordDao;
import qht.game.dao.Pk10PeriodDataDao;
import qht.game.dao.ReportDao;
import qht.game.node.AdminNode;
import qht.game.node.AmountRecord;
import qht.game.node.FinanceRecord;
import qht.game.node.GameRecord;
import qht.game.node.GameRecordPlayer;
import qht.game.node.Pk10PeriodDataInfo;
import qht.game.node.Pk10PeriodDataNode;
import qht.game.node.PlayerNode;
import qht.game.node.ReportBetPaidByDayNode;
import qht.game.node.ReportNode;

@Controller
@RequestMapping(value="/record/")
public class RecordCLR {

	/*
	 * http://---/record/playerprofit.do
	 * 查询  平台上期盈亏  zxb20170526
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 			},
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/playerprofit.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectpk10betinfo(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		Pk10OpenInfo openInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (openInfo==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		String period = openInfo.getPeriod();
//		period = "618072";
		    GameRecordDao dao = DaoMgr.getGameRecord();
		    
		    GameRecordPlayer bet = dao.selectcountbet(period);
		    
		    if (bet==null) 
		    	return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		    
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,bet);
	}
	
	/*
	 * http://---/record/financeselectlist.do
	 * 查询玩家的财务记录
	 * request: {
	 * 		"username":""	必填,自己的用户名（登入消息返回的）
	 * 		"type":1		0_全部 1_充值 2_提款
	 * 		"operator":""	操作员
	 * 		"begintime"""	起始时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"endtime"""		终止时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":1,		1_充值 2_提现
	 * 				"typeid":1,		1_真实 2_虚拟
	 * 				"amount":0.00
	 * 				"requestname":"",
	 * 				"oprname":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":1,		1_充值 2_提现
	 * 				"amount":0.00
	 * 				"requestname":"",
	 * 				"oprname":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":1,		1_充值 2_提现
	 * 				"amount":0.00
	 * 				"requestname":"",
	 * 				"oprname":"",
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/financeselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> financeSelectList(PlayerFinanceRecordNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		if (!username.equals(cs.getUsername()))
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
	
	/*
	 * http://---/record/bfinanceselectlist.do
	 * 查询玩家的财务记录(后台)
	 * request: {
	 * 		"username":""	必填,自己的用户名（登入消息返回的）
	 * 		"type":1		0_全部 1_充值 2_提款
	 * 		"operator":""	操作员
	 * 		"begintime"""	起始时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"endtime"""		终止时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":1,		1_充值 2_提现
	 * 				"amount":0.00
	 * 				"requestname":"",
	 * 				"oprname":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":1,		1_充值 2_提现
	 * 				"amount":0.00
	 * 				"requestname":"",
	 * 				"oprname":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":1,		1_充值 2_提现
	 * 				"amount":0.00
	 * 				"requestname":"",
	 * 				"oprname":"",
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/bfinanceselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bfinanceSelectList(AdminFinanceRecordNode cs, HttpServletRequest request) {
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
	
	/*
	 * http://---/record/gameselectlist.do
	 * 查询玩家的游戏记录
	 * request: {
	 * 		"username":""	必填,自己的用户名（登入消息返回的）
	 * 		"lottery_id":1	彩种编号（目前Pk10=1) 0表示 查全部
	 * 		"period":""		期号(空串表示查全部)
	 * 		"begintime"""	起始时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"endtime"""		终止时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"lotteryid":1,		彩种
	 * 				"period":"",		期号
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"typeid":1,		1_真实 2_虚拟
	 * 				"betamount":0.00,	压注金额
	 * 				"paidamount":0.00	赔付金额
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"lotteryid":1,		彩种
	 * 				"period":"",		期号
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"typeid":1,		1_真实 2_虚拟
	 * 				"betamount":0.00,	压注金额
	 * 				"paidamount":0.00	赔付金额
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"lotteryid":1,		彩种
	 * 				"period":"",		期号
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"typeid":1,		1_真实 2_虚拟
	 * 				"betamount":0.00,	压注金额
	 * 				"paidamount":0.00	赔付金额
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/gameselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> gameSelectList(PlayerGameRecordNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		if (!username.equals(cs.getUsername()))
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		GameRecordDao dao = DaoMgr.getGameRecord();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<GameRecord> pageList = dao.selectList(parameter);
			PageInfo<GameRecord> pageInfo = new PageInfo<GameRecord>(pageList);
			
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
	
	/*
	 * http://---/record/bgameselectlist.do
	 * 查询玩家的游戏记录(后台)
	 * request: {
	 * 		"username":""	
	 * 		"lottery_id":1	彩种编号（目前Pk10=1) 0表示 查全部
	 * 		"period":""		期号(空串表示查全部)
	 * 		"begintime"""	起始时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"endtime"""		终止时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"lottery_id":1,		彩种
	 * 				"period":"",		期号
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"group_id":"",		群ID
	 * 				"username":"",
	 * 				"bet_amount":0.00,	压注金额
	 * 				"paid_amount":0.00	赔付金额
	 * 				"bet_time":1490853310369,
	 * 				"paid_time":1490853310369,
	 * 				"detail":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"lottery_id":1,		彩种
	 * 				"period":"",		期号
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"group_id":"",		群ID
	 * 				"username":"",
	 * 				"bet_amount":0.00,	压注金额
	 * 				"paid_amount":0.00	赔付金额
	 * 				"bet_time":1490853310369,
	 * 				"paid_time":1490853310369,
	 * 				"detail":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"lottery_id":1,		彩种
	 * 				"period":"",		期号
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"group_id":"",		群ID
	 * 				"username":"",
	 * 				"bet_amount":0.00,	压注金额
	 * 				"paid_amount":0.00	赔付金额
	 * 				"bet_time":1490853310369,
	 * 				"paid_time":1490853310369,
	 * 				"detail":"",
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/bgameselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bgameSelectList(AdminGameRecordNode cs, HttpServletRequest request) {
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
		
		GameRecordDao dao = DaoMgr.getGameRecord();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<GameRecord> pageList = dao.selectList(parameter);
			PageInfo<GameRecord> pageInfo = new PageInfo<GameRecord>(pageList);
			
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

	/*
	 * http://---/record/bamountselectlist.do
	 * 查询玩家的资金变化记录(后台)
	 * request: {
	 * 		"username":""
	 * 		"type":1		0_全部 1_充值 2_提现 3_投注 4_赔付 5_取消投注
	 * 		"begintime"""	起始时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"endtime"""		终止时间  格式为YYYY-MM-DD HH:mm:ss
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"player_id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":"",			1_充值 2_提现 3_投注 4_赔付 5_取消投注
	 * 				"amount":0.00,		金额
	 * 				"bef_bal":0.00		操作前余额
	 *				"aft_bal":0.00		操作后余额
	 * 				"detail":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"player_id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":"",			1_充值 2_提现 3_投注 4_赔付 5_取消投注
	 * 				"amount":0.00,		金额
	 * 				"bef_bal":0.00		操作前余额
	 *				"aft_bal":0.00		操作后余额
	 * 				"detail":"",
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"player_id":1,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"type":"",			1_充值 2_提现 3_投注 4_赔付 5_取消投注
	 * 				"amount":0.00,		金额
	 * 				"bef_bal":0.00		操作前余额
	 *				"aft_bal":0.00		操作后余额
	 * 				"detail":"",
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/bamountselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bamountSelectList(AdminAmountRecordNode cs, HttpServletRequest request) {
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
		
		AmountRecordDao dao = DaoMgr.getAmountRecord();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<AmountRecord> pageList = dao.selectList(parameter);
			PageInfo<AmountRecord> pageInfo = new PageInfo<AmountRecord>(pageList);
			
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
	
	
	/*
	 * http://---/record/pk10opencodeselectlist.do
	 * 查询pk10开奖记录
	 * request: {
	 * 		"period":""		期数　空串为查全部
	 * 		"date":""		日期  	空串为查全部（YYYY-MM-DD）
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"period":"",
	 * 				"opencode":"",
	 * 				"opentime":1490853310369
	 * 			},
	 * 			{
	 * 				"period":"",
	 * 				"opencode":"",
	 * 				"opentime":1490853310369
	 * 			},
	 * 			{
	 * 				"period":"",
	 * 				"opencode":"",
	 * 				"opentime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/pk10opencodeselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pk10opencodeSelectList(PlayerPk10OpencodeNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10PeriodDataDao dao = DaoMgr.getPk10PeriodData();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<Pk10PeriodDataNode> pageList = dao.selectList(parameter);
			PageInfo<Pk10PeriodDataNode> pageInfo = new PageInfo<Pk10PeriodDataNode>(pageList);
			
			List<Pk10PeriodDataInfo> result = new ArrayList<Pk10PeriodDataInfo>();
			for (Pk10PeriodDataNode node : pageList) {
				Pk10PeriodDataInfo info = new Pk10PeriodDataInfo();
				info.setPeriod(node.getPeriod());
				info.setOpentime(node.getOpentime());
				String str = node.getOpencode();
				List<Integer> opencode = new Gson().fromJson(str, new TypeToken<List<Integer>>() {}.getType());
				info.setOpencode(opencode);
				
				result.add(info);
			}
			
			data = new KinPageInfo();
			data.setTotal(pageInfo.getTotal());
			data.setPages(pageInfo.getPages());
			data.setPageNum(pageInfo.getPageNum());
			data.setPageSize(pageInfo.getPageSize());
			data.setSize(pageInfo.getSize());
			data.setObj(result);
		} catch (Exception e) {
			data = null;
			System.out.println(e);
		}
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	/*
	 * http://---/record/bpk10opencodeselectlist.do
	 * 查询pk10开奖记录(后台)
	 * request: {
	 * 		"period":""		期数　空串为查全部
	 * 		"date":""		日期  	空串为查全部（YYYY-MM-DD）
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"period":"",
	 * 				"opencode":"",
	 * 				"opentime":1490853310369
	 * 			},
	 * 			{
	 * 				"period":"",
	 * 				"opencode":"",
	 * 				"opentime":1490853310369
	 * 			},
	 * 			{
	 * 				"period":"",
	 * 				"opencode":"",
	 * 				"opentime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="bpk10opencodeselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bpk10opencodeSelectList(PlayerPk10OpencodeNode cs, HttpServletRequest request) {
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
		
		Pk10PeriodDataDao dao = DaoMgr.getPk10PeriodData();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<Pk10PeriodDataNode> pageList = dao.selectList(parameter);
			PageInfo<Pk10PeriodDataNode> pageInfo = new PageInfo<Pk10PeriodDataNode>(pageList);
			
			List<Pk10PeriodDataInfo> result = new ArrayList<Pk10PeriodDataInfo>();
			for (Pk10PeriodDataNode node : pageList) {
				Pk10PeriodDataInfo info = new Pk10PeriodDataInfo();
				info.setPeriod(node.getPeriod());
				info.setOpentime(node.getOpentime());
				String str = node.getOpencode();
				List<Integer> opencode = new Gson().fromJson(str, new TypeToken<List<Integer>>() {}.getType());
				info.setOpencode(opencode);
				
				result.add(info);
			}
			
			data = new KinPageInfo();
			data.setTotal(pageInfo.getTotal());
			data.setPages(pageInfo.getPages());
			data.setPageNum(pageInfo.getPageNum());
			data.setPageSize(pageInfo.getPageSize());
			data.setSize(pageInfo.getSize());
			data.setObj(result);
		} catch (Exception e) {
			data = null;
			System.out.println(e);
		}
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	/*
	 * http://---/record/breportselectlist.do
	 * 查询报表(后台)
	 * request: {
	 * 		"username":""			玩家　空串为查全部
	 * 		"begindate":20170401	起始日期
	 * 		"enddate":20170401		终止日期
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"date":20170401,
	 * 				"agent":""
	 * 				"appcode":""
	 * 				"username":""
	 * 				"recharge_count":1
	 * 				"recharge_amount":0.00
	 * 				"withdrawals_count":1
	 * 				"withdrawals_amount":0.00
	 * 				"game_count":1
	 * 				"bet_amount":0.00
	 * 				"paid_amount":0.00
	 * 				"updatetime":1490513317079
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"date":20170401,
	 * 				"agent":""
	 * 				"appcode":""
	 * 				"username":""
	 * 				"recharge_count":1
	 * 				"recharge_amount":0.00
	 * 				"withdrawals_count":1
	 * 				"withdrawals_amount":0.00
	 * 				"game_count":1
	 * 				"bet_amount":0.00
	 * 				"paid_amount":0.00
	 * 				"updatetime":1490513317079
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"date":20170401,
	 * 				"agent":""
	 * 				"appcode":""
	 * 				"username":""
	 * 				"recharge_count":1
	 * 				"recharge_amount":0.00
	 * 				"withdrawals_count":1
	 * 				"withdrawals_amount":0.00
	 * 				"game_count":1
	 * 				"bet_amount":0.00
	 * 				"paid_amount":0.00
	 * 				"updatetime":1490513317079
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="breportselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> breportSelectList(AdminReportSelectListCSNode cs, HttpServletRequest request) {
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
		
		ReportDao dao = DaoMgr.getReport();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<ReportNode> pageList = dao.selectList(parameter);
			PageInfo<ReportNode> pageInfo = new PageInfo<ReportNode>(pageList);
			
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
	
	/*
	 * http://---/record/getbetpaidbydate.do
	 * 查询当日投注情况
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":{
	 * 			"bet":0.00,
	 * 			"paid":0.00
	 * 		}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="getbetpaidbydate.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getBetPaidByDate(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		if (player==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int date = DateUtil.getDay(DateUtil.getCurTimestamp());
		if (date<=0)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		Timestamp _begintime = DateUtil.getStartTimestamp(date);
		Timestamp _endtime = DateUtil.getEndTimestamp(date);
		ReportBetPaidByDayNode data = DaoMgr.getGameRecord().countByTime(_begintime.getTime(),_endtime.getTime());
		if (data==null) {
			data = new ReportBetPaidByDayNode();
			data.setBet(CoinUtil.zero);
			data.setPaid(CoinUtil.zero);
		}
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	@CrossOrigin
	@RequestMapping(value="bgetbetpaidbydate.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bGetBetPaidByDate(int date, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		if (date<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		int curDate = DateUtil.getDay(DateUtil.getCurTimestamp());
		if (curDate<=0)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		if (date>curDate)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		ReportBetPaidByDayNode data = null;
		if (date<curDate) { //已生成报表的查询
			data = DaoMgr.getReport().countByDay(date);
		} else {	//当天末生成报表的查询
			Timestamp _begintime = DateUtil.getStartTimestamp(date);
			Timestamp _endtime = DateUtil.getEndTimestamp(date);
			data = DaoMgr.getGameRecord().countByTime(_begintime.getTime(),_endtime.getTime());
		}
		
		if (data==null) {
			data = new ReportBetPaidByDayNode();
			data.setBet(CoinUtil.zero);
			data.setPaid(CoinUtil.zero);
		}
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
}
