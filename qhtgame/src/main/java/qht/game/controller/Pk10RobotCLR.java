package qht.game.controller;


import java.math.BigDecimal;
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
import com.pk10.logic.Pk10RobotMgr;
import com.pk10.node.Pk10RebotScheme;
import com.sysconst.Code;
import com.sysconst.Function;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.CoinUtil;
import com.util.ComUtil;
import com.util.DateUtil;

import net.node.Pk10RobotAddInfoCSNode;
import net.node.Pk10RobotSelectPlayerCSNode;
import net.node.Pk10RobotSelectRecordCSNode;
import net.node.Pk10RobotSetInfoCSNode;
import net.node.Pk10RobotSetInfoaddtextCSNode;
import net.node.Pk10RobotSetInfodeltextCSNode;
import net.node.Pk10RobotSetScheme1CSNode;
import net.node.Pk10RobotSetSchemeaddtextCSNode;
import qht.game.dao.Pk10PlayerDao;
import qht.game.dao.RebotRecordDao;
import qht.game.node.AdminNode;
import qht.game.node.Pk10PlayerNode;
import qht.game.node.Pk10PlayerNodeDB;
import qht.game.node.RebotRecordNode;
import qht.game.node.RebotSchemeNode;

@Controller
@RequestMapping(value="/pk10robot/")
public class Pk10RobotCLR {

	/*
	 * 查询机器人信息
	 * http://---/pk10robot/selectinfo.do
	 * request:{
	 * 		"nickname":""	昵称
	 * 		"status":1		0_全部 1_有效 2_无效
	 * 		"page":1,
	 * 		"count":8
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 				{
	 * 					"id":1,	
	 *					"appcode":"",	
	 * 					"username":"",
	 * 					"nickname":"",
	 * 					"status":1,
	 * 					"balance":0.00,
	 * 					"schemes":[
	 * 							{
	 *								"id":1,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":2,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":3,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							}
	 * 						],
	 * 					"createtime":1490513317079
	 * 					"updatetime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":2,	
	 *					"appcode":"",	
	 * 					"username":"",
	 * 					"nickname":"",
	 * 					"status":1,
	 * 					"balance":0.00,
	 * 					"schemes":[
	 * 							{
	 *								"id":1,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":2,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":3,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							}
	 * 						],
	 * 					"createtime":1490513317079
	 * 					"updatetime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":3,	
	 *					"appcode":"",	
	 * 					"username":"",
	 * 					"nickname":"",
	 * 					"status":1,
	 * 					"balance":0.00,
	 * 					"schemes":[
	 * 							{
	 *								"id":1,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":2,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":3,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							}
	 * 						],
	 * 					"createtime":1490513317079
	 * 					"updatetime":1490513317079
	 * 				}
	 * 			]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectinfo(Pk10RobotSelectPlayerCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		String nickname = cs.getNickname();
		int status = cs.getStatus();
		if (nickname==null || (status!=0 && status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE))
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		int page = cs.getPage();
		int count = cs.getCount();
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Pk10PlayerDao dao = DaoMgr.getPk10Player();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10PlayerNodeDB> pageList = dao.selectList2(nickname,status);
			PageInfo<Pk10PlayerNodeDB> pageInfo = new PageInfo<Pk10PlayerNodeDB>(pageList);
			
			List<Pk10PlayerNode> arrData = new ArrayList<Pk10PlayerNode>();
			for (Pk10PlayerNodeDB node : pageList) {
				List<RebotSchemeNode> schemes = new Gson().fromJson(new String(node.getSchemes()), new TypeToken<List<RebotSchemeNode>>(){}.getType());
				Pk10PlayerNode info = new Pk10PlayerNode();
				info.setId(node.getId());
				info.setAppcode(node.getAppcode());
				info.setUsername(node.getUsername());
				info.setNickname(node.getNickname());
				info.setStatus(node.getStatus());
				info.setBalance(node.getBalance());
				info.setSchemes(schemes);
				info.setCreatetime(node.getCreatetime());
				info.setUpdatetime(node.getUpdatetime());
				arrData.add(info);
			}
			
			data = new KinPageInfo();
			data.setTotal(pageInfo.getTotal());
			data.setPages(pageInfo.getPages());
			data.setPageNum(pageInfo.getPageNum());
			data.setPageSize(pageInfo.getPageSize());
			data.setSize(pageInfo.getSize());
			data.setObj(arrData);
		} catch (Exception e) {
			data = null;
			System.out.println(e);
		}
		
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	
	/*
	 * 查询机器人某条信息的方案集
	 * http://---/pk10robot/selectinfoonescheme.do
	 * request:{
	 * 		"username":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 							{
	 *								"id":1,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":2,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							},
	 * 							{
	 *								"id":3,
	 *								"time":23			//离开盘时间（秒）
	 *								"scheme":""			//方案名
	 * 								"probabi":23		//概率/百分
	 * 							}
	 * 						]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectinfoonescheme.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectinfoonescheme(String username, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		if (username==null || username.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10PlayerNode playerNode = Pk10RobotMgr.getPlayer(username);
		if (playerNode==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		List<RebotSchemeNode> arrList = playerNode.getSchemes();
		if (arrList==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,arrList);
	}
	
	/*
	 * 添加机器人信息
	 * http://---/pk10robot/addinfo.do
	 * request:{
	 * 		"appcode":"",
	 * 		"nickname":"",
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="addinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addinfo(Pk10RobotAddInfoCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		String appcode = cs.getAppcode();
		String nickname = cs.getNickname();
		if (	appcode==null || appcode.length()==0 ||
				nickname==null || nickname.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		String username = ComUtil.getOrderNo();
		UserInfo userinfo = PlayerMgr.getByName(username);
		Pk10PlayerNode player2 = Pk10RobotMgr.getPlayer(username);
		if (userinfo!=null || player2!=null)
			return Function.objToJson(Code.USERNAMEHASEXIST_CODE,Code.USERNAMEHASEXIST_DESC,null);
		
		//产生sig
		String usersig = TlsSig.generatorUsersig(username);
		if (usersig==null)
			Function.objToJson(Code.IMGENSIGFAIL_CODE,Code.IMGENSIGFAIL_DESC,null);
		//导入账号
		if (!ImOpr.accountImport(username))
			Function.objToJson(Code.IMIMPORTACCOUNT_CODE,Code.IMIMPORTACCOUNT_DESC,null);
		//加群app客服为好友
		if (!ImOpr.addFriend(username, TlsSig.APP_GROUP_CUSTOM))
			Function.objToJson(Code.IMADDFRIEND_CODE,Code.IMADDFRIEND_DESC,null);
		//加群pk10客服为好友
		if (!ImOpr.addFriend(username, TlsSig.PK10_GROUP_CUSTOM))
			Function.objToJson(Code.IMADDFRIEND_CODE,Code.IMADDFRIEND_DESC,null);
		//拉进app群组
		if (!ImOpr.addGroupMember(TlsSig.APP_GROUP_ID, username))
			Function.objToJson(Code.IMADDGROUP_CODE,Code.IMADDGROUP_DESC,null);
		//拉进pk10群组
		if (!ImOpr.addGroupMember(TlsSig.PK10_GROUP_ID, username))
			Function.objToJson(Code.IMADDGROUP_CODE,Code.IMADDGROUP_DESC,null);
		
		
		long tmpTime = DateUtil.getCurTimestamp().getTime();
		Pk10PlayerNode player = new Pk10PlayerNode();
		player.setAppcode(appcode);
		player.setUsername(username);
		player.setNickname(nickname);
		player.setStatus(Code.STATUS_UNEFFECTIVE);
		player.setBalance(CoinUtil.zero);
		player.setSchemes(new ArrayList<RebotSchemeNode>());
		player.setCreatetime(tmpTime);
		player.setUpdatetime(tmpTime);
		
		boolean result = false;
		try {
			if(Pk10RobotMgr.addPlayer(player))
				result = true;
		} catch (Exception e) {
			result = false;
			System.out.println(e);
		}
		
		if (!result) {
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 设置机器人信息——昵称与状态修改
	 * http://---/pk10robot/setinfo.do
	 * request:{
	 * 		"username":"",
	 * 		"nickname":"",
	 * 		"status":1,
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="setinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setinfo(Pk10RobotSetInfoCSNode cs, HttpServletRequest request) {
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
		String nickname = cs.getNickname();
		int status = cs.getStatus();
		if (	username==null || username.length()==0 ||
				nickname==null || nickname.length()==0 ||
				(status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE) )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		if (!Pk10RobotMgr.updatePlayer(username,nickname,status))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	/*
	 * 设置机器人信息——添加投注记录
	 * http://---/pk10robot/setinfoaddtext.do
	 * request:{
	 * 		"username":"",
	 * 		"id":1,
	 * 		"time":12,
	 * 		"scheme":"",	方案名
	 * 		"probabi":12	百分比
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="setinfoaddtext.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setinfoaddtext(Pk10RobotSetInfoaddtextCSNode cs, HttpServletRequest request) {
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
		int id = cs.getId();
		int time = cs.getTime();
		String scheme = cs.getScheme();
		int probabi = cs.getProbabi();
		if (username==null || username.length()==0 || 
				id<=0 || 
				scheme==null || scheme.length()==0 ||
				probabi<0 || probabi>100)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		RebotSchemeNode schemeNode = new RebotSchemeNode();
		schemeNode.setId(id);
		schemeNode.setTime(time);
		schemeNode.setScheme(scheme);
		schemeNode.setProbabi(probabi);
		if (!Pk10RobotMgr.addScheme(username,schemeNode))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 设置机器人信息——删除投注记录
	 * http://---/pk10robot/setinfodeltext.do
	 * request:{
	 * 		"username":"",
	 * 		"id":1,
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="setinfodeltext.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setinfodeltext(Pk10RobotSetInfodeltextCSNode cs, HttpServletRequest request) {
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
		int id = cs.getId();
		if (username==null || username.length()==0 || id<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		if (!Pk10RobotMgr.delScheme(username,id))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	
	
	
	
	
	
	
	/*
	 * 查询机器人方案记录
	 * http://---/pk10robot/selectscheme.do
	 * request:{
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 				{
	 * 					"id":1,	
	 *					"name":"",	
	 * 					"low_amount":0.00,		//剩余金额低于此值
	 * 					"sendup_amount":0.00,	//发送上分金额
	 * 					"up_amount":0.00,		//剩余金额高于此值
	 * 					"senddown_amount":0.00,	//发送下分金额
	 * 					"stop_amount":0.00,		//金额小于此值，停止投注
	 * 					"send_text":["大5","5 龙虎 5","9 大 5"],	//投注内容
	 * 					"updatetime":1490513317079,
	 * 					"createtime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":2,	
	 *					"name":"",	
	 * 					"low_amount":0.00,
	 * 					"sendup_amount":0.00,
	 * 					"up_amount":0.00,
	 * 					"senddown_amount":0.00,
	 * 					"stop_amount":0.00,
	 * 					"send_text":["大5","5 龙虎 5","9 大 5"],
	 * 					"updatetime":1490513317079,
	 * 					"createtime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":3,	
	 *					"name":"",	
	 * 					"low_amount":0.00,
	 * 					"sendup_amount":0.00,
	 * 					"up_amount":0.00,
	 * 					"senddown_amount":0.00,
	 * 					"stop_amount":0.00,
	 * 					"send_text":["大5","5 龙虎 5","9 大 5"],
	 * 					"updatetime":1490513317079,
	 * 					"createtime":1490513317079
	 * 				}
	 * 			]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectscheme.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectscheme(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		List<Pk10RebotScheme> pageList = Pk10RobotMgr.getSchemeAll();
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,pageList);
	}
	
	/*
	 * 查询机器人某条方案的投注集
	 * http://---/pk10robot/selectschemeonescheme.do
	 * request:{
	 * 		"name":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":["大5","5 龙虎 5","9 大 5"]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectschemeonescheme.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectschemeonescheme(String name, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		if (name==null || name.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10RebotScheme schemeNode = Pk10RobotMgr.getScheme(name);
		if (schemeNode==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		List<String> arrList = schemeNode.getSend_text();
		if (arrList==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,arrList);
	}
	
	/*
	 * 查询机器人方案名集
	 * http://---/pk10robot/selectschemenames.do
	 * request:{
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":["方案1","方案2","方案3"]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectschemenames.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectschemenames(String name, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		if (name==null || name.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		List<String> schemeNames = Pk10RobotMgr.getSchemeNames();
		if (schemeNames==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,schemeNames);
	}

	/*
	 * 添加机器人方案
	 * http://---/pk10robot/addscheme.do
	 * request:{
	 * 		"name":"",
	 * 		"low_amount":"0.00",		剩余金额低于此值
	 * 		"sendup_amount":"0.00",		发送上分金额
	 * 		"up_amount":"0.00",			剩余金额高于此值
	 * 		"senddown_amount":"0.00",	发送下分金额
	 * 		"stop_amount":"0.00"		金额小于此值，停止投注
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="addscheme.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addscheme(Pk10RobotSetScheme1CSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		String name = cs.getName();
		String str_low_amount = cs.getLow_amount();
		String str_sendup_amount = cs.getSendup_amount();
		String str_up_amount = cs.getUp_amount();
		String str_senddown_amount = cs.getSenddown_amount();
		String str_stop_amount = cs.getStop_amount();
		if (name==null || name.length()==0 ||
				str_low_amount==null || str_low_amount.length()==0 ||
				str_sendup_amount==null || str_sendup_amount.length()==0 ||
				str_up_amount==null || str_up_amount.length()==0 ||
				str_senddown_amount==null || str_senddown_amount.length()==0 ||
				str_stop_amount==null || str_stop_amount.length()==0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		BigDecimal low_amount = CoinUtil.createNew(str_low_amount);
		BigDecimal sendup_amount = CoinUtil.createNew(str_sendup_amount);
		BigDecimal up_amount = CoinUtil.createNew(str_up_amount);
		BigDecimal senddown_amount = CoinUtil.createNew(str_senddown_amount);
		BigDecimal stop_amount = CoinUtil.createNew(str_stop_amount);
		if (	low_amount==null || CoinUtil.compareZero(low_amount)<=0 ||
				sendup_amount==null || CoinUtil.compareZero(sendup_amount)<=0 ||
				up_amount==null || CoinUtil.compareZero(up_amount)<=0 ||
				senddown_amount==null || CoinUtil.compareZero(senddown_amount)<=0 ||
				stop_amount==null || CoinUtil.compareZero(stop_amount)<=0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10RebotScheme info = new Pk10RebotScheme();
		info.setName(name);
		info.setLow_amount(low_amount);
		info.setSendup_amount(sendup_amount);
		info.setUp_amount(up_amount);
		info.setSenddown_amount(senddown_amount);
		info.setStop_amount(stop_amount);
		info.setSend_text(new ArrayList<String>());
		info.setUpdatetime(DateUtil.getCurTimestamp().getTime());
		info.setCreatetime(DateUtil.getCurTimestamp().getTime());
		if (!Pk10RobotMgr.addScheme(info))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 设置机器人方案——时间设置
	 * http://---/pk10robot/setscheme.do
	 * request:{
	 * 		"name":"",
	 * 		"low_amount":"0.00",
	 * 		"sendup_amount":"0.00",
	 * 		"up_amount":"0.00",
	 * 		"senddown_amount":"0.00",
	 * 		"stop_amount":"0.00"
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="setscheme.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setscheme(Pk10RobotSetScheme1CSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		String name = cs.getName();
		String str_low_amount = cs.getLow_amount();
		String str_sendup_amount = cs.getSendup_amount();
		String str_up_amount = cs.getUp_amount();
		String str_senddown_amount = cs.getSenddown_amount();
		String str_stop_amount = cs.getStop_amount();
		if (name==null || name.length()==0 ||
				str_low_amount==null || str_low_amount.length()==0 ||
				str_sendup_amount==null || str_sendup_amount.length()==0 ||
				str_up_amount==null || str_up_amount.length()==0 ||
				str_senddown_amount==null || str_senddown_amount.length()==0 ||
				str_stop_amount==null || str_stop_amount.length()==0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		BigDecimal low_amount = CoinUtil.createNew(str_low_amount);
		BigDecimal sendup_amount = CoinUtil.createNew(str_sendup_amount);
		BigDecimal up_amount = CoinUtil.createNew(str_up_amount);
		BigDecimal senddown_amount = CoinUtil.createNew(str_senddown_amount);
		BigDecimal stop_amount = CoinUtil.createNew(str_stop_amount);
		if (	low_amount==null || CoinUtil.compareZero(low_amount)<=0 ||
				sendup_amount==null || CoinUtil.compareZero(sendup_amount)<=0 ||
				up_amount==null || CoinUtil.compareZero(up_amount)<=0 ||
				senddown_amount==null || CoinUtil.compareZero(senddown_amount)<=0 ||
				stop_amount==null || CoinUtil.compareZero(stop_amount)<=0 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		if (!Pk10RobotMgr.updateScheme(name,low_amount,sendup_amount,up_amount,senddown_amount,stop_amount))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 设置机器人方案——添加投注记录
	 * http://---/pk10robot/setschemeaddtext.do
	 * request:{
	 * 		"name":"",
	 * 		"text":"",
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="setschemeaddtext.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setschemeaddtext(Pk10RobotSetSchemeaddtextCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		String name = cs.getName();
		String text = cs.getText();
		if (name==null || name.length()==0 || text==null || text.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		if (!Pk10RobotMgr.addText(name,text))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 设置机器人方案——删除投注记录
	 * http://---/pk10robot/setschemedeltext.do
	 * request:{
	 * 		"name":"",
	 * 		"text":"",
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="setschemedeltext.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setschemedeltext(Pk10RobotSetSchemeaddtextCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);

		String name = cs.getName();
		String text = cs.getText();
		if (name==null || name.length()==0 || text==null || text.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		if (!Pk10RobotMgr.delText(name,text))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * 查询机器人投注记录
	 * http://---/pk10robot/selectrecord.do
	 * request:{
	 * 		"period":""		
	 * 		"nickname":""	
	 * 		"begintime":2017-04-06 17:47:21	(YYYY-MM-DD HH:mm:ss)
	 * 		"endtime":2017-04-06 17:47:21	(YYYY-MM-DD HH:mm:ss)
	 * 		"page":1
	 * 		"count":8
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 				{
	 * 					"id":1,	
	 *					"username":"",	
	 * 					"nickname":"",
	 * 					"period":"",
	 * 					"text":"",
	 * 					"status":"",
	 * 					"updatetime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":1,	
	 *					"username":"",	
	 * 					"nickname":"",
	 * 					"period":"",
	 * 					"text":"",
	 * 					"status":"",
	 * 					"updatetime":1490513317079
	 * 				},
	 * 				{
	 * 					"id":1,	
	 *					"username":"",
	 * 					"nickname":"",
	 * 					"period":"",
	 * 					"text":"",
	 * 					"status":"",
	 * 					"updatetime":1490513317079
	 * 				}
	 * 			]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectrecord.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectrecord(Pk10RobotSelectRecordCSNode cs, HttpServletRequest request) {
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
		String nickname = cs.getNickname();
		String begintime = cs.getBegintime();
		String endtime = cs.getEndtime();
		int page = cs.getPage();
		int count = cs.getCount();
		
		if (	period==null || period.length()>31 ||
				nickname==null || nickname.length()>31 ||
				begintime==null ||
				endtime==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		Timestamp _begin = DateUtil.StringToTimestamp(begintime);
		Timestamp _end = DateUtil.StringToTimestamp(endtime);
		if (_begin==null || _end==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		long lbegintime = _begin.getTime();
		long lendtime = _end.getTime();
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		RebotRecordDao dao = DaoMgr.getPk10RebotRecord();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<RebotRecordNode> pageList = dao.selectList(period,nickname,lbegintime,lendtime);
			PageInfo<RebotRecordNode> pageInfo = new PageInfo<RebotRecordNode>(pageList);
			
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
}
