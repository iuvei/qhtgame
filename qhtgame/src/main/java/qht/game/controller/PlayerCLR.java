package qht.game.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
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
import com.logic.mgr.AppinfoMgr;
import com.logic.mgr.AuthCodeMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.AdminInfo;
import com.node.AuthCode;
import com.node.KinPageInfo;
import com.node.PlayerInfo;
import com.node.UserInfo;
import com.pk10.logic.Pk10RobotMgr;
import com.sysconst.Code;
import com.sysconst.Function;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;
import com.util.CoinUtil;
import com.util.ComUtil;
import com.util.DateUtil;

import net.node.ChangesidCSNode;
import net.node.PlayerAuthCodeForResetNode;
import net.node.PlayerAuthCodeNode;
import net.node.PlayerEditInfoCSNode;
import net.node.PlayerInsertCSNode;
import net.node.PlayerLoginCSNode;
import net.node.PlayerResetPasswordCSNode;
import net.node.PlayerResetPswNode;
import net.node.PlayerSelectListCSNode;
import net.node.PlayerSetStatusCSNode;
import qht.game.dao.PlayerDao;
import qht.game.node.AdminNode;
import qht.game.node.AppinfoNode;
import qht.game.node.Pk10PlayerNode;
import qht.game.node.PlayerAMOrderNode;
import qht.game.node.PlayerNode;

@Controller
@RequestMapping(value="/player/")
public class PlayerCLR {

	/*
	 * 注册
	 * http://---/player/register.do
	 * request: {
	 * 		"loginname":"",
	 * 		"nickname":"",
	 * 		"password":"",
	 * 		"status":1,
	 * 		"qq":"",
	 * 		"weixin":"",
	 * 		"telephone":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> register(PlayerInsertCSNode cs) {
		String loginname = cs.getLoginname();
		String username = ComUtil.getOrderNo();
		String nickname = cs.getNickname();
		String password = cs.getPassword();
		int status = cs.getStatus();
		String qq = cs.getQq();
		String weixin = cs.getWeixin();
		String telephone = cs.getTelephone();
		String authCodeStr = cs.getAuthCode();
		if (	loginname==null || loginname.length()>31 || 
				username==null || username.length()>31 ||
				nickname==null || nickname.length()>31 ||
				password==null || password.length()>31 ||
				(status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE) ||
				qq==null || qq.length()>15 ||
				weixin==null || weixin.length()>31 ||
				telephone==null || telephone.length()>15 ||
                authCodeStr == null || authCodeStr.length() > 6)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		try {
			nickname= new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		}
		
        //---------------------短信验证
        AuthCode authCode = AuthCodeMgr.get(telephone);
        if (authCode == null) {
            return Function.objToJson(Code.SMS_SEND_FAIL_CODE, Code.SMS_SEND_FAIL_DESC, null);
        }
        if (!authCode.getCode().equals(authCodeStr) || authCode.getType() != 0) {
            AuthCodeMgr.remove(telephone);
            return Function.objToJson(Code.SMS_AUTH_FAIL_CODE, Code.SMS_AUTH_FAIL_DESC, null);
        }
        if (System.currentTimeMillis() - authCode.getTime() > 120000) {
            AuthCodeMgr.remove(telephone);
            return Function.objToJson(Code.SMS_AUTH_OVERDUE_CODE, Code.SMS_AUTH_OVERDUE_DESC, null);
        }
        
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", "GS1001");
		parameter.put("loginname", loginname);
		parameter.put("username", username);
		parameter.put("nickname", nickname);
		parameter.put("password", password);
		parameter.put("status", status);
		parameter.put("typeid", Code.PLAYER_IDENTIFEY_REAL);
		parameter.put("balance", CoinUtil.zero);
		parameter.put("frozen_bal", CoinUtil.zero);
		parameter.put("integral", CoinUtil.zero);
		parameter.put("qq", qq);
		parameter.put("weixin", weixin);
		parameter.put("telephone", telephone);
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());

		UserInfo userinfo = PlayerMgr.getByName(username);
		Pk10PlayerNode player2 = Pk10RobotMgr.getPlayer(username);
		if (userinfo!=null || player2!=null)
			return Function.objToJson(Code.USERNAMEHASEXIST_CODE,Code.USERNAMEHASEXIST_DESC,null);
		
		PlayerDao dao = DaoMgr.getPlayer();
		PlayerNode __player = dao.selectByLoginname(loginname);
		Pk10PlayerNode __player2 = Pk10RobotMgr.getPlayer(loginname);
		if (__player!=null || __player2!=null)
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
		
		
		boolean result = false;
		try {
			if(dao.insert(parameter))
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
	

    /**
     * 发送验证码
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "resetPsw.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> resetPsw(PlayerResetPswNode playerResetPswNode) {
        String loginName = playerResetPswNode.getLoginName();
        String password = playerResetPswNode.getPassword();
        String authCodeStr = playerResetPswNode.getAuthCode();
        if (loginName == null || loginName.length() > 31 ||
                password == null || password.length() > 31 ||
                authCodeStr == null || authCodeStr.length() > 6)
            return Function.objToJson(Code.PARAM_CODE, Code.PARAM_DESC, null);
        PlayerDao dao = DaoMgr.getPlayer();
        PlayerNode player = dao.selectByLoginname(loginName);
        if (player == null) {
            return Function.objToJson(Code.NOACCOUNT_CODE, Code.NOACCOUNT_DESC, null);
        } else {
            //---------------------短信验证
            AuthCode authCode = AuthCodeMgr.get(player.getTelephone());
            if (authCode == null) {
                return Function.objToJson(Code.SMS_SEND_FAIL_CODE, Code.SMS_SEND_FAIL_DESC, null);
            }
            if (!authCode.getCode().equals(authCodeStr) || authCode.getType() != 1) {
                AuthCodeMgr.remove(player.getTelephone());
                return Function.objToJson(Code.SMS_AUTH_FAIL_CODE, Code.SMS_AUTH_FAIL_DESC, null);
            }
            if (System.currentTimeMillis() - authCode.getTime() > 120000) {
                AuthCodeMgr.remove(player.getTelephone());
                return Function.objToJson(Code.SMS_AUTH_OVERDUE_CODE, Code.SMS_AUTH_OVERDUE_DESC, null);
            }
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("loginname", loginName);
            parameter.put("password", password);
            parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
            boolean result = false;
            try {
                if (dao.resetPsw(parameter))
                    result = true;
            } catch (Exception e) {
                result = false;
                System.out.println(e);
            }

            if (!result) {
                return Function.objToJson(Code.CATCH_CODE, Code.CATCH_DESC, null);
            }
            AuthCodeMgr.remove(player.getTelephone());
            return Function.objToJson(Code.OK_CODE, Code.OK_DESC, null);
        }
    }


    /**
     * 发送验证码 注册
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "sendAuthCode.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendAuthCode(PlayerAuthCodeNode playerAuthCodeNode) {
        try {
            PlayerDao dao = DaoMgr.getPlayer();
            boolean flag = dao.isExistByTel(playerAuthCodeNode.getTel());
            if (flag) {
                return Function.objToJson(Code.TEL_EXIST_CODE, Code.TEL_EXIST_DESC, null);
            }
            Map<String,String> resultMap = AuthCodeMgr.getAuthCode(playerAuthCodeNode.getAppCode(), playerAuthCodeNode.getTel(), 0);
            if (resultMap != null) {
                return Function.objToJson(Code.SMS_FAIL_CODE, Code.SMS_FAIL_DESC, null);
            }
            return Function.objToJson(Code.OK_CODE, Code.OK_DESC, null);
        } catch (Exception e) {
//            e.printStackTrace();
            return Function.objToJson(Code.SMS_FAIL_CODE, Code.SMS_FAIL_DESC, null);
        }
    }

    /**
     * 发送验证码
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "sendAuthCodeForReset.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendAuthCodeForReset(PlayerAuthCodeForResetNode playerAuthCodeForResetNode) {
        try {
            PlayerDao dao = DaoMgr.getPlayer();
            PlayerNode player = dao.selectByLoginname(playerAuthCodeForResetNode.getLoginName());
            if (player == null) {
                return Function.objToJson(Code.NOACCOUNT_CODE, Code.NOACCOUNT_DESC, null);
            } else {
                Map<String,String> resultMap = AuthCodeMgr.getAuthCode(playerAuthCodeForResetNode.getAppCode(), player.getTelephone(), 1);
                if (resultMap != null) {
                    return Function.objToJson(Code.SMS_FAIL_CODE, Code.SMS_FAIL_DESC, null);
                }
                return Function.objToJson(Code.OK_CODE, Code.OK_DESC, null);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            return Function.objToJson(Code.SMS_FAIL_CODE, Code.SMS_FAIL_DESC, null);
        }

    }
    
	/*
	 * 登 入
	 * http://---/player/login.do
	 * request: {
	 * 		"appcode":"",
	 * 		"username":"",
	 * 		"password":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info": {
	 * 				"id":1	,
	 * 				"agent":"",
	 * 				"appcode":"",
	 * 				"username":"",
	 * 				"nickname":"",
	 * 				"status":1,
	 * 				"balance":0.00
	 * 				"frozen_bal":0.00	冻结资金
	 * 				"integral":0.00		积分
	 * 				"qq":"",
	 * 				"weixin":"",
	 * 				"telephone":"",
	 * 				"createtime":1490915724784,
	 * 				"updatetime":1490915724784
	 * 			}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(PlayerLoginCSNode cs, HttpServletRequest request) {
		
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		AppinfoNode appinfoNode = AppinfoMgr.get(cs.getAppcode());
		if (appinfoNode==null)
			return Function.objToJson(Code.APPINFONOTEXIT_CODE,Code.APPINFONOTEXIT_DESC,null);
		long curTime = DateUtil.getCurTimestamp().getTime();
		if (appinfoNode.getActiontime()<=curTime)
			return Function.objToJson(Code.APPINFOEXCEEDTIME_CODE,Code.APPINFOEXCEEDTIME_DESC,null);
		
		UserInfo _userInfo = PlayerMgr.getByName(cs.getUsername());
		if (_userInfo!=null) {
			PlayerMgr.removeByName(cs.getUsername());
		}
		
		PlayerDao dao = DaoMgr.getPlayer();
		
		PlayerNode playerNode = null;
		try {
			playerNode = dao.selectOne(parameter);
			if (playerNode==null)
				playerNode = dao.selectOne2(parameter);
		} catch(Exception e) {
			playerNode = null;
			System.out.println(e);
		}
		if (playerNode==null)
			return Function.objToJson(Code.USER_OR_PWD_CODE,Code.USER_OR_PWD_DESC,null);
		if (playerNode.getStatus()!=Code.STATUS_EFFECTIVE)
			return Function.objToJson(Code.NOACCOUNT_CODE,Code.NOACCOUNT_DESC,null);
		
		String sessionID = ComUtil.get32UUID();
		PlayerMgr.put(new UserInfo(sessionID,playerNode));
		
		request.getSession(true).setAttribute(Code.PLAYER_MERCHER, sessionID);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,playerNode);
	}
	
	//查询所有玩家 记录
	@CrossOrigin
	@RequestMapping(value="selectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectList(PlayerSelectListCSNode cs, HttpServletRequest request) {
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
		
		PlayerDao dao = DaoMgr.getPlayer();
		
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<PlayerNode> pageList = dao.selectList(parameter);
			PageInfo<PlayerNode> pageInfo = new PageInfo<PlayerNode>(pageList);
			
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
	
	// 查询所有玩家 总额： http://---/player/amOrderselect.do
			@CrossOrigin
			@RequestMapping(value="/amOrderselect.do",method=RequestMethod.POST)
			@ResponseBody
			public Map<String,Object> amOrderselect( HttpServletRequest request) {
				String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
				if (sessionID==null)
					return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
				AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
				if (adminInfo==null)
					return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
				AdminNode admin = adminInfo.getAdmin();
				if (admin==null)
					return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
				
				
				PlayerDao dao = DaoMgr.getPlayer();
				
				PlayerAMOrderNode AM = dao.countByAMplayer();
				if (AM ==null)
					return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
				
				return Function.objToJson(Code.OK_CODE,Code.OK_DESC,AM);
			}
			
	@CrossOrigin
	@RequestMapping(value="insertone.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> insertOne(PlayerInsertCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		String loginname = cs.getLoginname();
		String username = ComUtil.getOrderNo();
		String nickname = cs.getNickname();
		String password = cs.getPassword();
		int status = cs.getStatus();
		String qq = cs.getQq();
		String weixin = cs.getWeixin();
		String telephone = cs.getTelephone();
		if (	loginname==null || loginname.length()>31 ||
				username==null || username.length()>31 ||
				nickname==null || nickname.length()>31 ||
				password==null || password.length()>31 ||
				(status!=Code.STATUS_EFFECTIVE && status!=Code.STATUS_UNEFFECTIVE) ||
				qq==null || qq.length()>15 ||
				weixin==null || weixin.length()>31 ||
				telephone==null || telephone.length()>15 )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", "GS1001");
		parameter.put("loginname", loginname);
		parameter.put("username", username);
		parameter.put("nickname", nickname);
		parameter.put("password", password);
		parameter.put("status", status);
		parameter.put("typeid", Code.PLAYER_IDENTIFEY_REAL);
		parameter.put("balance", CoinUtil.zero);
		parameter.put("frozen_bal", CoinUtil.zero);
		parameter.put("integral", CoinUtil.zero);
		parameter.put("qq", qq);
		parameter.put("weixin", weixin);
		parameter.put("telephone", telephone);
		parameter.put("createtime", DateUtil.getCurTimestamp().getTime());
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		UserInfo userinfo = PlayerMgr.getByName(username);
		Pk10PlayerNode player2 = Pk10RobotMgr.getPlayer(username);
		if (userinfo!=null || player2!=null)
			return Function.objToJson(Code.USERNAMEHASEXIST_CODE,Code.USERNAMEHASEXIST_DESC,null);
		
		PlayerDao dao = DaoMgr.getPlayer();
		PlayerNode __player = dao.selectByLoginname(loginname);
		Pk10PlayerNode __player2 = Pk10RobotMgr.getPlayer(loginname);
		if (__player!=null || __player2!=null)
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
		
		boolean result = false;
		try {
			if(dao.insert(parameter))
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
	
	@CrossOrigin
	@RequestMapping(value="setstatus.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> setStatus(PlayerSetStatusCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		if (admin.getType()!=Code.ADMIN_ROOT)
			return Function.objToJson(Code.NOJURI_CODE,Code.NOJURI_DESC,null);
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PlayerDao dao = DaoMgr.getPlayer();
		
		boolean result = false;
		try {
			if (dao.setStatus(parameter))
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
	
	@CrossOrigin
	@RequestMapping(value="resetpassword.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> resetPassword(PlayerResetPasswordCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		if (admin.getType()!=Code.ADMIN_ROOT)
			return Function.objToJson(Code.NOJURI_CODE,Code.NOJURI_DESC,null);
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PlayerDao dao = DaoMgr.getPlayer();
		
		boolean result = false;
		try {
			if (dao.resetPassword(parameter))
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
	
	@CrossOrigin
	@RequestMapping(value="editinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editInfo(PlayerEditInfoCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		if (admin.getType()!=Code.ADMIN_ROOT)
			return Function.objToJson(Code.NOJURI_CODE,Code.NOJURI_DESC,null);
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PlayerDao dao = DaoMgr.getPlayer();
		
		boolean result = false;
		try {
			if (dao.editInfo(parameter))
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
	 * http://---/player/synaccount.do
	 * 查询玩家账户信息
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":{
	 * 			"username":"",
	 * 			"nickname":"",
	 * 			"status":1,		1_有效 2_无效
	 * 			"balance":0.00
	 * 			"frozen_bal":0.00,
	 * 			"integral":0.00,
	 * 			"createtime":1490853310369
	 * 		}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/synaccount.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> synAccount(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		
		PlayerInfo info = new PlayerInfo();
		info.setUsername(player.getUsername());
		info.setNickname(player.getNickname());
		info.setStatus(player.getStatus());
		info.setBalance(player.getBalance());
		info.setFrozen_bal(player.getFrozen_bal());
		info.setIntegral(player.getIntegral());
		info.setCreatetime(player.getCreatetime());

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,info);
	}
	
	/*
	 * http://---/player/changesid.do
	 * 切换玩家身份（真实与虚拟）
	 * request: {
	 * 		"username":"",
	 * 		"typeid":1		1_真实 2_虚拟
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="/changesid.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> changesid(ChangesidCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		if (admin.getType()!=Code.ADMIN_ROOT)
			return Function.objToJson(Code.NOJURI_CODE,Code.NOJURI_DESC,null);

		String username = cs.getUsername();
		int typeid = cs.getTypeid();
		if (	username==null || username.length()==0 || 
				(typeid!=Code.PLAYER_IDENTIFEY_REAL && typeid!=Code.PLAYER_IDENTIFEY_VIRTUAL) )
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		
		PlayerNode player = PlayerMgr.getByNameWithDB(username);
		if (player==null)
			return Function.objToJson(Code.OBJNOTEXIT_CODE,Code.OBJNOTEXIT_DESC,null);
		
		BigDecimal balance = player.getBalance();
		if (CoinUtil.compareZero(balance)>0)
			return Function.objToJson(Code.PLAYERCHANGE_CODE,Code.PLAYERCHANGE_DESC,null);
		if (player.getTypeid()==typeid)	//相等就不需要转换了
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		
		PlayerDao dao = DaoMgr.getPlayer();
		
		boolean result = false;
		try {
			if (dao.setTypeid(player.getId(),typeid)) {
				player.setTypeid(typeid);
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
