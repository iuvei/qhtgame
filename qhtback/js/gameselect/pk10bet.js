var gIsCur;

var mapType = {
		3:"正常",
		5:"取消"
};

$(function(){
	$("#startcreatetime").val(new Date(new Date().getTime()-604800000).Format("yyyy-MM-ddThh:mm"));	//默认提前一周
	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
	gIsCur = true;
	mySelect();
});

function setCancle(id) {
	var record = _get(id);
	if (record==null) {
		alert("出错");
		return;
	}
	
	var type = record.type;
	if (type!=3) {
		alert("已经取消");
		return;
	}
	if (!confirm("确定要取消吗？"))
		return;
	
	var parameter = {
			"period":record.period,
			"username":record.username,
			"odd":record.odd
	};
	_send(getURL("pk10_bcanclebet"),parameter,cbSetCancle,baseCBErr);
}
function cbSetCancle(data) {
	alert("取消成功");
	mySelectCur();
}

function mySelectCur() {
	var username = $("#username").val();
	
	gIsCur = true;
	var paraterm = {
			"period":"000000",
			"username":username,
			"begintime":"",
			"endtime":""
	};
	_init(getURL("pk10_bgetbetinfo"),8,paraterm,cbOK,baseCBErr,cbClear);
}

function mySelect() {
	var period = $("#period").val();
	var username = $("#username").val();
	var begintime = $("#startcreatetime").val();
	var endtime = $("#endcreatetime").val();
	if (	begintime==null || begintime=='' ||
			endtime==null || endtime=='' ) {
		alert("请选择时间");
		return;
	}
	begintime = formatDatatime_local(begintime);
	endtime = formatDatatime_local(endtime);
	if (new Date(begintime)>new Date(endtime)) {
		alert("开始时间不能大于结束时间");
		return;
	}
	
	gIsCur = false;
	var paraterm = {
			"period":period,
			"username":username,
			"begintime":begintime,
			"endtime":endtime
	};
	_init(getURL("pk10_bgetbetinfo"),8,paraterm,cbOK,baseCBErr,cbClear);
}



function cbClear(data) {
	if (data!=null) {
		for (var i=0; i < data.length; i++) {
			$("tr[id='"+data[i].id+"']").remove();
		}
	}
	
	$("#size").html(0);
	$("#total").html(0);
	$("#pageNum").html(0);
	$("#pages").html(0);
	$("#change").val("");
}
//查询成功回调函数
function cbOK(size,total,pageNum,pages,data) {
	$("#error").html("");
	if (data==null || data.length==0) {
		$("#error").html('no data');
		$("#size").html(0);
		$("#total").html(0);
		$("#pageNum").html(0);
		$("#pages").html(0);
		$("#change").val("");
		return;
	}

	for (var i=0; i < data.length; i++) {
		var type = data[i].type;
		var strType = mapType[type];

		var betInfo = data[i].detail;
		var betInfoNum = data[i].detail.num;
		var context = "";
		if (betInfo.big>0) {
			context += "大&nbsp;"+betInfo.big+"<br>";
		}
		if (betInfo.small>0) {
			context += "小&nbsp;"+betInfo.small+"<br>";
		}
		if (betInfo.single>0) {
			context += "单&nbsp;"+betInfo.single+"<br>";
		}
		if (betInfo.dou>0) {
			context += "双&nbsp;"+betInfo.dou+"<br>";
		}
		if (betInfo.dragon>0) {
			context += "龙&nbsp;"+betInfo.dragon+"<br>";
		}
		if (betInfo.tiger>0) {
			context += "虎&nbsp;"+betInfo.tiger+"<br>";
		}
		for (var j=0; j<betInfoNum.length; j++) {
			if (betInfoNum[j].money>0) {
				context += betInfoNum[j].id+"&nbsp;"+betInfoNum[j].money+"<br>";
			}
		}
		
		var context = "<tr id="+data[i].id+">"+
			"<td>"+data[i].period+"</td>"+
			"<td>"+data[i].username+"</td>"+
			"<td>"+strType+"</td>"+
			"<td>"+data[i].detail.index+"</td>"+
			"<td>"+data[i].amount+"</td>"+
			"<td>"+context+"</td>"+
			"<td>"+formatTime(data[i].updatetime)+"</td>"+
			"<td>"
			if (gIsCur) {
			context += "<a onclick='setCancle("+data[i].id+");' href='#'>取消</a>";
		}
		context +="</td>"+
		 "</tr>";
		
		$("#mydb").append(context);
	}
	
	$("#size").html(size);
	$("#total").html(total);
	$("#pageNum").html(pageNum);
	$("#pages").html(pages);
	$("#change").val("");
}

//刷新
function flushPage() {
	_sendPage(cbOK,baseCBErr,cbClear);
}

//首页
function firstPage() {
	_firstPage(cbOK,baseCBErr,cbClear);
}

//尾页
function endPage() {
	_endPage(cbOK,baseCBErr,cbClear);
}

//上一页
function upPage() {
	_upPage(cbOK,baseCBErr,cbClear);
}

//下一页
function downPage() {
	_downPage(cbOK,baseCBErr,cbClear);
}

//跳转
function changePage() {
	var index = $("#change").val();
	_changePage(index,cbOK,baseCBErr,cbClear);
}

