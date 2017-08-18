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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logic.mgr.AdminMgr;
import com.logic.mgr.AppinfoMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.PlayerMgr;
import com.node.AdminInfo;
import com.node.KinPageInfo;
import com.node.UserInfo;
import com.pk10.logic.Pk10PrizeMgr;
import com.pk10.logic.Pk10RobotMgr;
import com.sysconst.Code;
import com.sysconst.Function;
import com.util.ComUtil;
import com.util.DateUtil;
import com.util.FileUpload;
import com.util.UuidUtil;

import net.node.AppinfoEditwcCSNode;
import net.node.AppinfoInsertCSNode;
import net.node.AppinfoSelectListCSNode;
import net.node.AppinfoSetTimeCSNode;
import net.node.LotteryEditRuleCSNode;
import net.node.LotteryInsetCSNode;
import net.node.LotterySelectListCSNode;
import net.node.LotterySetTimeCSNode;
import qht.game.dao.AppinfoDao;
import qht.game.dao.LotteryDao;
import qht.game.node.AdminNode;
import qht.game.node.AppinfoNode;
import qht.game.node.LotteryNode;
import qht.game.node.LotteryTwoNode;
import qht.game.node.PlayerNode;

@Controller
@RequestMapping(value="/system/")
public class SystemCLR {

	@CrossOrigin
	@RequestMapping(value="lotteryselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> lotterySelectList(LotterySelectListCSNode cs, HttpServletRequest request) {
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
		if (lottery_id<0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;

		LotteryDao dao = DaoMgr.getLottery();
		
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<LotteryNode> pageList = dao.selectList(lottery_id);
			PageInfo<LotteryNode> pageInfo = new PageInfo<LotteryNode>(pageList);
			
			List<LotteryTwoNode> result = new ArrayList<LotteryTwoNode>();
			for (LotteryNode node : pageList) {
				LotteryTwoNode info = new LotteryTwoNode();
				info.setId(node.getId());
				info.setName(node.getName());
				info.setShutname(node.getShutname());
				info.setOpentime(node.getOpentime());
				info.setSpacetime(node.getSpacetime());
				info.setNotbettime(node.getNotbettime());
				info.setRule(new String(ComUtil.decode(node.getRule())));
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
	
	//App查询规则接口：
	@CrossOrigin
	@RequestMapping(value="lotteryappselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> lotteryAPPSelectList(LotterySelectListCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int id = cs.getLottery_id();
		if (id<=0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		   LotteryDao dao = DaoMgr.getLottery();
		   LotteryTwoNode data = new LotteryTwoNode( );
		try {
			
			    LotteryNode pageList = dao.selectOne(id);
			    
			    
			
				data.setId(pageList.getId());
				data.setName(pageList.getName());
				data.setShutname(pageList.getShutname());
				data.setSpacetime(pageList.getSpacetime());
				data.setRule(new String(ComUtil.decode(pageList.getRule())));
				data.setCreatetime(pageList.getCreatetime());
				data.setUpdatetime(pageList.getUpdatetime());
				
				
		} catch (Exception e) {
			System.out.println(e);
		}
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	@CrossOrigin
	@RequestMapping(value="lotterysettime.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> lotterySettime(LotterySetTimeCSNode cs, HttpServletRequest request) {
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
		
		LotteryDao dao = DaoMgr.getLottery();
		boolean result = false;
		try {
			if (dao.setTime(parameter)) {
				Pk10PrizeMgr.setOpentime(cs.getOpentime());
				Pk10PrizeMgr.setNotBet(cs.getNotbettime());
				Pk10PrizeMgr.setSealplate(cs.getSpacetime());
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
	
	//编辑游戏规则
	@CrossOrigin
	@RequestMapping(value="lotteryeditrule.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> lotteryEditRule(LotteryEditRuleCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int lottery_id = cs.getId();
		String lottery_rule = cs.getRule();
		
		if (lottery_id<=0 ||lottery_rule==null || lottery_rule.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		LotteryDao dao = DaoMgr.getLottery();
		boolean result = false;
		try {
			if (dao.edit(lottery_id,lottery_rule)) {
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
	@RequestMapping(value="lotteryinsert.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> lotteryInsert(LotteryInsetCSNode cs, HttpServletRequest request) {
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
		
		LotteryDao dao = DaoMgr.getLottery();
		
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
	@RequestMapping(value="appinfoselectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> appinfoSelectList(AppinfoSelectListCSNode cs, HttpServletRequest request) {
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
		int page = cs.getPage();
		int count = cs.getCount();
		if (appcode==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;

		AppinfoDao dao = DaoMgr.getAppinfo();
		
		KinPageInfo data = null;
		try {
			PageHelper.startPage(cs.getPage(),cs.getCount());
			List<AppinfoNode> pageList = dao.selectList(appcode);
			PageInfo<AppinfoNode> pageInfo = new PageInfo<AppinfoNode>(pageList);
			
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
	@RequestMapping(value="appinfosettime.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> appinfoSettime(AppinfoSetTimeCSNode cs, HttpServletRequest request) {
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
		String actiontime = cs.getActiontime();
		if (	appcode==null || appcode.length()==0 || 
				actiontime==null || actiontime.length()==0)
			return null;
		
		Timestamp timestamp = DateUtil.StringToTimestamp(actiontime);
		if (timestamp==null)
			return null;
		long lactiontime = timestamp.getTime();
		long updatetime = DateUtil.getCurTimestamp().getTime();
		
		AppinfoNode appinfo = AppinfoMgr.get(cs.getAppcode());
		if (appinfo==null)
			return Function.objToJson(Code.APPINFONOTEXIT_CODE,Code.APPINFONOTEXIT_DESC,null);
		
		AppinfoDao dao = DaoMgr.getAppinfo();
		boolean result = false;
		try {
			if (dao.setTime(appcode,lactiontime,updatetime)) {
				appinfo.setActiontime(lactiontime);
				appinfo.setUpdatetime(updatetime);
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
	
	//编辑微信号
	@CrossOrigin
	@RequestMapping(value="appinfoEditwc.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> appinfoEditWC(AppinfoEditwcCSNode cs, HttpServletRequest request) {
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
		String wechat_code = cs.getWechat_code();
		String wechat_p = cs.getWechat_p();
		if (	appcode==null || appcode.length()==0 || 
				wechat_p==null || wechat_p.length()==0 || 
				wechat_code==null || wechat_code.length()==0)
			return null;
		
		long updatetime = DateUtil.getCurTimestamp().getTime();
		
		AppinfoNode appinfo = AppinfoMgr.get(cs.getAppcode());
		if (appinfo==null)
			return Function.objToJson(Code.APPINFONOTEXIT_CODE,Code.APPINFONOTEXIT_DESC,null);
		
		AppinfoDao dao = DaoMgr.getAppinfo();
		boolean result = false;
		try {
			if (dao.editWC(appcode,wechat_p,wechat_code,updatetime)) {
				appinfo.setWechat_code(wechat_code);
				appinfo.setWechat_p(wechat_p);
				appinfo.setUpdatetime(updatetime);
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
	
	//新增 app  0428
	@CrossOrigin
	@RequestMapping(value="appinfoinsertone.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> appinfoInsertOne(AppinfoInsertCSNode cs, HttpServletRequest request,
			@RequestParam(value="slidePic",required=false) MultipartFile tp,
			@RequestParam(value="appcode",required=false) String appcode,
			@RequestParam(value="wechat_img",required=false) String wechat_img,
			@RequestParam(value="wechat_p",required=false) String wechat_p) throws Exception {
		
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
		
		
		//图片上传
//		String pictureSaveFilePath =Const.FILE_PATH_IMG;
		String pictureSaveFilePath = request.getSession().getServletContext().getRealPath("/")+"TP/";
		pictureSaveFilePath=pictureSaveFilePath.replaceAll("%20", " ");
//		logger.info(pictureSaveFilePath);
		String tpid = UuidUtil.get32UUID();
		if (null != tp && !tp.isEmpty()) {
			try {
				String slidePic = FileUpload.fileUp(tp, pictureSaveFilePath, tpid);
				parameter.put("wechat_img", slidePic);
				parameter.put("wechat_p", slidePic);
			} catch (Exception e) {
				System.out.println(e);
//				logger.error(e.getMessage(), e);
			}
		}else{parameter.put("wechat_img", wechat_img);
		           parameter.put("wechat_p", wechat_p);
		}
		
		AppinfoDao dao = DaoMgr.getAppinfo();
		boolean result = false;
		try {
			if(dao.insert(parameter)) {
				AppinfoMgr.put(dao.selectOne(cs.getAppcode()));
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
	
	/*
	 * http://---/system/getonlinenumber.do
	 * 获取在线人数
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":100
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="getonlinenumber.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getOnlineNumber(HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		if (player==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		
		int realCount = PlayerMgr.getCount();
		int robotCount = Pk10RobotMgr.getOnlineCount();
		int count = realCount + robotCount;
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,count);
	}
	/*
	 * http://---/system/appselectwc.do
	 * APP微信信息 ，查询
	 * request: {
	 * "appcode"
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":1
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="appselectwc.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> appSelectwc(AppinfoSelectListCSNode cs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();
		if (player==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		
		String appcode = cs.getAppcode();
		if (appcode==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		

		//从内存中去取
		AppinfoNode pageList = AppinfoMgr.get(appcode);
		try {
			if (pageList==null)
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			
		
		} catch (Exception e) {
			System.out.println(e);
		}
		   
          
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,pageList);
	}
	
}
