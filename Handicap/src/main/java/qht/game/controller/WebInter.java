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
import com.mgr.DaoMgr;
import com.node.KinPageInfo;
import com.node.Pk10CurNode;
import com.pk10.mgr.Pk10DealMgr;
import com.sysconst.Code;
import com.sysconst.Function;
import com.util.DateUtil;

import net.node.HandicapEditWebCSNode;
import net.node.HandicapSelectDataCSNode;
import net.node.HandicapUseWebCSNode;
import qht.game.dao.Pk10HandicapDao;
import qht.game.dao.Pk10WebinfoDao;
import qht.game.node.Pk10HandicapBetNode;
import qht.game.node.Pk10WebinfoNode;

@Controller
@RequestMapping(value="/")
public class WebInter {

	/*
	 * http://---/getweb.do
	 * 获取站点信息
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"name":"站点1",
	 * 				"url":"",
	 * 				"username":"",
	 * 				"status":1		1_使用 2_停用 3_登入
	 * 			},
	 * 			{
	 * 				"id":2,
	 * 				"name":"站点2",
	 * 				"url":"",
	 * 				"username":"",
	 * 				"status":1		1_使用 2_停用 3_登入
	 * 			},
	 * 			{
	 * 				"id":3,
	 * 				"name":"站点3",
	 * 				"url":"",
	 * 				"username":"",
	 * 				"status":1		1_使用 2_停用 3_登入
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="getweb.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getweb(HttpServletRequest request) {

		List<Pk10WebinfoNode> pageList = null;
		Pk10WebinfoDao dao = DaoMgr.getPk10Webinfo();
		try {
			pageList = dao.selectList();
		} catch (Exception e) {
			pageList = null;
			System.out.println(e);
		}
		
		if (pageList==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,pageList);
	}
	
	/*
	 * http://---/editweb.do
	 * 编辑站点信息
	 * request: {
	 * 				"id":1,
	 * 				"url":"",
	 * 				"username":"",
	 * 				"password":""
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="editweb.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editweb(HandicapEditWebCSNode cs, HttpServletRequest request) {
		int id = cs.getId();
		String url = cs.getUrl();
		String username = cs.getUsername();
		String password = cs.getPassword();
		if (	id<=0 ||
				url==null || url.length()==0 ||
				username==null || username.length()==0 ||
				password==null || password.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10WebinfoNode info = Pk10DealMgr.getWebinfo(id);
		if (info==null)
			return Function.objToJson(Code.OBJECTNOTEXIT_CODE,Code.OBJECTNOTEXIT_DESC,null);
		
		long updatetime = DateUtil.getCurTimestamp().getTime();
		
		Pk10WebinfoDao dao = DaoMgr.getPk10Webinfo();
		if (!dao.edit(id,url,username,password,updatetime))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		info.setUrl(url);
		info.setUsername(username);
		info.setPassword(password);
		info.setUpdatetime(updatetime);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * http://---/useweb.do
	 * 使用或停用站点信息
	 * request: {
	 * 				"id":1,
	 * 				"type":1	1_使用 2_停用
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="useweb.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> useweb(HandicapUseWebCSNode cs, HttpServletRequest request) {
		
		int id = cs.getId();
		int type = cs.getType();
		if (id<=0 || (type!=Code.PK10_STATUS_USE && type!=Code.PK10_STATUS_STOP))
				return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		Pk10WebinfoDao dao = DaoMgr.getPk10Webinfo();
		if (dao==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		
		Pk10WebinfoNode info = Pk10DealMgr.getWebinfo(id);
		if (info==null)
			return Function.objToJson(Code.OBJECTNOTEXIT_CODE,Code.OBJECTNOTEXIT_DESC,null);
		String url = info.getUrl();
		String username = info.getUsername();
		String password = info.getPassword();
		
		if (type==Code.PK10_STATUS_STOP) {
			Pk10WebinfoNode _webinfo = Pk10DealMgr.getWebinfo();
			if (_webinfo!=null) {
				int _id = _webinfo.getId();
				Pk10DealMgr.close();
				long updatetime = DateUtil.getCurTimestamp().getTime();
				if (!dao.setStatus(_id,Code.PK10_STATUS_STOP,updatetime))
					return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
				Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(_id);
				if (___webinfo!=null)
					___webinfo.setStatus(Code.PK10_STATUS_STOP);
			}
		} else if (type == Code.PK10_STATUS_USE) {
			Pk10WebinfoNode _webinfo = Pk10DealMgr.getWebinfo();
			if (_webinfo!=null) {
				int _id = _webinfo.getId();
				Pk10DealMgr.close();
				long updatetime = DateUtil.getCurTimestamp().getTime();
				if (!dao.setStatus(_id,Code.PK10_STATUS_STOP,updatetime))
					return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
				Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(_id);
				if (___webinfo!=null)
					___webinfo.setStatus(Code.PK10_STATUS_STOP);
			}
			
			if (!Pk10DealMgr.open1(id,url,username,password)) {
				_webinfo = Pk10DealMgr.getWebinfo();
				if (_webinfo!=null) {
					int _id = _webinfo.getId();
					Pk10DealMgr.close();
					long updatetime = DateUtil.getCurTimestamp().getTime();
					if (!dao.setStatus(_id,Code.PK10_STATUS_STOP,updatetime))
						return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
					Pk10WebinfoNode ___webinfo = Pk10DealMgr.getWebinfo(_id);
					if (___webinfo!=null)
						___webinfo.setStatus(Code.PK10_STATUS_STOP);
				}
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			} else {
				long updatetime = DateUtil.getCurTimestamp().getTime();
				if (!dao.setStatus(info.getId(),Code.PK10_STATUS_USE,updatetime))
					return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
				info.setStatus(Code.PK10_STATUS_USE);
			}
		}

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * http://---/getcheckweb.do
	 * 获取验证码
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="getcheckweb.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getcheckweb(HttpServletRequest request) {
		@SuppressWarnings("deprecation")
		String path = request.getRealPath("/");
		if (!Pk10DealMgr.open2(path+"check.png")) {
			//Pk10DealMgr.close();
			return Function.objToJson(Code.CHECKIMG_CODE,Code.CHECKIMG_DESC,null);
		}
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * http://---/loginweb.do
	 * 登入站点
	 * request: {
	 * 		"check":""		验证码
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":""
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="loginweb.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> loginweb(String check, HttpServletRequest request) {
		if (check==null || check.length()==0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		Pk10WebinfoDao dao = DaoMgr.getPk10Webinfo();
		if (dao==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		Pk10WebinfoNode info = Pk10DealMgr.getWebinfo();
		if (info==null)
			return Function.objToJson(Code.OBJECTNOTEXIT_CODE,Code.OBJECTNOTEXIT_DESC,null);
		
		
		if (!Pk10DealMgr.open3(check)) {
			Pk10DealMgr.close();
			long updatetime = DateUtil.getCurTimestamp().getTime();
			if (!dao.setStatus(info.getId(),Code.PK10_STATUS_STOP, updatetime))
				return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
			info.setStatus(Code.PK10_STATUS_STOP);
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}
		
		long updatetime = DateUtil.getCurTimestamp().getTime();
		if (!dao.setStatus(info.getId(),Code.PK10_STATUS_LOGIN, updatetime))
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		info.setStatus(Code.PK10_STATUS_LOGIN);
		
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
	
	/*
	 * http://---/getuserinfo.do
	 * 获取玩家及盘口信息
	 * request: {
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":{
	 * 			"username":"",		玩家名
	 * 			"balance":0.00,		余额
	 * 			"amount":0.00,		该局累计飞盘额
	 * 			"period":"",		期号
	 * 			"sealtime":1234,	离封盘时间
	 * 			"opentime":1234		离开盘时间
	 * 		}
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="getuserinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getuserinfo(HttpServletRequest request) {
		Pk10CurNode info = Pk10DealMgr.getInfo();
		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,info);
	}

	/*
	 * http://---/selectdata.do
	 * 获取飞盘明细
	 * request: {
	 * 		"type":1  	1_全部 2_未同步 3_已同步
	 * 		"page":1,
	 *		"count":8
	 * }
	 * respone: {
	 * 		"code":1000,
	 * 		"desc":"",
	 * 		"info":[
	 * 			{
	 * 				"id":1,
	 * 				"odd":"",			单号
	 * 				"period":"",		期号
	 * 				"runway":"",		车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":"",		投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00	投注金额
	 * 			},
	 * 			{
	 * 				"id":2,
	 * 				"odd":"",			单号
	 * 				"period":"",		期号
	 * 				"runway":"",		车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":"",		投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00	投注金额
	 * 			},
	 * 			{
	 * 				"id":3,
	 * 				"odd":"",			单号
	 * 				"period":"",		期号
	 * 				"runway":"",		车道(1~10表示冠军道~第10道 11表示冠亚和)
	 * 				"bettype":"",		投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)
	 * 				"betamount":0.00	投注金额
	 * 			}
	 * 		]
	 * }
	 */
	@CrossOrigin
	@RequestMapping(value="selectdata.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> selectdata(HandicapSelectDataCSNode cs, HttpServletRequest request) {
		int type = cs.getType();
		int page = cs.getPage();
		int count = cs.getCount();
		if (type!=1 && type!=2 && type!=3)	//1_全部 2_未同步 3_已同步
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);

		if (page<1 || page>Code.PAGEINFO_MAX_PAGE)
			page = Code.PAGEINFO_DEFAULT_PAGE;
		if (count<1 || count>Code.PAGEINFO_MAX_COUNT)
			count = Code.PAGEINFO_DEFAULT_COUNT;
		
		Pk10CurNode pk10Info = Pk10DealMgr.getInfo();
		if (pk10Info==null)
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		String period = pk10Info.getPeriod();
		if (period==null || period.length()==0)
			return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		
		Pk10HandicapDao dao = DaoMgr.getPk10Handicap();
		KinPageInfo data = null;
		try {
			PageHelper.startPage(page,count);
			List<Pk10HandicapBetNode> pageList = dao.selectlist(type, period);
			PageInfo<Pk10HandicapBetNode> pageInfo = new PageInfo<Pk10HandicapBetNode>(pageList);
			
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
