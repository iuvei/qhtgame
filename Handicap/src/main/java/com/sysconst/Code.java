package com.sysconst;

public class Code {

	public static final int PK10_TYPE_HONGGUANG = 1;	//皇冠
	public static final int PK10_TYPE_YINHE = 2;		//银河
	public static final int PK10_TYPE_YONGLI = 3;		//永利
	
	
	//page默认首页
	public static final int PAGEINFO_DEFAULT_PAGE 			=	1;
	public static final int PAGEINFO_DEFAULT_COUNT			=	100;
	
	//page最大页数和每页最大条数
	public static final int PAGEINFO_MAX_PAGE 			=	1000;
	public static final int PAGEINFO_MAX_COUNT			=	100;
	
	public static final int PK10_HANDICAP_BET = 1;			//投注
	public static final int PK10_HANDICAP_SETTLE = 2;		//结算
	public static final int PK10_HANDICAP_DEALFAIL = 3;		//处理失败
	
	//投注返回
	public static final int BET_FAIL = 1;	//投注失败
	public static final int BET_SUCC = 2;	//投注成功
	public static final int BET_SUCC_BUT_RETURN_ERROR = 3;	//投注成功返回不成功
	public static final int BET_FAIL_CAUSE_SEAL = 4;	//由于封盘导致的投注失败
	
	
	public static final int PK10_STATUS_USE = 1;	//使用
	public static final int PK10_STATUS_STOP = 2;	//停用
	public static final int PK10_STATUS_LOGIN = 3;	//登入
	
	
	

	//pk10
	public static final int PK10_BIG = 20;		//大
	public static final int PK10_SMALL = 21;	//小
	public static final int PK10_SINGLE = 22;	//单
	public static final int PK10_DOU = 23;		//双
	public static final int PK10_DRAGON= 24;	//龙
	public static final int PK10_TIGER = 25;	//虎
	
	public final static int 		OK_CODE		= 1000;
	public final static String 		OK_DESC		= "成功";
	
	public final static int 		PARAM_CODE  	= 101;
	public final static String 		PARAM_DESC		= "参数错误";
	
	public final static int 		CATCH_CODE  	= 102;
	public final static String 		CATCH_DESC		= "异常";
	
	public final static int 		USER_OR_PWD_CODE  		= 103;
	public final static String 		USER_OR_PWD_DESC		= "用户名或密码不对";
	
	public final static int 		OBJECTNOTEXIT_CODE  	= 104;
	public final static String 		OBJECTNOTEXIT_DESC		= "对象不存在";
	
	public final static int 		CHECKIMG_CODE  		= 105;
	public final static String 		CHECKIMG_DESC		= "获取验证码失败";
}
