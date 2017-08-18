//var CLIENTIP = "119.23.125.241:8080";
//var SERVERIP = "119.23.125.241:8080";
var HAND = "119.23.125.241:8180";

//var HAND = "localhost:8180";
//
var CLIENTIP = "localhost:63342";
var SERVERIP = "localhost:8080";
//var HAND = "localhost:8180";

//var CLIENTIP = "192.168.1.133:63342";
//var SERVERIP = "192.168.1.133:8080";

//var HAND = "localhost:8180";

//var HAND = "192.168.1.101:8180";
//var CLIENTIP = "localhost:8020";
//var SERVERIP = "192.168.1.101:8080";

//var SERVERIP = "xiaoxian075.oicp.net:30926";




var GAME = "/qhtgame/";
var BACK = "/qhtback/";
var IM = "/im/index.html";
var ICAP= "/handicap/";
var mapURL = {
	"login":"admin/login.do",
	"record_bgetbetpaidbydate":"record/bgetbetpaidbydate.do",
	"finance_bselect":"finance/bselect.do",
	"finance_countselect":"finance/countselect.do",
	"finance_deal":"finance/deal.do",
	"finance_record":"finance/frecordlist.do",
	"admin_selectlist":"admin/selectlist.do",
	"admin_insertone":"admin/insertone.do",
	"admin_resetpassword":"admin/resetpassword.do",
	"admin_editnickname":"admin/editnickname.do",
	"admin_setstatus":"admin/setstatus.do",
	"player_selectlist":"player/selectlist.do",
	"player_amOrder":"player/amOrderselect.do",
	"player_update":"record/playerprofit.do",
	"player_insertone":"player/insertone.do",
	"player_resetpassword":"player/resetpassword.do",
	"player_setstatus":"player/setstatus.do",
	"player_editinfo":"player/editinfo.do",
	"player_typeid":"player/changesid.do",
	"finance_brequest":"finance/brequest.do",
	"pk10_bcanclebet":"pk10/bcanclebet.do",
	"pk10_bgetbetinfo":"webinter/selectpk10betinfo.do",
	"pk10_bgetbetPer":"webinter/selectpk10betinfoperiod.do",
	"pk10_bgetbetTime":"webinter/selectpk10betinfopast.do",
	"report_scheme":"webinter/selectpk10betinfopast.do",
	"record_bpk10opencodeselectlist":"record/bpk10opencodeselectlist.do",
	"record_bamountselectlist":"record/bamountselectlist.do",
	"record_bfinanceselectlist":"finance/fOrderselectlist.do",
	"record_upfinanceselectlist":"finance/upOrderselectlist.do",
	"record_downfinanceselectlist":"finance/downOrderselectlist.do",
	"record_bgameselectlist":"record/bgameselectlist.do",
	"record_breportselectlist":"record/breportselectlist.do",
	"system_appinfoselectlist":"system/appinfoselectlist.do",
	"system_appinfosettime":"system/appinfosettime.do",
	"system_appinfoeditwc":"system/appinfoEditwc.do",
	"system_appinfoinsertone":"system/appinfoinsertone.do",
	"system_lotteryselectlist":"system/lotteryselectlist.do",
	"system_lotterysettime":"system/lotterysettime.do",
	"system_lotteryinsert":"system/lotteryinsert.do",
	"system_lotteryrule":"system/lotteryeditrule.do",
	"pk10Prid_pridSelectlist":"pk10Prid/pridSelectlist.do",
	"pk10Prid_editinfo":"pk10Prid/editinfo.do",
	"pk10robot_getcount":"pk10robot/getcount.do",
	"pk10robot_setcount":"pk10robot/setcount.do",
	"pk10robot_selectlist":"pk10robot/selectlist.do",
	"pk10robot_editone":"pk10robot/editone.do",
	"pk10robot_delone":"pk10robot/deleteone.do",
	"pk10robot_insertone":"pk10robot/insertone.do",
	"pk10_betiniselectlist":"pk10/betiniselectlist.do",
	"pk10_betiniupdate":"pk10/betiniupdate.do",
	"pk10_Bet_Select":"pk10Prid/pk10_betselect.do",
	"pk10_Bet_edit":"pk10Prid/pk10_betedit.do",
	"pk10_pointoutselectlist":"pk10/pointoutselectlist.do",
	"pk10_pointoutinsertone":"pk10/pointoutinsertone.do",
	"pk10_pointoutedit":"pk10/pointoutedit.do",
	"pk10_pointoutdelete":"pk10/pointoutdelete.do",
	"customer_selectlist":"app/customerselectlist.do",
	"customer_editcus":"app/customerEditpwd.do",
	"return_water":"rw/selectlist.do",
	"return_change":"rw/returnwater.do",
	"set_robot":"pk10robot/selectrecord.do",
	"set_setscheme":"pk10robot/setscheme.do",
	"set_addrobot":"pk10robot/addscheme.do",
	"set_addscheme":"pk10robot/setschemeaddtext.do",
	"set_delscheme":"pk10robot/setschemedeltext.do",
	"set_scheme":"pk10robot/selectscheme.do",
	"robot_addinfo":"pk10robot/addinfo.do",
	"robot_setinfo":"pk10robot/setinfo.do",
	"robot_addtext":"pk10robot/setinfoaddtext.do",
	"robot_deltext":"pk10robot/setinfodeltext.do",
	"robot_schemenames":"pk10robot/selectschemenames.do",
	"robot_selectinfo":"pk10robot/selectinfo.do"
};

function getURL(url) {
	return "http://"+SERVERIP+GAME+mapURL[url];
}

function getIMURL() {
	return "http://"+CLIENTIP+IM;
}

function getBackURL() {
	return "http://"+CLIENTIP+BACK;
}

//飞盘
function getHandicapURL(URL) {
	return "http://"+HAND+ICAP+handURL[URL];
}

//验证码
function getChackURL() {
	return "http://"+HAND+"/handicap/check.png?rnd="+ Math.random();
}
var handURL = {
	"user_info":"getuserinfo.do",
	"url_mypp":"getcheckweb.do",
	"url_lg":"loginweb.do",
	"url_select":"getweb.do",
	"use_web":"useweb.do",
	"dont_web":"useweb.do",
	"edit_web":"editweb.do",
	"select_web":"selectdata.do"
	
	
};