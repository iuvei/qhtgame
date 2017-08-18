// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//date比较大小
function comparamDate(beginDate,endDate) {
	if(beginDate=="" || endDate=="") {
		//alert("请输入时间");
		return false;
	}
	
	var d1 = new Date(beginDate.replace(/\-/g,"V"));
	var d2 = new Date(endDate.replace(/\-/g,"V"));
	if(d1>=d2) {
		//alert("开始时间不能大于结束时间");
		return false;
	}
	
	return true;
}


//按YYYY/MM/dd HH:mm:ss显示时间
function formatTime(timestamp) {
	var da = new Date(timestamp);
	var year = da.getFullYear();
	var month = da.getMonth()+1;
	var day = da.getDate();
	var hour = da.getHours();
	var minute = da.getMinutes();
	var second = da.getSeconds();
	var strDate = year+"/"+month+"/"+day+" "+hour+":"+minute+":"+second;
	return strDate;
}

//将时间控件格式转换成YYYY-MM-DD HH:mm:ss
function formatDatatime_local(str) {
	year=str.substring(0,4);
    month=str.substring(5,7);
    day=str.substring(8,10);
    hour=str.substring(11,13);
    minute=str.substring(14);
	return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+"00";
}

//将时间控件格式转换成YYYY-MM-DD 00:00:00
function formatDatatime_localone(str) {
	year=str.substring(0,4);
    month=str.substring(5,7);
    day=str.substring(8,10);
    hour=str.substring(11,13);
    minute=str.substring(14);
	return year+"-"+month+"-"+day+" "+"00"+":"+"00"+":"+"00";
}

//将时间控件格式转换成YYYY-MM-DD 23:59:59
function formatDatatime_localtwo(str) {
	year=str.substring(0,4);
    month=str.substring(5,7);
    day=str.substring(8,10);
    hour=str.substring(11,13);
    minute=str.substring(14);
	return year+"-"+month+"-"+day+" "+"23"+":"+"59"+":"+"59";
}

//将时间控件格式转换成YYYY-MM-DD
function formatData(str) {
	year=str.substring(0,4);
    month=str.substring(5,7);
    day=str.substring(8,10);
	return year+"-"+month+"-"+day;
}

//将时间控件格式转换成YYYY-MM-DD
function formatDataInt(str) {
	year=str.substring(0,4);
    month=str.substring(5,7);
    day=str.substring(8,10);
	var str = year+month+day;
	return parseInt(str);
}

function baseCBErr(code,desc) {
	if (code==-1 || code==104) {
		if (code==-1) {

		} else {
			var ts = document.getElementById("alert");
			ts.style.display = "block";
			setTimeout(setTimeOut,5000);
			//window.open(getBackURL(),'_parent') ;
		}
	} else {
		alert(code+"-"+desc);
	}
}
function setTimeOut(){
	var ts = document.getElementById("alert");
	ts.style.display = "none";
	window.open(getBackURL(),'_parent') ;
}
function _send(url,param,cbOk,cbErr) {
	$.ajax({
		data:param,
		type:"POST",
		dataType:"json",
		url:url,
		xhrFields:{withCredentials: true},
		error:function(data){
			cbErr(-1,"return error");	//回调
		},
		success:function(data) {
			var code = data.code;
			var desc = data.desc;
			if (code!=1000) {
				cbErr(code,desc);	//回调
				return;
			}
			
			var info = data.info;
			cbOk(info);	//回调
		}
	});
}