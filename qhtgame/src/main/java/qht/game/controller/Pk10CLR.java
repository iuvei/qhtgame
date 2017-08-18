package qht.game.controller;

import java.io.UnsupportedEncodingException;
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
import com.logic.mgr.AdminMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.AdminInfo;
import com.node.ErrorCode;
import com.node.KinPageInfo;
import com.node.UserInfo;
import com.pk10.logic.Pk10OprMgr;
import com.pk10.logic.Pk10Player;
import com.pk10.logic.Pk10PointoutMgr;
import com.pk10.logic.Pk10PrizeMgr;
import com.pk10.logic.Pk10TextMgr;
import com.pk10.node.Pk10OpenInfo;
import com.pk10.node.Pk10OpenNode;
import com.sysconst.Code;
import com.sysconst.Function;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.DateUtil;

import net.node.AdminBetiniUpdateCSNode;
import net.node.BCancleBetNode;
import net.node.Pk10PointoutInsertOneCSNode;
import net.node.PointoutSelectListCSNode;
import net.node.pointoutEditCSNode;
import qht.game.dao.Pk10Pointout;
import qht.game.dao.Pk10SymbolDao;
import qht.game.node.AdminNode;
import qht.game.node.Pk10Pointout2Node;
import qht.game.node.Pk10PointoutNode;
import qht.game.node.Pk10SymbolNode;
import qht.game.node.PlayerNode;
import qht.game.node.PointoutDeleteCSNode;

@Controller
@RequestMapping(value="/pk10/")
public class Pk10CLR {
	
	/*
	 * pk10预投注
	 * http://---/pk10/prebet.do
	 * request:{
	 * 		"text":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/prebet.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pk10prebet(String text, HttpServletRequest request) {
		try {
			text= new String(text.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		}
		
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		if (text==null || text.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		ErrorCode result = Pk10OprMgr.predeal(username,TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, text);
		if (result==null) {
			return Function.objToJson(Code.PK10_BET_FAIL,Code.PK10_BET_DESC,null);
		} else if (result.getCode()==Code.OK_CODE) {
			//ImOpr.sendMsg(username, TlsSig.PK10_GROUP_ID, text, false);
			//ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, result.getDesc(), false);
			//ImOpr.sendGroupMsgSystem(GroupId, From_Account, respone);
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		} else {
			return Function.objToJson(result.getCode(),result.getDesc(),null);
		}
	}
	
	/*
	 * pk10投注
	 * http://---/pk10/bet.do
	 * request:{
	 * 		"text":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/bet.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pk10bet(String text, HttpServletRequest request) {
		try {
			text= new String(text.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		}
		
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		String username = userInfo.getUsername();
		
		if (text==null || text.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		ErrorCode result = Pk10OprMgr.deal(username,TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, text);
		if (result==null) {
			return Function.objToJson(Code.PK10_BET_FAIL,Code.PK10_BET_DESC,null);
		} else if (result.getCode()==Code.OK_CODE) {
			ImOpr.sendMsg(username, TlsSig.PK10_GROUP_ID, text, false);
			ImOpr.sendMsg(TlsSig.ADMIN_IDENTIFIER, TlsSig.PK10_GROUP_ID, result.getDesc(), false);
			//ImOpr.sendGroupMsgSystem(GroupId, From_Account, respone);
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		} else {
			return Function.objToJson(result.getCode(),result.getDesc(),null);
		}
	}
	
	/*
	 * 获取开奖信息(可定时更新)
	 * http://---/pk10/getopen.do
	 * request:{
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":{
	 * 			"timestamp":1490513317079,		当前时间
	 * 			"sealplatTime":1490513317079,	离封盘时间(毫秒)
	 * 			"nextPeriod":"",				下一期期号
	 * 			"nextOpentime":1490513317079,	下一期开奖时间
	 * 			"period":"",					上期期号
	 * 			"opentime":1490513317079,		上期开奖时间
	 * 			"opencode":[1,2,3,4,5,6,7,8,9,10]	上期开奖数据
	 * 			}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/getopen.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getOpen(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		Pk10OpenInfo info = Pk10PrizeMgr.getPk10OpenInfo();
		if (info==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		long timeSealplate = Pk10PrizeMgr.getSealplate();
		long sealplatTime = info.getNextOpentime()-timeSealplate-info.getTimestamp();
		if (sealplatTime<0)
			sealplatTime = 0;
		
		Pk10OpenNode result = new Pk10OpenNode();
		result.setTimestamp(info.getTimestamp());
		result.setSealplatTime(sealplatTime);
		result.setNextPeriod(info.getNextPeriod());
		result.setNextOpentime(info.getNextOpentime());
		result.setPeriod(info.getPeriod());
		result.setOpentime(info.getOpentime());
		result.setOpencode(info.getOpencode());
	
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,result);
	}
	
	
	/*
	 * 取消玩家压注记录
	 * http://---/pk10/canclebet.do
	 * request:{
	 * 		"odd":""		订单号 空串表示取消全部
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""	订单号
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/canclebet.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> cancleBet(String odd, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		
		if (odd==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		if (Pk10PrizeMgr.isSealPlate())
			return Function.objToJson(Code.HASSEALPLATE_CODE,Code.HASSEALPLATE_DESC,null);
		if (Pk10PrizeMgr.isNotBet())
			return Function.objToJson(Code.HASNOTBET_CODE,Code.HASNOTBET_DESC,null);
	
		Pk10Player pk10Player = Pk10PrizeMgr.getPk10Player();
		if (odd.length()==0) {
			if (!pk10Player.cancleBet(player))
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		} else {
			if (!pk10Player.cancleBet(odd, player))
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,odd);
	}
		
	/*
	 * 获取某一期玩家压注记录(后台)
	 * http://---/pk10/bgetbetinfo.do
	 * request:{
	 * 		"period":""		查询当期请传"000000"
	 * 		"username":""
	 * 		"begintime":2017-04-06 17:47:21	(YYYY-MM-DD HH:mm:ss) 如果是查当前期，该字段值不用
	 * 		"endtime":2017-04-06 17:47:21	(YYYY-MM-DD HH:mm:ss) 如果是查当前期，该字段值不用
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 				{
	 * 					"id":1,	
	 *					"odd":"",	
	 * 					"agent":"",
	 * 					"appcode":"",
	 * 					"username":"",
	 * 					"period":"",
	 * 					"type":3,		3:压注  4:赔付 5:取消压注
	 * 					"amount":0.00,
	 * 					"detail":{
	 * 						"index":1,
	 * 						"num":[{"id":1,"money":""},{"id":2,"money":""},{"id":3,"money":""}],
	 * 						"big":"",
	 * 						"small":"",
	 * 						"single":"",
	 * 						"dou":"",
	 * 						"dragon":"",
	 * 						"tiger":""
	 * 						},
	 * 					"updatetime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":1,
	 *					"odd":"",
	 * 					"agent":"",
	 * 					"appcode":"",
	 * 					"username":"",
	 * 					"period":"",
	 * 					"type":3,		3:压注  4:赔付 5:取消压注
	 * 					"amount":0.00,
	 * 					"detail":{
	 * 						"index":1,
	 * 						"num":[{"id":1,"money":""},{"id":2,"money":""},{"id":3,"money":""}],
	 * 						"big":"",
	 * 						"small":"",
	 * 						"single":"",
	 * 						"dou":"",
	 * 						"dragon":"",
	 * 						"tiger":""
	 * 						},
	 * 					"updatetime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":1,
	 *					"odd":"",
	 * 					"agent":"",
	 * 					"appcode":"",
	 * 					"username":"",
	 * 					"period":"",
	 * 					"type":3,		3:压注  4:赔付 5:取消压注
	 * 					"amount":0.00,
	 * 					"detail":{
	 * 						"index":1,
	 * 						"num":[{"id":1,"money":""},{"id":2,"money":""},{"id":3,"money":""}],
	 * 						"big":"",
	 * 						"small":"",
	 * 						"single":"",
	 * 						"dou":"",
	 * 						"dragon":"",
	 * 						"tiger":""
	 * 						},
	 * 					"updatetime":1490513317079
	 * 				}
	 * 			]
	 * }
	 */
	/*@CrossOrigin
	@RequestMapping(value="/bgetbetinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bgetBetinfo(AdminBetInfoNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
	
		String period = cs.getPeriod();
		String username = cs.getUsername();
		String begintime = cs.getBegintime();
		String endtime = cs.getEndtime();
		int page = cs.getPage();
		int count = cs.getCount();
		if (	period==null || username==null || begintime==null || endtime==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		long lbegintime = 0;
		long lendtime = 0;
		if (begintime.length()>0 && endtime.length()>0) {
			Timestamp tbegintime = DateUtil.StringToTimestamp(begintime);
			Timestamp tendtime = DateUtil.StringToTimestamp(endtime);
			if (tbegintime==null || tendtime==null)
				return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
			lbegintime = tbegintime.getTime();
			lendtime = tendtime.getTime();
		}
		
		if (period.equals("000000")) {
			Pk10OpenInfo openinfo = Pk10GetOpencodeMgr.getPk10OpenInfo();
			if (openinfo==null)
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			period = openinfo.getNextPeriod();
			lbegintime = 0;
			lendtime = 0;
		}
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("period", period);
		parameter.put("username", username);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10BetDetailNode> pageList = DaoMgr.getPk10BetDetail().selectList(parameter);
			if (pageList==null)
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			
			List<Pk10BetDetail> list = new ArrayList<Pk10BetDetail>();
			for (Pk10BetDetailNode node : pageList) {
				Pk10BetDetail _node = new Pk10BetDetail();
				String detail = new String(node.getDetail());
				Pk10BetInfo obj = null;
				if (detail!=null && detail.length()>0)
					obj = new Gson().fromJson(detail, Pk10BetInfo.class);
				
				_node.setId(node.getId());
				_node.setOdd(node.getOdd());
				_node.setAgent(node.getAgent());
				_node.setAppcode(node.getAppcode());
				_node.setUsername(node.getUsername());
				_node.setPeriod(node.getPeriod());
				_node.setType(node.getType());
				_node.setAmount(node.getAmount());
				_node.setDetail(obj);
				_node.setUpdatetime(node.getUpdatetime());
				
				list.add(_node);
			}
			PageInfo<Pk10BetDetailNode> pageInfo = new PageInfo<Pk10BetDetailNode>(pageList);
			
			data = new KinPageInfo();
			data.setTotal(pageInfo.getTotal());
			data.setPages(pageInfo.getPages());
			data.setPageNum(pageInfo.getPageNum());
			data.setPageSize(pageInfo.getPageSize());
			data.setSize(pageInfo.getSize());
			data.setObj(list);
		} catch (Exception e) {
			data = null;
			System.out.println(e);
		}
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}*/

	/*
	 * 取消玩家压注记录(后台)
	 * http://---/pk10/bcanclebet.do
	 * request:{
	 * 		"period":"",	期号
	 * 		"username":"",	玩家名
	 * 		"odd":"",		订单号
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""	
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/bcanclebet.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bcancleBet(BCancleBetNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		String period = cs.getPeriod();
		String odd = cs.getOdd();
		String username = cs.getUsername();
		if (	period==null || period.length()==0 ||
				odd==null || odd.length()==0 ||
				username==null || username.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10OpenInfo pk10OpenInfo = Pk10PrizeMgr.getPk10OpenInfo();
		if (pk10OpenInfo==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		if (!pk10OpenInfo.getNextPeriod().equals(period))
			return Function.objToJson(Code.NOTCURPERIOD_CODE,Code.NOTCURPERIOD_DESC,null);
		
		UserInfo userInfo = PlayerMgr.getByName(username);
		PlayerNode player = null;
		if (userInfo==null) {
			player = DaoMgr.getPlayer().selectOneByName(username);
		} else {
			player = userInfo.getPlayer();
		}
		if (player==null)
			return Function.objToJson(Code.PLAYERNOTEXIT_CODE,Code.PLAYERNOTEXIT_DESC,null);
		
		if (Pk10PrizeMgr.isSealPlate())
			return Function.objToJson(Code.HASSEALPLATE_CODE,Code.HASSEALPLATE_DESC,null);
		if (Pk10PrizeMgr.isNotBet())
			return Function.objToJson(Code.HASNOTBET_CODE,Code.HASNOTBET_DESC,null);
	
		Pk10Player pk10Player = Pk10PrizeMgr.getPk10Player();
		if (!pk10Player.cancleBet(odd, player))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,odd);
	}
	
	@CrossOrigin
	@RequestMapping(value="/betiniselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> betiniSelectList(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		List<Pk10SymbolNode> data = Pk10TextMgr.get();
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	@CrossOrigin
	@RequestMapping(value="betiniupdate.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> betiniUpdate(AdminBetiniUpdateCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int id = cs.getId();
		int tag = cs.getTag();
		if (id<=0 || (tag!=Code.STATUS_EFFECTIVE && tag!=Code.STATUS_UNEFFECTIVE))
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		List<Pk10SymbolNode> data = Pk10TextMgr.get();
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		Pk10SymbolNode focuNode = null;
		for (Pk10SymbolNode node : data) {
			if (node.getId()==id) {
				focuNode = node;
				break;
			}
		}
		if (focuNode==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		Pk10SymbolDao dao = DaoMgr.getPk10Symbol();
		boolean result = false;
		try {
			long updatetime = DateUtil.getCurTimestamp().getTime();
			if (dao.updateTag(id,tag,updatetime)) {
				focuNode.setTag(tag);
				focuNode.setUpdatetime(updatetime);
				result = true;
			}
		} catch (Exception e) {
			result = false;
			System.out.println(e);
		}
		
		if (!result)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 查询pk10群提示内容
	 */
	@CrossOrigin
	@RequestMapping(value="pointoutselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pointoutSelectList(PointoutSelectListCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int lottery_id = cs.getLottery_id();
		int page = cs.getPage();
		int count = cs.getCount();
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		if (lottery_id!=0 && lottery_id!=Code.LORREY_PK10)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10Pointout dao = DaoMgr.getPk10Pointout();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10PointoutNode> pageList = dao.selectList(lottery_id);
			PageInfo<Pk10PointoutNode> pageInfo = new PageInfo<Pk10PointoutNode>(pageList);

			List<Pk10Pointout2Node> result = new ArrayList<Pk10Pointout2Node>();
			for (Pk10PointoutNode node : pageList) {
				Pk10Pointout2Node info = new Pk10Pointout2Node();
				info.setId(node.getId());
				info.setLottery_id(node.getLottery_id());
				info.setText_id(node.getText_id());
				info.setSpacetime(node.getSpacetime());
				info.setText(new String(node.getText()));
				info.setCreatetime(node.getCreatetime());
				info.setUpdatetime(node.getUpdatetime());
				
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
	
	@CrossOrigin
	@RequestMapping(value="pointoutinsertone.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pointoutInsertOne(Pk10PointoutInsertOneCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int lottery_id = cs.getLottery_id();
		int text_id = cs.getText_id();
		int spacetime = cs.getSpacetime();
		String text = cs.getText();
		
		if (lottery_id!=Code.LORREY_PK10 || spacetime<=0 || spacetime>=300 || text==null || text.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		Pk10PointoutNode pointout = Pk10PointoutMgr.get(lottery_id, text_id);
		if (pointout!=null)
			return Function.objToJson(Code.OBJHASEXIT_CODE,Code.OBJHASEXIT_DESC,null);
		
		Pk10Pointout dao = DaoMgr.getPk10Pointout();
		boolean result = false;
		try {
			if(dao.insert(lottery_id,text_id,spacetime,text)) {
				Pk10PointoutNode node = dao.selectOne(lottery_id,text_id);
				if (node!=null) {
					Pk10PointoutMgr.add(node);
					result = true;
				}
			}
		} catch (Exception e) {
			result = false;
			System.out.println(e);
		}
		
		if (!result) {
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	@CrossOrigin
	@RequestMapping(value="pointoutedit.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pointoutEdit(pointoutEditCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int lottery_id = cs.getLottery_id();
		int text_id = cs.getText_id();
		int spacetime = cs.getSpacetime();
		String text = cs.getText();
		
		if (lottery_id<=0 || text_id<=0 || spacetime<=0 || spacetime>=300 || text==null || text.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10PointoutNode pointout = Pk10PointoutMgr.get(lottery_id, text_id);
		if (pointout==null)
			return Function.objToJson(Code.OBJNOTEXIT_CODE,Code.OBJNOTEXIT_DESC,null);
		
		
		Pk10Pointout dao = DaoMgr.getPk10Pointout();
		boolean result = false;
		try {
			if (dao.edit(lottery_id,text_id,spacetime,text)) {
				pointout.setSpacetime(spacetime);
				pointout.setText(text.getBytes());
				result = true;
			}
		} catch (Exception e) {
			result = false;
			System.out.println(e);
		}
		
		if (!result) {
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	@CrossOrigin
	@RequestMapping(value="pointoutdelete.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pointoutDelete(PointoutDeleteCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int lottery_id = cs.getLottery_id();
		int text_id = cs.getText_id();
		if (lottery_id<=0 || text_id<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10PointoutNode pointout = Pk10PointoutMgr.get(lottery_id, text_id);
		if (pointout==null)
			return Function.objToJson(Code.OBJNOTEXIT_CODE,Code.OBJNOTEXIT_DESC,null);
		
		Pk10Pointout dao = DaoMgr.getPk10Pointout();
		boolean result = false;
		try {
			if (dao.delete(lottery_id,text_id)) {
				Pk10PointoutMgr.del(lottery_id, text_id);
				result = true;
			}
		} catch (Exception e) {
			result = false;
			System.out.println(e);
		}
		if (!result) {
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
}
