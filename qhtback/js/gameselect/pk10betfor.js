var gIsCur;
var mapType = {
		1:"投注",
		2:"已结算",
		3:"取消",
		4:"飞单"
};

$(function(){
	$("#updatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-dd"));	//默认提前一周
//	$("#endcreatetime").val(new Date(new Date().getTime()+100000).Format("yyyy-MM-ddThh:mm"));	//默认当前时间
//	gIsCur = true;
});


//当期投注记录
function mySelectCur() {
	
	var username = $("#username").val();
	if (	username==null || username==''  ) {
		alert("玩家名 不能为空！");
		return;
	}
//	gIsCur = true;
	var paraterm = {
			"username":username,
			"selectdate":""
	};
	_init(getURL("pk10_bgetbetinfo"),8,paraterm,cbOK,baseCBErr,cbClear);
}
//按期号查询
function mySelectPer() {
	
	var username = $("#usernamePer").val();
	var period = $("#period").val();
	
	if (	username==null || username==''  ) {
		alert("玩家名 不能为空！");
		return;
	}
	if (	period==null || period==''  ) {
		alert("玩家名 不能为空！");
		return;
	}
//	gIsCur = true;
	var paraterm = {
			"username":username,
			"period":period,
			"selectdate":""
	};
	_init(getURL("pk10_bgetbetPer"),8,paraterm,cbOK,baseCBErr,cbClear);
}


//往期投注记录（按日期）
function mySelectTime() {
	var username = $("#usernameTime").val();
	var selectdate = $("#updatetime").val();
	
	if (	username==null || username==''  ) {
		alert("玩家名 不能为空！");
		return;
	}
	if (	selectdate==null || selectdate==''  ) {
		alert("请选择时间");
		return;
	}
//	selectdate = formatDatatime_local(selectdate);

//  int b = Conver.ToInt32(selectdate.ToString("yyyyMMdd"));//mm小写是minute

             var date = selectdate;
             //替换“-”
             var dateStr = date.replace(/\-/g, "");

             var a = parseInt(dateStr);
    
	var paraterm = {
			"selectdate":a,
			"username":username,
	};
	_init(getURL("pk10_bgetbetTime"),8,paraterm,cbOK,baseCBErr,cbClear);
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
		
		var odd = data[i].odd;
		var period = data[i].period;
		var appcode = data[i].appcode;
		var username = data[i].username;
		var runway = data[i].runway;
		var bettype = data[i].bettype;
		var betamount = data[i].betamount;
		var paidamount = data[i].paidamount;
		var bettime = data[i].bettime;
		var paidtime = data[i].paidtime;
		
		var status = data[i].status;
		var type = mapType[status];
		
		var context = "";
		var way = "";
		
		if (runway>=1 && runway<=10) {
			way += "第&nbsp;"+runway+"&nbsp;道"+"<br>";
			if (bettype<11) {
			context += bettype+"号&nbsp;"+betamount+"<br>";
		}
		}
		
		if (runway==11) {
			way += "冠亚"+"<br>";
			if (bettype<20) {
			context += "特&nbsp;"+bettype+"号&nbsp;"+betamount+"<br>";
		}
		}
		
		if (bettype==20) {
			context += "大&nbsp;"+betamount+"<br>";
		}
		if (bettype==21) {
			context += "小&nbsp;"+betamount+"<br>";
		}
		if (bettype==22) {
			context += "单&nbsp;"+betamount+"<br>";
		}
		if (bettype==23) {
			context += "双&nbsp;"+betamount+"<br>";
		}
		if (bettype==24) {
			context += "龙&nbsp;"+betamount+"<br>";
		}
		if (bettype==25) {
			context += "虎&nbsp;"+betamount+"<br>";
		}

		var context = "<tr id="+data[i].id+">"+
			"<td>"+odd+"</td>"+
			"<td>"+period+"</td>"+
			"<td>"+username+"</td>"+
			"<td>"+way+"</td>"+
			"<td>"+context+"</td>"+
			"<td>"+betamount+"</td>"+
			"<td>"+paidamount+"</td>"+
			"<td>"+formatTime(bettime)+"</td>"+
			"<td>"+formatTime(paidtime)+"</td>"+
			"<td>"+type+"</td>"+
			
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

