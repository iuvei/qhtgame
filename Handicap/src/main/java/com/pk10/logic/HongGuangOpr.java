package com.pk10.logic;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.internal.WrapsDriver;

import com.node.ErrorCode;
import com.node.Pk10CurNode;
import com.node.Pk10Info;
import com.node.Pk10Node;
import com.sysconst.Code;
import com.util.CoinUtil;

public class HongGuangOpr implements Pk10WebInter {
	
	private int id;
	
	private WebDriver driver;
	private Pk10CurNode info;
	private boolean isOK;
	private int count;
	
	public HongGuangOpr(int id) {
		this.id = id;
		this.driver = null;
		this.info = new Pk10CurNode();
		this.isOK = false;
		this.count = 0;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isOk() {
		return isOK;
	}
	
	public Pk10CurNode getInfo() {
		return info;
	}
	
	public boolean open1(String url,String username,String password){
		boolean result = false;
		try {
			driver = new FirefoxDriver();
			driver.get(url);
			Thread.sleep(3000);
			
			driver.manage().window().maximize();
			WebElement loginName = driver.findElement(By.id("loginName"));
			WebElement loginPwd = driver.findElement(By.id("loginPwd"));
			
			loginName.sendKeys(username);
			loginPwd.sendKeys(password);
			
			info.clear();
			info.setUsername(username);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public boolean open2(String path){
		boolean result = false;
		try {
			WebElement ValidateCode = driver.findElement(By.id("ValidateCode"));
			WebElement ValidateImage = driver.findElement(By.id("ValidateImage"));
			
			ValidateCode.sendKeys("0000");
			ValidateCode.clear();
			ValidateCode.click();
			Thread.sleep(500);
		
			File file = captureElement(ValidateImage);
			FileUtils.copyFile(file, new File(path));
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}	
	
	public boolean open3(String check) {
		boolean result = false;
		try {
			WebElement ValidateCode = driver.findElement(By.id("ValidateCode"));
			ValidateCode.sendKeys(check);
			
			WebElement Submit = driver.findElement(By.id("Submit"));
			Submit.click();
			
			Thread.sleep(1000);
			Submit = driver.findElement(By.id("Submit"));
			Submit.click();
			
			Thread.sleep(2000);
			WebElement topFrame = driver.findElement(By.id("topFrame"));
			driver.switchTo().frame(topFrame);
			//Thread.sleep(500);
			Submit = driver.findElement(By.id("bST_6"));
			Submit.click();
			Thread.sleep(500);
			
			isOK = true;
			this.count = 0;
			
			result = true;
        } catch (Exception e) {
        	result = false;
        	System.out.println(e);
        }
		
		return result;
	}
	
	public void close() {
		if (driver!=null) {
			try {
	    		driver.quit();
	    		driver = null;
			} catch (Exception e) {}
		}
		isOK = false;
		this.count = 0;
	}
	
	public boolean flushInfo() {
		boolean result = false;
		try {
			//获取余额
			driver.switchTo().defaultContent();
			WebElement topFrame = driver.findElement(By.id("topFrame"));
			driver.switchTo().frame(topFrame);
			WebElement Submit = driver.findElement(By.id("bST_6"));
			Submit.click();
			Thread.sleep(500);
			
			driver.switchTo().defaultContent();
			WebElement leftFrame = driver.findElement(By.id("leftFrame"));
			driver.switchTo().frame(leftFrame);
			Thread.sleep(500);
			WebElement webAmount = driver.findElement(By.id("currentCredits"));
			String strAmount = webAmount.getText();
			if (strAmount!=null && strAmount.length()>0) {
				strAmount = strAmount.replaceAll(",", "");
				BigDecimal balance = CoinUtil.createNew(strAmount);
				balance = CoinUtil.sub(balance, CoinUtil.createNew("1"));
				info.setBalance(balance);
			} else {
				return false;
			}
			
			
			//获取期号、离封盘时间、离开奖时间
			driver.switchTo().defaultContent();
			WebElement mainFrame = driver.findElement(By.id("mainFrame"));
			driver.switchTo().frame(mainFrame);
			Thread.sleep(500);
			WebElement webO = driver.findElement(By.id("o"));
			String strO = webO.getText();
			WebElement webEndTime = driver.findElement(By.id("endTime"));
			String strEndTime = webEndTime.getText();
			WebElement webEndTimes = driver.findElement(By.id("endTimes"));
			String strEndTimes = webEndTimes.getText();
			if (	strO!=null && strO.length()==6	&&
					strEndTime!=null && strEndTime.length()==5 &&
					strEndTimes!=null && strEndTimes.length()==5) {
				String strMinute = strEndTime.substring(0, 2);
				String strSecond = strEndTime.substring(3, 5);
				int minute = Integer.valueOf(strMinute);
				int second = Integer.valueOf(strSecond);
				int sealTime = minute*60+second;
				
				strMinute = strEndTimes.substring(0, 2);
				strSecond = strEndTimes.substring(3, 5);
				minute = Integer.valueOf(strMinute);
				second = Integer.valueOf(strSecond);
				int openTime = minute*60+second;
				
				if (!info.getPeriod().equals(strO)) {
					info.setAmount(CoinUtil.zero);
				}
				
				info.setPeriod(strO);
				info.setSealtime(sealTime);
				info.setOpentime(openTime);
				result = true;
				this.count = 0;
			}
		} catch (Exception e) {
			if (++count>3)
				isOK = false;
        	result = false;
        	System.out.println(e);
        }
		return result;
	}
	
	/*
	 * 投注
	 */
	public ErrorCode bet(String ctypep,String period,List<Pk10Info> data) {

		try {
			//定位
			driver.switchTo().defaultContent();
			WebElement topFrame = driver.findElement(By.id("topFrame"));
			driver.switchTo().frame(topFrame);
			Thread.sleep(500);
			WebElement Submit = driver.findElement(By.id("bST_6"));
			Submit.click();
			Thread.sleep(500);
			WebElement webCtypep = driver.findElement(By.id(ctypep));
			webCtypep.click();
			Thread.sleep(1000);
			
			//获取余额
			BigDecimal balance = CoinUtil.zero;
			driver.switchTo().defaultContent();
			WebElement leftFrame = driver.findElement(By.id("leftFrame"));
			driver.switchTo().frame(leftFrame);
			Thread.sleep(500);
			WebElement webAmount = driver.findElement(By.id("currentCredits"));
			String strAmount = webAmount.getText();
			if (strAmount!=null && strAmount.length()>0) {
				strAmount = strAmount.replaceAll(",", "");
				balance = CoinUtil.createNew(strAmount);
				balance = CoinUtil.sub(balance, CoinUtil.createNew("1"));
				info.setBalance(balance);
			} else {
				return new ErrorCode(Code.BET_FAIL,null);
			}
			
			
			//获取期号、离封盘时间、离开奖时间
			driver.switchTo().defaultContent();
			WebElement mainFrame = driver.findElement(By.id("mainFrame"));
			driver.switchTo().frame(mainFrame);
			Thread.sleep(500);
			WebElement webO = driver.findElement(By.id("o"));
			String _period = webO.getText();
			WebElement webEndTime = driver.findElement(By.id("endTime"));
			String strEndTime = webEndTime.getText();
			WebElement webEndTimes = driver.findElement(By.id("endTimes"));
			String strEndTimes = webEndTimes.getText();
			if (	_period!=null && _period.length()==6	&&
					strEndTime!=null && strEndTime.length()==5 &&
					strEndTimes!=null && strEndTimes.length()==5) {
				String strMinute = strEndTime.substring(0, 2);
				String strSecond = strEndTime.substring(3, 5);
				int minute = Integer.valueOf(strMinute);
				int second = Integer.valueOf(strSecond);
				int sealTime = minute*60+second;
				
				strMinute = strEndTimes.substring(0, 2);
				strSecond = strEndTimes.substring(3, 5);
				minute = Integer.valueOf(strMinute);
				second = Integer.valueOf(strSecond);
				int openTime = minute*60+second;
				
				if (!info.getPeriod().equals(_period)) {
					info.setAmount(CoinUtil.zero);
				}

				info.setPeriod(_period);
				info.setSealtime(sealTime);
				info.setOpentime(openTime);
				
				if (!period.equals(_period))
					return new ErrorCode(Code.BET_FAIL,null);
				if (sealTime<5)
					return new ErrorCode(Code.BET_FAIL_CAUSE_SEAL,openTime+8);
			}
			
			//投注
			boolean isFillin = false;
			if (ctypep.equals(CTYPEP2)) {
				isFillin = fillin2(data);
			} else if (ctypep.equals(CTYPEP3)) {
				isFillin = fillin3(data);
			} else if (ctypep.equals(CTYPEP4)) {
				isFillin = fillin4(data);
			}
			if (!isFillin)
				return new ErrorCode(Code.BET_FAIL,null);
			
			WebElement submits = driver.findElement(By.id("submits")); 	//提交
			submits.click();
			Thread.sleep(500);
			Alert alert = driver.switchTo().alert();
			if (alert==null)
				return new ErrorCode(Code.BET_FAIL,null);
			alert.accept();
			Thread.sleep(1000);
			
			BigDecimal _amount = CoinUtil.zero;
			for (Pk10Info node : data) {
				_amount = CoinUtil.add(_amount, node.getAllAmount());
			}
			BigDecimal aamount = info.getAmount();
			aamount = CoinUtil.add(aamount, _amount);
			
			info.setAmount(aamount);
			this.count = 0;
			
			try {
				//再次获取余额
				driver.switchTo().defaultContent();
				topFrame = driver.findElement(By.id("topFrame"));
				driver.switchTo().frame(topFrame);
				Thread.sleep(500);
				Submit = driver.findElement(By.id("bST_6"));
				Submit.click();
				Thread.sleep(500);
				webCtypep = driver.findElement(By.id(ctypep));
				webCtypep.click();
				Thread.sleep(1000);
				
				BigDecimal _balance = CoinUtil.zero;
				driver.switchTo().defaultContent();
				leftFrame = driver.findElement(By.id("leftFrame"));
				driver.switchTo().frame(leftFrame);
				Thread.sleep(500);
				webAmount = driver.findElement(By.id("currentCredits"));
				strAmount = webAmount.getText();
				if (strAmount!=null && strAmount.length()>0) {
					strAmount = strAmount.replaceAll(",", "");
					_balance = CoinUtil.createNew(strAmount);
					_balance = CoinUtil.sub(_balance, CoinUtil.createNew("1"));
					info.setBalance(_balance);
				} else {
					return new ErrorCode(Code.BET_SUCC_BUT_RETURN_ERROR,null);
				}
				
				BigDecimal _tmpMoney = CoinUtil.sub(balance, _amount);
				BigDecimal _lowMoney = CoinUtil.sub(_balance, CoinUtil.createNew("0.5"));
				BigDecimal _highMoney = CoinUtil.add(_balance, CoinUtil.createNew("0.5"));
				if (CoinUtil.compare(_tmpMoney, _lowMoney)<0 || CoinUtil.compare(_tmpMoney, _highMoney)>0) {
					aamount = CoinUtil.sub(aamount, _amount);
					info.setAmount(aamount);
					return new ErrorCode(Code.BET_FAIL,null);
				}
				
				
			} catch (Exception e) {
				 return new ErrorCode(Code.BET_SUCC_BUT_RETURN_ERROR,null);
		     }
			
			
			return new ErrorCode(Code.BET_SUCC,null); 
		 } catch (Exception e) {
			if (++count>3)
				isOK = false;
			 return new ErrorCode(Code.BET_FAIL,null);
	     }
	}

	//冠亚 组合
	private boolean fillin2(List<Pk10Info> data) {
		for (Pk10Info info : data) {
			int runway = info.getRunway();
			if (runway==11) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t12_h1"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t12_h2"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t12_h3"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t12_h4"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 3:
							str = "t11_h1";
							break;
						case 4:
							str = "t11_h2";
							break;
						case 5:
							str = "t11_h3";
							break;
						case 6:
							str = "t11_h4";
							break;
						case 7:
							str = "t11_h5";
							break;
						case 8:
							str = "t11_h6";
							break;
						case 9:
							str = "t11_h7";
							break;
						case 10:
							str = "t11_h8";
							break;
						case 11:
							str = "t11_h9";
							break;
						case 12:
							str = "t11_h10";
							break;
						case 13:
							str = "t11_h11";
							break;
						case 14:
							str = "t11_h12";
							break;
						case 15:
							str = "t11_h13";
							break;
						case 16:
							str = "t11_h14";
							break;
						case 17:
							str = "t11_h15";
							break;
						case 18:
							str = "t11_h16";
							break;
						case 19:
							str = "t11_h17";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			} else if (runway==1) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t1_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t1_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t1_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t1_h14"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t1_h15"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t1_h16"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 1:
							str = "t1_h1";
							break;
						case 2:
							str = "t1_h2";
							break;
						case 3:
							str = "t1_h3";
							break;
						case 4:
							str = "t1_h4";
							break;
						case 5:
							str = "t1_h5";
							break;
						case 6:
							str = "t1_h6";
							break;
						case 7:
							str = "t1_h7";
							break;
						case 8:
							str = "t1_h8";
							break;
						case 9:
							str = "t1_h9";
							break;
						case 10:
							str = "t1_h10";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			} else if (runway==2) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t2_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t2_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t2_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t2_h14"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t2_h15"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t2_h16"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 1:
							str = "t2_h1";
							break;
						case 2:
							str = "t2_h2";
							break;
						case 3:
							str = "t2_h3";
							break;
						case 4:
							str = "t2_h4";
							break;
						case 5:
							str = "t2_h5";
							break;
						case 6:
							str = "t2_h6";
							break;
						case 7:
							str = "t2_h7";
							break;
						case 8:
							str = "t2_h8";
							break;
						case 9:
							str = "t2_h9";
							break;
						case 10:
							str = "t2_h10";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			} else {
				return false;
			}
		}
		
		return true;
	}

	//三四五六
	private boolean fillin3(List<Pk10Info> data) {
		for (Pk10Info info : data) {
			int runway = info.getRunway();
			if (runway==3) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t3_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t3_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t3_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t3_h14"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t3_h15"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t3_h16"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
							money = CoinUtil.Round(money);
							String str = null;
							switch (index) {
							case 1:
								str = "t3_h1";
								break;
							case 2:
								str = "t3_h2";
								break;
							case 3:
								str = "t3_h3";
								break;
							case 4:
								str = "t3_h4";
								break;
							case 5:
								str = "t3_h5";
								break;
							case 6:
								str = "t3_h6";
								break;
							case 7:
								str = "t3_h7";
								break;
							case 8:
								str = "t3_h8";
								break;
							case 9:
								str = "t3_h9";
								break;
							case 10:
								str = "t3_h10";
								break;
							}
							WebElement webElement = driver.findElement(By.name(str));
							webElement.sendKeys(money.toString());
						}
					}	
				} else if (runway==4) {
					BigDecimal money = info.getBig();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t4_h11"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSmall();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t4_h12"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSingle();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t4_h13"));
						webElement.sendKeys(money.toString());
					}
					money = info.getDou();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t4_h14"));
						webElement.sendKeys(money.toString());
					}
					money = info.getDragon();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t4_h15"));
						webElement.sendKeys(money.toString());
					}
					money = info.getTiger();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t4_h16"));
						webElement.sendKeys(money.toString());
					}
					List<Pk10Node> arr = info.getArrNum();
					for (Pk10Node node : arr) {
						int index = node.getIndex();
						money = node.getAmount();
						if (CoinUtil.compareZero(money)>0) {
							money = CoinUtil.Round(money);
							String str = null;
							switch (index) {
							case 1:
								str = "t4_h1";
								break;
							case 2:
								str = "t4_h2";
								break;
							case 3:
								str = "t4_h3";
								break;
							case 4:
								str = "t4_h4";
								break;
							case 5:
								str = "t4_h5";
								break;
							case 6:
								str = "t4_h6";
								break;
							case 7:
								str = "t4_h7";
								break;
							case 8:
								str = "t4_h8";
								break;
							case 9:
								str = "t4_h9";
								break;
							case 10:
								str = "t4_h10";
								break;
							}
							WebElement webElement = driver.findElement(By.name(str));
							webElement.sendKeys(money.toString());
						}
					}
			} else if (runway==5) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t5_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t5_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t5_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t5_h14"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t5_h15"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t5_h16"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 1:
							str = "t5_h1";
							break;
						case 2:
							str = "t5_h2";
							break;
						case 3:
							str = "t5_h3";
							break;
						case 4:
							str = "t5_h4";
							break;
						case 5:
							str = "t5_h5";
							break;
						case 6:
							str = "t5_h6";
							break;
						case 7:
							str = "t5_h7";
							break;
						case 8:
							str = "t5_h8";
							break;
						case 9:
							str = "t5_h9";
							break;
						case 10:
							str = "t5_h10";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			} else if (runway==6) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t6_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t6_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t6_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t6_h14"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 1:
							str = "t6_h1";
							break;
						case 2:
							str = "t6_h2";
							break;
						case 3:
							str = "t6_h3";
							break;
						case 4:
							str = "t6_h4";
							break;
						case 5:
							str = "t6_h5";
							break;
						case 6:
							str = "t6_h6";
							break;
						case 7:
							str = "t6_h7";
							break;
						case 8:
							str = "t6_h8";
							break;
						case 9:
							str = "t6_h9";
							break;
						case 10:
							str = "t6_h10";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			}
		}
		return true;
	}

	//七八九十
	private boolean fillin4(List<Pk10Info> data) {
		for (Pk10Info info : data) {
			int runway = info.getRunway();
			if (runway==7) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t7_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t7_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t7_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t7_h14"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
							money = CoinUtil.Round(money);
							String str = null;
							switch (index) {
							case 1:
								str = "t7_h1";
								break;
							case 2:
								str = "t7_h2";
								break;
							case 3:
								str = "t7_h3";
								break;
							case 4:
								str = "t7_h4";
								break;
							case 5:
								str = "t7_h5";
								break;
							case 6:
								str = "t7_h6";
								break;
							case 7:
								str = "t7_h7";
								break;
							case 8:
								str = "t7_h8";
								break;
							case 9:
								str = "t7_h9";
								break;
							case 10:
								str = "t7_h10";
								break;
							}
							WebElement webElement = driver.findElement(By.name(str));
							webElement.sendKeys(money.toString());
						}
					}	
				} else if (runway==8) {
					BigDecimal money = info.getBig();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t8_h11"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSmall();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t8_h12"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSingle();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t8_h13"));
						webElement.sendKeys(money.toString());
					}
					money = info.getDou();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("t8_h14"));
						webElement.sendKeys(money.toString());
					}
					List<Pk10Node> arr = info.getArrNum();
					for (Pk10Node node : arr) {
						int index = node.getIndex();
						money = node.getAmount();
						if (CoinUtil.compareZero(money)>0) {
							money = CoinUtil.Round(money);
							String str = null;
							switch (index) {
							case 1:
								str = "t8_h1";
								break;
							case 2:
								str = "t8_h2";
								break;
							case 3:
								str = "t8_h3";
								break;
							case 4:
								str = "t8_h4";
								break;
							case 5:
								str = "t8_h5";
								break;
							case 6:
								str = "t8_h6";
								break;
							case 7:
								str = "t8_h7";
								break;
							case 8:
								str = "t8_h8";
								break;
							case 9:
								str = "t8_h9";
								break;
							case 10:
								str = "t8_h10";
								break;
							}
							WebElement webElement = driver.findElement(By.name(str));
							webElement.sendKeys(money.toString());
						}
					}
			} else if (runway==9) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t9_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t9_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t9_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t9_h14"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 1:
							str = "t9_h1";
							break;
						case 2:
							str = "t9_h2";
							break;
						case 3:
							str = "t9_h3";
							break;
						case 4:
							str = "t9_h4";
							break;
						case 5:
							str = "t9_h5";
							break;
						case 6:
							str = "t9_h6";
							break;
						case 7:
							str = "t9_h7";
							break;
						case 8:
							str = "t9_h8";
							break;
						case 9:
							str = "t9_h9";
							break;
						case 10:
							str = "t9_h10";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			} else if (runway==10) {
				BigDecimal money = info.getBig();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t10_h11"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t10_h12"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t10_h13"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("t10_h14"));
					webElement.sendKeys(money.toString());
				}
				List<Pk10Node> arr = info.getArrNum();
				for (Pk10Node node : arr) {
					int index = node.getIndex();
					money = node.getAmount();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						String str = null;
						switch (index) {
						case 1:
							str = "t10_h1";
							break;
						case 2:
							str = "t10_h2";
							break;
						case 3:
							str = "t10_h3";
							break;
						case 4:
							str = "t10_h4";
							break;
						case 5:
							str = "t10_h5";
							break;
						case 6:
							str = "t10_h6";
							break;
						case 7:
							str = "t10_h7";
							break;
						case 8:
							str = "t10_h8";
							break;
						case 9:
							str = "t10_h9";
							break;
						case 10:
							str = "t10_h10";
							break;
						}
						WebElement webElement = driver.findElement(By.name(str));
						webElement.sendKeys(money.toString());
					}
				}
			}
		}
		return true;
	}
	
	
	//页面元素截图
	private static File captureElement(WebElement element) throws Exception {
		WrapsDriver wrapsDriver = (WrapsDriver) element;
		// 截图整个页面
		File screen = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
		BufferedImage img = ImageIO.read(screen);
		// 获得元素的高度和宽度
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();
		// 创建一个矩形使用上面的高度，和宽度
		Rectangle rect = new Rectangle(width, height);
		// 得到元素的坐标
		Point p = element.getLocation();
		BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width,rect.height);
		//存为png格式
		ImageIO.write(dest, "png", screen);
		return screen;
	}
}
