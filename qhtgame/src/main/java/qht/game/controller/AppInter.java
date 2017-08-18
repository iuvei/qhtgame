package qht.game.controller;

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
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.KinPageInfo;
import com.node.UserInfo;
import com.pk10.logic.Pk10PrizeMgr;
import com.pk10.node.Pk10OpenInfo;
import com.sysconst.Code;
import com.sysconst.Function;
import com.util.CoinUtil;
import com.util.DateUtil;

import net.node.PageCSNode;
import net.node.SelectPk10BetInfoPastCSNode;
import net.node.SelectPk10BetinfoPeriodCSNode;
import net.node.StatitisPk10BetinfodateCSNode;
import qht.game.dao.Pk10InfoDao;
import qht.game.node.Pk10InfoNode;
import qht.game.node.Pk10InfoStatitisNode;

@Controller
@RequestMapping(value="/appinter/")
public class AppInter {
	/*
	 * http://---/appinter/selectpk10betinfo.do
	 * 玩家查询当期投注记录
	 * request: {
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			}
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/selectpk10betinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectpk10betinfo(PageCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		int page = cs.getPage();
		int count = cs.getCount();
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Pk10OpenInfo openInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (openInfo==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		String period = openInfo.getNextPeriod();
		
		Pk10InfoDao dao = DaoMgr.getPk10InfoDao();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10InfoNode> pageList = dao.selectCurPeriod(username,period);
			PageInfo<Pk10InfoNode> pageInfo = new PageInfo<Pk10InfoNode>(pageList);
			
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
	 * http://---/appinter/selectpk10betinfopast.do
	 * 玩家查询往期投注记录（按日期）
	 * request: {
	 * 		"selectdate":20170422
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			}
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/selectpk10betinfopast.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectpk10betinfopast(SelectPk10BetInfoPastCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		int selectdate = cs.getSelectdate();
		if (selectdate<=0 || selectdate>=99999999)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		int page = cs.getPage();
		int count = cs.getCount();
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		long begintime = DateUtil.getStartTimestamp(selectdate).getTime();
		long endtime = DateUtil.getEndTimestamp(selectdate).getTime();
		
		Pk10InfoDao dao = DaoMgr.getPk10InfoDao();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10InfoNode> pageList = dao.selectByDate(username,Code.PK10_BET,begintime,endtime);
			PageInfo<Pk10InfoNode> pageInfo = new PageInfo<Pk10InfoNode>(pageList);
			
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
	 * http://---/appinter/selectpk10betinfoperiod.do
	 * 玩家查询往期投注记录（按期号）
	 * request: {
	 * 		"period":""
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			}
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			},
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",					单号
	 * 				"period":"",				期号
	 * 				"appcode":"",				
	 * 				"username":"",				玩家
	 * 				"runway":"",				车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":1,				投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00			投注金额
	 * 				"paidamount":0.00,			赔付金额
	 * 				"bettime":1490853310369
	 * 				"paidtime":14908533103609
	 * 				"status":1					1:投注 2：结算 3：取消
	 * 				"updatetime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/selectpk10betinfoperiod.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectpk10betinfoperiod(SelectPk10BetinfoPeriodCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		String period = cs.getPeriod();
		if (period==null || period.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		int page = cs.getPage();
		int count = cs.getCount();
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Pk10InfoDao dao = DaoMgr.getPk10InfoDao();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10InfoNode> pageList = dao.selectByPeriod(username,Code.PK10_BET,period);
			PageInfo<Pk10InfoNode> pageInfo = new PageInfo<Pk10InfoNode>(pageList);
			
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
	 * http://---/appinter/statitispk10betinfodate.do
	 * 玩家查询往期投注流水（按日期）
	 * request: {
	 * 		"selectdate":20170422
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":{
	 * 			"bet":3.34,
	 * 			"paid":3.34
	 * 		}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/statitispk10betinfodate.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> statitispk10betinfodate(StatitisPk10BetinfodateCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		int selectdate = cs.getSelectdate();
		if (selectdate<=0 || selectdate>=99999999)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		long begintime = DateUtil.getStartTimestamp(selectdate).getTime();
		long endtime = DateUtil.getEndTimestamp(selectdate).getTime();
		
		Pk10InfoDao dao = DaoMgr.getPk10InfoDao();
		Pk10InfoStatitisNode data = dao.statitisByDate(username,Code.PK10_SETTLE,begintime,endtime);
		if (data==null) {
			data = new Pk10InfoStatitisNode();
			data.setBet(CoinUtil.zero);
			data.setPaid(CoinUtil.zero);
		}
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
}
