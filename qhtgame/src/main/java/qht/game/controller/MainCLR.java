package qht.game.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Controller;

import com.logic.mgr.AppinfoMgr;
import com.logic.mgr.DaoMgr;
import com.logic.mgr.ReportGenMgr;
import com.logic.mgr.TimerMgr;
import com.pk10.logic.Pk10PointoutMgr;
import com.pk10.logic.Pk10PrizeMgr;
import com.pk10.logic.Pk10RobotMgr;
import com.pk10.logic.Pk10TextMgr;
import com.tls.sigcheck.ImOpr;
import com.tls.sigcheck.TlsSig;

@Controller
public class MainCLR {
	
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	@PostConstruct
	public void init() {
		DaoMgr.init(sqlSessionTemplate);
		
		if (!Pk10TextMgr.init()) {
			System.out.println("Pk10Context init error");
			return;
		}
		/*if (!Pk10ConfigMgr.init()) {
			System.out.println("Pk10ConfigMgr init error");
			return;
		}*/
		if (!Pk10PrizeMgr.init()) {
			System.out.println("start error : init Pk10PrizeMgr fail");
			return;
		}
		if (!ReportGenMgr.init()) {
			System.out.println("start error : init ReportGenMgr fail");
			return;
		}
		if (!AppinfoMgr.init()) {
			System.out.println("start error : init AppinfoMgr fail");
			return;
		}
		if (!Pk10RobotMgr.init()) {
			System.out.println("start error : init Pk10RobotMgr fail");
			return;
		}
		if (!Pk10PointoutMgr.init()) {
			System.out.println("start error : init Pk10PointoutMgr fail");
			return;
		}
		
		if (!TimerMgr.init()) {	//定时任务放在所有对象都初始化后
			System.out.println("start error : init TimerMgr fail");
			return;
		}
		
		//IM初始化放到最后（有可能连接IM服务不成功，导致服务开不起来)
		if (!TlsSig.init()) {
			System.out.println("tlssig init error");
			return;
		}
		if (!ImOpr.init()) {
			System.out.println("start error : init group fail");
			return;
		}
	}
}
