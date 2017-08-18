package com.sysconst;

public class Code {
	
	//玩家身份类别
	public static final int PLAYER_IDENTIFEY_REAL = 1;		//真实账户
	public static final int PLAYER_IDENTIFEY_VIRTUAL = 2;	//虚拟账户
	
	//返水类别
	public static final int RETURN_NO = 0;		//未返水
	public static final int RETURN_PROFIT = 1;	//当日盈亏
	public static final int RETURN_WATER = 2;	//当日流水
	public static final int RETURN_UP = 3;		//当日上分
	public static final int RETURN_DOWN = 4;	//当日下分
	
	//彩种
	public static final int LORREY_PK10 = 1;
	public static final String LORREY_PK10_NAME = "北京pk10";
	
	//pk10
	public static final int PK10_BIG = 20;		//大
	public static final int PK10_SMALL = 21;	//小
	public static final int PK10_SINGLE = 22;	//单
	public static final int PK10_DOU = 23;		//双
	public static final int PK10_DRAGON= 24;	//龙
	public static final int PK10_TIGER = 25;	//虎
	
	//PK10记录状态
	public static final int PK10_BET 	= 1;	//投注
	public static final int PK10_SETTLE = 2;	//结算
	public static final int PK10_CANCLE = 3;	//取消
	public static final int PK10_HANDICAP = 4;	//飞盘中
	
	
	//qht_system编号
	public static final int SYSTEM_REPORT = 1;
	public static final int SYSTEM_PK10REPORTCOUNT = 2;	//pk10机器在线人数
	public static final int SYSTEM_HANDICAPURL = 3;	//盘口URL
	
	//标签状态
	public static final int TAG_DEAL = 1;	//已处理
	public static final int TAG_NODEAL = 2;	//未处理
	public static final int TAG_IGNORE = 3;	//忽略
	
	//后台账号验证关健词
	public static final String ADMIN_MERCHER = "admin_mercher";
	public static final String PLAYER_MERCHER = "player_mercher";
	
	//page默认首页
	public static final int PAGEINFO_DEFAULT_PAGE 			=	1;
	public static final int PAGEINFO_DEFAULT_COUNT			=	100;
	
	//page最大页数和每页最大条数
	public static final int PAGEINFO_MAX_PAGE 			=	1000;
	public static final int PAGEINFO_MAX_COUNT			=	100;
	
	//资金变化类型
	public static final int AMOUNT_RECHANGE 	= 1;	//充值
	public static final int AMOUNT_WITHDRAWALS 	= 2;	//提现
	public static final int AMOUNT_BET 			= 3;	//压注
	public static final int AMOUNT_PAID 		= 4;	//赔付
	public static final int AMOUNT_BET_CANCLE 	= 5;	//取消压注
	
	//后台账号类型
	public static final int ADMIN_ROOT 	=	1;
	public static final int ADMIN_AGENT =	2;
	public static final int ADMIN_APP 	=	3;
	
	//后台账号状态
	public static final int STATUS_EFFECTIVE = 1;	//有效
	public static final int STATUS_UNEFFECTIVE = 2;	//无效

	public final static int 		OK_CODE		= 1000;
	public final static String 		OK_DESC		= "成功";
	
	public final static int 		PARAM_CODE  	= 101;
	public final static String 		PARAM_DESC		= "参数错误";
	
	public final static int 		CATCH_CODE  	= 102;
	public final static String 		CATCH_DESC		= "异常";
	
	public final static int 		USER_OR_PWD_CODE  		= 103;
	public final static String 		USER_OR_PWD_DESC		= "用户名或密码不对";
	
	public final static int 		NOLOGIN_CODE  	= 104;
	public final static String 		NOLOGIN_DESC	= "请登入";
	
	public final static int 		NOJURI_CODE  	= 105;
	public final static String 		NOJURI_DESC		= "您没有权限操作";
	
	public final static int 		NOACCOUNT_CODE  	= 106;
	public final static String 		NOACCOUNT_DESC		= "无效账号";
	
	public final static int 		IMGENSIGFAIL_CODE  	= 107;
	public final static String 		IMGENSIGFAIL_DESC	= "IM产生SIG出错";
	
	public final static int 		IMIMPORTACCOUNT_CODE  	= 108;
	public final static String 		IMIMPORTACCOUNT_DESC	= "IM账号导入失败";
	
	public final static int 		IMADDFRIEND_CODE  	= 109;
	public final static String 		IMADDFRIEND_DESC	= "IM加好友失败";
	
	public final static int 		IMADDGROUP_CODE  	= 110;
	public final static String 		IMADDGROUP_DESC	= "IM加群失败";
	
	public final static int 		NOTCANCELBET_CODE  	= 111;
	public final static String 		NOTCANCELBET_DESC	= "没有可取消的压注";
	
	public final static int 		NOENGHOUMONEY_CODE  	= 112;
	public final static String 		NOENGHOUMONEY_DESC	= "没有足够余额";
	
	public final static int 		OBJNOTEXIT_CODE  	= 113;
	public final static String 		OBJNOTEXIT_DESC		= "对象不存在";
	
	public final static int 		STATUS_CODE  	= 114;
	public final static String 		STATUS_DESC		= "状态不对";
	
	public final static int 		PLAYERNOTEXIT_CODE  	= 115;
	public final static String 		PLAYERNOTEXIT_DESC		= "玩家不存在";
	
	public final static int 		NOTCURPERIOD_CODE  		= 116;
	public final static String 		NOTCURPERIOD_DESC		= "不是当期数据不能取消";
	
	public final static int 		APPINFONOTEXIT_CODE  	= 117;
	public final static String 		APPINFONOTEXIT_DESC		= "APP对象不存在";
	
	public final static int 		APPINFOEXCEEDTIME_CODE  	= 118;
	public final static String 		APPINFOEXCEEDTIME_DESC		= "APP过期";
	
	public final static int 		HASSEALPLATE_CODE  		= 119;
	public final static String 		HASSEALPLATE_DESC		= "已封盘";
	
	public final static int 		USERNAMEHASEXIST_CODE  		= 120;
	public final static String 		USERNAMEHASEXIST_DESC		= "玩家名已存在";

	public final static int 		COUNTRANGE_CODE  		= 121;
	public final static String 		COUNTRANGE_DESC			= "人数超限";
	
	public final static int 		OBJHASEXIT_CODE  	= 122;
	public final static String 		OBJHASEXIT_DESC		= "对象已存在";
	
	public final static int 		HASNOTBET_CODE  		= 123;
	public final static String 		HASNOTBET_DESC		= "已经不能撤单了";
	
	public final static int 		PK10_BET_FAIL  		= 124;
	public final static String 		PK10_BET_DESC		= "北京赛车投注失败";
	

	
	   public final static int TEL_EXIST_CODE = 125;
	    public final static String TEL_EXIST_DESC = "手机号已存在";

	    public final static int SMS_FAIL_CODE = 126;
	    public final static String SMS_FAIL_DESC = "短信发送失败";

	    public final static int SMS_AUTH_FAIL_CODE = 127;
	    public final static String SMS_AUTH_FAIL_DESC = "验证码错误";

	    public final static int SMS_SEND_FAIL_CODE = 128;
	    public final static String SMS_SEND_FAIL_DESC = "请先发送手机验证码";

	    public final static int SMS_AUTH_OVERDUE_CODE = 129;
	    public final static String SMS_AUTH_OVERDUE_DESC = "验证码失效";
	
		public final static int 		PLAYERCHANGE_CODE  		= 130;
		public final static String 		PLAYERCHANGE_DESC		= "玩家余额不为零，不能转换身份";
	

}

