package qht.game.controller;

import java.math.BigDecimal;
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
import com.sysconst.Code;
import com.sysconst.Function;
import com.util.CoinUtil;
import com.util.DateUtil;

import net.node.RWReturnwaterCSNode;
import net.node.RWSelectlistCSNode;
import qht.game.dao.PlayerDao;
import qht.game.dao.ReturnwaterDao;
import qht.game.node.AdminNode;
import qht.game.node.PlayerNode;
import qht.game.node.ReturnwaterNode;

@Controller
@RequestMapping(value="/rw/")
public class ReturnwaterCLR {
	
	/*
	 * http://---/rw/selectlist.do
	 * 查询报表记录(返水用)
	 * request: {
	 * 		"date":20170515	日期，int，YYYYMMDD
	 * 		"page":1
	 * 		"count":10
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 *				"id":1,
	 *				"date":20170515,
	 *				"appcode":"",
	 *				"username":"",
	 *				"typeid":1,
	 *				"water_amount":0.00,
	 *				"profit_amount":0.00,
	 *				"up_amount":0.00,
	 *				"down_amount":0.00,
	 *				"status":1,		0_未返水 1_当日盈亏 2_当日流水 3_当日上分 4_当日下分
	 *				"return_amount":0.00,
	 *				"updatetime":1490853310369
	 *				"createtime":1490853310369
	 * 			},
	 * 			{
	 *				"id":2,
	 *				"date":20170515,
	 *				"appcode":"",
	 *				"username":"",
	 *				"typeid":1,
	 *				"water_amount":0.00,
	 *				"profit_amount":0.00,
	 *				"up_amount":0.00,
	 *				"down_amount":0.00,
	 *				"status":1,		0_未返水 1_当日盈亏 2_当日流水 3_当日上分 4_当日下分
	 *				"return_amount":0.00,
	 *				"updatetime":1490853310369
	 *				"createtime":1490853310369
	 * 			},
	 * 			{
	 *				"id":3,
	 *				"date":20170515,
	 *				"appcode":"",
	 *				"username":"",
	 *				"typeid":1,
	 *				"water_amount":0.00,
	 *				"profit_amount":0.00,
	 *				"up_amount":0.00,
	 *				"down_amount":0.00,
	 *				"status":1,		0_未返水 1_当日盈亏 2_当日流水 3_当日上分 4_当日下分
	 *				"return_amount":0.00,
	 *				"updatetime":1490853310369
	 *				"createtime":1490853310369
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/selectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectlist(RWSelectlistCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		//if (admin.getStatus()!=Code.STATUS_EFFECTIVE)
		//	return Function.objToJson(Code.NOACCOUNT_CODE,Code.NOACCOUNT_DESC,null);
		
		int date = cs.getDate();
		int page = cs.getPage();
		int count = cs.getCount();
		if (date<20170101)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		ReturnwaterDao dao = DaoMgr.getReturnwater();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<ReturnwaterNode> pageList = dao.selectlist(date,Code.RETURN_NO);
			PageInfo<ReturnwaterNode> pageInfo = new PageInfo<ReturnwaterNode>(pageList);
			
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
	 * http://---/rw/returnwater.do
	 * 进行返水操作
	 * request: {
	 * 		"date":20170515	日期，int，YYYYMMDD
	 * 		"status":1		0_未返水 1_当日盈亏 2_当日流水 3_当日上分 4_当日下分
	 * 		"point":0.00	点数
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/returnwater.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> returnwater(RWReturnwaterCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		if (admin.getStatus()!=Code.STATUS_EFFECTIVE)
			return Function.objToJson(Code.NOACCOUNT_CODE,Code.NOACCOUNT_DESC,null);
		
		int date = cs.getDate();
		int status = cs.getStatus();
		String strPoint = cs.getPoint();
		if (	date<20170101	||
				(status!=Code.RETURN_WATER && status!=Code.RETURN_PROFIT && status!=Code.RETURN_UP && status!=Code.RETURN_DOWN) ||
				strPoint==null || strPoint.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		BigDecimal point = CoinUtil.createNew(strPoint);
		if (CoinUtil.compareZero(point)<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		point = CoinUtil.divide(point, CoinUtil.createNew("100"), 4);
		
		
		ReturnwaterDao dao = DaoMgr.getReturnwater();
		PlayerDao playerDao = DaoMgr.getPlayer();
		if (dao==null || playerDao==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		List<ReturnwaterNode> pageList = dao.selectlist2(date,Code.RETURN_NO);
		if (pageList==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		for (ReturnwaterNode node : pageList) {
			String username = node.getUsername();
			int typeid = node.getTypeid();
			PlayerNode player = PlayerMgr.getByNameWithDB(username);
			if (player==null)
				continue;
			if (player.getTypeid()!=typeid)
				continue;
			
			BigDecimal amount = CoinUtil.zero;
			if (status==Code.RETURN_PROFIT)
				amount = node.getProfit_amount();
			else if (status==Code.RETURN_WATER)
				amount = node.getWater_amount();
			else if (status==Code.RETURN_UP)
				amount = node.getUp_amount();
			else if (status==Code.RETURN_DOWN)
				amount = node.getDown_amount();
			else
				continue;
			BigDecimal returnamount = CoinUtil.mul(amount, point, 4);
			
			BigDecimal balance = player.getBalance();
			balance = CoinUtil.add(balance, returnamount);
			
			long tmpTime = DateUtil.getCurTimestamp().getTime();
			if (!playerDao.updateBalance(player.getId(), balance, tmpTime))
				continue;
			player.setBalance(balance);
			player.setUpdatetime(tmpTime);
			
			if (!dao.updateStatus(node.getId(), status, returnamount, tmpTime))
				continue;
			node.setStatus(status);
			node.setReturn_amount(returnamount);
			node.setUpdatetime(tmpTime);
		}
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
}
