package qht.game.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mgr.DaoMgr;
import com.node.Pk10HandicapCSNode;
import com.node.Pk10HandicapNode;
import com.pk10.mgr.Pk10DealMgr;
import com.pk10.mgr.TimerMgr;
import com.sysconst.Code;
import com.sysconst.Function;

@Controller
@RequestMapping(value="/main/")
public class MainCLR {
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@PostConstruct
	public void init() {
		DaoMgr.init(sqlSessionTemplate);
		
		if (!Pk10DealMgr.init()) {
			System.out.println("Pk10DealMgr init error");
			return;
		}
		
		if (!TimerMgr.init()) {
			System.out.println("TimerMgr init error");
			return;
		}
	}
	
	@CrossOrigin
	@RequestMapping(value="handicap.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> handicap(String str, HttpServletRequest request) {
		/*String sessionID = (String)request.getSession().getAttribute(Code.PLAYER_MERCHER);
		if (sessionID==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		UserInfo userInfo = PlayerMgr.getBySessionID(sessionID);
		if (userInfo==null)
			return Function.objToJson(Code.NOLOGIN_CODE,Code.NOLOGIN_DESC,null);
		PlayerNode player = userInfo.getPlayer();*/
		
		Pk10HandicapCSNode cs = new Gson().fromJson(str, Pk10HandicapCSNode.class);
		
		List<Pk10HandicapNode> arr = cs.getArr();
		if (arr==null || arr.size()==0)
			Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
		
		for (Pk10HandicapNode node : arr) {
			Pk10DealMgr.insert(node.getPeriod(), node.getRunway(), node.getBettype(), node.getBetamount());
		}

		return Function.objToJson(Code.OK_CODE,Code.OK_DESC,null);
	}
}
