/*
 * 请求内容格式控制类
 */
package com.pk10.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.logic.mgr.DaoMgr;
import com.node.ComIntBigNode;
import com.pk10.node.Pk10BetStr;
import com.pk10.node.Pk10BetTextNode;
import com.sysconst.Code;
import com.util.CoinUtil;
import com.util.ComUtil;
import com.util.DateUtil;

import qht.game.dao.Pk10SymbolDao;
import qht.game.node.Pk10InfoNode;
import qht.game.node.Pk10SymbolNode;

public class Pk10TextMgr {
	
	private static List<Pk10SymbolNode> arrSynbol;
	
	private static final int TYPE0 = 0;	//错误	大小单双龙虎12345678910111213141516171819
	private static final int TYPE1 = 1;	//11	大小单双345678910111213141516171819
	private static final int TYPE2 = 2;	//1~5	大小单双12345678910龙虎
	private static final int TYPE3 = 3;	//1~10	大小单双12345678910
	private static final int TYPE4 = 4;	//1~11 	大小单双
	
	public static boolean init() {
		Pk10SymbolDao dao = DaoMgr.getPk10Symbol();
		arrSynbol = dao.selectList();
		if (arrSynbol==null)
			return false;
		return true;
	}
	
	public static List<Pk10SymbolNode> get() {
		return arrSynbol;
	}
	
	public static String preDeal(String text) {
		text = text.trim();
		for (Pk10SymbolNode node : arrSynbol) {
			if (node.getTag()==Code.STATUS_EFFECTIVE) {
				String symbol = node.getSymbol();
				if (symbol.equals(".")) {
					text = text.replaceAll("\\.", "\\|");
				} else if (symbol.length()==0) {
					text = text.replaceAll(" ", "\\|");
					text = text.replaceAll("\t", "\\|");
				} else {
					text = text.replaceAll(symbol, "\\|");
				}
			}
		}
		return text;
	}
	

	public static boolean isUpDown(String text) {
		if (text.length()==0)
			return false;
		String str = text.substring(0,1);
		if (	str.equals("上") || 
				str.equals("下") ||
				str.equals("查") ||
				str.equals("回"))
			return true;
		if (text.length()<2)
			return false;
		str = text.substring(0,2);
		if ( 	str.equals("上分") ||
				str.equals("下发"))
			return true;
		return false;
	}

	public static ComIntBigNode parseUpDown(String text) {
		
		String str1 = null;
		String str2 = null;
		String[] args = text.split("\\|");
		if (args.length==1) {
			if (text.indexOf("分")==-1) {
				if (text.length()>1) {
					str1 = text.substring(0,1);
					str2 = text.substring(1,text.length());
				}
			} else {
				if (text.length()>2) {
					str1 = text.substring(0,2);
					str2 = text.substring(2,text.length());
				}
			}
			
		} else if (args.length==2) {
			str1 = args[0];
			str2 = args[1];
		} else {
			return null;
		}
		if (str1==null || str2==null)
			return null;
		
		//if (!str1.equals("上") && !str1.equals("下"))
		//	return null;
		if (!isNum(str2))
			return null;
		
		int type = 0;
		if (str1.equals("上") || str1.equals("上分") || str1.equals("查"))
			type = Code.AMOUNT_RECHANGE;
		else if (str1.equals("下") || str1.equals("下分") || str1.equals("回"))
			type = Code.AMOUNT_WITHDRAWALS;
		else
			return null;
		
		BigDecimal amount = CoinUtil.createNew(str2);
		if (amount==null || CoinUtil.compareZero(amount)<=0)
			return null;
		
		ComIntBigNode result = new ComIntBigNode();
		result.setId(type);
		result.setMoney(amount);
		
		return result;
	}

	public static Pk10BetTextNode parseBet(String text) {
		if(text==null)
			return null;

		String strRunway=null;	//跑道
		String strContent=null;	//内容
		String strMoney = null;	//金额
		String[] args = text.split("\\|");
		if (args.length==1) {
			Pk10BetStr info = getBetStr(text);
			if (info==null)
				return null;
			strRunway = info.getStrRunway();
			strContent = info.getStrContent();
			strMoney = info.getStrMoney();
		} else if (args.length==2) {
			String str1 = args[0].trim();
			String str2 = args[1].trim();
			if (str1.length()==0 || str2.length()==0)
				return null;
			String tmp = str1.substring(0, 1);
			if (tmp.equals("特")) {
				str1 = str1.substring(1,str1.length());
				strRunway = tmp;
				strContent = str1;
				strMoney = str2;
			} else {
				strRunway = "1";
				strContent = str1;
				strMoney = str2;
			}
		} else if (args.length==3) {
			strRunway = args[0].trim();
			strContent = args[1].trim();
			strMoney = args[2].trim();
		} else {
			return null;
		}
		
		return getInfo(strRunway,strContent,strMoney);
	}
	
	public static List<Pk10InfoNode> get(String period, Pk10BetTextNode info, String appcode, String username) {
		List<Integer> arrRunway = info.getArrRunway();
		List<Integer> arrContent = info.getArrContent();
		BigDecimal betmoney = info.getMoney();

		List<Pk10InfoNode> result = new ArrayList<Pk10InfoNode>();
		for (int runway : arrRunway) {
			for (int bettype : arrContent) {
				long tmpTime = DateUtil.getCurTimestamp().getTime();
				Pk10InfoNode node = new Pk10InfoNode();
				node.setOdd(ComUtil.get32UUID());
				node.setPeriod(period);
				node.setAppcode(appcode);
				node.setUsername(username);
				node.setRunway(runway);
				node.setBettype(bettype);
				node.setBetamount(betmoney);
				node.setPaidamount(CoinUtil.zero);
				node.setBettime(tmpTime);
				node.setPaidtime(0);
				node.setStatus(Code.PK10_BET);
				node.setUpdatetime(tmpTime);
				
				result.add(node);
			}
		}
		
		return result;
	}

	private static Pk10BetStr getBetStr(String text) {
		String strRunway=null;	//跑道
		String strContent=null;	//内容
		String strMoney = null;	//金额
		
		//跑道
		boolean b = true;
		if (b) {
			String tmp = text.substring(0, 1);
			if (isNum(tmp)) {
				strRunway = tmp;;
				text = text.substring(1,text.length());
				b = false;
			}
		}
		if (b) {
			String tmp = text.substring(0, 1);
			if (tmp.equals("特")) {
				strRunway = tmp;
				text = text.substring(1,text.length());
				b = false;
			}
		}
		if (b) {
			strRunway = "1";
		}
		if (text.length()<2)
			return null;
		
		
		//内容
		strContent = "";
		do {
			String tmp = text.substring(0, 1);
			if (	tmp.equals("大") ||
					tmp.equals("小") ||
					tmp.equals("单") ||
					tmp.equals("双") ||
					tmp.equals("龙") ||
					tmp.equals("虎") ) {
				strContent += tmp;
				text = text.substring(1,text.length());
			} else {
				break;
			}
		} while(text.length()>0);
		if (text.length()<1)
			return null;
		if (strContent.length()==0)
			return null;
		
		//金额
		String tmp = text;
		if (isNum(tmp)) {
			strMoney = tmp;
		} else {
			return null;
		}
		
		return new Pk10BetStr(strRunway,strContent,strMoney);
	}


	private static int getType(List<Integer> arrRunway) {
		boolean btype1 = true;
		boolean btype2 = true;
		boolean btype3 = true;
		boolean btype4 = true;
		for (int runway : arrRunway) {
			if (btype1 && runway!=11)
				btype1 = false;
			if (btype2 && (runway<1 || runway>5))
				btype2 = false;
			if (btype3 && (runway<1 || runway>10))
				btype3 = false;
			if (btype4 && (runway<1 || runway>11))
				btype4 = false;
		}
		
		if (btype1)
			return TYPE1;
		if (btype2)
			return TYPE2;
		if (btype3)
			return TYPE3;
		if (btype4)
			return TYPE4;
		return TYPE0;
	}
	
	private static Pk10BetTextNode getInfo(String strRunway,String strContent,String strMoney) {
		
		//跑道
		List<Integer> arrRunway = parseRunway(strRunway);
		if (arrRunway==null || arrRunway.size()==0)
			return null;
		
		int type = getType(arrRunway);
		if (type==TYPE0)
			return null;
		
		//金额
		BigDecimal money = parseMoney(strMoney);
		if (money==null)
			return null;
		
		//内容
		List<Integer> arrContent = parseContent(strContent,(type==TYPE1)?true:false);
		if (arrContent==null || arrContent.size()==0)
			return null;
		
		//验证是否对
		if (!check(arrContent,type))
			return null;
		
		return new Pk10BetTextNode(arrRunway,arrContent,money);
	}
	
	private static boolean check(List<Integer> arrContent, int type) {
		if (type==TYPE1) {
			//龙虎12
			for (int node : arrContent) {
				if (	node==Code.PK10_DRAGON || 
						node==Code.PK10_TIGER || 
						node==1 ||
						node==2 )
					return false;
			}
		} else if (type == TYPE2) {
			//111213141516171819
			for (int node : arrContent) {
				if (	node == 11 || 
						node == 12 || 
						node == 13 || 
						node == 14 ||
						node == 15 ||
						node == 16 || 
						node == 17 || 
						node == 18 || 
						node == 19 )
					return false;
			}
		} else if (type == TYPE3) {
			//龙虎111213141516171819
			for (int node : arrContent) {
				if (	node==Code.PK10_DRAGON || 
						node==Code.PK10_TIGER || 
						node == 11 || 
						node == 12 || 
						node == 13 || 
						node == 14 ||
						node == 15 ||
						node == 16 || 
						node == 17 || 
						node == 18 || 
						node == 19 )
					return false;
			}
		} else if (type == TYPE4) {
			//龙虎12345678910111213141516171819
			for (int node : arrContent) {
				if (	node==Code.PK10_DRAGON || 
						node==Code.PK10_TIGER || 
						node == 1 || 
						node == 2 || 
						node == 3 || 
						node == 4 ||
						node == 5 ||
						node == 6 || 
						node == 7 || 
						node == 8 || 
						node == 9 ||
						node == 10 ||
						node == 11 || 
						node == 12 || 
						node == 13 || 
						node == 14 ||
						node == 15 ||
						node == 16 || 
						node == 17 || 
						node == 18 || 
						node == 19 )
					return false;
			}
		} else {
			return false;
		}
		return true;
	}

	//金额
	private static BigDecimal parseMoney(String str) {
		Pattern pattern = Pattern.compile("[0-9\\.]+");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches())
            return null;
        
		BigDecimal money = CoinUtil.createNew(str);
		if (CoinUtil.compareZero(money)<=0)
			return null;
		return money;
	}
	
	//跑道
	private static List<Integer> parseRunway(String str) {
		
		List<Integer> arrRunway = new ArrayList<Integer>();
		while (str.length()>0) {
			int runway = 0;
			String tmp = str.substring(0, 1);
			if (tmp.equals("特")) {
				runway = 11;
			} else {
				if (!isNum(tmp))
					return null;
				runway = Integer.valueOf(tmp);
				if (runway<0 || runway>9)
					return null;
				if (runway==0)
					runway = 10;
			}
			arrRunway.add(runway);
			str = str.substring(1, str.length());
		}
		
		return arrRunway; 
	}
	

	private static List<Integer> parseContent(String str,boolean isSpecial) {
		List<Integer> listContent = new ArrayList<Integer>();
		str.replaceAll("\\s*", "");//去掉所有空格
		if (str.length()>0 && str.indexOf("大")>=0) {
			str = str.replaceAll("大", "");
			listContent.add(Code.PK10_BIG);
		}
		if (str.length()>0 && str.indexOf("小")>=0) {
			str = str.replaceAll("小", "");
			listContent.add(Code.PK10_SMALL);
		}
		if (str.length()>0 && str.indexOf("单")>=0) {
			str = str.replaceAll("单", "");
			listContent.add(Code.PK10_SINGLE);
		}
		if (str.length()>0 && str.indexOf("双")>=0) {
			str = str.replaceAll("双", "");
			listContent.add(Code.PK10_DOU);
		}
		if (str.length()>0 && str.indexOf("龙")>=0) {
			str = str.replaceAll("龙", "");
			listContent.add(Code.PK10_DRAGON);
		}
		if (str.length()>0 && str.indexOf("虎")>=0) {
			str = str.replaceAll("虎", "");
			listContent.add(Code.PK10_TIGER);
		}
		
		if (str.length()==0)
			return listContent;
		
		//判断是否全是数字
		Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches())
            return null;
        
        if (!isSpecial) {
        	for (int i=0; i<str.length(); i++) {
        		String tmp = str.substring(i, i+1);
        		int nTmp = Integer.valueOf(tmp);
        		if (nTmp==0)
        			nTmp = 10;
        		listContent.add(nTmp);
        	}
        } else {
        	for (int i=0; i<str.length(); i++) {
        		String strNum = str.substring(i, i+1);
        		if (Integer.valueOf(strNum)<3) {
	        		i++;
	        		if (i==str.length())
	        			return null;
	        		strNum += str.substring(i, i+1);
	        		if (Integer.valueOf(strNum)>19)
	        			return null;
        		}
        		listContent.add(Integer.valueOf(strNum));
        	}
        }
        
        return listContent;
	}
	
	private static boolean isNum(String str) {
		//判断是否全是数字
		Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
	}

}
