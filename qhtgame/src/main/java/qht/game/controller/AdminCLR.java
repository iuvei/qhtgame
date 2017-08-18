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
import com.logic.mgr.AdminMgr;
import com.logic.mgr.DaoMgr;
import com.node.AdminInfo;
import com.node.KinPageInfo;
import com.sysconst.Code;
import com.sysconst.Function;
import com.util.ComUtil;

import net.node.AdminCSNode;
import net.node.AdminEditNicknameCSNode;
import net.node.AdminInsertCSNode;
import net.node.AdminResetPasswordCSNode;
import net.node.AdminSetStatusCSNode;
import net.node.LoginCSNode;
import qht.game.dao.AdminDao;
import qht.game.node.AdminNode;

@Controller
@RequestMapping(value="/admin/")
public class AdminCLR {
	/*
	 * 登入
	 */
	@CrossOrigin//(origins = "http://localhost:8080",maxAge = 3600)
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> login(LoginCSNode cs, HttpServletRequest request) {
		Map<String,Object> parameter = cs.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		AdminInfo _adminNode = AdminMgr.getByName(cs.getUsername());
		if (_adminNode!=null) {
			AdminMgr.removeByName(cs.getUsername());
		}
		
		AdminDao dao = DaoMgr.getAdmin();
		
		AdminNode adminNode = null;
		try {
			adminNode = dao.selectOne(parameter);
		} catch(Exception e) {
			adminNode = null;
			System.out.println(e);
		}
		if (adminNode==null)
			return Function.objToJson(Code.USER_OR_PWD_CODE,Code.USER_OR_PWD_DESC,null);
		if (adminNode.getStatus()!=Code.STATUS_EFFECTIVE)
			return Function.objToJson(Code.NOACCOUNT_CODE,Code.NOACCOUNT_DESC,null);
		
		String sessionID = ComUtil.get32UUID();
		AdminMgr.put(new AdminInfo(sessionID,adminNode));
		request.getSession().setAttribute(Code.ADMIN_MERCHER, sessionID);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,adminNode.getNickname());
	}
	
	/*
	 * 查询admin账号信息集
	 */
	@CrossOrigin
	@RequestMapping(value="selectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectList(AdminCSNode cs, HttpServletRequest request) {
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
		
		AdminDao dao = DaoMgr.getAdmin();
		
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<AdminNode> pageList = dao.selectList(parameter);
			PageInfo<AdminNode> pageInfo = new PageInfo<AdminNode>(pageList);
			
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
	
	@CrossOrigin
	@RequestMapping(value="insertone.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> insertOne(AdminInsertCSNode cs, HttpServletRequest request) {
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
		
		AdminDao dao = DaoMgr.getAdmin();
		
		boolean result = false;
		try {
			int count = dao.insert(parameter);
			if (count==1)
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
	public Map<String,Object> setStatus(AdminSetStatusCSNode cs, HttpServletRequest request) {
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
		
		AdminDao dao = DaoMgr.getAdmin();
		
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
	public Map<String,Object> resetPassword(AdminResetPasswordCSNode cs, HttpServletRequest request) {
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
		
		AdminDao dao = DaoMgr.getAdmin();
		
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
	@RequestMapping(value="editnickname.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editNickname(AdminEditNicknameCSNode cs, HttpServletRequest request) {
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
		
		AdminDao dao = DaoMgr.getAdmin();
		
		boolean result = false;
		try {
			if (dao.editNickname(parameter))
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
}
