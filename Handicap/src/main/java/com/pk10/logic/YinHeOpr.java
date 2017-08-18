package com.pk10.logic;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class YinHeOpr implements Pk10WebInter {
	
	private int id;
	
	private WebDriver driver;
	private Pk10CurNode info;
	private boolean isOK;
	private int count;
	
	public YinHeOpr(int id) {
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
			driver.switchTo().defaultContent();
			WebElement topFrame = driver.findElement(By.name("topFrame"));
			driver.switchTo().frame(topFrame);
			
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
			//ValidateCode.click();
			ValidateImage.click();
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
			
			WebElement Submit = driver.findElement(By.id("MSubmit"));
			Submit.click();
			
			Thread.sleep(1000);
			Submit = driver.findElement(By.className("aui_state_highlight"));
			Submit.click();
			
			Thread.sleep(1000);
			Submit = driver.findElement(By.id("Submit"));
			Submit.click();
			
			Thread.sleep(2000);
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			Thread.sleep(500);
			jse.executeScript("bcyx(this,400)");
			Thread.sleep(3000);
			
			driver.switchTo().defaultContent();
			WebElement topifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(topifr_Index);
			WebElement topFrame = driver.findElement(By.id("topFrame"));
			driver.switchTo().frame(topFrame);
			jse.executeScript("goto_CI(2,'bjpk10_dq_1_2')");
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
			WebElement ifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(ifr_Index);
			WebElement topFrame = driver.findElement(By.id("topFrame"));
			driver.switchTo().frame(topFrame);
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			Thread.sleep(500);
			jse.executeScript("goto_CI(2,'bjpk10_dq_1_2')");
			Thread.sleep(500);
			
			driver.switchTo().defaultContent();
			ifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(ifr_Index);
			WebElement leftFrame = driver.findElement(By.id("leftFrame"));
			driver.switchTo().frame(leftFrame);
			Thread.sleep(500);
			WebElement webAmount = driver.findElement(By.id("Money_KY"));
			String strAmount = webAmount.getText();
			if (strAmount!=null && strAmount.length()>0) {
				BigDecimal balance = CoinUtil.createNew(strAmount);
				balance = CoinUtil.sub(balance, CoinUtil.createNew("1"));
				info.setBalance(balance);
			} else {
				return false;
			}

			//获取期号、离封盘时间、离开奖时间
			driver.switchTo().defaultContent();
			ifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(ifr_Index);
			WebElement mainFrame = driver.findElement(By.id("mainFrame"));
			driver.switchTo().frame(mainFrame);
			Thread.sleep(500);
			WebElement webO = driver.findElement(By.id("k_qs"));
			String strO = webO.getText();
			WebElement webEndTime = driver.findElement(By.id("sp_lTime"));
			String strEndTime = webEndTime.getText();
			WebElement webEndTimes = driver.findElement(By.id("sp_cTime"));
			String strEndTimes = webEndTimes.getText();
			if (	strO!=null && strO.length()==6	&&
					strEndTime!=null && strEndTime.length()==8 &&
					strEndTimes!=null && strEndTimes.length()==8) {
				String strhour = strEndTime.substring(0, 2);
				String strMinute = strEndTime.substring(3, 5);
				String strSecond = strEndTime.substring(6, 8);
				int hour = Integer.valueOf(strhour);
				int minute = Integer.valueOf(strMinute);
				int second = Integer.valueOf(strSecond);
				int openTime = hour*3600+minute*60+second;
				
				strhour = strEndTimes.substring(0, 2);
				strMinute = strEndTimes.substring(3, 5);
				strSecond = strEndTimes.substring(6, 8);
				hour = Integer.valueOf(strhour);
				minute = Integer.valueOf(strMinute);
				second = Integer.valueOf(strSecond);
				int sealTime = hour*3600+minute*60+second;
				
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
			String f = null;
			if (ctypep.equals(Pk10WebInter.CTYPEP2))
				f = "goto_CI(2,'bjpk10_dq_1_2')";
			else if (ctypep.equals(Pk10WebInter.CTYPEP3))
				f = "goto_CI(3,'bjpk10_dq_3_6')";
			else if (ctypep.equals(Pk10WebInter.CTYPEP4))
				f = "goto_CI(4,'bjpk10_dq_7_10')";
			driver.switchTo().defaultContent();
			WebElement ifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(ifr_Index);
			WebElement topFrame = driver.findElement(By.id("topFrame"));
			driver.switchTo().frame(topFrame);
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			Thread.sleep(500);
			jse.executeScript(f);
			Thread.sleep(500);
			
			//获取余额
			BigDecimal balance = CoinUtil.zero;
			driver.switchTo().defaultContent();
			ifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(ifr_Index);
			WebElement leftFrame = driver.findElement(By.id("leftFrame"));
			driver.switchTo().frame(leftFrame);
			Thread.sleep(500);
			WebElement webAmount = driver.findElement(By.id("Money_KY"));
			String strAmount = webAmount.getText();
			if (strAmount!=null && strAmount.length()>0) {
				balance = CoinUtil.createNew(strAmount);
				balance = CoinUtil.sub(balance, CoinUtil.createNew("1"));
				info.setBalance(balance);
			} else {
				return new ErrorCode(Code.BET_FAIL,null);
			}
			
			
			//获取期号、离封盘时间、离开奖时间
			driver.switchTo().defaultContent();
			ifr_Index = driver.findElement(By.id("ifr_Index"));
			driver.switchTo().frame(ifr_Index);
			WebElement mainFrame = driver.findElement(By.id("mainFrame"));
			driver.switchTo().frame(mainFrame);
			Thread.sleep(500);
			WebElement webO = driver.findElement(By.id("k_qs"));
			String strO = webO.getText();
			WebElement webEndTime = driver.findElement(By.id("sp_lTime"));
			String strEndTime = webEndTime.getText();
			WebElement webEndTimes = driver.findElement(By.id("sp_cTime"));
			String strEndTimes = webEndTimes.getText();
			if (	strO!=null && strO.length()==6	&&
					strEndTime!=null && strEndTime.length()==8 &&
					strEndTimes!=null && strEndTimes.length()==8) {
				String strhour = strEndTime.substring(0, 2);
				String strMinute = strEndTime.substring(3, 5);
				String strSecond = strEndTime.substring(6, 8);
				int hour = Integer.valueOf(strhour);
				int minute = Integer.valueOf(strMinute);
				int second = Integer.valueOf(strSecond);
				int openTime = hour*3600+minute*60+second;
				
				strhour = strEndTimes.substring(0, 2);
				strMinute = strEndTimes.substring(3, 5);
				strSecond = strEndTimes.substring(6, 8);
				hour = Integer.valueOf(strhour);
				minute = Integer.valueOf(strMinute);
				second = Integer.valueOf(strSecond);
				int sealTime = hour*3600+minute*60+second;
				
				if (!info.getPeriod().equals(strO)) {
					info.setAmount(CoinUtil.zero);
				}
				
				info.setPeriod(strO);
				info.setSealtime(sealTime);
				info.setOpentime(openTime);
				
				if (!period.equals(strO))
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
			
			WebElement submits = driver.findElement(By.id("confirm")); 	//提交
			submits.click();
			Thread.sleep(500);
			submits = driver.findElement(By.id("_ButtonOK_div_order")); 	//提交
			submits.click();
			Thread.sleep(500);
			
			BigDecimal _amount = CoinUtil.zero;
			for (Pk10Info node : data) {
				_amount = CoinUtil.add(_amount, node.getAllAmount());
			}
			BigDecimal aamount = info.getAmount();
			aamount = CoinUtil.add(aamount, _amount);
			
			this.count = 0;
			info.setAmount(aamount);
			
			try {
				//再次获取余额
				BigDecimal _balance = CoinUtil.zero;
				driver.switchTo().defaultContent();
				ifr_Index = driver.findElement(By.id("ifr_Index"));
				driver.switchTo().frame(ifr_Index);
				leftFrame = driver.findElement(By.id("leftFrame"));
				driver.switchTo().frame(leftFrame);
				Thread.sleep(500);
				webAmount = driver.findElement(By.id("Money_KY"));
				strAmount = webAmount.getText();
				if (strAmount!=null && strAmount.length()>0) {
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40152"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40153"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40150"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40151"));
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
							str = "jeuM_0_40154";
							break;
						case 4:
							str = "jeuM_0_40155";
							break;
						case 5:
							str = "jeuM_0_40156";
							break;
						case 6:
							str = "jeuM_0_40157";
							break;
						case 7:
							str = "jeuM_0_40158";
							break;
						case 8:
							str = "jeuM_0_40159";
							break;
						case 9:
							str = "jeuM_0_40160";
							break;
						case 10:
							str = "jeuM_0_40161";
							break;
						case 11:
							str = "jeuM_0_40162";
							break;
						case 12:
							str = "jeuM_0_40163";
							break;
						case 13:
							str = "jeuM_0_40164";
							break;
						case 14:
							str = "jeuM_0_40165";
							break;
						case 15:
							str = "jeuM_0_40166";
							break;
						case 16:
							str = "jeuM_0_40167";
							break;
						case 17:
							str = "jeuM_0_40168";
							break;
						case 18:
							str = "jeuM_0_40169";
							break;
						case 19:
							str = "jeuM_0_40170";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40010"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40011"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40012"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40013"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40014"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40015"));
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
							str = "jeuM_0_40000";
							break;
						case 2:
							str = "jeuM_0_40001";
							break;
						case 3:
							str = "jeuM_0_40002";
							break;
						case 4:
							str = "jeuM_0_40003";
							break;
						case 5:
							str = "jeuM_0_40004";
							break;
						case 6:
							str = "jeuM_0_40005";
							break;
						case 7:
							str = "jeuM_0_40006";
							break;
						case 8:
							str = "jeuM_0_40007";
							break;
						case 9:
							str = "jeuM_0_40008";
							break;
						case 10:
							str = "jeuM_0_40009";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40026"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40027"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40028"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40029"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40030"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40031"));
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
							str = "jeuM_0_40016";
							break;
						case 2:
							str = "jeuM_0_40017";
							break;
						case 3:
							str = "jeuM_0_40018";
							break;
						case 4:
							str = "jeuM_0_40019";
							break;
						case 5:
							str = "jeuM_0_40020";
							break;
						case 6:
							str = "jeuM_0_40021";
							break;
						case 7:
							str = "jeuM_0_40022";
							break;
						case 8:
							str = "jeuM_0_40023";
							break;
						case 9:
							str = "jeuM_0_40024";
							break;
						case 10:
							str = "jeuM_0_40025";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40042"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40043"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40044"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40045"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40046"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40047"));
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
								str = "jeuM_0_40032";
								break;
							case 2:
								str = "jeuM_0_40033";
								break;
							case 3:
								str = "jeuM_0_40034";
								break;
							case 4:
								str = "jeuM_0_40035";
								break;
							case 5:
								str = "jeuM_0_40036";
								break;
							case 6:
								str = "jeuM_0_40037";
								break;
							case 7:
								str = "jeuM_0_40038";
								break;
							case 8:
								str = "jeuM_0_40039";
								break;
							case 9:
								str = "jeuM_0_40040";
								break;
							case 10:
								str = "jeuM_0_40041";
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
						WebElement webElement = driver.findElement(By.name("jeuM_0_40058"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSmall();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40059"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSingle();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40060"));
						webElement.sendKeys(money.toString());
					}
					money = info.getDou();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40061"));
						webElement.sendKeys(money.toString());
					}
					money = info.getDragon();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40062"));
						webElement.sendKeys(money.toString());
					}
					money = info.getTiger();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40063"));
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
								str = "jeuM_0_40048";
								break;
							case 2:
								str = "jeuM_0_40049";
								break;
							case 3:
								str = "jeuM_0_40050";
								break;
							case 4:
								str = "jeuM_0_40051";
								break;
							case 5:
								str = "jeuM_0_40052";
								break;
							case 6:
								str = "jeuM_0_40053";
								break;
							case 7:
								str = "jeuM_0_40054";
								break;
							case 8:
								str = "jeuM_0_40055";
								break;
							case 9:
								str = "jeuM_0_40056";
								break;
							case 10:
								str = "jeuM_0_40057";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40074"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40075"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40076"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40077"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDragon();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40078"));
					webElement.sendKeys(money.toString());
				}
				money = info.getTiger();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40079"));
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
							str = "jeuM_0_40064";
							break;
						case 2:
							str = "jeuM_0_40065";
							break;
						case 3:
							str = "jeuM_0_40066";
							break;
						case 4:
							str = "jeuM_0_40067";
							break;
						case 5:
							str = "jeuM_0_40068";
							break;
						case 6:
							str = "jeuM_0_40069";
							break;
						case 7:
							str = "jeuM_0_40070";
							break;
						case 8:
							str = "jeuM_0_40071";
							break;
						case 9:
							str = "jeuM_0_40072";
							break;
						case 10:
							str = "jeuM_0_40073";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40090"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40091"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40092"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40093"));
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
							str = "jeuM_0_40080";
							break;
						case 2:
							str = "jeuM_0_40081";
							break;
						case 3:
							str = "jeuM_0_40082";
							break;
						case 4:
							str = "jeuM_0_40083";
							break;
						case 5:
							str = "jeuM_0_40084";
							break;
						case 6:
							str = "jeuM_0_40085";
							break;
						case 7:
							str = "jeuM_0_40086";
							break;
						case 8:
							str = "jeuM_0_40087";
							break;
						case 9:
							str = "jeuM_0_40088";
							break;
						case 10:
							str = "jeuM_0_40089";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40104"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40105"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40106"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40107"));
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
								str = "jeuM_0_40094";
								break;
							case 2:
								str = "jeuM_0_40095";
								break;
							case 3:
								str = "jeuM_0_40096";
								break;
							case 4:
								str = "jeuM_0_40097";
								break;
							case 5:
								str = "jeuM_0_40098";
								break;
							case 6:
								str = "jeuM_0_40099";
								break;
							case 7:
								str = "jeuM_0_40100";
								break;
							case 8:
								str = "jeuM_0_40101";
								break;
							case 9:
								str = "jeuM_0_40102";
								break;
							case 10:
								str = "jeuM_0_40103";
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
						WebElement webElement = driver.findElement(By.name("jeuM_0_40118"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSmall();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40119"));
						webElement.sendKeys(money.toString());
					}
					money = info.getSingle();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40120"));
						webElement.sendKeys(money.toString());
					}
					money = info.getDou();
					if (CoinUtil.compareZero(money)>0) {
						money = CoinUtil.Round(money);
						WebElement webElement = driver.findElement(By.name("jeuM_0_40121"));
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
								str = "jeuM_0_40108";
								break;
							case 2:
								str = "jeuM_0_40109";
								break;
							case 3:
								str = "jeuM_0_40110";
								break;
							case 4:
								str = "jeuM_0_40111";
								break;
							case 5:
								str = "jeuM_0_40112";
								break;
							case 6:
								str = "jeuM_0_40113";
								break;
							case 7:
								str = "jeuM_0_40114";
								break;
							case 8:
								str = "jeuM_0_40115";
								break;
							case 9:
								str = "jeuM_0_40116";
								break;
							case 10:
								str = "jeuM_0_40117";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40132"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40133"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40134"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40135"));
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
							str = "jeuM_0_40122";
							break;
						case 2:
							str = "jeuM_0_40123";
							break;
						case 3:
							str = "jeuM_0_40124";
							break;
						case 4:
							str = "jeuM_0_40125";
							break;
						case 5:
							str = "jeuM_0_40126";
							break;
						case 6:
							str = "jeuM_0_40127";
							break;
						case 7:
							str = "jeuM_0_40128";
							break;
						case 8:
							str = "jeuM_0_40129";
							break;
						case 9:
							str = "jeuM_0_40130";
							break;
						case 10:
							str = "jeuM_0_40131";
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
					WebElement webElement = driver.findElement(By.name("jeuM_0_40146"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSmall();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40147"));
					webElement.sendKeys(money.toString());
				}
				money = info.getSingle();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40148"));
					webElement.sendKeys(money.toString());
				}
				money = info.getDou();
				if (CoinUtil.compareZero(money)>0) {
					money = CoinUtil.Round(money);
					WebElement webElement = driver.findElement(By.name("jeuM_0_40149"));
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
							str = "jeuM_0_40136";
							break;
						case 2:
							str = "jeuM_0_40137";
							break;
						case 3:
							str = "jeuM_0_40138";
							break;
						case 4:
							str = "jeuM_0_40139";
							break;
						case 5:
							str = "jeuM_0_40140";
							break;
						case 6:
							str = "jeuM_0_40141";
							break;
						case 7:
							str = "jeuM_0_40142";
							break;
						case 8:
							str = "jeuM_0_40143";
							break;
						case 9:
							str = "jeuM_0_40144";
							break;
						case 10:
							str = "jeuM_0_40145";
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
