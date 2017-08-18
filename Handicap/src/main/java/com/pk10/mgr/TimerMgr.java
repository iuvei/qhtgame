package com.pk10.mgr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerMgr {
	private static ScheduledExecutorService service;
	private static ScheduledExecutorService service2;
	
	public static boolean init() {
		
		Runnable runnablePk10Opencode = new Runnable() {
			public void run() {
				while (true) {
					try {
						Pk10DealMgr.deal();
						Thread.sleep(2000);
					} catch (Exception e) {}
				}
			}
		};
		service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(runnablePk10Opencode, 5, TimeUnit.SECONDS);
		
		
		Runnable runnablePk10Flush = new Runnable() {
			public void run() {
				while (true) {
					try {
						Pk10DealMgr.flush();
						Thread.sleep(15000);
					} catch (Exception e) {}
				}
			}
		};
		service2 = Executors.newSingleThreadScheduledExecutor();
		service2.schedule(runnablePk10Flush, 7, TimeUnit.SECONDS);
				
		return true;
	}
}
