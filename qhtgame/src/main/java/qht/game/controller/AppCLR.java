package qht.game.controller;

import java.util.ArrayList;
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
import com.im.node.ImClientInfo;
import com.im.node.ImClientNode;
import com.logic.mgr.AdminMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.AdminInfo;
import com.node.KinPageInfo;
import com.node.UserInfo;
import com.sysconst.Code;
import com.sysconst.Function;
import com.tls.sigcheck.TlsSig;
import com.util.DateUtil;

import net.node.CustomerEditwcCSNode;
import net.node.CustomerSelectListCSNode;
import net.node.ImCustomLoginCSNode;
import qht.game.dao.CustomerDao;
import qht.game.node.AdminNode;
import qht.game.node.CustomerNode;
import qht.game.node.PlayerNode;

@Controller
@RequestMapping(value="/app/")
public class AppCLR {
	
	/*
	 * 获取IM信息
	 * http://---/app/getiminfo.do
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info": {
	 * 				"sdkAppId:"",
	 *				"accountType":"",
	 *				"userSig":"",
	 *				"appGroupid":""
	 *				"list":[
	 *						{"id":"","groupID":"","custom":""}
	 *						{"id":"","groupID":"","custom":""}
	 *						{"id":"","groupID":"","custom":""}
	 *					]
	 * 			}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="getiminfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getIMInfo(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		
		//产生sig
		String usersig = TlsSig.generatorUsersig(player.getUsername());
		if (usersig==null)
			Function.objToJson(Code.IMGENSIGFAIL_CODE,Code.IMGENSIGFAIL_DESC,null);
		
		ImClientInfo data = new ImClientInfo();
		data.setSdkAppId(TlsSig.SDK_APP_ID);
		data.setAccountType(TlsSig.ACCOUNT_TYPE);
		data.setUserSig(usersig);
		data.setAppGroupid(TlsSig.APP_GROUP_ID);
		List<ImClientNode> list = new ArrayList<ImClientNode>();
		ImClientNode node = new ImClientNode();
		node.setId("pk10");
		node.setGroupID(TlsSig.PK10_GROUP_ID);
		node.setCustom(TlsSig.PK10_GROUP_CUSTOM);
		list.add(node);
		data.setList(list);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	//客服登入
	@CrossOrigin
	@RequestMapping(value="customlogin.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(ImCustomLoginCSNode cs, HttpServletRequest request) {
		String username = cs.getUsername();
		String password = cs.getPassword();
		
		
		if (	username==null || 
				username.length()==0 ||
				password==null ||
				password.length()==0
				)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		// ZXB  登入验证密码
		CustomerDao dao = DaoMgr.getCustomer();
		CustomerNode cus = dao.selectOne(username);
		
		String userSig = null;
		if (TlsSig.APP_GROUP_CUSTOM.equals(username)) {
			if (!password.equals(cus.getCus_pwd()))
				return Function.objToJson(Code.USER_OR_PWD_CODE,Code.USER_OR_PWD_DESC,null);
			userSig = TlsSig.generatorUsersig(TlsSig.APP_GROUP_CUSTOM);
		} else if (TlsSig.PK10_GROUP_CUSTOM.equals(username)) {
			if (!password.equals(cus.getCus_pwd()))
				return Function.objToJson(Code.USER_OR_PWD_CODE,Code.USER_OR_PWD_DESC,null);
			userSig = TlsSig.generatorUsersig(TlsSig.PK10_GROUP_CUSTOM);
		} else {
			return Function.objToJson(Code.USER_OR_PWD_CODE,Code.USER_OR_PWD_DESC,null);
		}
		
		if (userSig==null)
			return Function.objToJson(Code.IMGENSIGFAIL_CODE,Code.IMGENSIGFAIL_DESC,null);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("username", username);
		data.put("sdkAppId",TlsSig.SDK_APP_ID);
		data.put("accountType",TlsSig.ACCOUNT_TYPE);
		data.put("userSig",userSig);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	//  ZXB  客服信息  /app/customerselectlist.do
	@CrossOrigin
	@RequestMapping(value="customerselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> customerSelectList(CustomerSelectListCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		String cus_num = cs.getCus_num();
		int cus_id = cs.getId();
		int page = cs.getPage();
		int count = cs.getCount();
		if (cus_num==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		if (cus_id<0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;

		CustomerDao dao = DaoMgr.getCustomer();
		
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<CustomerNode> pageList = dao.selectList(cus_num,cus_id);
			PageInfo<CustomerNode> pageInfo = new PageInfo<CustomerNode>(pageList);
			
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
	

	// ZXB 客服  更改密码  /app/customerEditpwd.do
	@CrossOrigin
	@RequestMapping(value="customerEditpwd.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> customerEdit(CustomerEditwcCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		String cus_num = cs.getCus_num();
		String cus_pwd = cs.getCus_pwd();
		String cus_name = cs.getCus_name();
		if (	cus_name==null || cus_name.length()==0 || 
				cus_pwd==null || cus_pwd.length()==0 || 
						cus_name==null || cus_name.length()==0)
			return null;
		
		long updatetime = DateUtil.getCurTimestamp().getTime();
		
		
		CustomerDao dao = DaoMgr.getCustomer();
		boolean result = false;
		try {
			if (dao.editCUS(cus_num,cus_pwd,cus_name,updatetime)) {
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
