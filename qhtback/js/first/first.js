var gdata;

var mapType = {
		1:"上",
		2:"下"
};
var mapTypeID = {
		1:"真实",
		2:"虚拟"
};

$(function(){
	//只执行一次
	//var t1 = window.setTimeout(hello,1000); 
	//var t2 = window.setTimeout("hello()",3000);//使用字符串执行方法 
	//window.clearTimeout(t1);//去掉定时器
	
	//重复执行
	//var t1 = window.setInterval(hello,1000); 
	//var t2 = window.setInterval("hello()",3000); 
	//window.clearInterval(t1);//去掉定时器的方法 

	$('#water_today').attr('disabled',true);
	$('#water_yesterday').attr('disabled',true);
	$('#profit_today').attr('disabled',true);
	$('#profit_yesterday').attr('disabled',true);
	$('#profit_update').attr('disabled',true);
	$('#profit_player').attr('disabled',true);
	
	
	oddSelect();
	window.setInterval(oddSelect,10000);	//10s更新一次
	
	synReport();
	window.setInterval(synReport,300000);	//5min更新一次
});

function synReport() {
	synToday();
	synYesterday();
	playeram();
	playerupdate();
}

//获取 平台 上期盈亏
function playerupdate() {
	
	var param = {
			
	};
	_send(getURL("player_update"),param,cbupdate,baseCBErr);
}
function cbupdate(data) {
	$('#profit_update').val((data.bet_all)-(data.paid_all));
}

//获取 玩家 总余额
function playeram() {
	
	var param = {
			
	};
	_send(getURL("player_amOrder"),param,cbamOrder,baseCBErr);
}
function cbamOrder(data) {
	$('#profit_player').val(data.amup);
}

//获取当日 流水 盈亏
function synToday() {
	var today = new Date().Format("yyyy-MM-dd");
	if (today==null || today=='') {
		alert("获取当前时间出错");
		return;
	}
	today = formatDataInt(today);
	
	var param = {
			"date":today
	};
	_send(getURL("record_bgetbetpaidbydate"),param,cbSynToday,baseCBErr);
}
function cbSynToday(data) {
	$('#water_today').val(data.bet);
	$('#profit_today').val((data.bet-data.paid).toFixed(2));
}
function synYesterday() {
	var yesterday = new Date(new Date().getTime()-86400000).Format("yyyy-MM-dd");
	if (yesterday==null || yesterday=='') {
		alert("获取昨天时间出错");
		return;
	}
	yesterday = formatDataInt(yesterday);
	
	var param = {
			"date":yesterday
	};
	_send(getURL("record_bgetbetpaidbydate"),param,cbSynYesterday,baseCBErr);
}
function cbSynYesterday(data) {
	$('#water_yesterday').val(data.bet);
	$('#profit_yesterday').val((data.bet-data.paid).toFixed(2));
//	$('#profit_yesterday').val(data.bet-data.paid);
}

function oddSelect() {
	var param = {};
	_send(getURL("finance_bselect"),param,cbOddSelect,baseCBErr);
}
function cbOddClear() {
	if (gdata!=null) {
		for (var i=0; i < gdata.length; i++) {
			$("tr[id='"+gdata[i].id+"']").remove();
		}
	}
	gdata=null;
}
function cbOddSelect(data) {
	var toname;
	
	cbOddClear();
	for (var i=0; i < data.length; i++) {
		var name = data[i].nickname;
	    var requestname =  data[i].requestname;
	if (name==null) {
		toname=requestname;
	}else{toname=name;}
	
		var strType = mapType[data[i].type];
		var strTypeID = mapTypeID[data[i].typeid];
		
		$("#mydb").append(
						"<tr id="+data[i].id+">"+
							"<td>"+data[i].odd+"</td>"+
							"<td>"+data[i].username+"</td>"+
							"<td>"+strTypeID+"</td>"+
							"<td>"+strType+"</td>"+
							"<td>"+data[i].amount+"</td>"+
							"<td>"+toname+"</td>"+
							"<td>"+formatTime(data[i].requesttime)+"</td>"+
							"<td>"+
								"<span><a onclick='oddAllow("+data[i].id+","+i+");' href='#'>允许</a></span>"+
								"<span>&nbsp;</span>"+
								"<span><a onclick='oddRefuse("+data[i].id+","+i+");' href='#'>拒绝</a></span>"+
							"</td>"+
						"</tr>");
	}
	
	gdata = data;
}

//1_处理 3_忽略
function oddAllow(id,index) {
	if (confirm("你确定要允许订单【"+gdata[index].odd+"】？"))
		oddDeal(id,1);
}
function oddRefuse(id,index) {
	if (confirm("你确定要忽略订单【"+gdata[index].odd+"】？"))
		oddDeal(id,3);
}
function oddDeal(id,type) {
	var param = {
			"id":id,
			"type":type
	};
	_send(getURL("finance_deal"),param,cbOddDeal,baseCBErr);
}
function cbOddDeal() {
	alert("受理成功");
	oddSelect();
}
