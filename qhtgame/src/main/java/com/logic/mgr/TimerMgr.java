/*
 * 定时管理
 */
package com.logic.mgr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.pk10.logic.Pk10PrizeMgr;
import com.pk10.logic.Pk10RobotMgr;

public class TimerMgr {
	
	private static ScheduledExecutorService servicePk10Opencode;
	private static ScheduledExecutorService serviceReport;
	private static ScheduledExecutorService servicePk10Robot;
	private static ScheduledExecutorService servicePk10Handicap;
	
	public static boolean init() {
		//pk10定时更新开奖数据
		Runnable runnablePk10Opencode = new Runnable() {
			public void run() {
				while (true) {
					try {
						Pk10PrizeMgr.timer();
						Thread.sleep(3000);
					} catch (Exception e) {
						System.out.println("runnablePk10Opencode try catch");
					}
				}
			}
		};
		servicePk10Opencode = Executors.newSingleThreadScheduledExecutor();
		servicePk10Opencode.schedule(runnablePk10Opencode, 2, TimeUnit.SECONDS);
		
		//pk10定时飞盘
		Runnable runnablePk10Handicap = new Runnable() {
			public void run() {
				while (true) {
					try {
						//System.out.println(DateUtil.TimestampToString(DateUtil.getCurTimestamp(),"YYYY-MM-DD HH:mm:ss")+" handicapTimer");
						Pk10PrizeMgr.handicapTimer();
						Thread.sleep(3000);
					} catch (Exception e) {
						System.out.println("runnableHandicapTimer try catch");
					}
				}
			}
		};
		servicePk10Handicap = Executors.newSingleThreadScheduledExecutor();
		servicePk10Handicap.schedule(runnablePk10Handicap, 3, TimeUnit.SECONDS);
		
		//轮询产生报表
		Runnable runnableReport = new Runnable() {
			public void run() {
				while (true) {
					try {
						ReportGenMgr.timer();
						Thread.sleep(300000);
					} catch (Exception e) {
						System.out.println("runnableReport try catch");
					}
				}
			}
		};
		serviceReport = Executors.newSingleThreadScheduledExecutor();
		serviceReport.schedule(runnableReport, 1, TimeUnit.MINUTES);
		
		//机器人投注动作
		Runnable runnablePk10Robot = new Runnable() {
			public void run() {
				while (true) {
					try {
						Pk10RobotMgr.timer();
						Thread.sleep(5000);
					} catch (Exception e) {
						System.out.println("runnablePk10Robot try catch");
					}
				}
			}
		};
		servicePk10Robot = Executors.newSingleThreadScheduledExecutor();
		servicePk10Robot.schedule(runnablePk10Robot, 4, TimeUnit.SECONDS);
		
		return true;
	}
}
