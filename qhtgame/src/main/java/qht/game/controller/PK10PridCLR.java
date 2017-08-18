package qht.game.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.logic.mgr.AdminMgr;
import com.logic.mgr.DaoMgr;
import com.node.AdminInfo;
import com.pk10.logic.Pk10PrizeMgr;
import com.sysconst.Code;
import com.sysconst.Function;

import net.node.PK10BetSelectCSNode;
import net.node.PK10PaidSelectListCSNode;
import qht.game.dao.PK10PaidDao;
import qht.game.node.AdminNode;
import qht.game.node.PK10BetNode;
import qht.game.node.PK10PaidNode;
/**
 * 描述：pridSelectlist 查询
 * @author @zxb
 *时间：@2017年4月11日下午3:48:27
 */
@Controller
@RequestMapping(value="/pk10Prid/")
public class PK10PridCLR {
	
	/**
	 * 赔率设置 查询
	 * @param ps
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="pridSelectlist.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pridSelectlist(PK10PaidSelectListCSNode ps, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int paid_id = ps.getId();
		if (paid_id<0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PK10PaidDao dao = DaoMgr.getPk10Paid();
		
		PK10PaidNode data = null;
		try {
			data = dao.selectOne(paid_id);
		} catch(Exception e) {
			data = null;
			System.out.println(e);
		}
//		PK10PaidDao dao = DaoMgr.getPk10Paid();
//		
//		List<PK10PaidNode>  data= dao.selectList(paid_id);
//		 JSONArray data = JSONArray.fromObject(subMsgs);
		
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	/**
	 * 下注设置 查询
	 * @param bs
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="pk10_betselect.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pk10BetSelectlist(PK10BetSelectCSNode bs, HttpServletRequest request) {
		String sessionID = (String)request.getSession().getAttribute(Code.ADMIN_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminInfo adminInfo = AdminMgr.getBySessionID(sessionID);
		if (adminInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		AdminNode admin = adminInfo.getAdmin();
		if (admin==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		
		int paid_id = bs.getId();
		if (paid_id<0)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PK10PaidDao dao = DaoMgr.getPk10Paid();
		
		PK10BetNode data = null;
		try {
			data = dao.selectOneBet(paid_id);
		} catch(Exception e) {
			data = null;
			System.out.println(e);
		}
//		PK10PaidDao dao = DaoMgr.getPk10Paid();
//		
//		List<PK10PaidNode>  data= dao.selectList(paid_id);
//		 JSONArray data = JSONArray.fromObject(subMsgs);
		
		if (data==null)
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,data);
	}
	
	/**
	 * 赔率设置 修改
	 * @param es
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="editinfo.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> editInfo(PK10PaidNode es, HttpServletRequest request) {
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
		Map<String,Object> parameter = es.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PK10PaidNode ini = Pk10PrizeMgr.getMpaid();
		if (ini==null) {
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}
				
				
		PK10PaidDao dao = DaoMgr.getPk10Paid();
		boolean result = false;
		try {
			if (dao.editInfo(parameter))
				ini.setN_number(es.getN_number());
				ini.setN_big(es.getN_big());
				ini.setN_small(es.getN_small());
				ini.setN_single(es.getN_single());
				ini.setN_double(es.getN_double());
				ini.setN_dragon(es.getN_dragon());
				ini.setN_tiger(es.getN_tiger());
				ini.setS_big(es.getS_big());
				ini.setS_small(es.getS_small());
				ini.setS_single(es.getS_single());
				ini.setS_double(es.getS_double());
				ini.setS_number_11(es.getS_number_11());
				ini.setS_number_341819(es.getS_number_341819());
				ini.setS_number_561617(es.getS_number_561617());
				ini.setS_number_781415(es.getS_number_781415());
				ini.setS_number_9101213(es.getS_number_9101213());
				
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
	 * 下注设置 修改
	 * @param es
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="pk10_betedit.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> pk10BetEdit(PK10BetNode bn, HttpServletRequest request) {
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
		Map<String,Object> parameter = bn.getParameter();
		if (parameter==null)
			return Function.objToJson(Code.PARAM_CODE,Code.PARAM_DESC,null);
		
		PK10BetNode pbn = Pk10PrizeMgr.getMpaid(Code.LORREY_PK10);
		
		if (pbn==null) {
			return Function.objToJson(Code.CATCH_CODE,Code.CATCH_DESC,null);
		}
		
		PK10PaidDao dao = DaoMgr.getPk10Paid();
		boolean result = false;
		try {
			
			if (dao.editInfoBet(parameter))
				
			pbn.setBig_small_low(bn.getBig_small_low());
			pbn.setBig_small_high(bn.getBig_small_high());
			pbn.setSingle_double_low(bn.getSingle_double_low());
			pbn.setSingle_double_high(bn.getSingle_double_high());
			pbn.setDragon_tiger_low(bn.getDragon_tiger_low());
			pbn.setDragon_tiger_high(bn.getDragon_tiger_high());
			pbn.setN_number_low(bn.getN_number_low());
			pbn.setN_number_high(bn.getN_number_high());
			pbn.setS_number_low(bn.getS_number_low());
			pbn.setS_number_high(bn.getS_number_high());
			pbn.setAllBet(bn.getAllBet());	
			
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
